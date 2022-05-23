package buildingModel.maze;

import buildingModel.Direction;

import java.awt.*;

/**
 * An object which has a location on a 2D plane and a cardinal {@link Direction}.
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
