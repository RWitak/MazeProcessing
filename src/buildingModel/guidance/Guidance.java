package buildingModel.guidance;

import buildingModel.Direction;

import java.awt.*;
import java.util.Optional;

public interface Guidance {
    Optional<Direction> nextDirection(Point position);
}
