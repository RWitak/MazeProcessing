import buildingModel.BuildingStep;
import buildingModel.MazeBuilder;
import buildingModel.guidance.PerPointGuidance;
import buildingModel.guidance.RandomGuidance;
import buildingModel.maze.TrackingMaze;
import buildingModel.wall.RectangleWall;
import buildingModel.wall.Wall;
import peasy.PeasyCam;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PShape;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;

import static buildingModel.Direction.*;

public class ProcessingMain extends PApplet {
    private int SCALE_X, SCALE_Y, MAZE_X, MAZE_Y;
    private float WALL_WIDTH;

    private MazeBuilder mazeBuilder;
    private TrackingMaze maze;

    private final Point position = new Point();
    private final LinkedBlockingQueue<RectangleWall> wallQueue = new LinkedBlockingQueue<>();

    private PeasyCam cam;

    private PShape hedgeVertical;


    public void settings() {
        final InputStream config = Thread
                .currentThread()
                .getContextClassLoader()
                .getResourceAsStream("config.properties");
        final Properties properties = new Properties();
        try {
            properties.load(config);
        } catch (IOException e) {
            e.printStackTrace();
        }
        final int SCREEN_X, SCREEN_Y;
        SCREEN_X = Integer.parseInt(properties.getProperty("screen.x"));
        SCREEN_Y = Integer.parseInt(properties.getProperty("screen.y"));
        size(SCREEN_X, SCREEN_Y, P3D);

        MAZE_X = Integer.parseInt(properties.getProperty("maze.x"));
        MAZE_Y = Integer.parseInt(properties.getProperty("maze.y"));
        SCALE_X = Integer.parseInt(properties.getProperty("scale.x"));
        SCALE_Y = Integer.parseInt(properties.getProperty("scale.y"));
        maze = new TrackingMaze(MAZE_X, MAZE_Y);

        WALL_WIDTH = Math.max(SCALE_X, SCALE_Y) / 3f;
    }

    public void setup() {
        position.setLocation(MAZE_X / 2f, MAZE_Y / 2f);

        final PerPointGuidance guidance = new RandomGuidance(List.of(NORTH, SOUTH, WEST, EAST));
        mazeBuilder = new MazeBuilder(maze, guidance);

        this.cam = new PeasyCam(this,
                MAZE_X * SCALE_X / 2f,
                MAZE_Y * SCALE_Y / 2f,
                0,
                sqrt(sq(MAZE_X * SCALE_X) + sq(MAZE_Y * SCALE_Y)) / 2);
        cam.rotateX(-PI/6);
        cam.setSuppressRollRotationMode();

        PImage img = loadImage("hedge.png");
        hedgeVertical = VerticalHedge.getPShape(SCALE_X, SCALE_Y, WALL_WIDTH, this, img);
    }

    public void draw() {
        pointLight(255, 255, 0, MAZE_X * SCALE_X / 2f, 0, 50);
        pointLight(0, 255, 255, 0, MAZE_Y * SCALE_Y, 50);
        directionalLight(255, 0, 255, 0, 0, -1);
        background(0, 22, 11);

        cam.lookAt(position.x * SCALE_X,
                position.y * SCALE_Y,
                0);
        thread("build");
        drawBuilder();
        for (RectangleWall rectangleWall : wallQueue) {
            drawWall(rectangleWall);
        }
    }

    private void drawBuilder() {
        push();
        translate(SCALE_X * (position.x + .5f), SCALE_Y * (position.y + .5f));
        fill(20, 45, 220);
        shininess(1f);
        sphere(Math.min(SCALE_X, SCALE_Y) / 2f - WALL_WIDTH);
        pop();
    }

    public static void main(String[] args) {
        PApplet.main("ProcessingMain", args);
    }

    public void drawWall(RectangleWall rectangle) {
        push();
        translate((float) rectangle.getRect().getCenterX() + SCALE_X / 2f,
                (float) rectangle.getRect().getCenterY() + SCALE_Y / 2f);

        if (rectangle.isHorizontal()) {
            rotateZ(PI / 2);
        }
        shape(hedgeVertical);

        pop();
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
