package view.pShapes;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PShape;

/**
 * A 3-dimensional vertical wall (separates two points on the x-axis) wrapped by a texture,
 * presented as drawable {@link PShape}.
 */
public class VerticalWall extends PShape {
    /**
     * Creates a drawable {@link PShape} wall
     * with vertical orientation (separates two points on the x-axis)
     * that is suitable for building more complex structures.
     * Facilitates joining adjacent and orthogonal walls seamlessly.
     * Must be explicitly rotated to build a horizontal wall instead.
     * @param width The width of the wall in pixels.
     *              Walls are created in a way that makes them fit together seamlessly
     *              around a square of the given side length.
     * @param thickness The thickness of the wall in pixels.
     * @param height The height of the wall in pixels.
     * @param caller The calling {@link PApplet}
     * @param img A texture {@link PImage} for the wall.
     *            Gets wrapped around all sides individually to facilitate seamless textures.
     * @return The created <code>VerticalWall</code> <code>PShape</code> ready to be drawn.
     */
    public static PShape getPShape(int width, int thickness, int height, PApplet caller, PImage img) {
        caller.push();
        caller.fill(255);
        caller.noStroke();
        caller.emissive(55);
        caller.textureMode(NORMAL);
        PShape wallVertical = caller.createShape(GROUP);

        final float outerLeft = -width / 2f;
        final float middleLeft = -(width - thickness) / 2f;
        final float middleRight = (width - thickness) / 2f;
        final float outerRight = width / 2f;

        final float backY = thickness / 2f;
        final float midY = 0f;
        final float frontY = -thickness / 2f;

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

        wallVertical.addChild(top);
//        do not draw bottom when using ground on same level to avoid clipping glitch
//        wallVertical.addChild(bottom);
        wallVertical.addChild(hull);
        caller.pop();

        return wallVertical;
    }
}
