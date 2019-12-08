package agh.evolutiongame;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Vector;

public class Vector2d {
    final public int x;
    final public int y;

    public Vector2d(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean precedes(Vector2d other){
        if(this.x <= other.x && this.y <= other.y)
            return true;
        else
            return false;
    }

    public boolean follows(Vector2d other){
        if(this.x >= other.x && this.y >= other.y)
            return true;
        else
            return false;
    }

    public Vector2d upperRight(Vector2d other){
        return new Vector2d(Math.max(this.x, other.x), Math.max(this.y, other.y));
    }

    public Vector2d lowerLeft(Vector2d other){
        return new Vector2d(Math.min(this.x, other.x), Math.min(this.y, other.y));
    }

    public ArrayList<Vector2d> positionsAround(){
        ArrayList<Vector2d> list = new ArrayList<>();
        for(MapDirection direct : MapDirection.values()){
            list.add(this.add(direct.toUnitVector()));
        }
        return list;
    }

    public Vector2d add(Vector2d other){
        return new Vector2d(this.x + other.x, this.y + other.y);
    }

    public Vector2d subtract(Vector2d other){
        return new Vector2d(this.x - other.x, this.y - other.y);
    }

    public Vector2d opposite(){
        return new Vector2d(-this.x, -this.y);
    }

    @Override
    public boolean equals(Object other){
        if(this == other) return true;
        return other instanceof Vector2d && this.x == ((Vector2d) other).x && this.y == ((Vector2d) other).y;
    }

    @Override
    public String toString(){
        return "(" + this.x + "," + this.y + ")";
    }

    @Override
    public int hashCode() {
        int tmp = (y +  ((x+1)/2));
        return x + (tmp * tmp);
    }
}
