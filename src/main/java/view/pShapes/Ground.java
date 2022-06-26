package view.pShapes;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PShape;

/**
 * Creates the {@link PShape} representation of a plane tiled with an image.
 */
public class Ground extends PShape {
    /**
     * Create a texture-wrapped {@link PShape} for the ground using image textures.
     * @param tilesX Number of tiles on the x-axis.
     * @param tilesY Number of tiles on the y-axis.
     * @param offsetZ The distance from the origin along the z-axis (in pixels).
     * @param scale The side length of the tile in pixels.
     * @param pApplet The caller; used to create the <code>PShape</code>.
     * @param img The texture used for tiling.
     * @return The already positioned <code>PShape</code> of the complete plane,
     * tiled by repeating the given <code>PImage</code> .
     */
    public static PShape getPShape(int tilesX, int tilesY, int offsetZ, float scale, PApplet pApplet, PImage img) {
        pApplet.push();
        pApplet.fill(0);
        pApplet.textureMode(NORMAL);
        pApplet.textureWrap(REPEAT);
        pApplet.emissive(55);

        PShape ground = pApplet.createShape();
        ground.beginShape();
        ground.texture(img);
        ground.vertex(0,0, 0f, 0f);
        ground.vertex(tilesX * scale,0, tilesX, 0f);
        ground.vertex(tilesX * scale,tilesY * scale, tilesX, tilesY);
        ground.vertex(0,tilesY * scale, 0f, tilesY);
        ground.endShape();
        ground.translate(0,0, offsetZ);

        pApplet.pop();
        return ground;
    }
}
