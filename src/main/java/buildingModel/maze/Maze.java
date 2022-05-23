package buildingModel.maze;

import buildingModel.BuildingStep;

import java.awt.*;

/**
 * Can be built by handing it {@link BuildingStep}s.
 * Reveals its size, if specific {@link Point}s are inside its bounds,
 * and if it is set to a <code>finished</code> state.
 */
public interface Maze {

    /**
     * @return If it is done being built.
     */
    boolean isFinished();

    /**
     * @param point A {@link Point} on the same 2D plane that also contains the area of the {@link Maze}.
     * @return If the give <code>Point</code> is inside the bounds of the <code>Maze</code>.
     */
    boolean isInside(Point point);

    /**
     * @return The extent of the {@link Maze} on the x coordinate.
     * More specifically: the number of <em>unique locations</em> along that axis.
     */
    int getSizeX();

    /**
     * @return The extent of the {@link Maze} on the y coordinate.
     * More specifically: the number of <em>unique locations</em> along that axis.
     */
    int getSizeY();

    /**
     * Adds a {@link BuildingStep} containing information to build the {@link Maze} one step further.
     * @param buildingStep The next <code>BuildingStep</code> to be implemented by the <code>Maze</code>.
     */
    void addBuildingStep(BuildingStep buildingStep);

    /**
     * Trigger the {@link Maze}'s finished state.
     */
    void finish();
}
