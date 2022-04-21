import buildingModel.BuildingStep;
import buildingModel.MazeBuilder;
import buildingModel.TrackingMaze;
import buildingModel.Wall;
import buildingModel.guidance.PerPointGuidance;
import processing.core.PApplet;

import java.awt.*;
import java.util.List;

import static buildingModel.Direction.*;

public class ProcessingMain extends PApplet {
    private final int SCREEN_X = 800;
    private final int SCREEN_Y = 600;
    int SCALE_X = 50;
    int SCALE_Y = 50;

    public static PApplet processing;
    private MazeBuilder mazeBuilder;
    private TrackingMaze maze;
    private int wallCount = 0;

    public static void main(String[] args) {
        PApplet.main("ProcessingMain", args);
    }

    public void settings() {
        size(SCREEN_X, SCREEN_Y);
    }

    public void setup() {
        background(0, 22, 11);
        noFill();
        stroke(255);
        textSize(12);

        maze = new TrackingMaze(5, 10);
        final PerPointGuidance guidance = new PerPointGuidance(List.of(NORTH, SOUTH, WEST, EAST));
        mazeBuilder = new MazeBuilder(maze, guidance);
    }

    public void draw() {
        thread("build");

        if (!maze.buildingSteps.empty()) {
            BuildingStep bs = maze.buildingSteps.remove(0);

            Point a = new Point(1, 2);
            a.setLocation(a.x * SCALE_X, a.y * SCALE_Y);
            Point b = new Point(1, 1);
            b.setLocation(b.x * SCALE_X, b.y * SCALE_Y);

            final Point position = bs.position();
            if (position != null) {
                circle(a.x, a.y, SCALE_X / 2f);
                fill(200);
                text("a", a.x, a.y);
                noFill();
                circle(b.x, b.y, SCALE_X / 2f);
                fill(200);
                text("b", b.x, b.y);
                noFill();
            }

            final Wall wall = new Wall(a, b);
            final Point p1 = wall.getP1();
            final Point p2 = wall.getP2();
            var middle = new Point((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
            circle(middle.x, middle.y, SCALE_X * 100);
            fill(200);
            text("mid", middle.x, middle.y);
            noFill();
            rectMode(CENTER);
            rect(middle.x, middle.y,
                    5 + Math.abs(p1.y - p2.y), 5 + Math.abs(p1.x - p2.x));
        }
    }
    public void build() {
        if (!maze.isFinished()) {
            mazeBuilder.moveAndBuild();
        } else {
            die("FINISHED");
        }
    }

}
