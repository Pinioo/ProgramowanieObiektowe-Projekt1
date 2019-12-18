package agh.evolutiongame.abstracts;

import agh.evolutiongame.spatialclasses.Vector2d;

public interface IPositionChangedObserver {
    void positionChanged(Vector2d oldPosition, Vector2d newPosition, IMapElement element);
    void objectRemoved(Vector2d position, IMapElement element);
    void objectAdded(Vector2d position, IMapElement element);
}
