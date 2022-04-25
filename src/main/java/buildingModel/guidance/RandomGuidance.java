package buildingModel.guidance;

import buildingModel.Direction;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class RandomGuidance extends PerPointGuidance {
    public RandomGuidance(Iterable<Direction> directionsPerPoint) {
        super(new ArrayList<>((Collection<Direction>) directionsPerPoint));
    }

    @Override
    protected @NotNull Iterator<Direction> getDirectionIterator() {
        List<Direction> dirs = new ArrayList<>((Collection<Direction>) directionsPerPoint);
        Collections.shuffle(dirs);
        return dirs.iterator();
    }
}
