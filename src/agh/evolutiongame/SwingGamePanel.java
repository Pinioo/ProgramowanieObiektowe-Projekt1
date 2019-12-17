package agh.evolutiongame;

import agh.evolutiongame.interfaces.Game;
import agh.evolutiongame.interfaces.IMapElement;

import javax.swing.*;
import java.awt.*;
import java.awt.Rectangle;
import java.util.LinkedList;

public class SwingGamePanel extends JPanel {
    int cellHeight;
    int cellWidth;
    EvolutionGame currentGame;

    SwingGamePanel(EvolutionGame game){
        super();
        this.currentGame = game;
        double WtoHratio = (double) game.getParameters().width / game.getParameters().height;
        double cellRatio = WtoHratio * 9.0 / 16.0;
        if (cellRatio > 1){
            this.setPreferredSize(new Dimension(1440, (int) (810.0 / cellRatio)));
        }
        else {
            this.setPreferredSize(new Dimension((int) (1440.0 * cellRatio), 810));
        }
        this.setSize(this.getPreferredSize());

        this.cellHeight = this.getHeight() / game.getParameters().height;
        this.cellWidth = this.getWidth() / game.getParameters().width;
    }

    private Color colorAtPosition(Vector2d position){
        LinkedList<IMapElement> elements = this.currentGame.getMap().objectsAt(position);
        if (elements == null) return Color.DARK_GRAY;
        if (elements.stream().anyMatch(x -> x instanceof Grass)) return new Color(133, 183, 80);
        else {
            int maxEnergy = this.currentGame.getMap().strongestAnimalsOnPosition(position).get(0).getEnergy();
            int startEnergy = this.currentGame.getParameters().startEnergy;
            double ratio = (double)maxEnergy / startEnergy;
            int alpha = ratio < 1 ? (int) (55 + ratio * 200) : 255;
            return new Color(255, 0, 0, alpha);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        for (Vector2d position : this.currentGame.getMap().area.positionsSet()){
            g2d.setColor(this.colorAtPosition(position));
            g2d.fill(new Rectangle(
                    position.getX() * this.cellWidth,
                    position.getY() * this.cellHeight,
                    this.cellWidth,
                    this.cellHeight)
            );
        }
    }
}
