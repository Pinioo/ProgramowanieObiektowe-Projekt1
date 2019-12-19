package agh.evolutiongame.parameters;

public class GameParameters {
    public long width;
    public long height;
    public double jungleRatio;
    public long startEnergy;
    public long moveEnergy;
    public long grassEnergy;
    public long randomAnimals;
    public long days;
    public long delay;

    public GameParameters(long width, long height, double jungleRatio, long startEnergy, long moveEnergy, long grassEnergy, long randomAnimals, long days, long delay){
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
