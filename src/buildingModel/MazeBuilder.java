package buildingModel;

import buildingModel.guidance.Guidance;

import java.awt.*;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class MazeBuilder {
    private final Point position;
    private Maze maze;
    private final Guidance guidance;
    private final boolean[][] map;
    private final Stack<Point> path;
    private Queue<BuildingStep> queue = new LinkedBlockingQueue<>();

    public MazeBuilder(Maze maze, Guidance guidance) {
        this(maze.getSizeX(), maze.getSizeY(), guidance);
        this.maze = maze;
    }

    public MazeBuilder(int mazeX, int mazeY, Guidance guidance, Queue<BuildingStep> buildingSteps) {
        this(mazeX, mazeY, guidance);
        this.queue = buildingSteps;
    }

    public MazeBuilder(int mazeX, int mazeY, Guidance guidance) {
        this.guidance = guidance;
        this.path = new Stack<>();

        this.position = new Point(mazeX / 2, mazeY / 2);
        this.map = new boolean[mazeY][mazeX];
        map[position.y][position.x] = true;
        this.maze = new MinimalMaze(mazeX, mazeY);
    }

    public boolean[][] getMapArray() {
        return map;
    }

    public Queue<Wall> getWalls() {
        return new LinkedList<>(maze.getWalls());
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
                queue.add(step);
                maze.addBuildingStep(step);
                return;
            }
        }
        if (!isPreviousPosition(nextPos)) {
            Wall wall = new Wall(position.getLocation(), nextPos.getLocation());
            queue.add(new BuildingStep(position.getLocation(), direction, wall));
            maze.addBuildingStep(new BuildingStep(position.getLocation(), direction, wall));
        } else {
            queue.add(new BuildingStep(position.getLocation(), direction, null));
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

    public Optional<BuildingStep> nextBuildingStep() {
        return Optional.ofNullable(queue.poll());
    }
}
