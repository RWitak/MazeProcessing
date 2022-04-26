import buildingModel.BuildingStep;
import buildingModel.MazeBuilder;
import buildingModel.guidance.PerPointGuidance;
import buildingModel.guidance.RandomGuidance;
import buildingModel.maze.TrackingMaze;
import buildingModel.wall.RectangleWall;
import buildingModel.wall.Wall;
import peasy.PeasyCam;
import processing.core.PApplet;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import static buildingModel.Direction.*;

public class ProcessingMain extends PApplet {
    private int SCALE_X, SCALE_Y, MAZE_X, MAZE_Y;
    private MazeBuilder mazeBuilder;
    private TrackingMaze maze;
    private final Point position = new Point();
    private final LinkedBlockingQueue<RectangleWall> wallQueue = new LinkedBlockingQueue<>();
    private PeasyCam cam;

    public void settings() {
        size(800, 600, P3D);

        MAZE_X = 40;
        MAZE_Y = 40;
        SCALE_X = 20;
        SCALE_Y = 20;
        
        position.setLocation(MAZE_X / 2f, MAZE_Y / 2f);
    }

    public void setup() {
        maze = new TrackingMaze(MAZE_X, MAZE_Y);
        final PerPointGuidance guidance = new RandomGuidance(List.of(NORTH, SOUTH, WEST, EAST));
        mazeBuilder = new MazeBuilder(maze, guidance);
        this.cam = new PeasyCam(this,
                MAZE_X * SCALE_X / 2f,
                MAZE_Y * SCALE_Y / 2f,
                0,
                sqrt(sq(MAZE_X * SCALE_X) + sq(MAZE_Y * SCALE_Y)) / 2);
        cam.rotateX(-PI/6);
        cam.setSuppressRollRotationMode();
    }

    public void draw() {
        pointLight(255, 255, 0, MAZE_X * SCALE_X / 2f, 0, 50);
        pointLight(0, 255, 255, 0, MAZE_Y * SCALE_Y, 50);
        directionalLight(255, 0, 255, 0, 0, 1);
        background(0, 22, 11);

        cam.lookAt(position.x * SCALE_X,
                position.y * SCALE_Y,
                0);
        thread("build");

        drawBuilder();
        noStroke();
        fill(255);
        ambient(100, 100, 100);
        specular(100, 100, 100);
        shininess(.9f);
        for (RectangleWall rectangleWall : wallQueue) {
            drawWall(rectangleWall.getRect());
        }
    }

    private void drawBuilder() {
        pushMatrix();
        translate(SCALE_X * (position.x + .5f), SCALE_Y * (position.y + .5f));
        fill(20, 45, 220);
        shininess(1f);
        sphere(SCALE_X / 2f);
        popMatrix();
    }

    public static void main(String[] args) {
        PApplet.main("ProcessingMain", args);
    }

    public void drawWall(Rectangle2D.Float rectangle) {
        pushMatrix();
        translate((float) rectangle.getCenterX() + SCALE_X / 2f,
                (float) rectangle.getCenterY() + SCALE_Y / 2f);
        box((float) rectangle.getWidth() + 5, (float) rectangle.getHeight() + 5, SCALE_X);
        popMatrix();
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
                wallQueue.add(rw);
            }

            final Point currPosition = bs.position();
            if (currPosition != null) {
                position.setLocation(currPosition);
            }
        }
    }
}
