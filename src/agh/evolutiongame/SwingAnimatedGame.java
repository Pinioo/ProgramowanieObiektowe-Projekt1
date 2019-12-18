package agh.evolutiongame;

import agh.evolutiongame.interfaces.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class SwingAnimatedGame extends JFrame {
    private JPanel panel;
    private GameStatsPanel statsPanel;
    private EvolutionGame currentGame;

    private Timer myTimer;


    public SwingAnimatedGame(EvolutionGame game){
        super("Evolution Game");

        this.currentGame = game;

        this.panel = new SwingGamePanel(game);
        this.statsPanel = new GameStatsPanel(game);

        this.add(this.panel);
        this.add(this.statsPanel, BorderLayout.EAST);

        this.panel.setVisible(true);
        this.statsPanel.setVisible(true);

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
        this.requestFocusInWindow();

        myTimer = new Timer(this.currentGame.getParameters().delay, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentGame.update();
                statsPanel.updateStats();
                panel.repaint();
                if (currentGame.getCurrentDay() > currentGame.getParameters().days)
                    ((Timer)e.getSource()).stop();
            }
        });
        this.start();
    }

    private void start() {
        Thread t = new Thread(myTimer::start);
        t.start();
    }

    private void stop() {
        myTimer.stop();
    }
}
