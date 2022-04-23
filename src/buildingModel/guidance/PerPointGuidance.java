package buildingModel.guidance;

import buildingModel.Direction;

import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Optional;

/**
 * Each position is assigned the same Directions to travel to in order.
 * Keeps track of provided positions and their consumed Directions.
 */
public class PerPointGuidance implements Guidance {
    protected final HashMap<Point, Iterator<Direction>> pointListMap = new HashMap<>();
    private final Iterable<Direction> directionsPerPoint;

    public PerPointGuidance(Iterable<Direction> directionsPerPoint) {
        this.directionsPerPoint = directionsPerPoint;
    }

    @Override
    public Optional<Direction> nextDirection(Point position) {
        if (!pointListMap.containsKey(position)) {
            pointListMap.put(position.getLocation(), directionsPerPoint.iterator());
        }
        if (pointListMap.get(position).hasNext()) {
            return Optional.of(pointListMap.get(position).next());
        }
        return Optional.empty();
    }
}
