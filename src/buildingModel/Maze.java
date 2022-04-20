package buildingModel;

import java.awt.*;
import java.util.Collection;

public interface Maze {
    Collection<Wall> getWalls();

    boolean isFinished();
    boolean isInside(Point point);

    int getSizeX();
    int getSizeY();

    void addBuildingStep(BuildingStep buildingStep);

    void finish();
}
