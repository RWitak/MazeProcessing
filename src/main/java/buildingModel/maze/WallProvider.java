package buildingModel.maze;

import buildingModel.wall.Wall;

import java.util.Collection;

/**
 * Can be used to get a <code>Collection</code> of {@link Wall}s.
 */
public interface WallProvider {
    /**
     * @return A <code>Collection</code> of {@link Wall}s associated with the object.
     */
    Collection<Wall> getWalls();
}
