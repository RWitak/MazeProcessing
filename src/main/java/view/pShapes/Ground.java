package view.pShapes;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PShape;

public class Ground extends PShape {

    public static PShape getPShape(int mazeX, int mazeY, int scale, int wallHeight, PApplet pApplet, PImage img) {
        pApplet.push();
        pApplet.fill(0);
        pApplet.textureMode(NORMAL);
        pApplet.textureWrap(REPEAT);
        pApplet.emissive(55);

        PShape ground = pApplet.createShape();
        ground.beginShape();
        ground.texture(img);
        ground.vertex(0,0, 0f, 0f);
        ground.vertex(mazeX * scale,0, mazeX, 0f);
        ground.vertex(mazeX * scale,mazeY * scale, mazeX, mazeY);
        ground.vertex(0,mazeY * scale, 0f, mazeY);
        ground.endShape();
        ground.translate(0,0,-wallHeight / 2f);

        pApplet.pop();
        return ground;
    }
}
