package buildingModel;

import buildingModel.wall.Wall;

import java.awt.*;

/**
 * Facilitates communication about location and direction of building and the associated {@link Wall}.
 */
public record BuildingStep(Point position, Direction direction, Wall wall) {}
