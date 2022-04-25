package buildingModel.guidance;

import buildingModel.Direction;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Iterator;
import java.util.Optional;


/**
 * Ignores current position and just guides in the provided directions.
 */
public class SequentialGuidance implements Guidance {
    private final Iterator<Direction> iter;

    public SequentialGuidance(@NotNull Iterable<Direction> directions) {
        iter = directions.iterator();
    }

    @Override
    public Optional<Direction> nextDirection(Point ignore) {
        return iter.hasNext() ? Optional.of(iter.next()) : Optional.empty();
    }
}
