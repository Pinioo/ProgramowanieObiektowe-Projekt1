package agh.evolutiongame;

public class EvolutionWorld {
    public static void main(String[] args) {
        int days = 1000;
        Game game = new EvolutionGame(100, 30, 0.4, 100, 1, 200, 2600, days);
        game.start();
        game.end();
    }
}
