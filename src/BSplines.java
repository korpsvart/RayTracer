public class BSplines {

    //Class for BSplines

    private Vector3f[] controlPoints;
    private double[] knots;
    private int degree;
    private boolean clamped = false;


    public BSplines(Vector3f[] controlPoints, double[] knots, int degree) {
        //check that length(controlPoints)=length(knots)-n+1
        this.controlPoints = controlPoints;
        this.knots = knots;
        this.degree = degree;
    }


    public void setClamped(boolean clamped) {
        this.clamped = clamped;
    }

    public BezierCurve extractBezier(int i) {
        if (clamped) {
            return this.extractBezierClamped(i);
        } else {
            return this.extractBezierNonClamped(i);
        }
    }

    public BezierCurve extractBezierNonClamped(int i) {
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


        //First, check if the interval is not empty
        //if it is, keep incrementing i
        while(knots[i]==knots[i+1]) {
            i++;
        }

        //Then, we have to check if the request interval is valid
        //for general non-clamped curves, only certain intervals are valid
        //and are those inside the spline domain, i.e. [u_p-1, u_n]
        //where p is the degree and n is the number of control points-1
        //thus i must satisfy i>=(p-1) && i<n
        if (i < degree-1 || i >= (controlPoints.length-1)) {
            throw new IllegalArgumentException("There's no Bezier Curve for the specified range");
        } else {
            double u0 = knots[i];
            double u1 = knots[i+1];
            //insert both u0 and u1 p-1 times
            BSplines newSpline = this;
            for (int j = 0; j < degree - 1; j++) {
                    newSpline = newSpline.knotInsertion(u0).knotInsertion(u1);
            }
            //the bezier points are given by Pi, Pi+1,...,Pi+p
            Vector3f[] cp = new Vector3f[degree+1];
            for (int j = 0; j < degree + 1; j++) {
                    cp[j] = newSpline.controlPoints[i+j];
            }

            return new BezierCurve(cp, degree);
        }
    }

    public BezierCurve extractBezierClamped(int i) {
        //only works for clamped bsplines

        i+=degree;
        while(knots[i]==knots[i+1]) {
            i++;
        }
        //for clamped bsplines
        //we simply ask that i is inside {degree,...,L}
        //(i.e. inside {0, L-degree} for original i before we increment it by degree)
        //where L is number of control points - 1
        if (i < degree || i >= controlPoints.length) {
            throw new IllegalArgumentException("There's no Bezier Curve for the specified range");
        }
        double u0 = knots[i];
        double u1 = knots[i+1];
        int c = 0;
        //insert both u0 and u1 p-1 times
        BSplines newSpline = this;
        if (i==degree) {
            //only need to insert u1
            //Unfortunately it's not only more efficient
            //but its actually needed because my code is very messy
            //and if we don't handle these two corner cases separately
            //we'll get an out of bound exception
            for (int j = 0; j < degree - 1; j++) {
                newSpline = newSpline.knotInsertion2(u1);
                c++;
            }
        } else if (i==(controlPoints.length-1)){
            //only need to insert u0
            for (int j = 0; j < degree - 1; j++) {
                newSpline = newSpline.knotInsertion2(u0);
                c++;
            }
        } else {
            for (int j = 0; j < degree - 1; j++) {
                newSpline = newSpline.knotInsertion2(u0).knotInsertion2(u1);
                c+=2;
            }
        }
        //the bezier points are given by Pi-1, Pi,...,Pi+p-1
        Vector3f[] cp = new Vector3f[degree+1];
        for (int j = 0; j < degree + 1; j++) {
            cp[j] = newSpline.controlPoints[(i-degree)*degree+j];
        }
        return new BezierCurve(cp, degree);

    }

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

    public BSplines knotInsertion(double u) {
        //perform knot insertion
        //based on Bohem's algorithm
        //Return new spline with new knot vectors
        //and new control points
        double[] newKnots = new double[knots.length+1];
        int I;
        for(I=0; knots[I+1]<=u; I++) {
            newKnots[I]=knots[I];
        }
        newKnots[I]=knots[I];
        newKnots[I+1]=u;
        for (int i = I+2; i < newKnots.length; i++) {
            newKnots[i]=knots[i-1];
        }
        Vector3f[] newCP = new Vector3f[controlPoints.length+1];
        for (int i = 0; i <= I - degree + 1; i++) {
            newCP[i] = controlPoints[i];
        }
        for (int i = I+2; i < newCP.length; i++) {
            newCP[i] = controlPoints[i-1];
        }
        for (int i = I-degree+2; i <= I+1; i++) {
            if ((i==I+1) && u==knots[I]) {
                //we need to handle this separately when new knot it equal to existing knot
                //and is being inserted in the last knot span
                //This insertion should be legit, but if the curve is not clamped it's gonna
                //throw an out of bound exception
                newCP[i] = controlPoints[i-1];
            } else {
                double a = (u - knots[i - 1]) / (knots[i + degree - 1] - knots[i - 1]);
                newCP[i] = controlPoints[i - 1].mix(controlPoints[i], a);
            }
        }
        return new BSplines(newCP, newKnots, degree);

    }

    public BSplines knotInsertion2(double u) {
        //only works on clamped bsplines
        //where first and last knots have degree+1 multiplicity
        Vector3f[] newCP = new Vector3f[controlPoints.length+1];
        double[] newKnots = new double[knots.length+1];
        int I;
        for(I=0; knots[I+1]<=u; I++) {
            newKnots[I]=knots[I];
        }
        newKnots[I]=knots[I];
        newKnots[I+1]=u;
        for (int i = I+2; i < newKnots.length; i++) {
            newKnots[i]=knots[i-1];
        }
        for (int i = 0; i <= I-degree; i++) {
            newCP[i] = controlPoints[i];
        }
        for (int i = I-degree+1; i <= I; i++) {
            double a = (u-knots[i])/(knots[i+degree]-knots[i]);
            newCP[i] = controlPoints[i-1].mix(controlPoints[i], a);
        }
        for (int i = I+1; i < newCP.length; i++) {
            newCP[i] = controlPoints[i-1];
        }
        return new BSplines(newCP, newKnots, degree);
    }


    //    public BezierCurve extractBezier2(int i) {
//        //second version
//        //by evaluating spline and using linear interpolation
//    }


}
