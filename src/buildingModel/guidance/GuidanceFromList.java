package buildingModel.guidance;

import buildingModel.Direction;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Iterator;
import java.util.Optional;


/**
 * Ignores current position and just guides in the provided directions.
 */
public class GuidanceFromList implements Guidance {
    private final Iterator<Direction> iter;

    public GuidanceFromList(@NotNull Iterable<Direction> directions) {
        iter = directions.iterator();
    }

    @Override
    public Optional<Direction> nextDirection(Point ignore) {
        if (iter.hasNext()) {
            return Optional.of(iter.next());
        }
        return Optional.empty();
    }
}
