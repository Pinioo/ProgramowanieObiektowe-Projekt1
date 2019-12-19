package agh.evolutiongame.maps;

import agh.evolutiongame.abstracts.AbstractWorldMap;
import agh.evolutiongame.mapelements.Animal;
import agh.evolutiongame.mapelements.Grass;
import agh.evolutiongame.spatialclasses.Vector2d;
import agh.evolutiongame.abstracts.IMapElement;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


// SafariMap is a class representing map of safari and containing inner map - JungleMap
// SafariMap takes care of elements on a border of the map (outside jungle)
// If something changes in jungle -> SafariMap tells jungle to deal with it
public class SafariMap extends AbstractWorldMap {
    private JungleMap jungle;
    private final long grassEnergy;
    private final long moveEnergy;

    public SafariMap(long width, long height, double jungleRatio, long grassEnergy, long moveEnergy, long startEnergy, long randomAnimals){
        super(new Vector2d(0,0), new Vector2d((int)width-1, (int)height-1));
        this.jungle = new JungleMap(this.area.scale(jungleRatio));
        this.maxElements -= this.jungle.getMaxElements();
        this.freePositions.removeIf(position -> this.jungle.isPositionInside(position));

        this.grassEnergy = grassEnergy;
        this.moveEnergy = moveEnergy;

        for (int i = 0; i < randomAnimals; i++) {
            this.placeRandomAnimal(startEnergy);
        }
    }

    public void placeRandomAnimal(long startEnergy){
        Vector2d randPosition;
        do {
            randPosition = this.area.randPoint();
        }while(this.isOccupied(randPosition));
        new Animal(this, randPosition, startEnergy)
                .addObserver(this);
    }

    // Stream of all animals on whole map
    private Stream<IMapElement> allElementsStream(){
        Stream<IMapElement> stream1 = this.getElementsHashMap().allElementsList().stream();
        Stream<IMapElement> stream2 = this.jungle.getElementsHashMap().allElementsList().stream();

        return Stream.concat(stream1, stream2);
    }

    private Stream<Animal> allAnimalsStream(){
        return this.allElementsStream()
                .filter(p -> p instanceof Animal)
                .map(Animal.class::cast);
    }

    public long animalsCount(){
        return (long) this.allAnimalsStream()
                .count();
    }

    private ArrayList<Vector2d> occupiedPositionsList(){
        Stream<Vector2d> stream1 = this.getElementsHashMap().getMap().keySet().stream();
        Stream<Vector2d> stream2 = this.jungle.getElementsHashMap().getMap().keySet().stream();
        return Stream.concat(stream1, stream2).collect(Collectors.toCollection(ArrayList::new));
    }

