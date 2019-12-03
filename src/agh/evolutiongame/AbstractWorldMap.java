package agh.evolutiongame;

import java.util.HashMap;
import java.util.LinkedList;

public abstract class AbstractWorldMap implements IWorldMap{
    protected LinkedList<Animal> animalsList = new LinkedList<>();

    HashMap<Vector2d, IMapElement> elementsHashMap = new HashMap<>();

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition){
        IMapElement element = (IMapElement)this.objectAt(oldPosition);
        elementsHashMap.remove(oldPosition);
        elementsHashMap.put(newPosition, element);
    }

    @Override
    public void objectRemoved(Vector2d position) {
        elementsHashMap.remove(position);
    }

    @Override
    public void objectAdded(Vector2d position, IMapElement element) {
        this.elementsHashMap.put(position, element);
    }

    @Override
    public void place(Animal animal) throws IllegalArgumentException {
        if(this.canMoveTo(animal.getPosition())) {
            this.animalsList.add(animal);
            this.elementsHashMap.put(animal.getPosition(), animal);
        }
        else
            throw new IllegalArgumentException("Animal cannot be placed at position " + animal.getPosition().toString());
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    @Override
    public Object objectAt(Vector2d position) {
        return this.elementsHashMap.get(position);
    }

    @Override
    public String toString(){
        MapVisualizer visual = new MapVisualizer(this);
        return visual.draw(this.getLowerLeft(), this.getUpperRight());
    }
}
