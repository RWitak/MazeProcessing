package buildingModel;

import buildingModel.guidance.Guidance;
import buildingModel.guidance.GuidanceFromList;
import buildingModel.guidance.PerPointGuidance;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.List;
import java.util.Queue;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

import static buildingModel.Direction.*;
import static org.junit.jupiter.api.Assertions.*;

class MazeBuilderTest {
    MazeBuilder mb;

    @Test
    void coversGround() {
        mb = new MazeBuilder(3,3, (ignore) -> Optional.of(SOUTH));
        final boolean[][] startingMap = {
                new boolean[]{false, false, false},
                new boolean[]{false, true, false},
                new boolean[]{false, false, false}
        };
        assertTrue(Arrays.deepEquals(mb.getMap(), startingMap));

        mb.moveAndBuild();
        assertFalse(Arrays.deepEquals(mb.getMap(), startingMap));
    }

    @Test
    void makesTruthfulMap() {
        final List<Direction> directions = List.of(
                SOUTH, WEST, WEST, NORTH, NORTH, EAST
        );
        mb = new MazeBuilder(5, 3, new GuidanceFromList(directions));

        for (int i = 0; i < directions.size(); i++) {
            mb.moveAndBuild();
        }

        boolean[][] expectedMap = new boolean[][]{
                {true, true, false, false, false},
                {true, false, true, false, false},
                {true, true, true, false, false}};

        assertTrue(Arrays.deepEquals(mb.getMap(), expectedMap));
    }

    @Test
    void stopsAtEdge() {
        final List<Direction> directions = List.of(NORTH, NORTH, EAST, EAST, SOUTH, SOUTH, SOUTH, WEST, WEST, WEST);
        mb = new MazeBuilder(3, 3, new GuidanceFromList(directions));

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
        assertTrue(Arrays.deepEquals(mb.getMap(), expectedMap));
    }

    @Test
    void doesNotCrossPath() {
        final List<Direction> directions = List.of(WEST, NORTH, EAST, SOUTH, SOUTH, EAST);

        mb = new MazeBuilder(3, 3, new GuidanceFromList(directions));
        for (int i = 0; i < directions.size(); i++) {
            mb.moveAndBuild();
        }

        final boolean[][] expectedMap = {
                {true, true, true},
                {true, true, false},
                {false, false, false}
        };
        assertTrue(Arrays.deepEquals(mb.getMap(), expectedMap));
    }

    @Test
    void buildsWalls() {
        final List<Direction> directions =
                List.of(WEST, NORTH, EAST, SOUTH, EAST, SOUTH, WEST, SOUTH, WEST, SOUTH, NORTH);

        mb = new MazeBuilder(3, 3, new GuidanceFromList(directions));
        for (int i = 0; i < directions.size(); i++) {
            mb.moveAndBuild();
        }
        final Wall wall1 = new Wall(1, 1, 1, 0);
        final Wall wall2 = new Wall(1, 1, 2, 1);
        final Wall wall3 = new Wall(1, 1, 1, 2);
        final Wall outerWall = new Wall(1, 3, 1, 2);

        final Queue<Wall> mbWalls = mb.getWalls();
        assertFalse(mbWalls.isEmpty());

        boolean correctWalls = true;
        for (Wall wall : mbWalls) {
            if (!(wall.equals(wall1) || wall.equals(wall2)
                    || wall.equals(wall3) || wall.equals(outerWall))) {
                correctWalls = false;
                break;
            }
        }
        assertTrue(correctWalls);
    }

    @Test
    void noWallBehindSelf() {
        final List<Direction> directions = List.of(WEST, EAST);
        mb = new MazeBuilder(3, 1, new GuidanceFromList(directions));
        for (int i = 0; i < directions.size(); i++) {
            mb.moveAndBuild();
        }

        assertTrue(mb.getWalls().isEmpty());
    }

    @Test
    void handlesEmptyGuidanceGracefully() {
        mb = new MazeBuilder(3, 3, (ignored) -> Optional.empty());
        assertDoesNotThrow(mb::moveAndBuild);
    }

    @Test
    void handlesEmptyStackGracefully() {
        mb = new MazeBuilder(3, 3, new PerPointGuidance(List.of(NORTH, SOUTH, WEST, EAST)));
        for (int i = 0; i < 41; i++) {
            mb.moveAndBuild();
        }
        assertDoesNotThrow(mb::moveAndBuild);
    }

    @Test
    void backtracksWhenDirectionsExhausted() {
        mb = new MazeBuilder(3, 1, new Guidance() {
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
        assertTrue(Arrays.deepEquals(mb.getMap(), expectedMap));

        mb.moveAndBuild();
        expectedMap[0][2] = true;
        assertTrue(Arrays.deepEquals(mb.getMap(), expectedMap));
    }

    @Test
    void setsFinishedFlag() {
        mb = new MazeBuilder(3, 3, new PerPointGuidance(List.of(NORTH, SOUTH, WEST, EAST)));
        for (int i = 0; i < 40; i++) {
            mb.moveAndBuild();
        }
        assertFalse(mb.isFinished());
        mb.moveAndBuild();
        assertTrue(mb.isFinished());
    }

    @Test
    void tracksSteps() {
        mb = new MazeBuilder(3, 3,
                new PerPointGuidance(List.of(NORTH, SOUTH, WEST, EAST)),
                new LinkedBlockingQueue<>());
        assertTrue(mb.nextBuildingStep().isEmpty());
        mb.moveAndBuild();
        assertTrue(mb.nextBuildingStep().isPresent());
        assertTrue(mb.nextBuildingStep().isEmpty());
    }

    @Test
    void tracksChangingPosition() {
        mb = new MazeBuilder(1, 5,
                new GuidanceFromList(List.of(NORTH, NORTH)),
                new LinkedBlockingQueue<>());

        mb.moveAndBuild();
        final Point pos1 = mb.nextBuildingStep().orElseThrow().position();
        mb.moveAndBuild();
        final Point pos2 = mb.nextBuildingStep().orElseThrow().position();

        final double moveDistance = pos1.distance(pos2);
        assertEquals(1.0, moveDistance);
    }

    @Test
    void tracksChangingDirection() {
        mb = new MazeBuilder(3, 5,
                new GuidanceFromList(List.of(NORTH, NORTH, WEST)),
                new LinkedBlockingQueue<>());

        mb.moveAndBuild();
        final Direction d1 = mb.nextBuildingStep().orElseThrow().direction();
        mb.moveAndBuild();
        final Direction d2 = mb.nextBuildingStep().orElseThrow().direction();
        mb.moveAndBuild();
        final Direction d3 = mb.nextBuildingStep().orElseThrow().direction();

        assertEquals(d1, d2);
        assertNotEquals(d2, d3);
    }

    @Test
    @Disabled
    void tracksStepsWhenBuilding() {
        mb = new MazeBuilder(1, 1,
                new PerPointGuidance(List.of(NORTH, WEST)),
                new LinkedBlockingQueue<>());

    }
}