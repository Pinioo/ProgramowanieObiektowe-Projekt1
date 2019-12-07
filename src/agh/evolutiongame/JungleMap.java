package agh.evolutiongame;

public class JungleMap extends AbstractWorldMap {
    public JungleMap(Vector2d lowerLeft, Vector2d upperRight) {
        super(lowerLeft, upperRight);
    }

    public JungleMap(Rectangle area){
        super(area);
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
