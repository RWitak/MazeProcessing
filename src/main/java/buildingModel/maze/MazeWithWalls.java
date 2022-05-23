package buildingModel.maze;

import buildingModel.BuildingStep;
import buildingModel.wall.Wall;

import java.awt.*;
import java.util.*;

/**
 * A {@link Wall}-providing (but otherwise minimal) implementation of the {@link Maze} interface.
 * @see buildingModel.maze.WallProvider
 */
public class MazeWithWalls implements Maze, WallProvider {
    private final int sizeX;
    private final int sizeY;
    private boolean finished = false;
    private final Collection<Wall> walls;

    /**
     * Sole constructor for the {@link MazeWithWalls}.
     * @param sizeX The number of unique locations along the x-axis.
     * @param sizeY The number of unique locations along the y-axis.
     */
    public MazeWithWalls(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.walls = new LinkedHashSet<>();
    }

    /**
     * @return The {@link Wall}s that were created so far.
     */
    @Override
    public Collection<Wall> getWalls() {
        return walls;
    }

    /**
     * @return {@inheritDoc}
     * @apiNote Does not prevent adding more {@link BuildingStep}s via {@link #addBuildingStep(BuildingStep)}!
     */
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
