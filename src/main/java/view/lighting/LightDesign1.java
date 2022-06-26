package view.lighting;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import processing.core.PApplet;

import java.util.List;

import static processing.core.PApplet.*;

/**
 * The main light design for its original package.
 * Combines different light sources which change over time.
 */
@SuppressWarnings("unused")
public record LightDesign1(PApplet proc, int sizeX, int sizeY) implements Lighting {

    @Override
    public void turnOn() {
        final List<Lighting> lights = getLights();
        lights.forEach(Lighting::turnOn);
    }

    /**
     * @return A <code>List</code> of several {@link Lighting}s,
     * ready to be turned on individually.
     * @see #turnOn()
     */
    private @Unmodifiable @NotNull List<Lighting> getLights() {
        final int millis = proc.millis();
        final float radius = getRadius(sizeX, sizeY);
        final float x = circleXAtTime(millis, radius);
        final float y = circleYAtTime(millis, radius);

        Lighting rotatingLights = () -> {
            proc.pointLight(189, 189, 0, x, y, radius);
            proc.pointLight(0, 189, 189, -x, -y, radius);
            proc.pointLight(189, 0, 0, -x, y, radius);
        };

        Lighting specularRotatingLight = withSpecular(55, 55, 55,
                () -> proc.pointLight(0, 0, 189, x, y, radius));

        return List.of(rotatingLights, specularRotatingLight);
    }

    /**
     * @param mazeX The length of a rectangle's base.
     * @param mazeY The length of a rectangle's side.
     * @return The radius of a circle that perfectly encloses
     * a rectangle of the given side lengths.
     */
    private float getRadius(int mazeX, int mazeY) {
        return sqrt(sq(mazeX) + sq(mazeY)) / 2f;
    }

    /**
     * Calculates the x-coordinate of a rotating point.
     * @param millis A millisecond timestamp.
     * @param radius The radius of the circle of rotation.
     * @return The x-coordinate on the circle at the specified moment in time.
     * @see #circleYAtTime(int, float)
     */
    private float circleXAtTime(int millis, float radius) {
        return radius * (cos(radians(millis / 30f % 360)));
    }

    /**
     * Calculates the y-coordinate of a rotating point.
     * @param millis A millisecond timestamp.
     * @param radius The radius of the circle of rotation.
     * @return The y-coordinate on the circle at the specified moment in time.
     * @see #circleXAtTime(int, float)
     */
    private float circleYAtTime(int millis, float radius) {
        return radius * (sin(radians(millis / 30f % 360)));
    }

    /**
     * Decorates an uncalled {@link Lighting} function with specular light,
     * making it a complex <code>Lighting</code> that can be turned on.
     * @param r red (0-255)
     * @param g green (0-255)
     * @param b blue (0-255)
     * @param lightFunction The <code>Runnable</code> to make a {@link Lighting}
     * @return The given <code>Lighting</code> decorated with specular light.
     * @apiNote It can not take a finished <code>Lighting</code> as input,
     * because this could not be decorated after the fact.
     * This is due to the implementation of {@link PApplet}.
     * @see #turnOn()
     * @see PApplet#lightSpecular(float, float, float)
     */
    @SuppressWarnings("SameParameterValue")
    @Contract(pure = true)
    private @NotNull Lighting withSpecular(int r, int g, int b, @NotNull Runnable lightFunction) {
        return () -> {
            proc.lightSpecular(r, g, b);
            lightFunction.run();
        };
    }
}
