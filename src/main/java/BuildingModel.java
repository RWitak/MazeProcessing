import buildingModel.Direction;
import buildingModel.wall.Wall;

import java.awt.Point;
import java.util.List;

public interface BuildingModel {
    Point getPosition();
    Direction getDirection();
    List<Wall> getWalls();
    List<Point> getPath();
    boolean isFinished();
    void build();
}
