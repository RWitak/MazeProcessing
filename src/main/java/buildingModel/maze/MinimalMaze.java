package buildingModel.maze;

import buildingModel.BuildingStep;
import buildingModel.wall.Wall;

import java.awt.*;
import java.util.*;

public class MinimalMaze implements Maze {
    private final int sizeX;
    private final int sizeY;
    private boolean finished = false;
    private final Collection<Wall> walls;

    public MinimalMaze(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.walls = new LinkedHashSet<>();
    }

    public Collection<Wall> getWalls() {
        return walls;
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public boolean isInside(Point point) {
        return point.x >= 0 && point.x < sizeX &&
                point.y >= 0 && point.y < sizeY;
    }

    @Override
    public int getSizeX() {
        return sizeX;
    }

    @Override
    public int getSizeY() {
        return sizeY;
    }

    @Override
    public void addBuildingStep(BuildingStep buildingStep) {
        if (buildingStep.wall() != null) {
            walls.add(buildingStep.wall());
        }
    }

    @Override
    public void finish() {
        this.finished = true;
    }
}
