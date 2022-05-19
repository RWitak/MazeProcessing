package view.lighting;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import processing.core.PApplet;

import java.util.List;

import static processing.core.PApplet.*;

@SuppressWarnings("unused")
public record LightDesign1(PApplet proc, int sizeX, int sizeY) implements Lighting {

    @Override
    public void turnOn() {
        final List<Lighting> lights = getLights();
        lights.forEach(Lighting::turnOn);
    }

    private @Unmodifiable List<Lighting> getLights() {
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

    private float getRadius(int mazeX, int mazeY) {
        return sqrt(sq(mazeX) + sq(mazeY)) / 2f;
    }

    private float circleXAtTime(int millis, float radius) {
        return radius * (cos(radians(millis / 30f % 360)));
    }

    private float circleYAtTime(int millis, float radius) {
        return radius * (sin(radians(millis / 30f % 360)));
    }

    @SuppressWarnings("SameParameterValue")
    @Contract(pure = true)
    private @NotNull Lighting withSpecular(int r, int g, int b, @NotNull Runnable lightFunction) {
        return () -> {
            proc.lightSpecular(r, g, b);
            lightFunction.run();
        };
    }
}
