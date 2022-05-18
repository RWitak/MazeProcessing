import buildingModel.BuildingStep;
import buildingModel.Direction;
import buildingModel.MazeBuilder;
import buildingModel.maze.TrackingMaze;
import buildingModel.wall.Wall;
import lombok.experimental.FieldNameConstants;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

@FieldNameConstants(onlyExplicitlyIncluded = true)
class BasicBuildingModel implements BuildingModel {
    private final TrackingMaze maze;
    private final MazeBuilder mazeBuilder;
    @FieldNameConstants.Include
    private Wall currentWall;
    private Direction direction;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public BasicBuildingModel(MazeBuilder mazeBuilder,
                              TrackingMaze maze,
                              PropertyChangeListener pcl) {
        this.mazeBuilder = mazeBuilder;
        this.maze = maze;
        support.addPropertyChangeListener(pcl);
    }

    @Override
    public Point getPosition() {
        return mazeBuilder.getPosition();
    }

    @Override
    public Direction getDirection() {
        return direction;
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
            support.firePropertyChange(Fields.currentWall, currentWall, wall);
            this.currentWall = wall;
        }

        final Direction direction = step.direction();
        if (direction != null) {
            this.direction = direction;
        }
    }
}
