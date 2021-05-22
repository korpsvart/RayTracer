package rendering;

public class BezierCurve2 {

    //coded explicitly for performance
    //a class for bezier curve of degree 2
    //is useful for calculating derivatives
    //of degree 3 curves

    private Vector3d[] controlPoints;

    public BezierCurve2(Vector3d[] controlPoints) {
        //we should check it contains 4 elements
        this.controlPoints = controlPoints.clone();
    }

    public BezierCurve2(Vector3d p0, Vector3d p1, Vector3d p2) {
        controlPoints = new Vector3d[4];
        controlPoints[0] = p0;
        controlPoints[1] = p1;
        controlPoints[2] = p2;
    }

    public Vector3d evaluate(double t) {
        Vector3d p10 = controlPoints[0].mul(1-t).add(controlPoints[1].mul(t));
        Vector3d p11 = controlPoints[1].mul(1-t).add(controlPoints[2].mul(t));

        Vector3d p20 = p10.mul(1-t).add(p11.mul(t));

        return p20;

    }
}
