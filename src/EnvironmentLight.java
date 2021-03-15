import java.awt.*;

public class EnvironmentLight extends LightSource {

    //no translation, no direction, no scaling
    //we should probably do a refactoring for this...

    protected EnvironmentLight(Color color, float intensity, Matrix4D lightToWorld) {
        super(color, intensity, lightToWorld);
    }

    public EnvironmentLight(Color color, float intensity) {
        super(color, intensity, Matrix4D.identity);
    }

    public Vector3f illuminate(double distance) {
        return Vector3f.colorToVector(this.getColor()).mul(this.getIntensity());
    }
}
