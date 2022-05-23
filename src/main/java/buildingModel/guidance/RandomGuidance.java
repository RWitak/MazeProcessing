package buildingModel.guidance;

import buildingModel.Direction;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Extends its base class {@link PerPointGuidance} by shuffling the {@link Direction}s
 * for each call to {@link #getDirectionIterator()}.
 * This makes the resulting (pseudo-random) route unpredictable.
 */
public class RandomGuidance extends PerPointGuidance {
    /**
     * Sole constructor for {@link PerPointGuidance}
     * @param directionsPerPoint The <code>Iterable</code> of {@link Direction}s to be shuffled for each point.
     */
    public RandomGuidance(Iterable<Direction> directionsPerPoint) {
        super(new ArrayList<>((Collection<Direction>) directionsPerPoint));
    }

    /**
     * @return A shuffled <code>Iterator</code> copy of the {@link Direction}s to be used for each point.
     */
    @Override
    protected @NotNull Iterator<Direction> getDirectionIterator() {
        List<Direction> dirs = new ArrayList<>((Collection<Direction>) directionsPerPoint);
        Collections.shuffle(dirs);
        return dirs.iterator();
    }
}
