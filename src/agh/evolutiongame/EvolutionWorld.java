package agh.evolutiongame;

public class EvolutionWorld {
    public static void main(String[] args) {
        Game game = new EvolutionGame();
        game.start(new SafariMap(15, 8, 0.3, 10));
        System.out.println("finish");
    }
}
