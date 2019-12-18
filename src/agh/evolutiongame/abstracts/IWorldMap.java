package agh.evolutiongame.abstracts;

import agh.evolutiongame.spatialclasses.Vector2d;

import java.util.LinkedList;

public interface IWorldMap extends IPositionChangedObserver {
    boolean isOccupied(Vector2d position);
    LinkedList<IMapElement> objectsAt(Vector2d position);
    Vector2d correctPosition(Vector2d requestedPosition);
    Vector2d getLowerLeft();
    Vector2d getUpperRight();
    String symbolOnPosition(Vector2d currentPosition);
}