package buildingModel;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.*;
import java.util.List;

import static buildingModel.Direction.*;
import static org.junit.jupiter.api.Assertions.*;

class MazeBuilderTest {
    MazeBuilder mb;

    private Guidance guidanceFromList(Iterable<Direction> directions) {
        return new Guidance() {
            private final Iterator<Direction> iter = directions.iterator();

            @Override
            public Optional<Direction> nextDirection(Point ignore) {
                if (iter.hasNext()) {
                    return Optional.of(iter.next());
                }
                return Optional.empty();
            }
        };
    }

    private Guidance perPointGuidance(Iterable<Direction> directionsPerPoint) {
        return new Guidance() {
            final HashMap<Point, Iterator<Direction>> pointListMap = new HashMap<>();

            @Override
            public Optional<Direction> nextDirection(Point position) {
                if (!pointListMap.containsKey(position)) {
                    pointListMap.put(position, directionsPerPoint.iterator());
                }
                if (pointListMap.get(position).hasNext()) {
                    return Optional.of(pointListMap.get(position).next());
                }
                return Optional.empty();
            }
        };
    }

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
        mb = new MazeBuilder(5, 3, guidanceFromList(directions));

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
        mb = new MazeBuilder(3, 3, guidanceFromList(directions));

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

        mb = new MazeBuilder(3, 3, guidanceFromList(directions));
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

        mb = new MazeBuilder(3, 3, guidanceFromList(directions));
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
        mb = new MazeBuilder(3, 1, guidanceFromList(directions));
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
        mb = new MazeBuilder(3, 3, perPointGuidance(List.of(NORTH, SOUTH, WEST, EAST)));
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
        mb = new MazeBuilder(3, 3, perPointGuidance(List.of(NORTH, SOUTH, WEST, EAST)));
        for (int i = 0; i < 40; i++) {
            mb.moveAndBuild();
        }
        assertFalse(mb.isFinished());
        mb.moveAndBuild();
        assertTrue(mb.isFinished());
    }
}