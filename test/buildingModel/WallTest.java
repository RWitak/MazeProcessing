package buildingModel;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WallTest {

    @Test
    void testEquals() {

        final Point p1 = new Point(88, 6);
        final Point p2 = new Point(88, 5);

        assertEquals(
                new Wall(p1, p2),
                new Wall(new Point(88, 5), new Point(88, 6)));
    }

    @Test
    void testToString() {
        final Wall wall = new Wall(new Point(312, 420), new Point(313, 420));
        assertTrue(wall.toString().contains("312") &
                wall.toString().contains("313") &
                wall.toString().contains("420")
        );
    }

    @Test
    void providesPointsFromCoordinates() {
        int x1 = 112;
        int y1 = 223;
        int x2 = 112;
        int y2 = 224;

        List<Point> points = List.of(new Point(x1, y1), new Point(x2, y2));

        final Wall wall = new Wall(x1, y1, x2, y2);
        assertTrue(points.containsAll(List.of(wall.getP1(), wall.getP2())));
    }

    @Test
    void rejectsNonAdjacentPoints() {
        assertThrows(IllegalArgumentException.class,
                () -> new Wall(123, 456, 0, 4));
    }

    @Test
    void rejectsIdenticalPoints() {
        assertThrows(IllegalArgumentException.class,
                () -> new Wall(17, 4, 17, 4));
    }
}