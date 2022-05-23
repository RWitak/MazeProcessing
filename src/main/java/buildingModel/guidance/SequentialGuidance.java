package buildingModel.guidance;

import buildingModel.Direction;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Iterator;
import java.util.Optional;


/**
 * Ignores current position and just guides in the provided {@link Direction}s.
 * Does not repeat when the given <code>Direction</code>s are exhausted!
 */
public class SequentialGuidance implements Guidance {
    private final Iterator<Direction> iter;

    /**
     * Sole constructor for {@link SequentialGuidance}.
     * @param directions The <code>Iterable</code> of {@link Direction}s to be used sequentially.
     */
    public SequentialGuidance(@NotNull Iterable<Direction> directions) {
        iter = directions.iterator();
    }

    @Override
    public Optional<Direction> nextDirection(Point ignore) {
        return iter.hasNext() ? Optional.of(iter.next()) : Optional.empty();
    }
}
