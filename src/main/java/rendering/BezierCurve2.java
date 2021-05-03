package rendering;

public class BezierCurve2 {

    //coded explicitly for performance
    //a class for bezier curve of degree 2
    //is useful for calculating derivatives
    //of degree 3 curves

    private Vector3f[] controlPoints;

    public BezierCurve2(Vector3f[] controlPoints) {
        //we should check it contains 4 elements
        this.controlPoints = controlPoints.clone();
    }

    public BezierCurve2(Vector3f p0, Vector3f p1, Vector3f p2) {
        controlPoints = new Vector3f[4];
        controlPoints[0] = p0;
        controlPoints[1] = p1;
        controlPoints[2] = p2;
    }

    public Vector3f evaluate(double t) {
        Vector3f p10 = controlPoints[0].mul(1-t).add(controlPoints[1].mul(t));
        Vector3f p11 = controlPoints[1].mul(1-t).add(controlPoints[2].mul(t));

        Vector3f p20 = p10.mul(1-t).add(p11.mul(t));

        return p20;

    }
}
