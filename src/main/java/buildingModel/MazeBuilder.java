package buildingModel;

import buildingModel.wall.Wall;
import buildingModel.guidance.Guidance;
import buildingModel.maze.Maze;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.*;

public class MazeBuilder {
    private final Point position;
    private final Maze maze;
    private final Guidance guidance;
    private final boolean[][] map;
    private final Stack<Point> path = new Stack<>();

    public MazeBuilder(Maze maze, Guidance guidance) {
        this.guidance = guidance;
        this.maze = maze;
        this.position = getStartingPoint(maze);
        this.map = new boolean[maze.getSizeY()][maze.getSizeX()];
        map[position.y][position.x] = true;
    }

    @NotNull
    private Point getStartingPoint(@NotNull Maze maze) {
        return new Point(maze.getSizeX() / 2, maze.getSizeY() / 2);
    }

    public boolean[][] getMapArray() {
        return map;
    }

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

        if (maze.isInside(nextPos)) {
            if (doesNotCross(nextPos)) {
                path.push(position.getLocation());
                position.setLocation(nextPos);
                map[position.y][position.x] = true;

                final BuildingStep step = new BuildingStep(position.getLocation(), direction, null);
                maze.addBuildingStep(step);
                return;
            }
        }
        if (!isPreviousPosition(nextPos)) {
            Wall wall = new Wall(position.getLocation(), nextPos.getLocation());
            maze.addBuildingStep(new BuildingStep(position.getLocation(), direction, wall));
        } else {
            maze.addBuildingStep(new BuildingStep(position.getLocation(), direction, null));
        }

    }

    private void backtrack() {
        if (!path.empty()) {
            position.setLocation(path.pop());
        }
    }

    private boolean doesNotCross(Point point) {
        return !map[point.y][point.x];
    }

    private boolean isPreviousPosition(Point nextPos) {
        return !this.path.empty() && path.peek().equals(nextPos);
    }

    public boolean isFinished() {
        return maze.isFinished();
    }
}
