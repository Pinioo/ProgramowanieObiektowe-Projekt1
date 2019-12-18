package agh.evolutiongame.maps;

import agh.evolutiongame.abstracts.AbstractWorldMap;
import agh.evolutiongame.spatialclasses.Rectangle;
import agh.evolutiongame.spatialclasses.Vector2d;

public class JungleMap extends AbstractWorldMap {
    public JungleMap(Vector2d lowerLeft, Vector2d upperRight) {
        super(lowerLeft, upperRight);
    }

    public JungleMap(Rectangle area){
        super(area);
    }

    public void randGrass(int caloricValue){
        super.randGrass(caloricValue);
    }

    @Override
    public Vector2d correctPosition(Vector2d requestedPosition) {
        return requestedPosition;
    }

    @Override
    public Vector2d getLowerLeft() {
        return this.area.lowerLeft;
    }

    @Override
    public Vector2d getUpperRight() {
        return this.area.lowerLeft;
    }

    @Override
    public String symbolOnPosition(Vector2d currentPosition) {
        return null;
    }
}
