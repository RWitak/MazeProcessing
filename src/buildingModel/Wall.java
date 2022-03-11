package buildingModel;

import java.awt.*;

/**
 * A Wall is the separation between two points, NOT a line FROM p1 TO p2!
 */
public class Wall {
    public Point getP1() {
        return p1;
    }

    public Point getP2() {
        return p2;
    }

    private final Point p1;
    private final Point p2;

    public Wall(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public Wall(int x1, int y1, int x2, int y2) {
        this.p1 = new Point(x1, y1);
        this.p2 = new Point(x2, y2);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Wall other) {
            return (this.p1.equals(other.p1) & this.p2.equals(other.p2)) |
                    (this.p1.equals(other.p2) & this.p2.equals(other.p1));
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "Wall between " +
                this.p1.toString() +
                ", " +
                this.p2.toString();
    }
}
