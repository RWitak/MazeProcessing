import buildingModel.maze.PathTracker;

import java.awt.Point;
import java.util.List;

/**
 * Access the most basic features of a building process.
 * @see buildingModel.maze.PathTracker
 * @see #build()
 * @see #isFinished()
 * @see #getPath()
 */
public interface BuildingModel extends PathTracker {
    /**
     * Takes the next step in the building process and updates environment accordingly.
     */
    void build();

    /**
     * @return <code>true</code> if the building process has reached its final state.
     */
    boolean isFinished();


    /**
     * @return A List of {@link Point}s that have been passed through while building.
     */
    List<Point> getPath();
}
