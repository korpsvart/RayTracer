package rendering;

public class EnvironmentLight extends LightSource {

    //no translation, no direction, no scaling
    //we should probably do a refactoring for this...

    protected EnvironmentLight(Vector3f color, float intensity, Matrix4D lightToWorld) {
        super(color, intensity, lightToWorld);
    }

    public EnvironmentLight(Vector3f color, float intensity) {
        super(color, intensity, Matrix4D.identity);
    }

    public Vector3f illuminate(double distance) {
        return this.getColor().mul(this.getIntensity());
    }
}
