package agh.evolutiongame;

import agh.evolutiongame.interfaces.Game;
import org.w3c.dom.xpath.XPathResult;

public class EvolutionGame implements Game {
    private SafariMap map;
    private int days;
    private int delay;
    private GameParameters parameters;

    public EvolutionGame(GameParameters parameters){
        this.map = new SafariMap(
                parameters.width,
                parameters.height,
                parameters.jungleRatio,
                parameters.grassEnergy,
                parameters.moveEnergy,
                parameters.startEnergy,
                parameters.randomAnimals
        );
        this.parameters = parameters;
        this.days = parameters.days;
        this.delay = parameters.delay;
    }

    @Override
    public void start() {
        long startTime;
        long endTime;
        long duration;
        for(int i = 1; i <= days; i++){
            // Timer for update() call
            startTime = System.currentTimeMillis();
            this.update();
            endTime = System.currentTimeMillis();

            // If update time was less than delay -> wait for difference of delay and method call time
            duration = endTime - startTime;
            if(delay - duration > 0){
                try {
                    Thread.sleep(delay - duration);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Clear the screen and show updated map
            ScreenCleaner.clear();
            this.drawMap();
            System.out.println();

            // Show additional stats
            System.out.println("Day: " + i);
            System.out.println("Animals on map: " + this.map.animalsCount());

        }
    }

    @Override
    public void update() {
        this.map.newDay();
    }

    @Override
    public void end() {

    }

    public SafariMap getMap(){
        return this.map;
    }

    private void drawMap() {
        System.out.println(this.map);
    }

    public GameParameters getParameters() {
        return parameters;
    }
}
