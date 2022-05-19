package view.pShapes;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PShape;

public class VerticalWall extends PShape {
    public static PShape getPShape(int scale, int wallWidth, int wallHeight, PApplet caller, PImage img) {
        caller.push();
        caller.fill(255);
        caller.noStroke();
        caller.emissive(55);
        caller.textureMode(NORMAL);
        PShape wallVertical = caller.createShape(GROUP);

        final float outerLeft = -scale / 2f;
        final float middleLeft = -(scale - wallWidth) / 2f;
        final float middleRight = (scale - wallWidth) / 2f;
        final float outerRight = scale / 2f;

        final float backY = wallWidth / 2f;
        final float midY = 0f;
        final float frontY = -wallWidth / 2f;

        final float topZ = wallHeight / 2f;
        final float bottomZ = -wallHeight / 2f;

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

        wallVertical.addChild(top);
//        do not draw bottom when using ground to avoid clipping glitch
//        wallVertical.addChild(bottom);
        wallVertical.addChild(hull);
        caller.pop();

        return wallVertical;
    }
}
