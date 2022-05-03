import buildingModel.BuildingStep;
import buildingModel.MazeBuilder;
import buildingModel.guidance.RandomGuidance;
import buildingModel.maze.TrackingMaze;
import buildingModel.wall.RectangleWall;
import buildingModel.wall.Wall;
import pShapes.BuilderSprite;
import pShapes.Ground;
import pShapes.VerticalWall;
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
    private int SCALE, MAZE_X, MAZE_Y, WALL_HEIGHT, WALL_WIDTH;

    private MazeBuilder mazeBuilder;
    private TrackingMaze maze;

    private final Point position = new Point();
    private final LinkedBlockingQueue<RectangleWall> wallQueue = new LinkedBlockingQueue<>();

    private PeasyCam cam;

    private PShape wallVertical;
    private PShape ground;
    private PShape builderSprite;


    public static void main(String[] args) {
        PApplet.main("ProcessingMain", args);
    }

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
        SCREEN_X = Integer.parseInt(properties.getProperty("screen.x", String.valueOf(displayWidth)));
        SCREEN_Y = Integer.parseInt(properties.getProperty("screen.y", String.valueOf(displayHeight)));
        size(SCREEN_X, SCREEN_Y, P3D);

        MAZE_X = Integer.parseInt(properties.getProperty("maze.x", "20"));
        MAZE_Y = Integer.parseInt(properties.getProperty("maze.y", "20"));
        SCALE = Integer.parseInt(properties.getProperty("scale", "10"));

        maze = new TrackingMaze(MAZE_X, MAZE_Y);
        mazeBuilder = new MazeBuilder(maze, new RandomGuidance(List.of(NORTH, SOUTH, WEST, EAST)));

        WALL_HEIGHT = Integer.parseInt(properties.getProperty("wall.height", String.valueOf(SCALE)));
        WALL_WIDTH = Integer.parseInt(properties.getProperty("wall.width", String.valueOf(SCALE / 3)));
    }

    public void setup() {
        position.setLocation(MAZE_X / 2f, MAZE_Y / 2f);

        cam = new PeasyCam(this,
                MAZE_X * SCALE / 2f,
                MAZE_Y * SCALE / 2f,
                0,
                2 * sqrt(sq(MAZE_X * SCALE) + sq(MAZE_Y * SCALE)) / 2);
        cam.rotateX(-PI/6);
        cam.setSuppressRollRotationMode();

        final PImage imageHedge = loadImage("hedge.png");
        final PImage imageGround = loadImage("gravel_dark.png");

        wallVertical = VerticalWall.getPShape(SCALE, WALL_WIDTH, WALL_HEIGHT, this, imageHedge);
        ground = Ground.getPShape(MAZE_X, MAZE_Y, SCALE, WALL_HEIGHT, this, imageGround);
        builderSprite = BuilderSprite.getPShape(SCALE, WALL_WIDTH, this);
        builderSprite.translate(0, 0, -WALL_HEIGHT / 2f);
    }

    public void draw() {
        thread("build");

        background(0, 22, 11);
        noLights();
        final float radius = sqrt(sq(MAZE_X * SCALE) + sq(MAZE_Y * SCALE)) / 2f;
        pointLight(89, 89, 0,
                radius * (cos(radians(millis() / 30f % 360))),
                radius * (sin(radians(millis() / 30f % 360))),
                radius
                );
        pointLight(0, 89, 89,
                -radius * (cos(radians(millis() / 30f % 360))),
                -radius * (sin(radians(millis() / 30f % 360))),
                radius
                );
        pointLight(89, 0, 0,
                -radius * (cos(radians(millis() / 30f % 360))),
                radius * (sin(radians(millis() / 30f % 360))),
                radius
                );
        pointLight(0, 89,0,
                radius * (cos(radians(millis() / 30f % 360))),
                -radius * (sin(radians(millis() / 30f % 360))),
                radius
                );
//        pointLight(251, 222, 26, MAZE_X * SCALE / 2f, MAZE_Y * SCALE / 2f, 160);
        cam.lookAt((position.x + .5f) * SCALE,
                (position.y + .5f) * SCALE,
                0);

        drawGround();
        drawBuilder();
        for (RectangleWall rectangleWall : wallQueue) {
            drawWall(rectangleWall);
        }
    }

    /**
     * Build maze asynchronously in background.
     */
    @SuppressWarnings("unused") // used as stringified parameter to thread() in draw()
    public void build() {
        if (maze.isFinished()) {
            return;
        }

        mazeBuilder.moveAndBuild();
        if (maze.buildingSteps.empty()) {
            return;
        }
        BuildingStep bs = maze.buildingSteps.pop();

        final Wall wall = bs.wall();
        if (wall != null) {
            RectangleWall rw = new RectangleWall(wall, SCALE, SCALE);
            wallQueue.add(rw);
        }

        final Point currPosition = bs.position();
        if (currPosition != null) {
            position.setLocation(currPosition);
        }
    }

    private void drawGround() {
        push();
        shape(ground);
        pop();
    }

    private void drawBuilder() {
        push();
        translate(SCALE * (position.x + .5f), SCALE * (position.y + .5f));
        emissive(0, 0, 255);
        shape(builderSprite);
        pop();
    }

    public void drawWall(RectangleWall rectWall) {
        push();
        translate((float) rectWall.getRect().getCenterX() + SCALE / 2f,
                (float) rectWall.getRect().getCenterY() + SCALE / 2f);

        if (rectWall.isHorizontal()) {
            rotateZ(PI / 2);
        }
        shape(wallVertical);
        pop();
    }
}
