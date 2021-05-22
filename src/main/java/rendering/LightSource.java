package rendering;

public abstract class LightSource {

    //Lights are unaffected by scaling transform

    private final Vector3d color;
    private double normalizedIntensity;
    private final double intensity;
    private final Matrix4D lightToWorld;

    public double getIntensity() {
        return intensity;
    }

    public abstract Vector3d illuminate(double distance);



    protected LightSource(Vector3d color, double intensity, Matrix4D lightToWorld) {
        this.color = color;
        this.intensity = intensity;
        this.lightToWorld = lightToWorld;
    }


    public Vector3d getColor() {
        return color;
    }

    public LightInfo getDirectionAndDistance(Vector3d hitPoint) {
        return null;
    }

    class LightInfo {

        Vector3d lightDir;
        double distance;

        public LightInfo(Vector3d lightDir, double distance) {
            this.lightDir = lightDir;
            this.distance = distance;
        }

        public double getDistance() {
            return distance;
        }

        public Vector3d getLightDir() {
            return lightDir;
        }
    }

    public void setNormalizedIntensity(double normalizedIntensity) {
        this.normalizedIntensity = normalizedIntensity;
    }

    public double getNormalizedIntensity() {
        return normalizedIntensity;
    }
}
