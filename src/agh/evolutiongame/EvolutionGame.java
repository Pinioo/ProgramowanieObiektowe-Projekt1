package agh.evolutiongame;

import java.io.IOException;

public class EvolutionGame implements Game {
    private SafariMap map;
    private final int days;

    public EvolutionGame(int width, int height, double jungleRatio, int grassEnergy, int moveEnergy, int startEnergy, int randomAnimals, int days){
        this.map = new SafariMap(width, height, jungleRatio, grassEnergy, moveEnergy, startEnergy, randomAnimals);
        this.days = days;
    }

    @Override
    public void start() {
        for(int i = 1; i <= days; i++){
            clearScreen();
            this.update();
            this.drawMap();
            System.out.println("Day: " + i);
            System.out.println("Animals on map: " + this.map.animalsCount());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void update() {
        this.map.newDay();
    }

    @Override
    public void end() {

    }

    public void drawMap() {
        System.out.println(this.map);
    }

    public static void clearScreen() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
