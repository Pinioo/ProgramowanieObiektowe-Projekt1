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
        super(new Vector2d(0,0), new Vector2d(width, height));
        this.jungle = new JungleMap(this.area.scale(jungleRatio));
        this.maxElements -= this.jungle.maxElements;

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

    private LinkedList<Vector2d> occupiedPositionsList(){
        Stream<Vector2d> stream1 = this.elementsHashMap.getMap().keySet().stream();
        Stream<Vector2d> stream2 = this.jungle.elementsHashMap.getMap().keySet().stream();
        return Stream.concat(stream1, stream2).collect(Collectors.toCollection(LinkedList::new));
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

    private void eatAtPosition(Vector2d position) {
        Grass grassOnPosition = this.grassOnPosition(position);
        if (grassOnPosition != null) {
            LinkedList<Animal> animalsFromStrongest = this.animalsOnPositionFromStrongest(position);
            if (!animalsFromStrongest.isEmpty()) {
                //Energy of the strongest animals
                int maxEnergy = animalsFromStrongest.getFirst().getEnergy();

                //Creating list of strongest animals which are about to be fed
                LinkedList<Animal> strongestAnimalsList = animalsFromStrongest.stream()
                        .filter(an -> an.getEnergy() == maxEnergy)
                        .collect(Collectors.toCollection(LinkedList::new));

                int energyPartForAnimal = grassOnPosition.getCaloricValue() / strongestAnimalsList.size();
                strongestAnimalsList.forEach(an -> an.increaseEnergy(energyPartForAnimal));

                grassOnPosition.grassEaten();
            }
        }
    }

    private void reproduceAtPosition(Vector2d position){
        LinkedList<Animal> animalsFromStrongest = this.animalsOnPositionFromStrongest(position);
        if(animalsFromStrongest.size() >= 2){
            Animal parent1 = animalsFromStrongest.get(0);
            Animal parent2 = animalsFromStrongest.get(1);
            if(parent1.canReproduce() && parent2.canReproduce()){
                this.tryToReproduce(parent1, parent2);
            }
        }
    };

    public void newDay(){
        this.randGrass(this.grassEnergy);
        this.jungle.randGrass(this.grassEnergy);
        this.allAnimalsStream().forEach(
                an -> {
                    if(!an.decreaseEnergy(this.moveEnergy)) {
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

    public LinkedList<Animal> animalsOnPosition(Vector2d position){
        LinkedList<IMapElement> list = this.objectsAt(position);
        if (list == null)
            return new LinkedList<>();
        return list.stream()
                .filter((p) -> p instanceof Animal)
                .map(Animal.class::cast)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    private LinkedList<Animal> animalsOnPositionFromStrongest(Vector2d position){
        LinkedList<Animal> onPosition = this.animalsOnPosition(position);
        onPosition.sort(
                (a,b) -> -Animal.compareByEnergy(a,b)
        );
        return onPosition;
    }

    public Vector2d correctPosition(Vector2d requestedPosition){
        return new Vector2d(
                Math.floorMod(requestedPosition.getX(), this.area.upperRight.getX() + 1),
                Math.floorMod(requestedPosition.getY(), this.area.upperRight.getY() + 1)
        );
    }

    @Override
    protected void randGrass(int caloricValue){
        if(this.elementsHashMap.getMap().size() < this.maxElements){
            Vector2d randPosition;
            do{
                randPosition = this.area.randPoint();
            }while(this.jungle.isPositionInside(randPosition) || this.isOccupied(randPosition));
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
