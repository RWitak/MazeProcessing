import java.awt.*;
import java.awt.geom.Rectangle2D;

import java.util.List;

import buildingModel.*;

import buildingModel.guidance.PerPointGuidance;
import buildingModel.guidance.RandomGuidance;

import processing.core.PApplet;

import static buildingModel.Direction.*;

public class ProcessingMain extends PApplet {
    private int          SCALE_X;
    private int          SCALE_Y;
    private MazeBuilder  mazeBuilder;
    private TrackingMaze maze;

    @SuppressWarnings("unused")
    public void build() {
        if (!maze.isFinished()) {
            mazeBuilder.moveAndBuild();
        } else {
            die("FINISHED");
        }
    }

    public void draw() {
        thread("build");

        if (!maze.buildingSteps.empty()) {
            BuildingStep    bs        = maze.buildingSteps.remove(0);
            final Point     position  = bs.position();
            final Direction direction = bs.direction();

            if ((position != null) && (direction != null)) {
                fill(0);
                stroke(255);

                final int scaledX = position.x * SCALE_X;
                final int scaledY = position.y * SCALE_Y;

                circle(scaledX, scaledY, SCALE_X / 2f);

                final float indicatorX = scaledX + direction.dx * SCALE_X / 4f;
                final float indicatorY = scaledY + direction.dy * SCALE_Y / 4f;

                line(scaledX, scaledY, indicatorX, indicatorY);
            }

            final Wall wall = bs.wall();

            if (wall != null) {
                RectangleWall rw = new RectangleWall(wall, SCALE_X, SCALE_Y);

                fill(300);
                rect(rw.getRect());
                translate(10, 10);
            }
        }
    }

    public static void main(String[] args) {
        PApplet.main("ProcessingMain", args);
    }

    public void rect(Rectangle2D.Float rectangle) {
        rectMode(CORNER);
        rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    public void settings() {
        size(800, 600);
    }

    public void setup() {
        background(0, 22, 11);

        int mazeX = 10;
        int mazeY = 10;

        SCALE_X = 20;
        SCALE_Y = 20;
        maze    = new TrackingMaze(mazeX, mazeY);

        final PerPointGuidance guidance = new RandomGuidance(List.of(NORTH, SOUTH, WEST, EAST));

        mazeBuilder = new MazeBuilder(maze, guidance);
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
