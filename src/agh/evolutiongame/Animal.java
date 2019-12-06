package agh.evolutiongame;

import java.util.Comparator;
import java.util.Map;
import java.util.Random;

public class Animal extends AbstractObservedMapElement {
    private MapDirection direction;
    private IWorldMap map;
    private Genome genome;
    private int energy;

    public Animal(IWorldMap map, Vector2d initialPosition, int energy){
        super(initialPosition);
        this.map = map;
        this.genome = new Genome();
        this.direction = MapDirection.rand();
        map.objectAdded(this.position, this);
        this.energy = energy;
        this.addObserver(map);
    }

    public Animal(IWorldMap map, Vector2d initialPosition, Animal parent1, Animal parent2){
        super(initialPosition);
        this.map = map;
        this.genome = new Genome(parent1, parent2);
        this.direction = MapDirection.rand();
        this.energy = parent1.birthMiracle() + parent2.birthMiracle();
        map.objectAdded(this.position, this);
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
        for(IPositionChangedObserver obs : observersList){
            obs.positionChanged(oldPosition, this.position, this);
        }
    }

    public int birthMiracle(){
        int energyLost = this.energy / 4;
        this.energy -= energyLost;
        return energyLost;
    }

    public boolean decreaseEnergy() {
        this.energy--;
        if(this.energy <= 0) {
            this.removeAnimal();
            return true;
        }
        return false;
    }

    public void plantEaten(Grass plant){
        this.energy += plant.getCaloricValue();
    }

    private void removeAnimal() {
        for(IPositionChangedObserver obs : this.observersList)
            obs.objectRemoved(this.getPosition(), this);
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
