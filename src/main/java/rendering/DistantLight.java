package rendering;

public class DistantLight extends LightSource {
    //Distant lights are only "directional"
    //So we only care about the rotation part (containted in the matrix "A")
    //of the affine matrix
    //The default direction is assumed to be (0, 0, -1) (camera orientation)
    //The direction is computed by multiplying this vector with the rotation matrix

    private final Vector3d direction;


    public Vector3d getDirection() {
        return direction;
    }

    protected DistantLight(Vector3d color, double intensity, Matrix4D lightToWorld) {
        super(color, intensity, lightToWorld);
        Vector3d v = new Vector3d(0, 0, -1);
        direction = v.matrixLinearTransform(lightToWorld.getA()).normalize(); //normalize just to be sure
    }

    public DistantLight(Vector3d color, double intensity, Vector3d direction) {
        super(color, intensity, Matrix4D.identity);
        this.direction = direction.normalize();
    }


    public Vector3d illuminate(double distance) {
        return this.getColor().mul(this.getIntensity());
    }

    @Override
    public LightInfo getDirectionAndDistance(Vector3d hitPoint) {
        return new LightInfo(direction.mul(-1), Double.POSITIVE_INFINITY);
    }

    @Override
    public String toString() {
        return "Distant light; " + "Direction: " + getDirection();
    }
}
