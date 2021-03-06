package buildingModel;

/**
 * Describes displacement on the x and y axes of a 2D plane after taking a step in the given cardinal direction.
 */
public enum Direction {
    NORTH(0, -1),
    SOUTH(0, 1),
    WEST(-1, 0),
    EAST(1, 0);

    public final int dx;
    public final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }
}
