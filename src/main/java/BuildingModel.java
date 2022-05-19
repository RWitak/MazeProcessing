import buildingModel.maze.PathTracker;

import java.awt.Point;
import java.util.List;

public interface BuildingModel extends PathTracker {
    void build();
    boolean isFinished();
    List<Point> getPath();
}
