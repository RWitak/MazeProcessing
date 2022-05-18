package buildingModel.maze;

import buildingModel.Direction;

import java.awt.*;

public interface PathTracker {
    Point getPosition();
    Direction getDirection();
}
