import buildingModel.MazeBuilder;
import buildingModel.guidance.RandomGuidance;
import buildingModel.maze.TrackingMaze;
import buildingModel.wall.RectangleWall;
import buildingModel.wall.Wall;
import org.jetbrains.annotations.NotNull;
import peasy.PeasyCam;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PShape;
import view.Drawer;
import view.lighting.LightDesign1;
import view.lighting.Lighting;
import view.pShapes.Ground;
import view.pShapes.VerticalWall;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;

import static buildingModel.Direction.*;

public class ProcessingMain extends PApplet {
    private final LinkedBlockingQueue<RectangleWall> wallQueue = new LinkedBlockingQueue<>();
    private boolean SHOW_PATH;
    private int SCALE, MAZE_X, MAZE_Y, WALL_HEIGHT, WALL_WIDTH;
    private BuildingModel model;
    private Lighting lighting;
    private PeasyCam cam;

    private PShape wallVertical;
    private PShape ground;


    public static void main(String[] args) {
        PApplet.main("ProcessingMain", args);
    }

    public void settings() {
        final Properties properties = getProperties();

        setConstants(properties);
        setScreen(properties);
        setModel();
    }

    @NotNull
    private Properties getProperties() {
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
        return properties;
    }

    private void setConstants(@NotNull Properties properties) {
        MAZE_X = Integer.parseInt(properties.getProperty("maze.x", "20"));
        MAZE_Y = Integer.parseInt(properties.getProperty("maze.y", "20"));
        SCALE = Integer.parseInt(properties.getProperty("scale", "10"));

        WALL_HEIGHT = Integer.parseInt(properties.getProperty("wall.height", String.valueOf(SCALE)));
        WALL_WIDTH = Integer.parseInt(properties.getProperty("wall.width", String.valueOf(SCALE / 3)));

        SHOW_PATH = Boolean.parseBoolean(properties.getProperty("path.show", String.valueOf(true)));
    }

    private void setScreen(@NotNull Properties properties) {
        final int screenX = Integer.parseInt(properties.getProperty("screen.x", String.valueOf(displayWidth)));
        final int screenY = Integer.parseInt(properties.getProperty("screen.y", String.valueOf(displayHeight)));
        size(screenX, screenY, P3D);
    }

    private void setModel() {
        final TrackingMaze maze = new TrackingMaze(MAZE_X, MAZE_Y);
        final Point startingPoint = new Point(MAZE_X / 2, MAZE_Y / 2);
        final RandomGuidance guidance = new RandomGuidance(List.of(NORTH, SOUTH, WEST, EAST));
        final MazeBuilder mazeBuilder = new MazeBuilder(maze, guidance, startingPoint);

        final PropertyChangeListener propertyChangeListener = event -> {
            if (event.getPropertyName().equals(BasicBuildingModel.Fields.currentWall)) {
                wallQueue.add(new RectangleWall((Wall) event.getNewValue(), SCALE, SCALE));
            }
        };
        model = new BasicBuildingModel(mazeBuilder, maze, propertyChangeListener);
    }

    public void setup() {
        setUpCam();
        setUpLighting();
        setUpPShapes();
    }

    private void setUpLighting() {
        this.lighting = new LightDesign1(this, MAZE_X * SCALE, MAZE_Y * SCALE);
    }

    private void setUpCam() {
        Point p = model.getPosition();
        cam = new PeasyCam(this,
                p.x * SCALE,
                p.y * SCALE,
                0,
                sqrt(sq(MAZE_X * SCALE) + sq(MAZE_Y * SCALE)) / 2);
        cam.rotateX(-PI / 3);
        cam.setFreeRotationMode();
    }

    private void setUpPShapes() {
        final PImage imageHedge = loadImage("hedge.png");
        final PImage imageGround = loadImage("gravel_dark.png");

        wallVertical = VerticalWall.getPShape(SCALE, WALL_WIDTH, WALL_HEIGHT, this, imageHedge);
        ground = Ground.getPShape(MAZE_X, MAZE_Y, SCALE, WALL_HEIGHT, this, imageGround);
    }

    public void draw() {
        model.build();

        setLighting();
        setCamera();
        drawScenery();
    }

    private void setLighting() {
        noLights();
        lighting.turnOn();
    }

    private void setCamera() {
        Point position = model.getPosition();
        cam.lookAt((position.x) * SCALE,
                (position.y) * SCALE,
                0);
    }

    private void drawScenery() {
        background(0, 22, 11);

        Drawer drawer = new Drawer(this,
                model.getPosition(),
                model.getDirection(),
                SCALE,
                WALL_WIDTH,
                WALL_HEIGHT);

        drawer.drawGround(ground);
        drawer.drawBuilder();
        wallQueue.forEach(rectangleWall -> drawer.drawWall(rectangleWall, wallVertical));

        if (SHOW_PATH) {
            drawer.drawPath(model.getPath().iterator());
        }
    }
}
