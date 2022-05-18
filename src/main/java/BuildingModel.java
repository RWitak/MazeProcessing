import buildingModel.maze.PathTracker;

import java.awt.Point;
import java.util.List;

public interface BuildingModel extends PathTracker {
    List<Point> getPath();
    boolean isFinished();
    void build();
}
