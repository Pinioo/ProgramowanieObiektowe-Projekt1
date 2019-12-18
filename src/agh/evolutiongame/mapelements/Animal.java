package agh.evolutiongame.mapelements;

import agh.evolutiongame.Genome;
import agh.evolutiongame.spatialclasses.MapDirection;
import agh.evolutiongame.spatialclasses.Vector2d;
import agh.evolutiongame.abstracts.IWorldMap;
import agh.evolutiongame.abstracts.AbstractObservedMapElement;

public class Animal extends AbstractObservedMapElement {
    private MapDirection direction;
    private IWorldMap map;
    private Genome genome;

    private int energy;
    private final int startingEnergy;

    public Animal(IWorldMap map, Vector2d initialPosition, int energy){
        super(initialPosition);
        this.map = map;

        // Creating random genome
        this.genome = new Genome();
        this.direction = MapDirection.rand();

        // Informs map that animal was created at position = initialPosition
        map.objectAdded(this.position, this);
        this.energy = energy;
        this.startingEnergy = energy;
    }

    public Animal(IWorldMap map, Vector2d initialPosition, Animal parent1, Animal parent2){
        super(initialPosition);
        this.map = map;

        // Animal inherits genome from it's parents
        this.genome = new Genome(parent1,parent2);
        this.direction = MapDirection.rand();

        // Informs map that Animal was created at position = initialPosition
        map.objectAdded(this.position, this);

        // Born animal's energy is sum of 1/4 parents' energies
        this.energy = parent1.animalReproduced() + parent2.animalReproduced();
        this.startingEnergy = this.energy;
    }

    // Random rotate depending on gene chosen randomly
    public void randomRotate(){
        this.rotateBy(this.genome.getRandomGene());
    }

    private void rotateBy(int factor){
        this.direction = this.direction.rotateBy(factor);
    }

    public void moveForward(){
        Vector2d oldPosition = this.position;

        // As map is looped, check which position to occupy
        this.position = this.map.correctPosition(this.position.add(this.direction.toUnitVector()));
        this.positionChanged(oldPosition, this.position);
    }

    // Animal can reproduce if it's energy is at least half of it's starting energy
    public boolean canReproduce(){
        return 2*this.energy >= this.startingEnergy;
    }

    // Method called if animal is about to reproduce
    // Returns energy lost during this process
    private int animalReproduced(){
        int energyLost = this.energy / 4;
        this.energy -= energyLost;
        return energyLost;
    }

    public void increaseEnergy(int energyDelta) {
        this.energy += energyDelta;
    }

    // Decreases energy
    // If animal is dead after -> remove it and return true
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
        return Integer.compare(an1.energy, an2.energy);
    }

    @Override
    public String toString(){
        return "A";
    }
}
