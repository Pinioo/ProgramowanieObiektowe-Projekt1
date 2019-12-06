package agh.evolutiongame;

import javax.naming.SizeLimitExceededException;
import javax.swing.*;
import java.util.LinkedList;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SafariMap extends AbstractWorldMap {
    private JungleMap jungle;
    private int startingGrassEnergy = 10;

    public SafariMap(int width, int height, double jungleRatio, int randomAnimals){
        super(new Vector2d(0,0), new Vector2d(width, height));
        this.jungle = new JungleMap(this.area.scale(jungleRatio));
        this.maxElements -= this.jungle.maxElements;
        System.out.println(this.jungle.area.lowerLeft);
        System.out.println(this.jungle.area.upperRight);
        System.out.println(this.jungle.maxElements);
        System.out.println(this.maxElements);
        for (int i = 0; i < randomAnimals; i++) {
            this.placeRandomAnimal();
        }
    }

    public void placeRandomAnimal(){
        Vector2d randPosition;
        do {
            randPosition = this.area.randPoint();
        }while(this.isOccupied(randPosition));
        new Animal(this, randPosition, 1000);
    }

    private Stream<IMapElement> allElementsStream(){
        Stream<IMapElement> stream1 = this.elementsHashMap.allElementsList().stream();
        Stream<IMapElement> stream2 = this.jungle.elementsHashMap.allElementsList().stream();

        return Stream.concat(stream1, stream2);
    }

    private Stream<Animal> allAnimalsStream(){
        return this.allElementsStream().filter(p -> p instanceof Animal).map(Animal.class::cast);
    }

    public void newDay(){
        this.randGrass(startingGrassEnergy);
        this.jungle.randGrass(startingGrassEnergy);
        this.allAnimalsStream().forEach(
                an -> {
                    if(!an.decreaseEnergy()) {
                        an.randomRotate();
                        an.moveForward();
                    }
                }
        );
    }

    public LinkedList<Animal> animalsOnPosition(Vector2d position){
        LinkedList<IMapElement> list = this.objectsAt(position);
        if (list == null)
            return new LinkedList<>();
        return list.stream().filter((p) -> p instanceof Animal).map(Animal.class::cast).collect(Collectors.toCollection(LinkedList::new));
    }

    public LinkedList<Animal> strongestAnimalsAtPosition(Vector2d position) {
        LinkedList<Animal> animalsOnPosition = this.animalsOnPosition(position);
        if (!animalsOnPosition.isEmpty()) {
            final int maxEnergy = animalsOnPosition.stream().max(Animal::compareByEnergy).get().getEnergy();
            return animalsOnPosition.stream().filter((p) -> p.getEnergy() == maxEnergy).collect(Collectors.toCollection(LinkedList::new));
        }
        else
            return animalsOnPosition;
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
            new Grass(this, randPosition, caloricValue);
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
