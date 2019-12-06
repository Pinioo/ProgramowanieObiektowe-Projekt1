package agh.evolutiongame;

public interface Game {
    void start(IWorldMap map);
    void update();
    void end();
}
