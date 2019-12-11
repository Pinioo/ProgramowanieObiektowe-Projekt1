package agh.evolutiongame;

import agh.evolutiongame.interfaces.Game;

import java.io.IOException;

public class EvolutionGame implements Game {
    private SafariMap map;
    private final int days;

    public EvolutionGame(GameParameters parameters){
        if(parameters.map instanceof SafariMap)
            this.map = (SafariMap)parameters.map;
        else
            throw new IllegalArgumentException("Evolution game's map must be SafariMap!");
        this.days = parameters.days;
    }

    @Override
    public void start() {
        for(int i = 1; i <= days; i++){
            this.update();
            clearScreen();
            this.drawMap();
            System.out.println("Day: " + i);
            System.out.println("Animals on map: " + this.map.animalsCount());
            try {
                Thread.sleep(30);
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
