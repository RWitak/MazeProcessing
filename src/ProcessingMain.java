import buildingModel.Direction;
import buildingModel.guidance.Guidance;
import buildingModel.MazeBuilder;
import buildingModel.Wall;
import processing.core.PApplet;

import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static buildingModel.Direction.*;
import static buildingModel.Direction.EAST;

public class ProcessingMain extends PApplet {
    private final int SCREEN_X = 800;
    private final int SCREEN_Y = 600;
    private final int MAZE_X = 3;
    private final int MAZE_Y = 3;
    private final int SCALAR_X = 100;
    private final int SCALAR_Y = 100;

    public static PApplet processing;
    private MazeBuilder mazeBuilder;

    public static void main(String[] args) {
        PApplet.main("ProcessingMain", args);
    }

    public void settings() {
        size(SCREEN_X, SCREEN_Y);
    }

    public void setup() {
        background(0, 22, 11);
        fill(230);
        stroke(0, 22, 300);
        mazeBuilder = new MazeBuilder(3, 3, perPointGuidance(List.of(NORTH, SOUTH, WEST, EAST)));
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

    public void draw() {
        mazeBuilder.moveAndBuild();
        final boolean[][] map = mazeBuilder.getMap();
        translate(SCREEN_X/2f, SCREEN_Y/2f);
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                if (map[y][x]) {
                    circle(x * 100 -50, y * 100 -50, 10);
                }
            }
        }
        for (Wall wall : mazeBuilder.getWalls()) {
            final Point p1 = wall.getP1();
            final Point p2 = wall.getP2();
            line(p1.x * 10, p1.y * 10, p2.x * 10, p2.y * 10);
        }
    }

}
