public class BezierSpline3 {

    //class for designing degree 3 bezier splines


    private int l; //number of bezier curves making up the spline
    private double[] nodes;
    private double[] deltas;
    private BezierCurve3[] curves;

    public BezierSpline3(int l, double[] nodes, Vector3f[] deBorPoints) {
        //check that
        //number of nodes is l+1
        //number of debor points is l + 3
        if (nodes.length != (l+1) || deBorPoints.length != (l+3)) {
            throw new IllegalArgumentException("Invalid array length for one of the arguments");
        }
        this.l = l;
        this.deltas = new double[l];
        for (int i = 0; i < l; i++) {
            deltas[i] = nodes[i+1]-nodes[i];
        }
        this.curves = new BezierCurve3[l];
        this.nodes = nodes.clone();
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
            this.curves[i-1] = new BezierCurve3(controlPoints[h],
                    controlPoints[h+1],
                    controlPoints[h+2],
                    controlPoints[h+3]);
        }
        curves[l-1] = new BezierCurve3(controlPoints[3*l-3],
                controlPoints[3*l-2],
                controlPoints[3*l-1],
                controlPoints[3*l]);
    }

    public Vector3f evaluate(double t) {
        //t must fall inside
        //first and last node values
        //i.e. inside [u0, ul]
        if (t < nodes[0] || t > nodes[l]) {
            throw new IllegalArgumentException("t is not inside valid range");
        }
        int i;
        for (i = 1; t <= nodes[i]; i++);
        double a = nodes[i-1];
        double b = nodes[i];
        //remap over [0,1]
        double u = (t-a) / (b-a);
        return this.curves[i-1].evaluate(u);
    }
}
