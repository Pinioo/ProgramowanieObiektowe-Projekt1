package agh.evolutiongame;

import java.util.Random;

public class Rectangle {
    public final Vector2d lowerLeft;
    public final Vector2d upperRight;

    public Rectangle(Vector2d lowerLeft, Vector2d upperRight) {
        this.lowerLeft = lowerLeft;
        this.upperRight = upperRight;
    }

    public boolean isPointInside(Vector2d position){
        return position.follows(this.lowerLeft) && position.precedes(this.upperRight);
    }

    public Vector2d randPoint(){
        Random rand = new Random();
        int x = this.lowerLeft.getX() + rand.nextInt(this.upperRight.getX() - this.lowerLeft.getX() + 1);
        int y = this.lowerLeft.getY() + rand.nextInt(this.upperRight.getY() - this.lowerLeft.getY() + 1);
        return new Vector2d(x, y);
    }

    public Rectangle scale(double scale){
        if(!this.lowerLeft.precedes(upperRight))
            throw new IllegalArgumentException("Rectangle must be represented by lower left and upper right corners");
        else {
            //Vector moved to (0,0)
            Vector2d tmpUpperRight = this.upperRight.subtract(lowerLeft);

            Vector2d resultUpperRight = new Vector2d((int)(tmpUpperRight.getX() * scale), (int)(tmpUpperRight.getY() * scale));

            Vector2d diffTmp = tmpUpperRight.subtract(resultUpperRight);

            resultUpperRight = resultUpperRight.add(new Vector2d(diffTmp.getX()/2, diffTmp.getY()/2));
            Vector2d resultLowerLeft = new Vector2d(diffTmp.getX()/2, diffTmp.getY()/2);

            return new Rectangle(resultLowerLeft.add(lowerLeft), resultUpperRight.add(lowerLeft));
        }
    }
}
