package agh.evolutiongame;

import java.util.HashMap;
import java.util.LinkedList;

public abstract class AbstractWorldMap implements IWorldMap{
    protected ListsHashMap<Vector2d, IMapElement> elementsHashMap = new ListsHashMap<>();

    protected final Rectangle area;

    protected int maxElements;

    protected AbstractWorldMap(Vector2d lowerLeft, Vector2d upperRight) {
        this.area = new Rectangle(lowerLeft, upperRight);
        maxElements = (this.area.upperRight.x - this.area.lowerLeft.x + 1)*(this.area.upperRight.y - this.area.lowerLeft.y + 1);
    }

    protected AbstractWorldMap(Rectangle area){
        this.area = area;
        maxElements = (this.area.upperRight.x - this.area.lowerLeft.x + 1)*(this.area.upperRight.y - this.area.lowerLeft.y + 1);
    }

    protected void randGrass(int caloricValue){
        if(this.elementsHashMap.getMap().size() < maxElements){
            Vector2d randPosition;
            do{
                randPosition = this.area.randPoint();
            }while(this.isOccupied(randPosition));
            new Grass(this, randPosition, caloricValue);
        }
    }

    public boolean isPositionInside(Vector2d position){
        return this.area.isPointInside(position);
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, IMapElement element){
        this.elementsHashMap.remove(oldPosition, element);
        this.elementsHashMap.put(newPosition, element);
    }

    @Override
    public void objectRemoved(Vector2d position, IMapElement element) {
        this.elementsHashMap.remove(position, element);
    }

    @Override
    public void objectAdded(Vector2d position, IMapElement element) {
        this.elementsHashMap.put(position, element);
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return this.objectsAt(position) != null;
    }

    @Override
    public LinkedList<IMapElement> objectsAt(Vector2d position) {
        return this.elementsHashMap.get(position);
    }

    @Override
    public String toString(){
        MapVisualizer visual = new MapVisualizer(this);
        return visual.draw(this.getLowerLeft(), this.getUpperRight());
    }
}
