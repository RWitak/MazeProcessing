package buildingModel;

import java.awt.*;
import java.util.Stack;

public class TrackingMaze extends MinimalMaze implements PathTracker {
    public final Stack<BuildingStep> buildingSteps = new Stack<>();

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
