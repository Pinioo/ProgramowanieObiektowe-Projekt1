package agh.evolutiongame.visualisers.swing;

import agh.evolutiongame.EvolutionGame;

import javax.swing.*;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class GameStatsPanel extends JPanel implements ActionListener {
    private EvolutionGame currentGame;
    private JLabel animalCountLabel;
    private JLabel dayLabel;
    private JButton pauseButton;
    private JSlider delayRatioSlider;

    private final int maxDelayPercentage = 500;
    private final int minDelayPercentage = 20;
    private final int startingDelayPercentage = 100;

    public GameStatsPanel(EvolutionGame game) {
        super();

        this.currentGame = game;

        this.animalCountLabel = new JLabel();
        this.dayLabel = new JLabel();

        this.pauseButton = new JButton();
        this.pauseButton.addActionListener(this);

        this.delayRatioSlider = new JSlider(JSlider.HORIZONTAL, minDelayPercentage, maxDelayPercentage, startingDelayPercentage);
        this.delayRatioSlider.setMajorTickSpacing(40);
        this.delayRatioSlider.setMinorTickSpacing(10);
        this.delayRatioSlider.setBackground(SwingGamePanel.emptyColor);

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        this.setBackground(new Color(209, 206, 200));

        this.add(animalCountLabel);
        this.add(dayLabel);
        this.add(pauseButton);
        this.add(delayRatioSlider);

        this.dayLabel.setVisible(true);
        this.animalCountLabel.setVisible(true);
        this.pauseButton.setVisible(true);
        this.delayRatioSlider.setVisible(true);
    }

    private void toggleGamePaused(){
        this.currentGame.togglePaused();
    }

    private void updateGameDelayRatio(){
        if (this.currentGame.isFinished())
            this.delayRatioSlider.setEnabled(false);
        this.currentGame.setDelayRatio(100.0 / this.delayRatioSlider.getValue());
    }

    private void updatePauseButtonLabel(){
        if (this.currentGame.isFinished()){
            this.pauseButton.setText("Finished");
            this.pauseButton.setEnabled(false);
        }
        else if (this.currentGame.isPaused()){
            this.pauseButton.setText("Continue");
        } else{
            this.pauseButton.setText("Pause");
        }
    }

    private void updateAnimalCountLabel(){
        animalCountLabel.setText("Animals: " + currentGame.getMap().animalsCount());
    }

    private void updateDayLabel(){
        dayLabel.setText("Day: " + currentGame.getCurrentDay());
    }

    public void updateStats(){
        this.updateGameDelayRatio();
        this.updateDayLabel();
        this.updateAnimalCountLabel();
        this.updatePauseButtonLabel();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == this.pauseButton){
            this.toggleGamePaused();
        }
    }
}
