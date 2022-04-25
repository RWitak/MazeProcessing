package buildingModel.guidance;

import buildingModel.*;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Predicate;

import static buildingModel.Direction.*;
import static org.junit.jupiter.api.Assertions.*;

class RandomGuidanceTest {
    @Test
    void shufflesDirections() {
        final RandomGuidance guidance = new RandomGuidance(List.of(NORTH, SOUTH, EAST, WEST));

        final TrackingMaze maze1 = new TrackingMaze(10, 10);
        final MazeBuilder mazeBuilder1 = new MazeBuilder(maze1, guidance);
        while (!mazeBuilder1.isFinished()) {
            mazeBuilder1.moveAndBuild();
        }
        final TrackingMaze maze2 = new TrackingMaze(10, 10);
        final MazeBuilder mazeBuilder2 = new MazeBuilder(maze2, guidance);
        while (!mazeBuilder2.isFinished()) {
            mazeBuilder2.moveAndBuild();
        }
        assertNotEquals(getArray(maze1), getArray(maze2));
    }

    @Test
    void hasNoShutOffRooms() {
        // Test might (but will probably never) give a false positive due to randomness.
        final RandomGuidance guidance = new RandomGuidance(List.of(EAST, WEST, NORTH, SOUTH));

        final TrackingMaze maze = new TrackingMaze(10, 10);
        final MazeBuilder mazeBuilder = new MazeBuilder(maze, guidance);
        while (!mazeBuilder.isFinished()) {
            mazeBuilder.moveAndBuild();
        }
        final HashMap<Point, Set<Point>> hashMap = getPointSetHashMap(maze);

        assertTrue(hashMap.values().stream().map(Set::size).noneMatch(size -> size == 4));
    }

    @Test
    void pointsOutsideMazeAreShutOut() {
        // Test might (but will probably never) give a false positive due to randomness.
        final RandomGuidance guidance = new RandomGuidance(List.of(EAST, SOUTH, WEST, NORTH));

        final int sizeX = 10;
        final int sizeY = 10;
        final TrackingMaze maze = new TrackingMaze(sizeX, sizeY);

        final MazeBuilder mazeBuilder = new MazeBuilder(maze, guidance);

        while (!mazeBuilder.isFinished()) {
            mazeBuilder.moveAndBuild();
        }
        final HashMap<Point, Set<Point>> hashMap = getPointSetHashMap(maze);

        assertEquals(2 * sizeX + 2 * sizeY,
                hashMap.keySet().stream()
                        .filter(Predicate.not(maze::isInside))
                        .count());
    }

    @NotNull
    private HashMap<Point, Set<Point>> getPointSetHashMap(TrackingMaze maze) {
        final HashMap<Point, Set<Point>> hashMap = new HashMap<>();

        Point p1, p2;
        for (BuildingStep step : maze.buildingSteps) {
            if (step.wall() == null) {
                continue;
            }

            p1 = step.wall().getP1();
            p2 = step.wall().getP2();

            if (!hashMap.containsKey(p1)) {
                hashMap.put(p1, new HashSet<>());
            }
            if (!hashMap.containsKey(p2)) {
                hashMap.put(p2, new HashSet<>());
            }
            hashMap.get(p1).add(p2);
            hashMap.get(p2).add(p1);
        }
        return hashMap;
    }

    @NotNull
    private Object[] getArray(TrackingMaze maze) {
        return maze.buildingSteps.stream()
                .map(BuildingStep::direction)
                .map(Direction::ordinal)
                .toArray();
    }
}