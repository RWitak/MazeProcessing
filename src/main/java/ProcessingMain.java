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

/**
 * Provides all controlling logic and the main entry point for this module.
 */
public class ProcessingMain extends PApplet {
    private final LinkedBlockingQueue<RectangleWall> wallQueue = new LinkedBlockingQueue<>();
    private boolean SHOW_PATH;
    private int SCALE, MAZE_X, MAZE_Y, WALL_HEIGHT, WALL_WIDTH;
    private BuildingModel model;
    private Lighting lighting;
    private PeasyCam cam;

    private PShape wallVertical;
    private PShape ground;


    /**
     * Runs the class as {@link PApplet}.
     * @param args CLI arguments. Get ignored as none are defined yet.
     */
    public static void main(String[] args) {
        PApplet.main("ProcessingMain", args);
    }

    /**
     * Standard {@link PApplet} function that does the most fundamental setup before starting the main loop.
     * @see #setup()
     * @see #draw()
     */
    public void settings() {
        final Properties properties = getProperties();

        setConstants(properties);
        setScreen(properties);
        setModel();
    }

    /**
     * @return The properties defined in the module's <code>.properties</code> file(s).
     */
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

    /**
     * @param properties The most important {@link Properties} of the maze building process.
     */
    private void setConstants(@NotNull Properties properties) {
        MAZE_X = Integer.parseInt(properties.getProperty("maze.x", "20"));
        MAZE_Y = Integer.parseInt(properties.getProperty("maze.y", "20"));
        SCALE = Integer.parseInt(properties.getProperty("scale", "10"));

        WALL_HEIGHT = Integer.parseInt(properties.getProperty("wall.height", String.valueOf(SCALE)));
        WALL_WIDTH = Integer.parseInt(properties.getProperty("wall.width", String.valueOf(SCALE / 3)));

        SHOW_PATH = Boolean.parseBoolean(properties.getProperty("path.show", String.valueOf(true)));
    }

    /**
     * Sets the screen size and 3D mode.
     * @param properties {@link Properties} containing screen configuration constants.
     */
    private void setScreen(@NotNull Properties properties) {
        final int screenX = Integer.parseInt(properties.getProperty("screen.x", String.valueOf(displayWidth)));
        final int screenY = Integer.parseInt(properties.getProperty("screen.y", String.valueOf(displayHeight)));
        size(screenX, screenY, P3D);
    }

    /**
     * Set up the "business logic" model of creating a maze.
     */
    private void setModel() {
        final TrackingMaze maze = new TrackingMaze(MAZE_X, MAZE_Y);
        final Point startingPoint = new Point(MAZE_X / 2, MAZE_Y / 2);
        final RandomGuidance guidance = new RandomGuidance(List.of(NORTH, SOUTH, WEST, EAST));
        final MazeBuilder mazeBuilder = new MazeBuilder(maze, guidance, startingPoint);

        final PropertyChangeListener propertyChangeListener = event -> {
            if (event.getPropertyName().equals(BuildingModelAdapter.Fields.currentWall)) {
                wallQueue.add(new RectangleWall((Wall) event.getNewValue(), SCALE, SCALE));
            }
        };
        model = new BuildingModelAdapter(mazeBuilder, maze, propertyChangeListener);
    }

    /**
     * Standard {@link PApplet} function that runs once before the draw loop starts.
     * Sets up the camera, lighting and the used {@link PShape}s.
     * @see #settings()
     * @see #draw()
     */
    public void setup() {
        setUpCam();
        setUpLighting();
        setUpPShapes();
    }

    /**
     * Sets up all <code>Processing</code> light sources for the scene.
     */
    private void setUpLighting() {
        this.lighting = new LightDesign1(this, MAZE_X * SCALE, MAZE_Y * SCALE);
    }

    /**
     * Places and orients a virtual camera on the scene.
     */
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

    /**
     * Create the {@link PShape}s with their respective textures and assign them to their respective fields.
     */
    private void setUpPShapes() {
        final PImage imageHedge = loadImage("hedge.png");
        final PImage imageGround = loadImage("gravel_dark.png");

        wallVertical = VerticalWall.getPShape(SCALE, WALL_WIDTH, WALL_HEIGHT, this, imageHedge);
        ground = Ground.getPShape(MAZE_X, MAZE_Y, -WALL_HEIGHT / 2, SCALE, this, imageGround);
    }

    /**
     * Standard {@link PApplet} function that acts as the draw loop for each frame.
     * Advances building of the maze, updates lights and cameras and draws the scene.
     * @see #settings()
     * @see #setup()
     */
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

    /**
     * Draws all the specified visual elements of the maze.
     */
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
