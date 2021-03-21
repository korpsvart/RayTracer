public class BSplines {

    //Class for BSplines

    private Vector3f[] controlPoints;
    private double[] knots;
    private int degree;


    public BSplines(Vector3f[] controlPoints, double[] knots, int degree) {
        //check that length(controlPoints)=length(knots)-n+1
        this.controlPoints = controlPoints;
        this.knots = knots;
        this.degree = degree;
    }

//    public BezierCurve extractBezier(int i) {
//        //i is the interval on which is defined the bezier curve
//        //First approach: by knot insertion
//        //Example: suppose the bezier curve is degree n=3
//        //and is defined over the interval [c, d]
//        //then the first control point is b[c,c,c]
//        //the second is b[c,c,d]
//        //the third is b[c,d,d]
//        //and the last is b[d,d,d]
//        //by performing knot insertion
//        //(inserting c two times and d two times)
//        //we get new DeBoor points for the spline,
//        //which correspond to bezier control points
//
//    }

    public Vector3f evaluate(int u) {
        //check u is inside spline domain
        if (u < knots[degree-1] || u > knots[controlPoints.length-1]) {
            throw new IllegalArgumentException("Parameter value outside of spline domain");
        }
        //find relevant interval
        int I;
        int r = 0; //multiplicity
        for(I=0; knots[I+1]<=u; I++) {
            if (knots[I+1]==u) r++;
        }
        Vector3f[] localPoints = new Vector3f[degree+1];
        for (int j = 0; j <= degree; j++) {
            localPoints[j] = controlPoints[I-degree+1+j];
        }
        for (int k = r+1; k <= degree; k++) {
            for (int i = 0; i <= degree - k; i++) {
                double a = (u-knots[I-degree+i+k])/(knots[I+i+1] - knots[I-degree+i+k]);
                localPoints[i] = localPoints[i].mix(localPoints[i+1], a);
            }
        }
        return localPoints[0];
    }


    public BezierCurve extractBezier2(int i) {
        //second version
        //by evaluating spline and using linear interpolation
    }


}
