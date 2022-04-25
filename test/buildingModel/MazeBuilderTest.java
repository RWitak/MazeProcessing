package buildingModel;

import buildingModel.guidance.Guidance;
import buildingModel.guidance.PerPointGuidance;
import buildingModel.guidance.SequentialGuidance;
import buildingModel.maze.TrackingMaze;
import buildingModel.wall.Wall;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.List;
import java.util.*;

import static buildingModel.Direction.*;
import static org.junit.jupiter.api.Assertions.*;

class MazeBuilderTest {
    MazeBuilder mb;

    @Test
    void coversGround() {
        final TrackingMaze maze = new TrackingMaze(3, 3);
        mb = new MazeBuilder(maze, (ignore) -> Optional.of(SOUTH));
        final boolean[][] startingMap = {
                new boolean[]{false, false, false},
                new boolean[]{false, true, false},
                new boolean[]{false, false, false}
        };
        assertTrue(Arrays.deepEquals(mb.getMapArray(), startingMap));

        mb.moveAndBuild();
        assertFalse(Arrays.deepEquals(mb.getMapArray(), startingMap));
    }

    @Test
    void makesTruthfulMap() {
        final List<Direction> directions = List.of(
                SOUTH, WEST, WEST, NORTH, NORTH, EAST
        );
        final TrackingMaze maze = new TrackingMaze(5, 3);
        mb = new MazeBuilder(maze, new SequentialGuidance(directions));

        for (int i = 0; i < directions.size(); i++) {
            mb.moveAndBuild();
        }

        boolean[][] expectedMap = new boolean[][]{
                {true, true, false, false, false},
                {true, false, true, false, false},
                {true, true, true, false, false}};

        assertTrue(Arrays.deepEquals(mb.getMapArray(), expectedMap));
    }

    @Test
    void stopsAtEdge() {
        final List<Direction> directions = List.of(NORTH, NORTH, EAST, EAST, SOUTH, SOUTH, SOUTH, WEST, WEST, WEST);
        mb = new MazeBuilder(new TrackingMaze(3, 3), new SequentialGuidance(directions));

        assertDoesNotThrow(() -> {
            for (int i = 0; i < directions.size(); i++) {
                mb.moveAndBuild();
            }
        });
        boolean[][] expectedMap = {
            {false, true, true},
            {false, true, true},
            {true, true, true}
        };
        assertTrue(Arrays.deepEquals(mb.getMapArray(), expectedMap));
    }

    @Test
    void doesNotCrossPath() {
        final List<Direction> directions = List.of(WEST, NORTH, EAST, SOUTH, SOUTH, EAST);

        mb = new MazeBuilder(new TrackingMaze(3, 3), new SequentialGuidance(directions));
        for (int i = 0; i < directions.size(); i++) {
            mb.moveAndBuild();
        }

        final boolean[][] expectedMap = {
                {true, true, true},
                {true, true, false},
                {false, false, false}
        };
        assertTrue(Arrays.deepEquals(mb.getMapArray(), expectedMap));
    }

    @Test
    void buildsWalls() {
        final List<Direction> directions =
                List.of(WEST, NORTH, EAST, SOUTH, EAST, SOUTH, WEST, SOUTH, WEST, SOUTH, NORTH);

        final TrackingMaze maze = new TrackingMaze(3, 3);
        mb = new MazeBuilder(maze, new SequentialGuidance(directions));
        for (int i = 0; i < directions.size(); i++) {
            mb.moveAndBuild();
        }
        final Wall wall1 = new Wall(1, 1, 1, 0);
        final Wall wall2 = new Wall(1, 1, 2, 1);
        final Wall wall3 = new Wall(1, 1, 1, 2);
        final Wall outerWall = new Wall(1, 3, 1, 2);

        final Collection<Wall> walls = maze.getWalls();
        assertFalse(walls.isEmpty());

        boolean correctWalls = true;
        for (Wall wall : walls) {
            if (!(wall.equals(wall1) || wall.equals(wall2)
                    || wall.equals(wall3) || wall.equals(outerWall))) {
                correctWalls = false;
                break;
            }
        }
        assertTrue(correctWalls);
    }

    @Test
    void ignoresDirectionIfSameAsBacktracking() {
        final List<Direction> directions = List.of(WEST, EAST, SOUTH);
        final TrackingMaze maze = new TrackingMaze(3, 3);
        mb = new MazeBuilder(maze, new SequentialGuidance(directions));

        mb.moveAndBuild();
        assertEquals(new Point(0, 1), maze.getPosition());
        mb.moveAndBuild();
        assertEquals(new Point(0, 1), maze.getPosition());
        mb.moveAndBuild();
        assertEquals(new Point(0, 2), maze.getPosition());
        assertTrue(maze.getWalls().isEmpty());
    }

