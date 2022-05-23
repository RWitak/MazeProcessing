package buildingModel.maze;

import buildingModel.BuildingStep;
import buildingModel.Direction;

import java.awt.*;
import java.util.Stack;

/**
 * A {@link MazeWithWalls} that will reveal its current location and {@link Direction}.
 */
public class TrackingMaze extends MazeWithWalls implements PathTracker {
    public final Stack<BuildingStep> buildingSteps = new Stack<>();

    /**
     * Sole constructor for the {@link TrackingMaze}.
     * @param sizeX The number of unique locations along the x-axis.
     * @param sizeY The number of unique locations along the y-axis.
     */
    public TrackingMaze(int sizeX, int sizeY) {
        super(sizeX, sizeY);
    }

    @Override
    public Point getPosition() {
        return buildingSteps.peek().position();
    }

    @Override
    public Direction getDirection() {
        return buildingSteps.peek().direction();
    }

    @Override
    public void addBuildingStep(BuildingStep buildingStep) {
        buildingSteps.push(buildingStep);
        super.addBuildingStep(buildingStep);
    }
}
