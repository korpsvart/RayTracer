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
        double a = nodes[i-1];
        double b = nodes[i];
        //remap over [0,1]
        double u = (t-a) / (b-a);
        return this.curves[i-1].evaluate(u);
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
            curves[i] = new BezierCurve3(finalCP[i*3], finalCP[i*3+1], finalCP[i*3+2], finalCP[i*3+3]);
        }
        return new BezierSpline3(l, nodes, curves);
    }
}
