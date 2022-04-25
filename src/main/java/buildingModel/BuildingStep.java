package buildingModel;

import buildingModel.wall.Wall;

import java.awt.*;

public record BuildingStep(Point position, Direction direction, Wall wall) {}
