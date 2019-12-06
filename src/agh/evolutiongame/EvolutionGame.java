package agh.evolutiongame;

public class EvolutionGame implements Game {
    private SafariMap map;

    @Override
    public void start(IWorldMap map) {
        if(map instanceof SafariMap)
            this.map = (SafariMap)map;
        else
            throw new IllegalArgumentException("Map " + map.getClass().getName() + " is incompatible with EvolutionGame");
        System.out.println(map.toString());
        for(int i = 0; i < 200; i++){
            clearScreen();
            this.update();
        }
    }

    @Override
    public void update() {
        this.map.newDay();
        System.out.println(map.toString());
    }

    @Override
    public void end() {

    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
