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

    public Vector3f derivative(double t) {
        //Evaluate derivative of Bezier Curve at t

        //There are several ways of doing this
        //Here we use the one which employs the DeCasteljau
        //algorithm on two curves of degree 2 (n-1 in general)

        //We know that the derivative at t is equal to
        //n*(C1(t) - C2(t))
        //where C1 is a Bezier Curve on the points {P1, ..., Pn}
        //and C2 is a Bezier Curve on the points {P0, ..., Pn-1}

        BezierCurve2 c1 = new BezierCurve2(controlPoints[1], controlPoints[2], controlPoints[3]);
        BezierCurve2 c2 = new BezierCurve2(controlPoints[0], controlPoints[1], controlPoints[2]);

        Vector3f p1 = c1.evaluate(t);
        Vector3f p2 = c2.evaluate(t);

        return p1.add(p2.mul(-1)).mul(3); //n is the degree, 3 in this case

    }

    public Vector3f derivative_v2(double t) {
        //calculate tangent vector
        //using bernstein polynomial
        return controlPoints[0].mul(-3 * (1 - t) * (1 - t)).add(
                controlPoints[1].mul(3 * (1 - t) * (1 - t) - 6 * t * (1 - t))).add(
                        controlPoints[2].mul((6 * t * (1 - t) - 3 * t * t))).add(
                                controlPoints[3].mul(3 * t * t)
        );
    }

    public Vector3f[] evaluateAndDerivative(double t) {
        //Using DeCasteljau algorithm
        //we can evaluate both the original curve
        //and the derivative at the same time

        //Level 1
        Vector3f p10 = controlPoints[0].mul(1-t).add(controlPoints[1].mul(t));
        Vector3f p11 = controlPoints[1].mul(1-t).add(controlPoints[2].mul(t));
        Vector3f p12 = controlPoints[2].mul(1-t).add(controlPoints[3].mul(t));

        //Level 2
        Vector3f p20 = p10.mul(1-t).add(p11.mul(t));
        Vector3f p21 = p11.mul(1-t).add(p12.mul(t));

        //Level 3, point is P(t)
        Vector3f p30 = p20.mul(1-t).add(p21.mul(t));

        //Tangent vector is given by
        //n*(P21 - P20)
        Vector3f tangent = p21.add(p20.mul(-1)).mul(3);

        return new Vector3f[]{p30, tangent};

    }

    public boolean checkG0(BezierCurve3 bezierCurve) {
        //check G0 (or C0, as they are the same) condition for joining two
        //degree 3 bezier curves
        return this.controlPoints[3].equals(bezierCurve.controlPoints[0]);
    }

    public boolean checkG1(BezierCurve3 bezierCurve3) {
        //check G1 condition for joining
        //we simply check if the first and last segment
        //have the same direction
        Vector3f last = this.controlPoints[3].add(this.controlPoints[2].mul(-1));
        Vector3f first = bezierCurve3.controlPoints[1].add(bezierCurve3.controlPoints[0].mul(-1));
        return last.normalize().equals(first.normalize());
    }


}
