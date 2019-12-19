package agh.evolutiongame.visualisers.swing;

import agh.evolutiongame.EvolutionGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SwingAnimatedGame extends JFrame {
    private JPanel panel;
    private GameStatsPanel statsPanel;
    private EvolutionGame currentGame;

    private final Timer mapTimer;
    private final Timer updateManager;

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

        // Timer for map activities
        mapTimer = new Timer((int) this.currentGame.getParameters().delay, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentGame.update();
                panel.repaint();
            }
        });

        // Timer for applying game state changes and updating statistics
        updateManager = new Timer((int) this.currentGame.getParameters().delay / 5, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mapTimer.setDelay(currentGame.getDelay());
                statsPanel.updateStats();
                if (currentGame.isPaused()) {
                    mapTimer.stop();
                } else if (!mapTimer.isRunning()){
                    mapTimer.start();
                }
                else if (currentGame.isFinished()){
                    updateManager.stop();
                    mapTimer.stop();
                }
            }
        });

        this.start();
    }

    private void start(){
        this.mapTimer.start();
        this.updateManager.start();
    };
}
