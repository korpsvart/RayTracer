package rendering;

public class PointLight extends LightSource {
    //PointLight are unaffected by rotation
    //(and by scaling, as any other light)
    //Only translation is relevant
    //Thus we only care about the translation part (c vector)
    //of the affine matrix

    private final Vector3d position;

    public Vector3d getPosition() {
        return position;
    }


    public PointLight(Vector3d color, double intensity, Matrix4D lightToWorld) {
        super(color, intensity, lightToWorld);
        position = lightToWorld.getC();
    }

    public PointLight(Vector3d color, double intensity, Vector3d position) {
        super(color, intensity, new Matrix4D(position));
        this.position = position;
    }


    @Override
    public LightInfo getDirectionAndDistance(Vector3d hitPoint) {
        Vector3d lightDir = hitPoint.moveTo(position);
        double distance = lightDir.magnitude();
        return new LightInfo(lightDir.normalize(), distance);
    }

    public Vector3d illuminate(double distance) {
        double intensity = this.getIntensity()/(4*Math.PI*Math.pow(distance, 2));
        return this.getColor().mul(intensity);
    }


    @Override
    public String toString() {
        return "Point light; " + "Translation: " + getPosition();
    }
}
