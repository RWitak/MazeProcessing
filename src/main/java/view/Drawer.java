package view;

import buildingModel.Direction;
import buildingModel.wall.RectangleWall;
import org.jetbrains.annotations.NotNull;
import processing.core.PApplet;
import processing.core.PShape;
import view.pShapes.BuilderSprite;

import java.awt.*;
import java.util.Iterator;

public record Drawer(PApplet proc, Point position, Direction direction,
                     int scale, int wall_width, int wall_height) {

    public void drawGround(PShape ground) {
        proc.push();
        proc.shape(ground);
        proc.pop();
    }

    public void drawBuilder() {
        proc.push();
        PShape builderSprite = BuilderSprite.getDirectionalPShape(scale, wall_width, direction, proc);
        builderSprite.translate(scale * (position.x + .5f), scale * (position.y + .5f));
        builderSprite.translate(0, 0, -wall_height / 2f);
        proc.shape(builderSprite);
        proc.pop();
    }

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

    public void drawPath(@NotNull Iterator<Point> waypoints) {
        while (waypoints.hasNext()) {
            Point p = waypoints.next();
            proc.push();
            proc.noStroke();
            if (waypoints.hasNext()) {
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
