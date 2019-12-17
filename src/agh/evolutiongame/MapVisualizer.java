package agh.evolutiongame;

import agh.evolutiongame.interfaces.IWorldMap;

/**
 * The map visualizer converts the {@link IWorldMap} map into a string
 * representation.
 *
 * @author apohllo
 */
public class MapVisualizer {
    private static final String CELL_SEGMENT = "|";
    private IWorldMap map;

    /**
     * Initializes the MapVisualizer with an instance of map to visualize.
     *
     * @param map map to visualize
     */
    public MapVisualizer(IWorldMap map) {
        this.map = map;
    }

    /**
     * Convert selected region of the map into a string. It is assumed that the
     * indices of the map will have no more than two characters (including the
     * sign).
     *
     * @param lowerLeft  The lower left corner of the region that is drawn.
     * @param upperRight The upper right corner of the region that is drawn.
     * @return String representation of the selected region of the map.
     */
    public String draw(Vector2d lowerLeft, Vector2d upperRight) {
        StringBuilder builder = new StringBuilder();
        for (int i = upperRight.y; i >= lowerLeft.y; i--) {
            for (int j = lowerLeft.x; j <= upperRight.x; j++) {
                builder.append(CELL_SEGMENT);
                builder.append(drawObject(new Vector2d(j, i)));
            }
            builder.append(CELL_SEGMENT);
            builder.append(System.lineSeparator());
        }
        return builder.toString();
    }

    private String drawObject(Vector2d currentPosition) {
        return this.map.symbolOnPosition(currentPosition);
    }
}