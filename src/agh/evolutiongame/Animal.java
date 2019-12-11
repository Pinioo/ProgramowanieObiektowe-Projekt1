package agh.evolutiongame;

import agh.evolutiongame.interfaces.IWorldMap;

import java.util.Random;

public class Animal extends AbstractObservedMapElement {
    private MapDirection direction;
    private IWorldMap map;
    private Genome genome;

    private int energy;
    private final int startingEnergy;

    public Animal(IWorldMap map, Vector2d initialPosition, int energy){
        super(initialPosition);
        this.map = map;
        this.genome = new Genome();
        this.direction = MapDirection.rand();
        map.objectAdded(this.position, this);
        this.energy = energy;
        this.startingEnergy = energy;
    }

    public Animal(IWorldMap map, Vector2d initialPosition, Animal parent1, Animal parent2){
        super(initialPosition);
        this.map = map;
        this.genome = new Genome(parent1,parent2);
        this.direction = MapDirection.rand();
        map.objectAdded(this.position, this);
        this.energy = parent1.animalReproduced() + parent2.animalReproduced();
        this.startingEnergy = this.energy;
    }

    public void randomRotate(){
        this.rotateBy((new Random()).nextInt(32));
    }

    private void rotateBy(int factor){
        this.direction = this.direction.rotateBy(factor);
    }

    public void moveForward(){
        Vector2d oldPosition = this.position;
        this.position = this.map.correctPosition(this.position.add(this.direction.toUnitVector()));
        this.positionChanged(oldPosition, this.position);
    }

    public boolean canReproduce(){
        return 2*this.energy >= this.startingEnergy;
    }

    private int animalReproduced(){
        int energyLost = this.energy / 4;
        this.energy -= energyLost;
        return energyLost;
    }

    public void increaseEnergy(int energyDelta) {
        this.energy += energyDelta;
    }

    public boolean decreaseEnergy(int energyDelta) {
        this.energy -= energyDelta;
        if(this.energy <= 0) {
            this.remove();
            return true;
        }
        return false;
    }

    public Genome getGenome() {
        return this.genome;
    }

    public int getEnergy(){
        return this.energy;
    }

    public static int compareByEnergy(Animal an1, Animal an2){
        if(an1.energy < an2.energy) return 1;
        if(an1.energy > an2.energy) return -1;
        return 0;
    }

    @Override
    public String toString(){
        return "A";
    }
}
