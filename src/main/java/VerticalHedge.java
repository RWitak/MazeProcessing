import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PShape;

public class VerticalHedge extends PShape {
    public static PShape getPShape(float SCALE_X, float SCALE_Y, float WALL_WIDTH, PApplet caller, PImage img) {
        caller.noStroke();
        PShape hedgeVertical = caller.createShape(GROUP);

        final float height = (SCALE_X + SCALE_Y) / 2f;

        PShape bottom = caller.createShape();

        bottom.beginShape();
        bottom.fill(255);
        bottom.vertex(-(SCALE_X + WALL_WIDTH) / 2f, 0, -height / 2f);
        bottom.vertex(-SCALE_X / 2f, -WALL_WIDTH / 2f, -height / 2f);
        bottom.vertex(SCALE_X / 2f, -WALL_WIDTH / 2f, -height / 2f);
        bottom.vertex((SCALE_X + WALL_WIDTH) / 2f, 0, -height / 2f);
        bottom.vertex(SCALE_X / 2f, WALL_WIDTH / 2f, -height / 2f);
        bottom.vertex(-SCALE_X / 2f, WALL_WIDTH / 2f, -height / 2f);
        bottom.vertex(-(SCALE_X + WALL_WIDTH) / 2f, 0, -height / 2f);
        bottom.texture(img);
        bottom.endShape();

        PShape top = caller.createShape();
        top.beginShape();
        top.fill(255);
        top.vertex(-SCALE_X / 2f, WALL_WIDTH / 2f, height / 2f);
        top.vertex(SCALE_X / 2f, WALL_WIDTH / 2f, height / 2f);
        top.vertex((SCALE_X + WALL_WIDTH) / 2f, 0, height / 2f);
        top.vertex(SCALE_X / 2f, -WALL_WIDTH / 2f, height / 2f);
        top.vertex(-SCALE_X / 2f, -WALL_WIDTH / 2f, height / 2f);
        top.vertex(-(SCALE_X + WALL_WIDTH) / 2f, 0, height / 2f);
        top.vertex(-SCALE_X / 2f, WALL_WIDTH / 2f, height / 2f);
        top.texture(img);
        top.endShape(CLOSE);

        PShape hull = caller.createShape();
        hull.beginShape(QUAD_STRIP);
        hull.fill(255);
        hull.vertex(-SCALE_X / 2f, WALL_WIDTH / 2f, -height / 2f);
        hull.vertex(-SCALE_X / 2f, WALL_WIDTH / 2f, height / 2f);
        hull.vertex(SCALE_X / 2f, WALL_WIDTH / 2f, -height / 2f);
        hull.vertex(SCALE_X / 2f, WALL_WIDTH / 2f, height / 2f);
        hull.vertex((SCALE_X + WALL_WIDTH) / 2f, 0, -height / 2f);
        hull.vertex((SCALE_X + WALL_WIDTH) / 2f, 0, height / 2f);
        hull.vertex(SCALE_X / 2f, -WALL_WIDTH / 2f, -height / 2f);
        hull.vertex(SCALE_X / 2f, -WALL_WIDTH / 2f, height / 2f);
        hull.vertex(-SCALE_X / 2f, -WALL_WIDTH / 2f, -height / 2f);
        hull.vertex(-SCALE_X / 2f, -WALL_WIDTH / 2f, height / 2f);
        hull.vertex(-(SCALE_X + WALL_WIDTH) / 2f, 0, -height / 2f);
        hull.vertex(-(SCALE_X + WALL_WIDTH) / 2f, 0, height / 2f);
        hull.vertex(-SCALE_X / 2f, WALL_WIDTH / 2f, -height / 2f);
        hull.vertex(-SCALE_X / 2f, WALL_WIDTH / 2f, height / 2f);
        hull.texture(img);
        hull.endShape();

        hedgeVertical.addChild(top);
        hedgeVertical.addChild(bottom);
        hedgeVertical.addChild(hull);

        return hedgeVertical;
    }
}