    @Test
    void handlesEmptyGuidanceGracefully() {
        mb = new MazeBuilder(new TrackingMaze(3, 3), (ignored) -> Optional.empty());
        assertDoesNotThrow(mb::moveAndBuild);
    }

    @Test
    void handlesEmptyStackGracefully() {
        mb = new MazeBuilder(new TrackingMaze(3, 3), new PerPointGuidance(List.of(NORTH, SOUTH, WEST, EAST)));
        for (int i = 0; i < 41; i++) {
            mb.moveAndBuild();
        }
        assertDoesNotThrow(mb::moveAndBuild);
    }

    @Test
    void backtracksWhenDirectionsExhausted() {
        mb = new MazeBuilder(new TrackingMaze(3, 1), new Guidance() {
            final HashMap<Point, Iterator<Direction>> mapping = new HashMap<>();

            @Override
            public Optional<Direction> nextDirection(Point position) throws NoSuchElementException {
                if (!mapping.containsKey(position)) {
                    mapping.put(position, List.of(WEST, EAST).iterator());
                }
                if (mapping.get(position).hasNext())
                    return Optional.of(mapping.get(position).next());
                return Optional.empty();
            }
        });

        for (int i = 0; i < 4; i++) {
            mb.moveAndBuild();
        }
        boolean[][] expectedMap = {{true, true, false}};
        assertTrue(Arrays.deepEquals(mb.getMapArray(), expectedMap));

        mb.moveAndBuild();
        expectedMap[0][2] = true;
        assertTrue(Arrays.deepEquals(mb.getMapArray(), expectedMap));
    }

    @Test
    void setsFinishedFlag() {
        final TrackingMaze maze = new TrackingMaze(3, 3);
        final PerPointGuidance guidance = new PerPointGuidance(List.of(NORTH, SOUTH, WEST, EAST));
        mb = new MazeBuilder(maze, guidance);
        for (int i = 0; i < 40; i++) {
            mb.moveAndBuild();
        }
        assertFalse(mb.isFinished());
        mb.moveAndBuild();
        assertTrue(mb.isFinished());
    }

    @Test
    void tracksSteps() {
        final TrackingMaze maze = new TrackingMaze(3, 3);
        final PerPointGuidance guidance = new PerPointGuidance(List.of(NORTH, SOUTH, WEST, EAST));
        mb = new MazeBuilder(maze, guidance);

        assertTrue(maze.buildingSteps.empty());
        mb.moveAndBuild();
        assertFalse(maze.buildingSteps.empty());
    }

    @Test
    void tracksChangingPosition() {
        final TrackingMaze maze = new TrackingMaze(1, 5);
        final SequentialGuidance guidance = new SequentialGuidance(List.of(NORTH, NORTH));
        mb = new MazeBuilder(maze, guidance);

        mb.moveAndBuild();
        final Point pos1 = maze.getPosition();
        mb.moveAndBuild();
        final Point pos2 = maze.getPosition();

        final double moveDistance = pos1.distance(pos2);
        assertEquals(1.0, moveDistance);
    }

    @Test
    void tracksChangingDirection() {
        final TrackingMaze maze = new TrackingMaze(3, 5);
        final SequentialGuidance guidance = new SequentialGuidance(List.of(NORTH, NORTH, WEST));
        mb = new MazeBuilder(maze, guidance);

        mb.moveAndBuild();
        final Direction d1 = maze.getDirection();
        mb.moveAndBuild();
        final Direction d2 = maze.getDirection();
        mb.moveAndBuild();
        final Direction d3 = maze.getDirection();

        assertEquals(d1, d2);
        assertNotEquals(d2, d3);
    }

    @Test
    void tracksStepsWhenBuilding() {
        final TrackingMaze maze = new TrackingMaze(1, 3);
        final PerPointGuidance guidance = new PerPointGuidance(List.of(NORTH, WEST, SOUTH, EAST));
        mb = new MazeBuilder(maze, guidance);

        mb.moveAndBuild();
        assertTrue(maze.getWalls().isEmpty());
        mb.moveAndBuild();
        assertTrue(maze.getWalls().stream()
                .anyMatch(wall -> wall.equals(new Wall(0, 0, 0, -1))));
        mb.moveAndBuild();
        assertTrue(maze.getWalls().stream()
                .anyMatch(wall -> wall.equals(new Wall(0, 0, -1, 0))));
        mb.moveAndBuild();
        assertEquals(2, maze.getWalls().size());
    }
}