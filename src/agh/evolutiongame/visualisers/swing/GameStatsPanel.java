package agh.evolutiongame.visualisers.swing;

import agh.evolutiongame.EvolutionGame;

import javax.swing.*;
import java.awt.*;

public class GameStatsPanel extends JPanel {
    private EvolutionGame currentGame;
    private JLabel animalCountLabel;
    private JLabel dayLabel;

    public GameStatsPanel(EvolutionGame game) {
        super();

        this.currentGame = game;

        this.animalCountLabel = new JLabel();
        this.dayLabel = new JLabel();

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        this.setBackground(new Color(209, 206, 200));

        this.add(animalCountLabel);
        this.add(dayLabel);

        this.dayLabel.setVisible(true);
        this.animalCountLabel.setVisible(true);
    }

    public void updateStats(){
        animalCountLabel.setText("Animals: " + currentGame.getMap().animalsCount());
        dayLabel.setText("Day: " + currentGame.getCurrentDay());
    }
}