    private ArrayList<Vector2d> freePositionsAround(Vector2d position){
        return position.positionsAround().stream()
                .filter(this::isPositionInside)
                .filter(pos -> !this.isOccupied(pos))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private Grass grassOnPosition(Vector2d position){
        for(IMapElement element: this.objectsAt(position)){
            if(element instanceof Grass){
                return (Grass)element;
            }
        }
        return null;
    }

    private boolean tryToReproduce(Animal parent1, Animal parent2){
        if(parent1.canReproduce() && parent2.canReproduce()){
            ArrayList<Vector2d> possiblePositions = this.freePositionsAround(parent1.getPosition());
            if(!possiblePositions.isEmpty()){
                int randIndex = (new Random()).nextInt(possiblePositions.size());
                new Animal(this, possiblePositions.get(randIndex), parent1, parent2)
                        .addObserver(this);
                return true;
            }
        }
        return false;
    }

    // If grass is at position -> split it between all the strongest animals
    private void eatAtPosition(Vector2d position) {
        Grass grassOnPosition = this.grassOnPosition(position);
        if (grassOnPosition != null) {
            LinkedList<Animal> animals = this.animalsOnPosition(position);
            if (!animals.isEmpty()) {
                LinkedList<Animal> strongestAnimalsList = strongestAnimalsOnPosition(position);

                long energyPartForAnimal = grassOnPosition.getCaloricValue() / strongestAnimalsList.size();
                strongestAnimalsList.forEach(an -> an.increaseEnergy(energyPartForAnimal));

                grassOnPosition.grassEaten();
            }
        }
    }

    private Animal[] findTwoStrongest(Vector2d position){
        LinkedList<Animal> strongestAnimals = this.strongestAnimalsOnPosition(position);
        if (strongestAnimals.isEmpty())
            return null;
        if (strongestAnimals.size() >= 2){
            return new Animal[]{strongestAnimals.get(0), strongestAnimals.get(1)};
        }
        else{
            Animal strongest = strongestAnimals.get(0);
            LinkedList<Animal> allAnimals = this.animalsOnPosition(position);
            if (allAnimals.size() >= 2) {
                Animal secondStrongest = allAnimals.stream()
                        .filter(an -> an != strongest)
                        .max(Animal::compareByEnergy)
                        .get();
                return new Animal[]{strongest, secondStrongest};
            }
        }
        return null;
    }

    // If the two strongest animals can reproduce -> do it
    private void reproduceAtPosition(Vector2d position){
        Animal[] twoStrongest = this.findTwoStrongest(position);
        if(twoStrongest != null){
            Animal parent1 = twoStrongest[0];
            Animal parent2 = twoStrongest[1];
            if(parent1.canReproduce() && parent2.canReproduce()){
                this.tryToReproduce(parent1, parent2);
            }
        }
    };

    // New day activities
    public void newDay(){
        this.randGrass(this.grassEnergy);
        this.jungle.randGrass(this.grassEnergy);
        this.allAnimalsStream().forEach(
                an -> {
                    if(!an.decreaseEnergy(this.moveEnergy)) {
                        // If animal hasn't died
                        an.randomRotate();
                        an.moveForward();
                    }
                }
        );
        this.occupiedPositionsList().forEach(
                position -> {
                    this.eatAtPosition(position);
                    this.reproduceAtPosition(position);
                }
        );
    }

    // List of animals at position
    public LinkedList<Animal> animalsOnPosition(Vector2d position){
        LinkedList<IMapElement> list = this.objectsAt(position);
        if (list == null)
            return new LinkedList<>();
        return list.stream()
                .filter((p) -> p instanceof Animal)
                .map(Animal.class::cast)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    // List of animals having the same, greatest energy
    public LinkedList<Animal> strongestAnimalsOnPosition(Vector2d position){
        LinkedList<Animal> animals = this.animalsOnPosition(position);

        if(animals.isEmpty())
            return animals;

        long maxEnergy = animals.stream()
                .max(Animal::compareByEnergy).get().getEnergy();

        return animals.stream()
                .filter(an -> an.getEnergy() == maxEnergy).collect(Collectors.toCollection(LinkedList::new));

    }

    // If position went out of area's range -> return position on the opposite side
    public Vector2d correctPosition(Vector2d requestedPosition){
        return new Vector2d(
                Math.floorMod(requestedPosition.getX(), this.area.upperRight.getX() + 1),
                Math.floorMod(requestedPosition.getY(), this.area.upperRight.getY() + 1)
        );
    }

    @Override
    public boolean isPositionInside(Vector2d position){
        return this.area.isPointInside(position) && !this.jungle.isPositionInside(position);
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, IMapElement element){
        this.objectRemoved(oldPosition, element);
        this.objectAdded(newPosition, element);
    }

    @Override
    public void objectRemoved(Vector2d position, IMapElement element) {
        if(this.jungle.isPositionInside(position))
            this.jungle.objectRemoved(position, element);
        else
            super.objectRemoved(position, element);
    }

    @Override
    public void objectAdded(Vector2d position, IMapElement element) {
        if(this.jungle.isPositionInside(position))
            this.jungle.objectAdded(position, element);
        else
            super.objectAdded(position, element);
    }

    @Override
    public LinkedList<IMapElement> objectsAt(Vector2d position) {
        if(this.jungle.isPositionInside(position))
            return this.jungle.objectsAt(position);
        else
            return super.objectsAt(position);
    }

    @Override
    public String symbolOnPosition(Vector2d position){
        LinkedList<Animal> animalsOnPosition = this.animalsOnPosition(position);
        int animalsCount = animalsOnPosition.size();
        switch(animalsCount){
            case 0: {
                if (this.objectsAt(position) != null)
                    return "*";
                else
                    return " ";
            }
            case 1:
                return "A";
            default: {
                if(animalsCount < 10)
                    return String.valueOf(animalsCount);
                else
                    return "S";
            }
        }
    }
}
