package buildingModel.maze;

import buildingModel.Direction;

import java.awt.*;

public interface PathTracker {
    @SuppressWarnings("unused")
    Point getPosition();
    @SuppressWarnings("unused")
    Direction getDirection();
}
