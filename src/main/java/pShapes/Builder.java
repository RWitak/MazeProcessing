package pShapes;

import processing.core.PApplet;
import processing.core.PShape;

public class Builder extends PShape {
    public static PShape getPShape(int scale, int wallWidth, PApplet pApplet) {
        pApplet.noStroke();
        pApplet.fill(120, 145, 255);
        return pApplet.createShape(SPHERE, scale / 2f - wallWidth);
    }
}
