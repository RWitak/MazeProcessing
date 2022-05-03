package pShapes;

import processing.core.PApplet;
import processing.core.PShape;

public class BuilderSprite extends PShape {
    public static PShape getPShape(int scale, int wallWidth, PApplet proc) {
        proc.push();
        proc.noStroke();

        float bigRadius = (scale - wallWidth / 2f) / 3f;
        float middleRadius = bigRadius * .8f;
        float smallRadius = bigRadius * .6f;
        float buttonRadius = bigRadius * .1f;
        float snowCompression = .85f;
        float buttonCompression = .2f;

        PShape snowman = proc.createShape(GROUP);
        proc.ambientLight(155, 155, 155);
        proc.push();
        proc.fill(255);
        proc.emissive(0, 26, 51);
        PShape bigBall = proc.createShape(SPHERE, bigRadius);
        bigBall.scale(1,1, snowCompression);
        snowman.addChild(bigBall);
        proc.pop();

        proc.push();
        proc.fill(0);
        PShape button1 = proc.createShape(SPHERE,buttonRadius);
        button1.scale(1, buttonCompression, 1);
        button1.translate(0, bigRadius, 0);
        snowman.addChild(button1);
        proc.pop();


        proc.push();
        proc.fill(255);
        proc.emissive(0, 26, 51);
        PShape middleBall = proc.createShape(SPHERE,middleRadius);
        middleBall.scale(1, 1, snowCompression);
        middleBall.translate(0,0,bigRadius * snowCompression);
        snowman.addChild(middleBall);
        proc.pop();

        proc.push();
        proc.fill(100, 0, 0);
        PShape button2 = proc.createShape(SPHERE,buttonRadius);
        button2.scale(1, buttonCompression, 1);
        button2.translate(0,middleRadius,bigRadius * snowCompression);
        snowman.addChild(button2);
        proc.pop();

        proc.push();
        proc.fill(255);
        proc.emissive(0, 26, 51);
        PShape smallBall = proc.createShape(SPHERE,smallRadius);
        smallBall.scale(1,1, snowCompression);
        smallBall.translate(0,0,(bigRadius + middleRadius) * snowCompression);
        snowman.addChild(smallBall);
        proc.pop();

        proc.push();
        proc.fill(255,130,0);
        proc.emissive(95,43,0);
        proc.shininess(.6f);
        PShape carrot = cylinder(buttonRadius, 0, bigRadius, 32, proc);
        carrot.rotateY(HALF_PI);
        carrot.translate(0, smallRadius, (bigRadius + middleRadius) * snowCompression);
        snowman.addChild(carrot);
        proc.pop();

        proc.push();
        proc.fill(50);
        proc.emissive(41, 21, 0);
        PShape arm1 = cylinder(buttonRadius / 2,
                buttonRadius / 2,
                middleRadius * 1.5f,
                3,
                proc);
        arm1.translate(0, middleRadius, (bigRadius + middleRadius / 2f) * snowCompression);
        arm1.rotateZ(QUARTER_PI);
        snowman.addChild(arm1);
        proc.pop();

        proc.push();
        proc.fill(50);
        proc.emissive(41, 21, 0);
        PShape arm2 = cylinder(buttonRadius / 2,
                buttonRadius / 2,
                middleRadius * 1.5f,
                3,
                proc);
        arm2.translate(0, middleRadius, (bigRadius + middleRadius / 2f) * snowCompression);
        arm2.rotateZ(-QUARTER_PI);
        snowman.addChild(arm2);
        proc.pop();

        proc.pop();
        snowman.translate(0, 0, bigRadius / 2f);
        return snowman;
    }

    private static PShape cylinder(float bottomRadius, float topRadius, float height, int sides, PApplet proc) {
        float angle;
        float[] x = new float[sides + 1];
        float[] z = new float[sides + 1];
        float[] x2 = new float[sides + 1];
        float[] z2 = new float[sides + 1];

        proc.push();

        //get the x and z position on a circle for all the sides
        for (int i = 0; i < x.length; i++) {
            angle = TWO_PI / (sides) * i;
            x[i] = PApplet.sin(angle) * bottomRadius;
            z[i] = PApplet.cos(angle) * bottomRadius;
        }
        for (int i = 0; i < x.length; i++) {
            angle = TWO_PI / (sides) * i;
            x2[i] = PApplet.sin(angle) * topRadius;
            z2[i] = PApplet.cos(angle) * topRadius;
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
