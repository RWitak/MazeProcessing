package buildingModel;

import buildingModel.guidance.Guidance;
import buildingModel.maze.Maze;
import buildingModel.wall.Wall;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Stack;

/**
 * Allows stepwise building of a {@link Maze} following a given {@link Guidance}.
 */
public class MazeBuilder {
    private final Maze maze;
    private final Guidance guidance;
    @Getter
    private final Point position;
    @Getter
    private final boolean[][] map;
    @Getter
    private final Stack<Point> path = new Stack<>();

    /**
     * Creates a {@link MazeBuilder} that starts at the center.
     * @param maze The {@link Maze} to build.
     * @param guidance The {@link Guidance} to apply at each step.
     * @see #MazeBuilder(Maze, Guidance, Point)
     */
    public MazeBuilder(Maze maze, Guidance guidance) {
        this(maze, guidance, getCenter(maze));
    }

    @NotNull
    private static Point getCenter(@NotNull Maze maze) {
        return new Point(maze.getSizeX() / 2, maze.getSizeY() / 2);
    }

    /**
     * Creates a {@link MazeBuilder} that starts at a given starting {@link Point}.
     * @param maze The {@link Maze} to build.
     * @param guidance The {@link Guidance} to apply at each step.
     * @param startingPoint Where to start building.
     * @see #MazeBuilder(Maze, Guidance)
     */
    public MazeBuilder(@NotNull Maze maze, Guidance guidance, Point startingPoint) {
        this.guidance = guidance;
        this.maze = maze;
        this.position = startingPoint;
        this.map = new boolean[maze.getSizeY()][maze.getSizeX()];
        map[position.y][position.x] = true;
    }

    /**
     * Triggers the next step in building while updating the virtual position of the "builder". <br/>
     * Modifies the {@link BuildingStep}s of the {@link Maze}.
     * Will backtrack when all possible steps are exhausted.
     * When no more routes are available, the Maze is set to <code>finished</code>.
     */
    public void moveAndBuild() {
        Direction direction = guidance.nextDirection(position).orElse(null);

        if (direction == null) {
            backtrack();
            if (path.empty()) {
                maze.finish();
            }
            return;
        }

        final Point nextPos = position.getLocation();
        nextPos.translate(direction.dx, direction.dy);

        if (canAdvanceTo(nextPos)) {
            path.push(position.getLocation());
            position.setLocation(nextPos);
            map[position.y][position.x] = true;

            final BuildingStep step = new BuildingStep(position.getLocation(), direction, null);
            maze.addBuildingStep(step);
            return;
        }
        if (isPreviousPosition(nextPos)) {
            maze.addBuildingStep(new BuildingStep(position.getLocation(), direction, null));
        } else {
            Wall wall = new Wall(position.getLocation(), nextPos.getLocation());
            maze.addBuildingStep(new BuildingStep(position.getLocation(), direction, wall));
        }
    }

    private boolean canAdvanceTo(Point nextPos) {
        return maze.isInside(nextPos) && doesNotCrossPath(nextPos);
    }

    private void backtrack() {
        if (!path.empty()) {
            position.setLocation(path.pop());
        }
    }

    private boolean doesNotCrossPath(@NotNull Point point) {
        return !map[point.y][point.x];
    }

    private boolean isPreviousPosition(Point nextPos) {
        return !this.path.empty() && path.peek().equals(nextPos);
    }

    public boolean isFinished() {
        return maze.isFinished();
    }
}
