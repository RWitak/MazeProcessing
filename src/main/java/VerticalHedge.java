import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PShape;

public class VerticalHedge extends PShape {
    public static PShape getPShape(float SCALE_X, float SCALE_Y, float WALL_WIDTH, PApplet caller, PImage img) {
        caller.push();
        caller.fill(255);
        caller.noStroke();
        caller.textureMode(NORMAL);
        PShape hedgeVertical = caller.createShape(GROUP);

        final float outerLeft = -SCALE_X / 2f;
        final float middleLeft = -(SCALE_X - WALL_WIDTH) / 2f;
        final float middleRight = (SCALE_X - WALL_WIDTH) / 2f;
        final float outerRight = SCALE_X / 2f;

        final float backY = WALL_WIDTH / 2f;
        final float midY = 0f;
        final float frontY = -WALL_WIDTH / 2f;

        final float height = (SCALE_X + SCALE_Y) / 2f;
        final float topZ = height / 2f;
        final float bottomZ = -height / 2f;

        PShape bottom = caller.createShape();
        bottom.beginShape(QUAD_STRIP);
        bottom.vertex(middleLeft, backY, bottomZ, 0f, 0f);
        bottom.vertex(middleRight, backY, bottomZ, 1f, 0f);
        bottom.vertex(outerLeft, midY, bottomZ, 0f, 0.5f);
        bottom.vertex(outerRight, midY, bottomZ, 1f, 0.5f);
        bottom.vertex(middleLeft, frontY, bottomZ, 0f, 1f);
        bottom.vertex(middleRight, frontY, bottomZ, 1f, 1f);
        bottom.texture(img);
        bottom.endShape();

        PShape top = caller.createShape();
        top.beginShape(QUAD_STRIP);
        top.vertex(middleLeft, backY, topZ, 0f, 0f);
        top.vertex(middleRight, backY, topZ, 1f, 0f);
        top.vertex(outerLeft, midY, topZ, 0f, 0.5f);
        top.vertex(outerRight, midY, topZ, 1f, 0.5f);
        top.vertex(middleLeft, frontY, topZ, 0f, 1f);
        top.vertex(middleRight, frontY, topZ, 1f, 1f);
        top.texture(img);
        top.endShape();

        PShape hull = caller.createShape();
        hull.beginShape(QUAD_STRIP);
        hull.vertex(middleLeft, backY, bottomZ, 0f, 1f);
        hull.vertex(middleLeft, backY, topZ, 0f, 0f);
        hull.vertex(middleRight, backY, bottomZ, 1f, 1f);
        hull.vertex(middleRight, backY, topZ, 1f, 0f);
        hull.vertex(outerRight, midY, bottomZ, 0f, 1);
        hull.vertex(outerRight, midY, topZ, 0f, 0f);
        hull.vertex(middleRight, frontY, bottomZ, 1f, 1f);
        hull.vertex(middleRight, frontY, topZ, 1f, 0f);
        hull.vertex(middleLeft, frontY, bottomZ, 0f, 1);
        hull.vertex(middleLeft, frontY, topZ, 0f, 0f);
        hull.vertex(outerLeft, midY, bottomZ, 1f, 1f);
        hull.vertex(outerLeft, midY, topZ, 1f, 0f);
        hull.vertex(middleLeft, backY, bottomZ, 0f, 1);
        hull.vertex(middleLeft, backY, topZ, 0f, 0f);
        hull.texture(img);
        hull.endShape();

        hedgeVertical.addChild(top);
        hedgeVertical.addChild(bottom);
        hedgeVertical.addChild(hull);
        caller.pop();

        return hedgeVertical;
    }
}
