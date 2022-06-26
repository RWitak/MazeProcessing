package view.pShapes;

import org.jetbrains.annotations.NotNull;
import processing.core.PApplet;
import processing.core.PShape;

import static processing.core.PApplet.cos;
import static processing.core.PApplet.sin;
import static processing.core.PConstants.*;
/**
 * Creates a cylindrical {@link PShape} - useful for creating equal pyramids, cones or cylinders.
 */
public class Cylindrical {
    /**
     * Use this to create a cylindrical {@link PShape} with a given number of sides.
     * <br/>
     * More sides make the cylinder smooth,
     * a radius of 0 makes the top or bottom pointy -
     * helping to create an equal pyramid or cone.
     * Keep in mind: The <code>PShape</code> is positioned relative to its center,
     * not the top or bottom!
     * @param bottomRadius Radius of the bottom face of the cylinder
     * @param topRadius Radius of the top face of the cylinder
     * @param height The height (= distance between top and bottom face)
     * @param sides How many equal sides the cylinder should be divided into.
     * @param proc A {@link PApplet} to call the draw functions with.
     * @return The finished cylinder as <code>PShape</code>.
     */
    static @NotNull PShape cylinder(float bottomRadius,
                                    float topRadius,
                                    float height,
                                    int sides,
                                    @NotNull PApplet proc) {
        if (bottomRadius < 0 || topRadius < 0 || height < 0 || sides < 0) {
            throw new IllegalArgumentException("None of the arguments can be negative!");
        }

        float angle;
        float[] x = new float[sides + 1];
        float[] z = new float[sides + 1];
        float[] x2 = new float[sides + 1];
        float[] z2 = new float[sides + 1];

        proc.push();

        //get the x and z position on a circle for all the sides
        for (int i = 0; i < x.length; i++) {
            angle = TWO_PI / (sides) * i;
            x[i] = sin(angle) * bottomRadius;
            z[i] = cos(angle) * bottomRadius;
        }
        for (int i = 0; i < x.length; i++) {
            angle = TWO_PI / (sides) * i;
            x2[i] = sin(angle) * topRadius;
            z2[i] = cos(angle) * topRadius;
        }

        PShape cylinder = proc.createShape(GROUP);

        PShape bottom = proc.createShape();
        bottom.beginShape(TRIANGLE_FAN);
        bottom.vertex(0, -height / 2, 0);
        for (int i = 0; i < x.length; i++) {
            bottom.vertex(x[i], -height / 2, z[i]);
        }
        bottom.endShape();
        cylinder.addChild(bottom);

        PShape hull = proc.createShape();
        hull.beginShape(QUAD_STRIP);
        for (int i = 0; i < x.length; i++) {
            hull.vertex(x[i], -height / 2, z[i]);
            hull.vertex(x2[i], height / 2, z2[i]);
        }
        hull.endShape();
        cylinder.addChild(hull);

        PShape top = proc.createShape();
        top.beginShape(TRIANGLE_FAN);
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