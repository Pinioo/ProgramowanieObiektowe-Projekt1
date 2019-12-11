package agh.evolutiongame;

import agh.evolutiongame.interfaces.IWorldMap;

public class GameParameters {
    public final IWorldMap map;
    public final int width;
    public final int height;
    public final double jungleRatio;
    public final int startEnergy;
    public final int moveEnergy;
    public final int grassEnergy;
    public final int randomAnimals;
    public final int days;

    public GameParameters(int width, int height, double jungleRatio, int startEnergy, int moveEnergy, int grassEnergy, int randomAnimals, int days){
        this.map = new SafariMap(width, height, jungleRatio, startEnergy, moveEnergy, grassEnergy, randomAnimals);
        this.width = width;
        this.height = height;
        this.jungleRatio = jungleRatio;
        this.startEnergy = startEnergy;
        this.moveEnergy = moveEnergy;
        this.grassEnergy = grassEnergy;
        this.randomAnimals = randomAnimals;
        this.days = days;
    }
}
