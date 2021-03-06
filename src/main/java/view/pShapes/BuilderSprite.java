package view.pShapes;

import buildingModel.Direction;
import org.jetbrains.annotations.NotNull;
import processing.core.PApplet;
import processing.core.PShape;

import static view.pShapes.Cylindrical.*;
import static processing.core.PApplet.*;

/**
 * A {@link PShape} meant to follow the point of building of a maze.
 * Can indicate {@link Direction} if called with {@link #getDirectionalPShape(float, Direction, PApplet)}.
 */
public class BuilderSprite extends PShape {
    /**
     * The not yet rotated {@link PShape} representing a builder.
     * Uses {@link #getSnowman(float, PApplet)} internally.
     * @param maxFootPrint The maximum dimension (x or y) of the footprint in pixels.
     *                     Makes sure the <code>PShape</code> will fit inside certain bounds.
     *                     (The <code>PShape</code> is assumed to be centered
     *                     so that the assured fit will hold under any rotation around z-axis.)
     * @param proc The calling {@link PApplet}.
     * @return A sprite <code>PShape</code> facing {@link Direction#SOUTH}
     * (or not showing direction at all).
     */
    public static @NotNull PShape getUnidirectionalPShape(float maxFootPrint, @NotNull PApplet proc) {
        return getSnowman(maxFootPrint, proc);
    }

    @NotNull
    private static PShape getSnowman(float maxFootPrint, @NotNull PApplet proc) {
        proc.push();
        proc.noStroke();

        float bigRadius = maxFootPrint / 2.5f;
        float middleRadius = bigRadius * .8f;
        float smallRadius = bigRadius * .6f;
        float buttonRadius = bigRadius * .1f;
        float eyeRadius = middleRadius * .1f;
        float snowCompression = .85f;
        float buttonCompression = .2f;

        final PShape snowman = proc.createShape(GROUP);
        proc.push();
        proc.fill(255);
        proc.emissive(0, 26, 51);
        final PShape bigBall = proc.createShape(SPHERE, bigRadius);
        bigBall.scale(1,1, snowCompression);
        snowman.addChild(bigBall);
        proc.pop();

        proc.push();
        proc.fill(0);
        final PShape button1 = proc.createShape(SPHERE,buttonRadius);
        button1.scale(1, buttonCompression, 1);
        button1.translate(0, bigRadius, 0);
        snowman.addChild(button1);
        proc.pop();


        proc.push();
        proc.fill(255);
        proc.emissive(0, 26, 51);
        final PShape middleBall = proc.createShape(SPHERE,middleRadius);
        middleBall.scale(1, 1, snowCompression);
        middleBall.translate(0,0,bigRadius * snowCompression);
        snowman.addChild(middleBall);
        proc.pop();

        proc.push();
        proc.fill(100, 0, 0);
        final PShape button2 = proc.createShape(SPHERE,buttonRadius);
        button2.scale(1, buttonCompression, 1);
        button2.translate(0,middleRadius,bigRadius * snowCompression);
        snowman.addChild(button2);
        proc.pop();

        proc.push();
        proc.fill(255);
        proc.emissive(0, 26, 51);
        final PShape smallBall = proc.createShape(SPHERE,smallRadius);
        // no compression for top ball as it messes with hat fit.
        smallBall.translate(0,0,(bigRadius + middleRadius) * snowCompression);
        snowman.addChild(smallBall);
        proc.pop();

        proc.push();
        proc.fill(255,130,0);
        proc.emissive(95,43,0);
        final PShape carrot = cylinder(buttonRadius, 0, bigRadius, 32, proc);
        carrot.rotateY(HALF_PI);
        carrot.translate(0, smallRadius, (bigRadius + middleRadius) * snowCompression);
        snowman.addChild(carrot);
        proc.pop();

        proc.push();
        proc.fill(50);
        proc.emissive(41, 21, 0);
        final PShape arm1 = cylinder(buttonRadius / 2,
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
        final PShape arm2 = cylinder(buttonRadius / 2,
                buttonRadius / 2,
                middleRadius * 1.5f,
                3,
                proc);
        arm2.translate(0, middleRadius, (bigRadius + middleRadius / 2f) * snowCompression);
        arm2.rotateZ(-QUARTER_PI);
        snowman.addChild(arm2);
        proc.pop();

        proc.push();
        proc.fill(0, 20, 200);
        proc.emissive(0, 2, 20);
        proc.specular(100, 150, 255);
        PShape leftEye = proc.createShape(SPHERE, eyeRadius);
        leftEye.translate(smallRadius * (sin(radians(20)) + 0),
                smallRadius * (cos(radians(20)) + 0),
                (bigRadius + middleRadius) * snowCompression + smallRadius * sin(radians(20)));
        snowman.addChild(leftEye);
        proc.pop();

        proc.push();
        proc.fill(0, 2, 20);
        proc.emissive(0, 2, 20);
        proc.specular(200, 200, 255);
        PShape rightEye = proc.createShape(SPHERE, eyeRadius);
        rightEye.translate(-smallRadius * (sin(radians(20)) + 0),
                smallRadius * (cos(radians(20)) + 0),
                (bigRadius + middleRadius) * snowCompression + smallRadius * sin(radians(20)));
        snowman.addChild(rightEye);
        proc.pop();

        proc.push();
        proc.fill(0);
        proc.shininess(.1f);
        proc.specular(255, 255, 255);
        final PShape hat = proc.createShape(GROUP);
        final PShape flatPart = proc.createShape(ELLIPSE, 0, 0, 2 * bigRadius, 2 * bigRadius);
        hat.addChild(flatPart);
        proc.push();
        final PShape cylinderPart = cylinder(smallRadius, smallRadius, 2 * smallRadius, 64, proc);
        cylinderPart.rotateX(HALF_PI);
        cylinderPart.translate(0, 0, smallRadius);
        hat.addChild(cylinderPart);
        proc.pop();
        hat.translate(0, 0,  (smallRadius / 2) * snowCompression);
        hat.rotateX(PI/6);
        hat.rotateY(PI/6);
        hat.translate(0, 0, (bigRadius + middleRadius) * snowCompression);
        snowman.addChild(hat);
        proc.pop();

        proc.pop();
        snowman.translate(0, 0, bigRadius / 2f);
        return snowman;
    }

    /**
     * A rotated {@link PShape} representing a builder.
     * Uses {@link #getSnowman(float, PApplet)} internally.
     * @param maxFootPrint The maximum dimension (x or y) of the footprint in pixels.
     *                     Makes sure the <code>PShape</code> will fit inside certain bounds.
     *                     (The <code>PShape</code> is assumed to be centered
     *                     so that the assured fit will hold under any rotation around z-axis.)
     * @param direction The {@link Direction} in which the builder should face.
     * @param proc The calling {@link PApplet}.
     * @return A sprite <code>PShape</code> facing {@link Direction#SOUTH}
     * (or not showing direction at all).
     */
    public static @NotNull PShape getDirectionalPShape(float maxFootPrint, @NotNull Direction direction, PApplet proc) {
        final PShape pShape = getUnidirectionalPShape(maxFootPrint, proc);
        final float angle;
        switch (direction) {
            case EAST -> angle = -HALF_PI;
            case NORTH -> angle = PI;
            case WEST -> angle = HALF_PI;
            default -> angle = 0;
        }
        pShape.rotateZ(angle);
        return pShape;
    }
}
