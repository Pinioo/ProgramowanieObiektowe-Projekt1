package agh.evolutiongame;

public class EvolutionWorld {
    public static void main(String[] args) {
        int days = 20;
        Game game = new EvolutionGame(days);
        game.start(new SafariMap(30, 15, 0.2, 2, 1, 20, 150));
        System.out.println("finish");
    }
}
