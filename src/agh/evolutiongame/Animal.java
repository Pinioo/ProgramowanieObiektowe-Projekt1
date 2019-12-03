package agh.evolutiongame;

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
    }

    public Animal(IWorldMap map, Animal parent1, Animal parent2){
        super(parent1.getPosition());
        this.map = map;
        this.genome = new Genome(parent1, parent2);
        this.direction = MapDirection.rand();
    }

    public void randomRotate(){
        this.rotateBy((new Random()).nextInt(32));
    }

    private void rotateBy(int factor){
        this.direction = this.direction.rotateBy(factor);
    }

    public MapDirection getDirection(){
        return this.direction;
    }

    public void moveForward(){
        Vector2d oldPosition = this.position;
        this.position = this.map.correctPosition(this.position.add(this.direction.toUnitVector()));
        for(IPositionChangedObserver obs : observersList){
            obs.positionChanged(oldPosition, this.position);
        }
    }

    public void decreaseEnergy() {
        energy--;
        if(this.energy <= 0)
            this.removeAnimal();
    }

    public void plantEaten(Grass plant){
        this.energy += plant.getCaloricValue();
    }

    private void removeAnimal() {
        for(IPositionChangedObserver obs : this.observersList)
            obs.objectRemoved(this.getPosition());
    }

    public Genome getGenome() {
        return genome;
    }

    @Override
    public String toString(){
        return this.direction.toString();
    }
}
