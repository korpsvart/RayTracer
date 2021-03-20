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

    public BezierCurve extractBezier(int i) {
        //i is the interval on which is defined the bezier curve
        //First approach: by knot insertion
        //Example: suppose the bezier curve is degree n=3
        //and is defined over the interval [c, d]
        //then the first control point is b[c,c,c]
        //the second is b[c,c,d]
        //the third is b[c,d,d]
        //and the last is b[d,d,d]
        //by performing knot insertion
        //(inserting c two times and d two times)
        //we get new DeBoor points for the spline,
        //which correspond to bezier control points
        
    }


}
