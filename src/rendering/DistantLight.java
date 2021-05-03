package rendering;

public class DistantLight extends LightSource {
    //Distant lights are only "directional"
    //So we only care about the rotation part (containted in the matrix "A")
    //of the affine matrix
    //The default direction is assumed to be (0, 0, -1) (camera orientation)
    //The direction is computed by multiplying this vector with the rotation matrix

    private final Vector3f direction;


    protected DistantLight(Vector3f color, double intensity, Matrix4D lightToWorld) {
        super(color, intensity, lightToWorld);
        Vector3f v = new Vector3f(0, 0, -1);
        direction = v.matrixLinearTransform(lightToWorld.getA()).normalize(); //normalize just to be sure
    }

    public DistantLight(Vector3f color, double intensity, Vector3f direction) {
        super(color, intensity, Matrix4D.identity);
        this.direction = direction.normalize();
    }


    public Vector3f illuminate(double distance) {
        return this.getColor().mul(this.getIntensity());
    }

    @Override
    public LightInfo getDirectionAndDistance(Vector3f hitPoint) {
        return new LightInfo(direction.mul(-1), Double.POSITIVE_INFINITY);
    }
}
