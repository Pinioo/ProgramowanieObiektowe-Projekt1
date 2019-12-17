package agh.evolutiongame;

import agh.evolutiongame.interfaces.IMapElement;
import agh.evolutiongame.interfaces.IWorldMap;

import java.util.*;

public abstract class AbstractWorldMap implements IWorldMap {
    protected ListsHashMap<Vector2d, IMapElement> elementsHashMap = new ListsHashMap<>();
    protected HashSet<Vector2d> freePositions;

    protected final Rectangle area;

    protected int maxElements;

    protected AbstractWorldMap(Vector2d lowerLeft, Vector2d upperRight) {
        this.area = new Rectangle(lowerLeft, upperRight);
        freePositions = this.area.positionsSet();
        maxElements = this.area.area();
    }

    protected AbstractWorldMap(Rectangle area){
        this.area = area;
        freePositions = this.area.positionsSet();
        maxElements = this.area.area();
    }

    // If map is not full places new grass
    protected void randGrass(int caloricValue){
        int mapElements = this.elementsHashMap.size();
        if(mapElements < this.maxElements){
            Vector2d randPosition;
            // If map is filled in less then 70% -> brute force randomizing position
            if (mapElements < 0.7 * this.maxElements) {
                do{
                    randPosition = this.area.randPoint();
                }while(this.isOccupied(randPosition));
            }
            // If map is filled in more then 70% -> taking random from free positions' list
            else{
                ArrayList<Vector2d> freePositionsList = new ArrayList<>(this.freePositions);
                randPosition = freePositionsList.get(new Random().nextInt(freePositionsList.size()));
            }
            new Grass(this, randPosition, caloricValue).addObserver(this);
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
        if(!this.isOccupied(position))
            this.freePositions.add(position);
    }

    @Override
    public void objectAdded(Vector2d position, IMapElement element) {
        if(!isOccupied(position))
            this.freePositions.remove(position);
        this.elementsHashMap.put(position, element);
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return this.elementsHashMap.getMap().containsKey(position);
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
