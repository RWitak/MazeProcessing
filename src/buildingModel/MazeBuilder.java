package buildingModel;

import buildingModel.guidance.Guidance;

import java.awt.*;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class MazeBuilder{
    private final Point position;
    private final int maze_x;
    private final int maze_y;
    private final Guidance guidance;
    private final boolean[][] map;
    private final Stack<Point> path;
    private Queue<BuildingStep> queue = new LinkedBlockingQueue<>();
    private boolean isFinished = false;

    public MazeBuilder(int mazeX, int mazeY, Guidance guidance, Queue<BuildingStep> buildingSteps) {
        this(mazeX, mazeY, guidance);
        this.queue = buildingSteps;
    }

    public boolean[][] getMap() {
        return map;
    }

    public Queue<Wall> getWalls() {
        return walls;
    }

    private final Queue<Wall> walls = new ArrayDeque<>();

    public MazeBuilder(int maze_x, int maze_y, Guidance guidance) {
        this.maze_x = maze_x;
        this.maze_y = maze_y;
        this.guidance = guidance;
        this.path = new Stack<>();

        this.position = new Point(maze_x / 2, maze_y / 2);
        this.map = new boolean[maze_y][maze_x];
        map[position.y][position.x] = true;
    }

    public void moveAndBuild() {
        Direction direction = guidance.nextDirection(position).orElse(null);
        if (direction == null) {
            backtrack();
            if (path.empty()) {
                isFinished = true;
            }
            return;
        }

        final Point nextPos = position.getLocation();
        nextPos.translate(direction.dx, direction.dy);

        if (isInsideMaze(nextPos)) {
            if (doesNotCross(nextPos)) {
                path.push(position.getLocation());
                position.setLocation(nextPos);
                map[position.y][position.x] = true;

                queue.add(new BuildingStep(position.getLocation(), direction, null));
                return;
            }
        }
        if (!isPreviousPosition(nextPos)) {
            buildWall(position, nextPos);
        }
    }

    private void backtrack() {
        if (!path.empty()) {
            position.setLocation(path.pop());
        }
    }

    private void buildWall(Point p1, Point p2) {
        walls.add(new Wall(p1, p2));
    }

    private boolean isInsideMaze(Point point) {
        return point.x >= 0 && point.x < maze_x &&
                point.y >= 0 && point.y < maze_y;
    }

    private boolean doesNotCross(Point point) {
        return !map[point.y][point.x];
    }

    private boolean isPreviousPosition(Point nextPos) {
        return !this.path.empty() && path.peek().equals(nextPos);
    }

    public boolean isFinished() {
        return this.isFinished;
    }

    public Optional<BuildingStep> nextBuildingStep() {
        return Optional.ofNullable(queue.poll());
    }
}
