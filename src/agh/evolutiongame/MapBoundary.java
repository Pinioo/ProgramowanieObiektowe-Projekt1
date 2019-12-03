package agh.evolutiongame;

import java.util.SortedSet;
import java.util.TreeSet;

public class MapBoundary implements IPositionChangedObserver {
    SortedSet<Vector2d> sortedByX = new TreeSet<>((Vector2d ls, Vector2d rs) -> {
        if(ls.getX() < rs.getX()) return -1;
        if(ls.getX() > rs.getX()) return 1;
        if(ls.getY() < rs.getY()) return -1;
        if(ls.getY() > rs.getY()) return 1;
        return 0;
    });

    SortedSet<Vector2d> sortedByY = new TreeSet<>((Vector2d ls, Vector2d rs) -> {
        if(ls.getY() < rs.getY()) return -1;
        if(ls.getY() > rs.getY()) return 1;
        if(ls.getX() < rs.getX()) return -1;
        if(ls.getX() > rs.getX()) return 1;
        return 0;
    });

    public Vector2d upperRight(){
        if(sortedByX.isEmpty())
            return new Vector2d(0,0);
        else
            return sortedByY.last().upperRight(sortedByX.last());
    }

    public Vector2d lowerLeft(){
        if(sortedByX.isEmpty())
            return new Vector2d(0,0);
        else
            return sortedByY.first().lowerLeft(sortedByX.first());
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        this.remove(oldPosition);
        this.place(newPosition);
    }

    @Override
    public void objectRemoved(Vector2d position) {
        this.remove(position);
    }

    @Override
    public void objectAdded(Vector2d position, IMapElement element) {
        this.place(position);
    }

    private void place(Vector2d position){
        sortedByY.add(position);
        sortedByX.add(position);
    }

    private void remove(Vector2d position){
        sortedByX.remove(position);
        sortedByY.remove(position);
    }
}
