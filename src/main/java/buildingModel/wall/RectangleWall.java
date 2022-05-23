package buildingModel.wall;

import lombok.NonNull;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * A wrapper for {@link Rectangle2D.Float},
 * used mainly to convert a {@link Wall} into a <code>Rectangle</code>.
 * <br/>
 * The resulting <code>Wall</code> is scaled along both axes individually.
 * It also differs from a <code>Wall</code> in that it is not defined by being
 * <em>between</em> <code>Point</code>s, but has an actual location and dimensions.
 * It also provides info about its orientation.
 */
public class RectangleWall {
    private final Rectangle2D.Float rect;
    private final float scalarX;
    private final float scalarY;
    private boolean horizontal;

    /**
     * Constructor to create an unscaled {@link RectangleWall}.
     * @param wall The {@link Wall} to be converted.
     * @see #toRectangle2DFloat(Wall)
     */
    public RectangleWall(@NonNull Wall wall) {
        this(wall, 1, 1);
    }

    /**
     * Constructor to create a scaled {@link RectangleWall}.
     * @param wall The {@link Wall} to be converted.
     * @param scalarX The scale along the x-axis.
     * @param scalarY The scale along the y-axis.
     * @see #toRectangle2DFloat(Wall)
     */
    public RectangleWall(@NonNull Wall wall, float scalarX, float scalarY) {
        this.scalarX = scalarX;
        this.scalarY = scalarY;
        this.rect = toRectangle2DFloat(wall);
    }

    /**
     * Converts the given {@link Wall} to a {@link Rectangle2D.Float} for internal representation
     * and sets this {@link RectangleWall}'s orientation.
     * @param wall The <code>Wall</code> to be converted.
     * @return The resulting <code>Rectangle2D.Float</code>.
     */
    private Rectangle2D.Float toRectangle2DFloat(@NotNull Wall wall) {
        final Point p1 = wall.getP1();
        final Point p2 = wall.getP2();
        final Point2D.Float center = midpoint(p1, p2);
        this.horizontal = isHorizontal(p1, p2);
        return horizontal ? rectHorizontal(center) : rectVertical(center);
    }

    private boolean isHorizontal(@NotNull Point p1, @NotNull Point p2) {
        return p1.y == p2.y;
    }

    /**
     * @return <code>true</code> if the original {@link Wall} given to the constructor
     * separates two points on the same y-axis
     */
    public boolean isHorizontal() {
        return this.horizontal;
    }

    @Contract(value = "_, _ -> new", pure = true)
    private Point2D.@NotNull Float midpoint(@NotNull Point p1, @NotNull Point p2) {
        float centerX = ((float) p1.x + (float) p2.x) / 2f;
        float centerY = ((float) p1.y + (float) p2.y) / 2f;
        return new Point2D.Float(centerX, centerY);
    }

    @Contract("_ -> new")
    private Rectangle2D.@NotNull Float rectHorizontal(Point2D.@NotNull Float center) {
        float origX = center.x * scalarX;
        float origY = (center.y - .5f) * scalarY;
        return new Rectangle2D.Float(origX, origY, 0f, scalarY);
    }

    @Contract("_ -> new")
    private Rectangle2D.@NotNull Float rectVertical(Point2D.@NotNull Float center) {
        float origX = (center.x - .5f) * scalarX;
        float origY = center.y * scalarY;
        return new Rectangle2D.Float(origX, origY, scalarX, 0f);
    }

    /**
     * @return The {@link Rectangle2D.Float} (a 2D rectangle with <code>float</code> coordinates)
     * representation of the {@link Wall} and scale values given to the constructor.
     */
    public Rectangle2D.Float getRect() {
        return this.rect;
    }
}
