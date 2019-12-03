package agh.evolutiongame;

import java.util.Random;

public class SafariMap extends AbstractWorldMap {
    private Vector2d lowerLeft = new Vector2d(0,0);
    private Vector2d upperRight = new Vector2d(99,29);

    private Vector2d jungleLowerLeft = new Vector2d(45, 10);
    private Vector2d jungleUpperRight = new Vector2d(54, 19);
    private MapBoundary boundary = new MapBoundary();

    private int startingGrassEnergy = 10;

    private Random rand = new Random();

    public void newDay(){
        for(Animal an : this.animalsList){
            an.decreaseEnergy();
            an.randomRotate();
            an.moveForward();
        }
    }

    public Vector2d correctPosition(Vector2d requestedPosition){
        return new Vector2d(
                Math.floorMod(requestedPosition.getX(), this.upperRight.getX()),
                Math.floorMod(requestedPosition.getY(), this.upperRight.getY())
        );
    }

    private void randGrassInSafari(){
        Vector2d newPosition = Vector2d.randInRect(jungleLowerLeft, jungleUpperRight);

        //If newPosition is occupied this loop finds next available spot
        while(this.isOccupied(newPosition)){
            newPosition = nextPositionInSafari(newPosition);
        }

        Grass gr = new Grass(newPosition, this.startingGrassEnergy);
        gr.addObserver(this);
        gr.addObserver(this.boundary);
        this.objectAdded(newPosition, gr);
        this.boundary.objectAdded(newPosition, gr);
    }

    private void randGrassInJungle(){
        Vector2d newPosition = Vector2d.randInRect(jungleLowerLeft, jungleUpperRight);

        //If newPosition is occupied this loop finds next available spot
        while(this.isOccupied(newPosition)){
            newPosition = this.nextPositionInJungle(newPosition);
        }

        Grass gr = new Grass(newPosition, this.startingGrassEnergy);
        gr.addObserver(this);
        gr.addObserver(this.boundary);
        this.objectAdded(newPosition, gr);
        this.boundary.objectAdded(newPosition, gr);
    }

    private Vector2d nextPositionOnMap(Vector2d position){
        Vector2d nextPosition = new Vector2d(position.getX() + 1, position.getY());
        if(!nextPosition.precedes(this.upperRight))
            nextPosition = new Vector2d(0, position.getY() + 1);
        if(!nextPosition.follows(this.lowerLeft))
            nextPosition = new Vector2d(0,0);
        return nextPosition;
    }

    private Vector2d nextPositionInSafari(Vector2d position){
        Vector2d nextPosition = position;
        do{
            nextPosition = this.nextPositionOnMap(nextPosition);
        }while(nextPosition.follows(jungleLowerLeft) && nextPosition.precedes(jungleUpperRight));

        return nextPosition;
    }

    private Vector2d nextPositionInJungle(Vector2d position){
        Vector2d nextPosition = position;
        do{
            nextPosition = this.nextPositionOnMap(nextPosition);
        }while(!(nextPosition.follows(jungleLowerLeft) && nextPosition.precedes(jungleUpperRight)));

        return nextPosition;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return !(this.objectAt(position) instanceof Animal);
    }

    @Override
    public Vector2d getLowerLeft() {
        return this.boundary.lowerLeft();
    }

    @Override
    public Vector2d getUpperRight() {
        return this.boundary.upperRight();
    }
}
