package agh.evolutiongame;

import agh.evolutiongame.interfaces.IMapElement;

import javax.swing.*;
import java.awt.*;
import java.awt.Rectangle;
import java.util.LinkedList;

public class SwingGamePanel extends JPanel {
    private int cellHeight;
    private int cellWidth;
    private EvolutionGame currentGame;
    private Color emptyColor = new Color(143, 140, 134);
    private Color grassColor =  new Color(50, 218, 50);
    private Color animalColor = new Color(230, 25, 5);

    SwingGamePanel(EvolutionGame game){
        super();
        this.currentGame = game;
        double WtoHratio = (double) game.getParameters().width / game.getParameters().height;
        double cellRatio = WtoHratio * 9.0 / 16.0;
        if (cellRatio > 1){
            this.setPreferredSize(new Dimension(1600, (int) (900.0 / cellRatio)));
        }
        else {
            this.setPreferredSize(new Dimension((int) (1600.0 * cellRatio), 900));
        }

        this.cellHeight = this.getPreferredSize().height / game.getParameters().height;
        this.cellWidth = this.getPreferredSize().width / game.getParameters().width;

        this.setPreferredSize(new Dimension(
                game.getParameters().width * this.cellWidth,
                game.getParameters().height * this.cellHeight
        ));

        this.setSize(this.getPreferredSize());
        this.setBackground(emptyColor);
    }

    private Color colorAtPosition(Vector2d position){
        LinkedList<IMapElement> elements = this.currentGame.getMap().objectsAt(position);
        if (elements == null) return emptyColor;
        if (elements.stream().anyMatch(x -> x instanceof Grass)) return grassColor;
        else {
            int maxEnergy = this.currentGame.getMap().strongestAnimalsOnPosition(position).get(0).getEnergy();
            int startEnergy = this.currentGame.getParameters().startEnergy;
            double ratio = (double)maxEnergy / startEnergy;
            int additionalGreen = ratio < 1 ? (int) (200 - ratio * 200) : 0;
            return new Color(animalColor.getRed(), animalColor.getGreen() + additionalGreen, animalColor.getBlue());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        for (Vector2d position : this.currentGame.getMap().area.positionsSet()){
            g2d.setColor(this.colorAtPosition(position));
            Rectangle toDrawRect = new Rectangle(
                    position.getX() * this.cellWidth,
                    position.getY() * this.cellHeight,
                    this.cellWidth,
                    this.cellHeight
            );
            g2d.fill(toDrawRect);
        }
    }
}
