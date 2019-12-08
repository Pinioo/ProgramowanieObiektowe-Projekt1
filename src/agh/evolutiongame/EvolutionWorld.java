package agh.evolutiongame;

public class EvolutionWorld {
    public static void main(String[] args) {
        int days = 1000;
        Game game = new EvolutionGame(10, 5, 1.0, 100, 1, 200, 10, days);
        game.start();
        game.end();
    }
}
