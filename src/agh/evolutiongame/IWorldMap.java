package agh.evolutiongame;

public interface IWorldMap extends IPositionChangedObserver {
    boolean canMoveTo(Vector2d position);
    void place(Animal animal);
    boolean isOccupied(Vector2d position);
    Object objectAt(Vector2d position);
    Vector2d correctPosition(Vector2d requestedPosition);
    Vector2d getLowerLeft();
    Vector2d getUpperRight();
}