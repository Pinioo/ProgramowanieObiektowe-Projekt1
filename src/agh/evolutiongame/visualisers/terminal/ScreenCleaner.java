package agh.evolutiongame.visualisers.terminal;

import java.io.IOException;

public class ScreenCleaner {
    // Static method to clear screen in Windows based terminals
    public static void clear() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
