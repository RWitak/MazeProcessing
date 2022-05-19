package view.pShapes;

import processing.core.PApplet;
import processing.core.PShape;

public class Cylindrical {
    static PShape cylinder(float bottomRadius, float topRadius, float height, int sides, PApplet proc) {
        float angle;
        float[] x = new float[sides + 1];
        float[] z = new float[sides + 1];
        float[] x2 = new float[sides + 1];
        float[] z2 = new float[sides + 1];

        proc.push();

        //get the x and z position on a circle for all the sides
        for (int i = 0; i < x.length; i++) {
            angle = BuilderSprite.TWO_PI / (sides) * i;
            x[i] = PApplet.sin(angle) * bottomRadius;
            z[i] = PApplet.cos(angle) * bottomRadius;
        }
        for (int i = 0; i < x.length; i++) {
            angle = BuilderSprite.TWO_PI / (sides) * i;
            x2[i] = PApplet.sin(angle) * topRadius;
            z2[i] = PApplet.cos(angle) * topRadius;
        }

        PShape cylinder = proc.createShape(BuilderSprite.GROUP);

        PShape bottom = proc.createShape();
        bottom.beginShape(BuilderSprite.TRIANGLE_FAN);
        bottom.vertex(0, -height / 2, 0);
        for (int i = 0; i < x.length; i++) {
            bottom.vertex(x[i], -height / 2, z[i]);
        }
        bottom.endShape();
        cylinder.addChild(bottom);

        PShape hull = proc.createShape();
        hull.beginShape(BuilderSprite.QUAD_STRIP);
        for (int i = 0; i < x.length; i++) {
            hull.vertex(x[i], -height / 2, z[i]);
            hull.vertex(x2[i], height / 2, z2[i]);
        }
        hull.endShape();
        cylinder.addChild(hull);

        PShape top = proc.createShape();
        top.beginShape(BuilderSprite.TRIANGLE_FAN);
        top.vertex(0, height / 2, 0);
        for (int i = 0; i < x.length; i++) {
            top.vertex(x2[i], height / 2, z2[i]);
        }
        top.endShape();
        cylinder.addChild(top);

        proc.pop();

        return cylinder;
    }
}