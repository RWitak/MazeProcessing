import buildingModel.BuildingStep;
import buildingModel.Direction;
import buildingModel.MazeBuilder;
import buildingModel.maze.TrackingMaze;
import buildingModel.wall.Wall;
import lombok.experimental.FieldNameConstants;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

/**
 * Fits the {@link buildingModel} to the requirements of the {@link BuildingModel}.
 * Uses {@link PropertyChangeSupport} to inform a {@link PropertyChangeListener}
 * of changes to the most recent {@link Wall} that has been built.
 */
@FieldNameConstants(onlyExplicitlyIncluded = true)
class BuildingModelAdapter implements BuildingModel {
    private final TrackingMaze maze;
    private final MazeBuilder mazeBuilder;
    @FieldNameConstants.Include
    private Wall currentWall;
    private Direction direction;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    /**
     * Sole constructor for the class.
     * @param mazeBuilder A {@link MazeBuilder} used to create {@link BuildingStep}s.
     * @param maze The {@link buildingModel.maze.Maze} to be built.
     * @param wallPcl A {@link PropertyChangeListener}
     *            to receive {@link java.beans.PropertyChangeEvent}s
     *            about changes to {@link #currentWall}.
     */
    public BuildingModelAdapter(MazeBuilder mazeBuilder,
                                TrackingMaze maze,
                                PropertyChangeListener wallPcl) {
        this.mazeBuilder = mazeBuilder;
        this.maze = maze;
        support.addPropertyChangeListener(wallPcl);
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

        updateWall(step);
        updateDirection(step);
    }

    /**
     * @param step Updates {@link #direction}
     */
    private void updateDirection(@NotNull BuildingStep step) {
        final Direction direction = step.direction();
        if (direction != null) {
            this.direction = direction;
        }
    }

    /**
     * Updates {@link #currentWall} and fires {@link java.beans.PropertyChangeEvent}.
     * @param step The current {@link BuildingStep}
     */
    private void updateWall(@NotNull BuildingStep step) {
        final Wall wall = step.wall();
        if (wall != null) {
            support.firePropertyChange(Fields.currentWall, currentWall, wall);
            this.currentWall = wall;
        }
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
}
