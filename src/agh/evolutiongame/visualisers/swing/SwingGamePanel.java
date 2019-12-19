package agh.evolutiongame.visualisers.swing;

import agh.evolutiongame.EvolutionGame;
import agh.evolutiongame.spatialclasses.Vector2d;
import agh.evolutiongame.abstracts.IMapElement;
import agh.evolutiongame.mapelements.Grass;

import javax.swing.*;
import java.awt.*;
import java.awt.Rectangle;
import java.util.LinkedList;

public class SwingGamePanel extends JPanel {
    private int cellHeight;
    private int cellWidth;
    private EvolutionGame currentGame;

    public static final Color emptyColor = new Color(209, 206, 200);
    public static final Color grassColor =  new Color(45, 179, 45);
    public static final Color animalColor = new Color(230, 25, 5);

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

        this.cellHeight = (int) (this.getPreferredSize().height / game.getParameters().height);
        this.cellWidth = (int) (this.getPreferredSize().width / game.getParameters().width);

        this.setPreferredSize(new Dimension(
                (int) game.getParameters().width * this.cellWidth,
                (int) game.getParameters().height * this.cellHeight
        ));

        this.setSize(this.getPreferredSize());
        this.setBackground(emptyColor);
    }

    private Color colorAtPosition(Vector2d position){
        LinkedList<IMapElement> elements = this.currentGame.getMap().objectsAt(position);
        if (elements == null) return emptyColor;
        if (elements.stream().anyMatch(x -> x instanceof Grass)) return grassColor;
        else {
            long maxEnergy = this.currentGame.getMap().strongestAnimalsOnPosition(position).get(0).getEnergy();
            long startEnergy = this.currentGame.getParameters().startEnergy;
            double ratio = (double)maxEnergy / startEnergy;
            int additionalGreen = ratio < 1 ? (int) (200 - ratio * 200) : 0;
            return new Color(animalColor.getRed(), animalColor.getGreen() + additionalGreen, animalColor.getBlue());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        for (Vector2d position : this.currentGame.getMap().getArea().positionsSet()){
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
