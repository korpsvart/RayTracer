public class BezierCurve3 {

    //degree 3 Bezier Curve
    //Coded explicitly for performance reasons
    private final Vector3f[] controlPoints;


    public BezierCurve3(Vector3f p0, Vector3f p1, Vector3f p2, Vector3f p3) {
        controlPoints = new Vector3f[4];
        controlPoints[0] = p0;
        controlPoints[1] = p1;
        controlPoints[2] = p2;
        controlPoints[3] = p3;
    }

    public BezierCurve3(Vector3f[] controlPoints) {
        //we should check it contains 4 elements
        this.controlPoints = controlPoints.clone();
    }

    public Vector3f evaluate(double t) {
        //Using DeCasteljau algorithm

        //Level 1
        Vector3f p10 = controlPoints[0].mul(1-t).add(controlPoints[1].mul(t));
        Vector3f p11 = controlPoints[1].mul(1-t).add(controlPoints[2].mul(t));
        Vector3f p12 = controlPoints[2].mul(1-t).add(controlPoints[3].mul(t));

        //Level 2
        Vector3f p20 = p10.mul(1-t).add(p11.mul(t));
        Vector3f p21 = p11.mul(1-t).add(p12.mul(t));

        //Level 3, point is P(t)
        return p20.mul(1-t).add(p21.mul(t));
    }

    public BezierCurve3[] split(double t) {
        //Generate 2 Bezier curves (always of degree 3)
        //by subdivision of original curve, evaluated at t
        //Always based on DeCasteljau algorithm
        //(Useful to simplify intersection problems)

        BezierCurve3 subCurves[] = new BezierCurve3[2];

        //Level 1
        Vector3f p10 = controlPoints[0].mul(1-t).add(controlPoints[1].mul(t));
        Vector3f p11 = controlPoints[1].mul(1-t).add(controlPoints[2].mul(t));
        Vector3f p12 = controlPoints[2].mul(1-t).add(controlPoints[3].mul(t));

        //Level 2
        Vector3f p20 = p10.mul(1-t).add(p11.mul(t));
        Vector3f p21 = p11.mul(1-t).add(p12.mul(t));

        //Level 3, point is P(t)
        Vector3f p = p20.mul(1-t).add(p21.mul(t));

        subCurves[0] = new BezierCurve3(controlPoints[0], p10, p20, p);
        subCurves[1] = new BezierCurve3(p, p21, p12, controlPoints[3]);

        return subCurves;

    }
}
