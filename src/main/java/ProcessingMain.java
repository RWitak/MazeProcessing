import java.awt.geom.Rectangle2D;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import buildingModel.*;

import buildingModel.guidance.PerPointGuidance;
import buildingModel.guidance.RandomGuidance;

import buildingModel.maze.TrackingMaze;
import buildingModel.wall.RectangleWall;
import buildingModel.wall.Wall;
import processing.core.PApplet;

import static buildingModel.Direction.*;

public class ProcessingMain extends PApplet {
    private int SCREEN_X, SCREEN_Y, SCALE_X, SCALE_Y, MAZE_X, MAZE_Y;
    private MazeBuilder mazeBuilder;
    private TrackingMaze maze;
    private final LinkedBlockingQueue<RectangleWall> walls = new LinkedBlockingQueue<>();

    public void settings() {
        SCREEN_X = 800;
        SCREEN_Y = 600;
        size(SCREEN_X, SCREEN_Y, P3D);

        MAZE_X = 20;
        MAZE_Y = 20;
        SCALE_X = 20;
        SCALE_Y = 20;
    }

    public void setup() {
        background(0, 22, 11);
        maze = new TrackingMaze(MAZE_X, MAZE_Y);

        final PerPointGuidance guidance = new RandomGuidance(List.of(NORTH, SOUTH, WEST, EAST));

        mazeBuilder = new MazeBuilder(maze, guidance);
    }

    @SuppressWarnings("unused")
    public void build() {
        if (!maze.isFinished()) {
            mazeBuilder.moveAndBuild();
            if (maze.buildingSteps.empty()) {
                return;
            }

            BuildingStep bs = maze.buildingSteps.pop();
            final Wall wall = bs.wall();

            if (wall != null) {
                RectangleWall rw = new RectangleWall(wall, SCALE_X, SCALE_Y);
                walls.add(rw);
            }
        } else {
            die("FINISHED");
        }
    }

    public void draw() {
        thread("build");
        centerView();
        rotateX(PI/3);

        for (RectangleWall rectangleWall : walls) {
            fill(100);
            stroke(200);
            rect(rectangleWall.getRect());
        }
    }

    private void centerView() {
        translate((SCREEN_X - MAZE_X * SCALE_X) / 2f, (SCREEN_Y - MAZE_Y * SCALE_Y) / 2f);
    }

    public static void main(String[] args) {
        PApplet.main("ProcessingMain", args);
    }

    public void rect(Rectangle2D.Float rectangle) {
        rectMode(CORNER);
        rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }
}
