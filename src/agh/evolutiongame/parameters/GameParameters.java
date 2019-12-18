package agh.evolutiongame.parameters;

public class GameParameters {
    public int width;
    public int height;
    public double jungleRatio;
    public int startEnergy;
    public int moveEnergy;
    public int grassEnergy;
    public int randomAnimals;
    public int days;
    public int delay;

    public GameParameters(int width, int height, double jungleRatio, int startEnergy, int moveEnergy, int grassEnergy, int randomAnimals, int days, int delay){
        this.width = width;
        this.height = height;
        this.jungleRatio = jungleRatio;
        this.startEnergy = startEnergy;
        this.moveEnergy = moveEnergy;
        this.grassEnergy = grassEnergy;
        this.randomAnimals = randomAnimals;
        this.days = days;
        this.delay = delay;
    }

    public GameParameters(GameParameters other){
        this.width = other.width;
        this.height = other.height;
        this.jungleRatio = other.jungleRatio;
        this.startEnergy = other.startEnergy;
        this.moveEnergy = other.moveEnergy;
        this.grassEnergy = other.grassEnergy;
        this.randomAnimals = other.randomAnimals;
        this.days = other.days;
        this.delay = other.delay;
    }
}
