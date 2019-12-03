package agh.evolutiongame;

public interface IPositionChangedObserver {
    void positionChanged(Vector2d oldPosition, Vector2d newPosition);
    void objectRemoved(Vector2d position);
    void objectAdded(Vector2d position, IMapElement element);
}
