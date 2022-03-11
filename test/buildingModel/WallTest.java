package buildingModel;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class WallTest {

    @Test
    void testEquals() {

        final Point p1 = new Point(88, 6);
        final Point p2 = new Point(102, 5);

        assertNotEquals(
                new Wall(p1, p2),
                new Object() {
                    public final Point x = p1;
                    public final Point y = p2;
                });

        assertEquals(
                new Wall(p1, p2),
                new Wall(new Point(102, 5), new Point(88, 6)));
    }

    @Test
    void testToString() {
        final Wall wall = new Wall(new Point(147, 420), new Point(313, 444));
        assertTrue(wall.toString().contains("147") &
                wall.toString().contains("420") &
                wall.toString().contains("313") &
                wall.toString().contains("444")
        );
    }
}