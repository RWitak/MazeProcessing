package buildingModel.maze;

import buildingModel.BuildingStep;

import java.awt.*;

public interface Maze {

    boolean isFinished();
    boolean isInside(Point point);

    int getSizeX();
    int getSizeY();

    void addBuildingStep(BuildingStep buildingStep);

    void finish();
}
