package main.java.rendering;

public class BezierCurve3 {

    //degree 3 Bezier Curve
    //Coded explicitly for performance reasons
    private final Vector3f[] controlPoints;

    //generalized range endpoints
    private double a = 0;
    private double b = 1;

    private boolean generalized = false;



    public BezierCurve3(Vector3f p0, Vector3f p1, Vector3f p2, Vector3f p3) {
        controlPoints = new Vector3f[4];
        controlPoints[0] = p0;
        controlPoints[1] = p1;
        controlPoints[2] = p2;
        controlPoints[3] = p3;
    }

    public BezierCurve3(Vector3f p0, Vector3f p1, Vector3f p2, Vector3f p3, double a, double b) {
        controlPoints = new Vector3f[4];
        controlPoints[0] = p0;
        controlPoints[1] = p1;
        controlPoints[2] = p2;
        controlPoints[3] = p3;
        this.a = a;
        this.b = b;
        this.generalized = true;
    }

    private double remap(double u) {
        //remap generalized domain to [0, 1]
        return (u-a)/(b-a);
    }

    public BezierCurve3(Vector3f[] controlPoints) {
        //we should check it contains 4 elements
        this.controlPoints = controlPoints.clone();
    }

    public Vector3f evaluate(double t) {
        //Using DeCasteljau algorithm

        if (generalized) {
            t = remap(t);
        }

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

        return generalized ? p1.add(p2.mul(-1)).mul(3/(b-a)) : p1.add(p2.mul(-1)).mul(3); //n is the degree, 3 in this case

    }

    public Vector3f derivative_v2(double t) {
        //calculate tangent vector
        //using bernstein polynomial
        Vector3f derivative = controlPoints[0].mul(-3 * (1 - t) * (1 - t)).add(
                controlPoints[1].mul(3 * (1 - t) * (1 - t) - 6 * t * (1 - t))).add(
                        controlPoints[2].mul((6 * t * (1 - t) - 3 * t * t))).add(
                                controlPoints[3].mul(3 * t * t)

        );
        return generalized ? derivative.mul(1/(b-a)) : derivative;
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
        Vector3f tangent = generalized ? p21.add(p20.mul(-1)).mul(3/(b-a)) : p21.add(p20.mul(-1)).mul(3);

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

    public boolean checkC1(BezierCurve3 bezierCurve3) {
        //we check collinearity and ratios
        if (checkG0(bezierCurve3) && checkG1(bezierCurve3)) {
            double pointsRatio = getRatio(this.controlPoints[2], this.controlPoints[3], bezierCurve3.controlPoints[1]);
            //we should also ensure that the domains are adjacent, but we dont for the moment
            double domainRatio = getRatio(this.a, this.b, bezierCurve3.b);
            return Math.abs(pointsRatio-domainRatio) < 10e-5;
        }
        return false;
    }

    public double getRatio(Vector3f p1, Vector3f p2, Vector3f p3) {
        //assuming its already known that the points are collinear
        //we can project them onto the x or y axis (if not on perpendicular to them)
        //and calculate the ratios using scalar values
        Vector3f xAxis = new Vector3f(1, 0, 0);
        double p1s = p1.dotProduct(xAxis);
        double p2s = p2.dotProduct(xAxis);
        double p3s = p3.dotProduct(xAxis);

        //if you wish you can check this is equal to doing
//        double r1 = p1.moveTo(p2).magnitude();
//        double r2 = p2.moveTo(p3).magnitude();
//        double ratio = r1/r2;

        return getRatio(p1s, p2s, p3s);
    }

    public double getRatio(double a, double b, double c) {
        return (b-a)/(c-b);
    }

    public Vector3f[] getControlPoints() {
        return controlPoints;
    }
}
