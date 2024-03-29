package agh.evolutiongame.mapelements;

import agh.evolutiongame.spatialclasses.Vector2d;
import agh.evolutiongame.abstracts.IWorldMap;
import agh.evolutiongame.abstracts.AbstractObservedMapElement;

public class Grass extends AbstractObservedMapElement {
    private final long caloricValue;

    public Grass(IWorldMap map, Vector2d initialPosition, long caloricValue) {
        super(initialPosition);
        this.caloricValue = caloricValue;

        // Inform map that grass was added at position = initialPosition
        map.objectAdded(initialPosition, this);
    }

    // Removes grass when eaten
    public void grassEaten(){
        this.remove();
    }

    @Override
    public String toString(){
        return "*";
    }

    public long getCaloricValue() {
        return caloricValue;
    }
}
