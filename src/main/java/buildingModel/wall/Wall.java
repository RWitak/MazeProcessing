package buildingModel.wall;

import java.awt.*;
import java.util.Set;

/**
 * A Wall is the <em>separation</em> between two given {@link Point}s.
 * WARNING: It is <strong>not</strong> a line <strong>from</strong> point1 <strong>to</strong> point2!
 */
public class Wall {
    private final Point p1;
    private final Point p2;

    /**
     * Construct a {@link Wall} by giving two {@link Point}s that <code>Wall</code> should separate.
     * The order of the <code>Point</code>s is irrelevant.
     * The <code>Point</code>s must be adjacent either vertically or horizontally.
     * @param p1 A <code>Point</code>
     * @param p2 An adjacent <code>Point</code>
     */
    public Wall(Point p1, Point p2) {
        if (notAdjacentPoints(p1, p2)) {
            String msg = String.format("Walls can only separate adjacent points! (Given: %s, %s)", p1, p2);
            throw new IllegalArgumentException(msg);
        } else if (identicalPoints(p1, p2)) {
            String msg = String.format("Walls can't be built between identical points! (Given: %s, %s)", p1, p2);
            throw new IllegalArgumentException(msg);
        }

        this.p1 = p1;
        this.p2 = p2;
    }

    /**
     * Construct a {@link Wall} by giving the coordinates of two locations that <code>Wall</code> should separate.
     * The order of the locations is irrelevant.
     * They must be adjacent either vertically or horizontally.
     * @param x1 The x-coordinate of a location A.
     * @param y1 The y-coordinate of a location A.
     * @param x2 The x-coordinate of a location B that is adjacent to A.
     * @param y2 The y-coordinate of a location B that is adjacent to A.
     */
    public Wall(int x1, int y1, int x2, int y2) {
        this(new Point(x1, y1), new Point(x2, y2));
    }


    /**
     * @return One of the two {@link Point}s this {@link Wall} separates.
     * (The one that is not returned by {@link #getP2()}).
     */
    public Point getP1() {
        return p1;
    }

    /**
     * @return One of the two {@link Point}s this {@link Wall} separates.
     * (The one that is not returned by {@link #getP1()}.)
     */
    public Point getP2() {
        return p2;
    }

    private boolean identicalPoints(Point p1, Point p2) {
        return p1.distance(p2) == 0;
    }

    private boolean notAdjacentPoints(Point p1, Point p2) {
        return p1.distance(p2) > 1;
    }

    /**
     * @param obj Another {@link Wall} (Will return <code>false</code> for all other objects.)
     * @return <code>true</code> if (and only if) the two <code>Wall</code>s are separating the same two {@link Point}s.
     * The <code>Point</code>s do not have to be in the same order or the same instances of <code>Point</code>.
     */
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Wall other) &&
                ((this.p1.equals(other.p1) && this.p2.equals(other.p2)) ||
                (this.p1.equals(other.p2) && this.p2.equals(other.p1)));
    }

    @Override
    public String toString() {
        return "Wall between " +
                this.p1.toString() +
                ", " +
                this.p2.toString();
    }
}
