import java.awt.*;

public abstract class LightSource {

    //Lights are unaffected by scaling transform

    private final Color color;
    private final float intensity;
    private final Matrix4D lightToWorld;

    public float getIntensity() {
        return intensity;
    }

    public abstract Vector3f illuminate(double distance);



    protected LightSource(Color color, float intensity, Matrix4D lightToWorld) {
        this.color = color;
        this.intensity = intensity;
        this.lightToWorld = lightToWorld;
    }


    public Color getColor() {
        return color;
    }

    public LightInfo getDirectionAndDistance(Vector3f hitPoint) {
        return null;
    }

    class LightInfo {

        Vector3f lightDir;
        double distance;

        public LightInfo(Vector3f lightDir, double distance) {
            this.lightDir = lightDir;
            this.distance = distance;
        }

        public double getDistance() {
            return distance;
        }

        public Vector3f getLightDir() {
            return lightDir;
        }
    }

}
