package buildingModel.guidance;

import buildingModel.Direction;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Optional;

/**
 * Each position is assigned an identical set of {@link Direction}s to travel to in order.
 * Keeps track of provided positions and their consumed Directions.
 */
public class PerPointGuidance implements Guidance {
    protected final HashMap<Point, Iterator<Direction>> pointListMap = new HashMap<>();
    protected final Iterable<Direction> directionsPerPoint;

    /**
     * Sole constructor for {@link PerPointGuidance}
     * @param directionsPerPoint The <code>Iterable</code> of {@link Direction}s to be used for each point.
     */
    public PerPointGuidance(Iterable<Direction> directionsPerPoint) {
        this.directionsPerPoint = directionsPerPoint;
    }

    @Override
    public Optional<Direction> nextDirection(Point position) {
        if (!pointListMap.containsKey(position)) {
            pointListMap.put(position.getLocation(), getDirectionIterator());
        }
        if (pointListMap.get(position).hasNext()) {
            return Optional.of(pointListMap.get(position).next());
        }
        return Optional.empty();
    }

    /**
     * @return An <code>Iterator</code> copy of the {@link Direction}s to be used for each point.
     */
    @NotNull
    protected Iterator<Direction> getDirectionIterator() {
        return directionsPerPoint.iterator();
    }
}
