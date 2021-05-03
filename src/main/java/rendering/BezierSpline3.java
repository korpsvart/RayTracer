package main.java.rendering;

public class BezierSpline3 {

    //class for designing degree 3 bezier splines


    private int l; //number of bezier curves making up the spline
    private double[] nodes;
    private BezierCurve3[] curves;

    public BezierSpline3(int l, double[] nodes, BezierCurve3[] curves) {
        this.l = l;
        this.nodes = nodes;
        this.curves = curves;
    }

    public static BezierSpline3 CreateC2Spline(int l, double[] nodes, Vector3f[] deBorPoints) {
        //NOT WORKING!
        //create a spline which satisfies C2 linking condition

        //check that
        //number of nodes is l+1
        //number of debor points is l + 3
        if (nodes.length != (l+1) || deBorPoints.length != (l+3)) {
            throw new IllegalArgumentException("Invalid array length for one of the arguments");
        }
        double[] deltas = new double[l];
        for (int i = 0; i < l; i++) {
            deltas[i] = nodes[i+1]-nodes[i];
        }
        BezierCurve3[] curves = new BezierCurve3[l];
        Vector3f[] controlPoints = new Vector3f[3*l+1];
        controlPoints[0] = deBorPoints[0];
        controlPoints[1] = deBorPoints[1];
        controlPoints[3*l] = deBorPoints[l+2];
        controlPoints[3*l-1] = deBorPoints[l+1];
        //not sure how to calculate controlPoints[2], here is my guess
        double denom = deltas[0]+deltas[1];
        controlPoints[2] = deBorPoints[1].mul(deltas[1]/denom).add(deBorPoints[2].mul(deltas[0]/denom));
        for (int i = 1; i < l; i++) {
            int i2 = i+1;
            denom = deltas[i2-2]+deltas[i2-1]+deltas[i2];
            controlPoints[i2*3-2] = deBorPoints[i2-1].mul((deltas[i2-1]+deltas[i2])/denom).
                    add(deBorPoints[i2].mul(deltas[i2-2]/denom));
            if (i < l-1) {
                controlPoints[i2*3-1] = deBorPoints[i2-1].mul(deltas[i2]/denom).
                        add(deBorPoints[i2].mul((deltas[i2-2]+deltas[i2-1])/denom));
            }
            denom = deltas[i+1]+deltas[i];
            controlPoints[i*3] = controlPoints[i*3-1].mul(deltas[i]/denom).
                    add(controlPoints[i*3+1].mul(deltas[i-1]/denom));
            int h = (i-1)*3;
            curves[i-1] = new BezierCurve3(controlPoints[h],
                    controlPoints[h+1],
                    controlPoints[h+2],
                    controlPoints[h+3]);
        }
        curves[l-1] = new BezierCurve3(controlPoints[3*l-3],
                controlPoints[3*l-2],
                controlPoints[3*l-1],
                controlPoints[3*l]);
        return new BezierSpline3(l, nodes, curves);
    }

    public Vector3f evaluate(double t) {
        //t must fall inside
        //first and last node values
        //i.e. inside [u0, ul]
        if (t < nodes[0] || t > nodes[l]) {
            throw new IllegalArgumentException("t is not inside valid range");
        }
        int i;
        for (i = 1; t > nodes[i]; i++);
        //remapping is done inside the curve
        return this.curves[i-1].evaluate(t);
    }


    public static BezierSpline3 CreateC1Spline(int l, double[] nodes, Vector3f[] cP) {
        if (nodes.length != (l+1) || cP.length != (2*l+2)) {
            throw new IllegalArgumentException("Invalid array lengths");
        }
        Vector3f[] finalCP = new Vector3f[l*3+1];
        BezierCurve3[] curves = new BezierCurve3[l];
        finalCP[0] = cP[0];
        finalCP[1] = cP[1];
        finalCP[2] = cP[2];
        for (int i = 1; i < l; i++) {
            double deltaN = nodes[i+1]-nodes[i-1];
            double deltaD = nodes[i]-nodes[i-1];
            double inverseT = deltaD / deltaN;
            finalCP[i*3] = cP[2*i].mix(cP[2*i+1], inverseT);
            finalCP[i*3+1] = cP[i*2+1];
            finalCP[i*3+2] = cP[i*2+2];
        }
        finalCP[l*3] = cP[2*l+1];
        for (int i = 0; i < l; i++) {
            curves[i] = new BezierCurve3(finalCP[i*3], finalCP[i*3+1], finalCP[i*3+2], finalCP[i*3+3], nodes[i], nodes[i+1]);
        }
        return new BezierSpline3(l, nodes, curves);
    }


    public static BezierSpline3 piecewiseCubicInterpolation(Vector3f[] dataPoints, Vector3f[] tangents) {
        //check that the arrays have same number of elements
        int l = dataPoints.length;
        if (l != tangents.length) {
            throw new IllegalArgumentException("Arrays must have same length");
        }

        //tangents must be normalized
        //actually its not really necessary
        //but if we want to guarantee exact tangent equivalence at junction points
        //we should divide every tangent by 3 (as we did for the second and second to last point)
        for (int i = 0; i < tangents.length; i++) {
            tangents[i] = tangents[i].normalize();
        }
        double[] alfa = new double[l-1];
        double[] beta = new double[l-1];
        double[] pointDeltas = new double[l];
        for (int i = 0; i < l-1; i++) {
            pointDeltas[i] = dataPoints[i].moveTo(dataPoints[i+1]).magnitude();
        }
        for (int i = 0; i < l-1; i++) {
            alfa[i]=beta[i]=0.9*pointDeltas[i]; //farin heuristic
        }
        double nodes[] = new double[l];
        nodes[0] = 0; //arbitrary
        nodes[1] = nodes[0]+beta[0];
        for (int i = 2; i < l; i++) {
            nodes[i] = nodes[i-1]+alfa[i-1];
        }
        BezierCurve3[] curves = new BezierCurve3[l-1];
        Vector3f[] controlPoints = new Vector3f[(l-1)*3+1];
        for (int i = 0; i < l; i++) {
            controlPoints[3*i] = dataPoints[i];
        }
        controlPoints[1] = controlPoints[0].add(tangents[0].mul(alfa[0]/3));
        controlPoints[(l-1)*3-1] = controlPoints[(l-1)*3].add(tangents[l-1].mul(-beta[l-2]/3));
        for (int i = 1; i < l-1; i++) {
            controlPoints[i*3+1] = controlPoints[3*i].add(tangents[i].mul(alfa[i]/3));
            controlPoints[i*3-1] = controlPoints[3*i].add(tangents[i].mul(-beta[i-1]/3));
        }
        for (int i = 0; i < l - 1; i++) {
            curves[i] = new BezierCurve3(controlPoints[i*3], controlPoints[i*3+1], controlPoints[i*3+2], controlPoints[i*3+3], nodes[i], nodes[i+1]);
        }

        return new BezierSpline3(l-1, nodes, curves);
    }



    public BezierCurve3[] getCurves() {
        return curves;
    }
}
