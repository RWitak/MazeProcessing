import buildingModel.BuildingStep;
import buildingModel.Direction;
import buildingModel.MazeBuilder;
import buildingModel.maze.TrackingMaze;
import buildingModel.wall.RectangleWall;
import buildingModel.wall.Wall;

import java.awt.*;
import java.util.List;
import java.util.Queue;

import static buildingModel.Direction.NORTH;

class BasicBuildingModel implements BuildingModel {
    private final TrackingMaze maze;
    private final int SCALE;
    private final Queue<RectangleWall> wallQueue;
    private final MazeBuilder mazeBuilder;

    public BasicBuildingModel(MazeBuilder mazeBuilder, TrackingMaze maze, int SCALE, Queue<RectangleWall> wallQueue) {
        this.mazeBuilder = mazeBuilder;
        this.maze = maze;
        this.SCALE = SCALE;
        this.wallQueue = wallQueue;
        // FIXME: 18.05.2022 Hardcoded NORTH
        this.direction = NORTH;
    }

    Direction direction;

    @Override
    public Point getPosition() {
        return mazeBuilder.getPosition();
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public java.util.List<Wall> getWalls() {
        return (java.util.List<Wall>) maze.getWalls();
    }

    @Override
    public List<Point> getPath() {
        return mazeBuilder.getPath();
    }

    @Override
    public boolean isFinished() {
        return mazeBuilder.isFinished();
    }

    @Override
    public void build() {
        if (isFinished()) {
            return;
        }

        mazeBuilder.moveAndBuild();
        if (maze.buildingSteps.empty()) {
            return;
        }

        BuildingStep step = maze.buildingSteps.pop();
        final Wall wall = step.wall();
        if (wall != null) {
            // TODO: 18.05.2022 Should not be responsible for scaled RectangleWalls!
            RectangleWall rw = new RectangleWall(wall, SCALE, SCALE);
            this.wallQueue.add(rw);
        }

        final Direction direction = step.direction();
        if (direction != null) {
            this.direction = direction;
        }
    }
}
