package buildingModel.guidance;

import buildingModel.Direction;
import javafx.beans.property.Property;

import java.awt.*;
import java.util.Optional;
import java.util.Stack;

/**
 * Defines how to move from one {@link Point} to the next.
 */
public interface Guidance {
    /**
     * @param position The current position.
     * @return The {@link Direction} in which to advance.
     */
    Optional<Direction> nextDirection(Point position);
}
