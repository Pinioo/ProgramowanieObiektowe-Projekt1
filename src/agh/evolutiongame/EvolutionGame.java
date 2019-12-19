package agh.evolutiongame;

import agh.evolutiongame.abstracts.Game;
import agh.evolutiongame.maps.SafariMap;
import agh.evolutiongame.parameters.GameParameters;
import agh.evolutiongame.visualisers.terminal.ScreenCleaner;

public class EvolutionGame implements Game {
    private SafariMap map;
    private boolean paused;
    private long days;
    private long currentDay;
    private double delayRatio;
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
        this.paused = false;
        this.delayRatio = 1.0;
        this.parameters = parameters;
        this.days = parameters.days;
    }

    @Override
    public void start() {
        long startTime;
        long endTime;
        long duration;
        while (currentDay < days){
            // Timer for update() call
            startTime = System.currentTimeMillis();
            this.update();
            endTime = System.currentTimeMillis();

            // If update time was less than delay -> wait for difference of delay and method call time
            duration = endTime - startTime;
            if(this.getDelay() - duration > 0){
                try {
                    Thread.sleep(this.getDelay() - duration);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Clear the screen and show updated map
            ScreenCleaner.clear();
            this.drawMap();
            System.out.println();

            // Show additional stats
            System.out.println("Day: " + currentDay);
            System.out.println("Animals on map: " + this.map.animalsCount());

        }
    }

    @Override
    public void update() {
        this.map.newDay();
        currentDay++;
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
        return this.parameters;
    }

    public long getCurrentDay(){
        return this.currentDay;
    }

    @Override
    public boolean isPaused() {
        return paused;
    }

    public boolean isFinished(){
        return this.currentDay >= this.parameters.days;
    }

    public void togglePaused(){
        this.paused = !this.paused;
    }

    public int getDelay(){
        return (int) (this.delayRatio * this.parameters.delay);
    }

    public void setDelayRatio(double delayRatio) {
        this.delayRatio = delayRatio;
    }
}
