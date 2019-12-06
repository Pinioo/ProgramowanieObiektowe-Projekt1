package agh.evolutiongame;

public class Grass extends AbstractObservedMapElement {
    private final int caloricValue;

    public Grass(IWorldMap map, Vector2d initialPosition, int caloricValue) {
        super(initialPosition);
        this.caloricValue = caloricValue;
        this.addObserver(map);
        map.objectAdded(initialPosition, this);
    }

    public void changePosition(Vector2d newGrassPosition) {
        this.positionChanged(this.position, newGrassPosition);
        this.position = newGrassPosition;
    }

    @Override
    public String toString(){
        return "*";
    }

    public int getCaloricValue() {
        return caloricValue;
    }
}
