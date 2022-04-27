package buildingModel.wall;

import lombok.NonNull;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class RectangleWall {
    private final Rectangle2D.Float rect;
    private final float scalarX;
    private final float scalarY;
    private boolean horizontal;

    public RectangleWall(@NonNull Wall wall) {
        this(wall, 1, 1);
    }

    public RectangleWall(@NonNull Wall wall, float scalarX, float scalarY) {
        this.scalarX = scalarX;
        this.scalarY = scalarY;
        this.rect = toRectangle2DFloat(wall);
    }

    private Rectangle2D.Float toRectangle2DFloat(Wall wall) {
        final Point p1 = wall.getP1();
        final Point p2 = wall.getP2();
        final Point2D.Float center = midpoint(p1, p2);
        this.horizontal = isHorizontal(p1, p2);
        return horizontal ? rectHorizontal(center) : rectVertical(center);
    }

    private boolean isHorizontal(Point p1, Point p2) {
        return p1.y == p2.y;
    }

    public boolean isHorizontal() {
        return this.horizontal;
    }

    private Point2D.Float midpoint(Point p1, Point p2) {
        float centerX = ((float) p1.x + (float) p2.x) / 2f;
        float centerY = ((float) p1.y + (float) p2.y) / 2f;
        return new Point2D.Float(centerX, centerY);
    }

    private Rectangle2D.Float rectHorizontal(Point2D.Float center) {
        float origX = center.x * scalarX;
        float origY = (center.y - .5f) * scalarY;
        return new Rectangle2D.Float(origX, origY, 0f, scalarY);
    }

    private Rectangle2D.Float rectVertical(Point2D.Float center) {
        float origX = (center.x - .5f) * scalarX;
        float origY = center.y * scalarY;
        return new Rectangle2D.Float(origX, origY, scalarX, 0f);
    }

    public Rectangle2D.Float getRect() {
        return this.rect;
    }
}
