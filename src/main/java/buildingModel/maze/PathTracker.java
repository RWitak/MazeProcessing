package buildingModel.maze;

import buildingModel.Direction;

import java.awt.*;

/**
 * An object which has a location on a 2D plane and a cardinal {@link Direction}.
 * These values are expected to change over time, forming a path -
 * hence the name <code>PathTracker</code>.
 */
public interface PathTracker {
    /**
     * @return The current position of the object.
     */
    Point getPosition();

    /**
     * @return The current {@link Direction} of the object.
     */
    Direction getDirection();
}
