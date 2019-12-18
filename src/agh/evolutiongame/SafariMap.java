package agh.evolutiongame;

import agh.evolutiongame.interfaces.IMapElement;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SafariMap extends AbstractWorldMap {
    private JungleMap jungle;
    private final int grassEnergy;
    private final int moveEnergy;

    public SafariMap(int width, int height, double jungleRatio, int grassEnergy, int moveEnergy, int startEnergy, int randomAnimals){
        super(new Vector2d(0,0), new Vector2d(width-1, height-1));
        this.jungle = new JungleMap(this.area.scale(jungleRatio));
        this.maxElements -= this.jungle.maxElements;
        this.freePositions.removeIf(position -> this.jungle.isPositionInside(position));

        this.grassEnergy = grassEnergy;
        this.moveEnergy = moveEnergy;

        for (int i = 0; i < randomAnimals; i++) {
            this.placeRandomAnimal(startEnergy);
        }
    }

    public void placeRandomAnimal(int startEnergy){
        Vector2d randPosition;
        do {
            randPosition = this.area.randPoint();
        }while(this.isOccupied(randPosition));
        new Animal(this, randPosition, startEnergy)
                .addObserver(this);
    }

    private Stream<IMapElement> allElementsStream(){
        Stream<IMapElement> stream1 = this.elementsHashMap.allElementsList().stream();
        Stream<IMapElement> stream2 = this.jungle.elementsHashMap.allElementsList().stream();

        return Stream.concat(stream1, stream2);
    }

    private Stream<Animal> allAnimalsStream(){
        return this.allElementsStream()
                .filter(p -> p instanceof Animal)
                .map(Animal.class::cast);
    }

    public int animalsCount(){
        return (int) this.allAnimalsStream()
                .count();
    }

    private ArrayList<Vector2d> occupiedPositionsList(){
        Stream<Vector2d> stream1 = this.elementsHashMap.getMap().keySet().stream();
        Stream<Vector2d> stream2 = this.jungle.elementsHashMap.getMap().keySet().stream();
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
            ArrayList<Vector2d> possiblePositions = this.freePositionsAround(parent1.position);
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

                int energyPartForAnimal = grassOnPosition.getCaloricValue() / strongestAnimalsList.size();
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

    public LinkedList<Animal> strongestAnimalsOnPosition(Vector2d position){
        LinkedList<Animal> animals = this.animalsOnPosition(position);

        if(animals.isEmpty())
            return animals;

        int maxEnergy = animals.stream()
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
    protected void randGrass(int caloricValue){
        int mapElements = this.elementsHashMap.size();
        if(mapElements < this.maxElements){
            Vector2d randPosition;
            // If map is filled in less then 80% -> brute force randomizing position
            if (mapElements < 0.8 * this.maxElements) {
                do{
                    randPosition = this.area.randPoint();
                    // Position must be free and not in jungle
                }while(this.jungle.isPositionInside(randPosition) || this.isOccupied(randPosition));
            }
            // If map is filled in more then 80% -> taking random from free positions' list
            else{
                ArrayList<Vector2d> freePositionsList = new ArrayList<>(this.freePositions);
                randPosition = freePositionsList.get(new Random().nextInt(freePositionsList.size()));
            }
            new Grass(this, randPosition, caloricValue).addObserver(this);
        }
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
    public Vector2d getLowerLeft() {
        return this.area.lowerLeft;
    }

    @Override
    public Vector2d getUpperRight() {
        return this.area.upperRight;
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
