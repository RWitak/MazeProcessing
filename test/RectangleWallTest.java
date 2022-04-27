import buildingModel.wall.RectangleWall;
import buildingModel.wall.Wall;
import org.junit.jupiter.api.Test;

import java.awt.geom.Rectangle2D;

import static org.junit.jupiter.api.Assertions.*;

class RectangleWallTest {
    @Test
    void recreatesBasicHorizontalWall() {
        Wall wall = new Wall(1, 1, 1, 2);
        Rectangle2D.Float rect = new RectangleWall(wall).getRect();
        assertArrayEquals(
                new float[]{0.5f, 1.5f, 1.5f, 1.5f},
                new float[]{rect.x, rect.y, rect.x + rect.width, rect.y + rect.height}
        );
    }

    @Test
    void recreatesBasicVerticalWall() {
        Wall wall = new Wall(4, 3, 3, 3);
        Rectangle2D.Float rect = new RectangleWall(wall).getRect();

        assertArrayEquals(
                new float[]{3.5f, 2.5f, 3.5f, 3.5f},
                new float[]{rect.x, rect.y, rect.x + rect.width, rect.y + rect.height}
        );
    }

    @Test
    void recreatesScaledWalls() {
        final Wall wallV = new Wall(4, 3, 3, 3);
        final Rectangle2D.Float rectV = new RectangleWall(wallV, 3, 2).getRect();

        assertArrayEquals(
                new float[]{10.5f, 5f, 10.5f, 7f},
                new float[]{rectV.x, rectV.y, rectV.x + rectV.width, rectV.y + rectV.height}
        );

        final Wall wallH = new Wall(3, 3, 3, 4);
        final Rectangle2D.Float rectH = new RectangleWall(wallH, 5, 10).getRect();

        assertArrayEquals(
                new float[]{12.5f, 35f, 17.5f, 35f},
                new float[]{rectH.x, rectH.y, rectH.x + rectH.width, rectH.y + rectH.height}
        );
    }

    @Test
    void correctOutputFromNegativeValues() {
        final Wall wall = new Wall(-1, -2, 0, -2);
        final Rectangle2D.Float rect = new RectangleWall(wall).getRect();

        assertArrayEquals(
                new float[]{-0.5f, -2.5f, -0.5f, -1.5f},
                new float[]{rect.x, rect.y, rect.x + rect.width, rect.y + rect.height}
        );
    }

    @Test
    void tellsIfHorizontal() {
        final Wall wallH = new Wall(30, 2, 31, 2);
        final Wall wallV = new Wall(-3, 0, -3, -1);

        assertTrue(new RectangleWall(wallH).isHorizontal());
        assertFalse(new RectangleWall(wallV).isHorizontal());
    }
}