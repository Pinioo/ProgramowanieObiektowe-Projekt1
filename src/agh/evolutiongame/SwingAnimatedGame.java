package agh.evolutiongame;

import agh.evolutiongame.interfaces.Game;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SwingAnimatedGame extends JFrame {
    private JPanel panel;
    private EvolutionGame currentGame;

    public SwingAnimatedGame(EvolutionGame game){
        super("Evolution Game");

        this.currentGame = game;

        panel = new SwingGamePanel(game);
        add(panel);
        panel.setVisible(true);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
        this.requestFocusInWindow();
        this.start();
    }

    private void start() {
        Timer myTimer = new Timer(this.currentGame.getParameters().delay, new ActionListener() {
            int currentDay = 1;
            public void actionPerformed(ActionEvent e) {
                currentDay++;
                currentGame.update();
                panel.repaint();
                if (currentDay >= currentGame.getParameters().days)
                    ((Timer)e.getSource()).stop();
            }
        });

        Thread t = new Thread(myTimer::start);
        t.start();
    }
}
