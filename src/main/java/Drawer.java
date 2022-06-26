import buildingModel.Direction;
import buildingModel.wall.RectangleWall;
import org.jetbrains.annotations.NotNull;
import processing.core.PApplet;
import processing.core.PShape;
import view.pShapes.BuilderSprite;

import java.awt.*;
import java.util.Iterator;

/**
 * A delegate for {@link ProcessingMain} containing all concrete drawing logic for building a maze.
 * @param proc The calling {@link PApplet}.
 * @param position The current {@link Point} position in the maze.
 * @param direction The {@link Direction} for the {@link BuilderSprite}.
 * @param scale The side length in pixels of each positional square <code>Point</code>
 * @param wall_width The width of maze walls in pixels.
 * @param wall_height The height of maze walls in pixels.
 */
public record Drawer(PApplet proc, Point position, Direction direction,
                     int scale, int wall_width, int wall_height) {

    /**
     * Draws the given {@link PShape} as the maze's ground.
     * @param ground The <code>PShape</code> to be placed at the bottom of the maze.
     */
    public void drawGround(PShape ground) {
        proc.push();
        proc.shape(ground);
        proc.pop();
    }

    /**
     * Draws the {@link BuilderSprite} for the position and direction given by the constructor.
     * @see Drawer
     */
    public void drawBuilder() {
        proc.push();
        PShape builderSprite = BuilderSprite.getDirectionalPShape(scale - wall_width, direction, proc);
        builderSprite.translate(scale * (position.x + .5f), scale * (position.y + .5f));
        builderSprite.translate(0, 0, -wall_height / 2f);
        proc.shape(builderSprite);
        proc.pop();
    }

    /**
     * @param rectWall The unscaled {@link RectangleWall} using theoretical positioning.
     * @param wallVertical The scalable {@link PShape} representing a wall in 3 dimensions.
     */
    public void drawWall(@NotNull RectangleWall rectWall, PShape wallVertical) {
        proc.push();
        proc.translate((float) rectWall.getRect().getCenterX() + scale / 2f,
                (float) rectWall.getRect().getCenterY() + scale / 2f);

        if (rectWall.isHorizontal()) {
            proc.rotateZ(proc.HALF_PI);
        }
        proc.shape(wallVertical);
        proc.pop();
    }

    /**
     * Draws the path created by building the maze.
     * @param waypoints All waypoints where the maze has not been completed.
     */
    public void drawPath(@NotNull Iterator<Point> waypoints) {
        while (waypoints.hasNext()) {
            Point p = waypoints.next();
            proc.push();
            proc.noStroke();

            if (waypoints.hasNext()) {
                // special coloring for the most recent previous position.
                proc.fill(150);
            } else {
                proc.fill(255, 0, 0);
            }
            proc.translate((float) ((p.getX() + .5f) * scale), (float) (p.getY() + .5f) * scale);
            proc.sphere(scale / 10f);
            proc.pop();
        }
    }
}
