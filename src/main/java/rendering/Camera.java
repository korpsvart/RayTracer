package rendering;

public class Camera {

    private Vector3d position;
    private Vector3d up, forward, left;

    public Camera(Vector3d position, Vector3d up, Vector3d forward, Vector3d left) {
        this.position = position;
        this.up = up;
        this.forward = forward;
        this.left = left;
    }

    public Camera() {
        //default
        this.position = new Vector3d(0, 0, 0);
        this.up = new Vector3d(0, 1, 0);
        this.forward = new Vector3d(0, 0, 1);
        this.left = new Vector3d(1, 0, 0); //it's cross product of up and forward
    }

    public void rotateX(double angle) {
        //up-down camera rotation
        //angle is given in degrees
        //and is converted in radians by the static matrix function
        //left axis is left unchanged

        //we calculate the forward vector
        //then we obtain the up vector by taking the cross product forward X left
        forward = Matrix3D.rotationAroundxAxis(angle).transformVector(forward).normalize();
        up = forward.crossProduct(left);

    }

    public void rotateY(double angle) {
        //rotating left or right

        //up vector is left unchanged

        left = Matrix3D.rotationAroundyAxis(angle).transformVector(left).normalize();
        forward = left.crossProduct(up);

    }

    public void translate(Vector3d displacement) {
        //displacement is relative to local coordinate system
        //As it's usually given when dealing with user input
        //So it must be translated to global fixed system
        //This can be achieved simply by multiplying the displacement vector
        //by a matrix having the 3 axis as columns

        Matrix3D lcs = new Matrix3D(new Vector3d[]{left, up, forward}, Matrix3D.COL_VECTOR);
        displacement = lcs.transformVector(displacement);
        this.position = this.position.add(displacement);

    }

    public Vector3d getPosition() {
        return position;
    }

    public Vector3d getForward() {
        return forward;
    }

    public Vector3d convertToFixedSystem(Vector3d v) {
        Matrix3D lcs = new Matrix3D(new Vector3d[]{left, up, forward}, Matrix3D.COL_VECTOR);
        return lcs.transformVector(v);
    }
}
