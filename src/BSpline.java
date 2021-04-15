import java.util.Arrays;

public class BSpline {

    //Class for BSplines

    private Vector3f[] controlPoints;
    private double[] knots;
    private int degree;
    private boolean clamped = true;
    private double[] t; //interpolation parameters, only for interpolant bsplines and for debugging purposes


    public double[] getT() {
        return t;
    }

    public enum ParameterMethod {
        UNIFORM, CHORD_LENGTH, BARYCENTRIC, UNIVERSAL
    };


    public BSpline(Vector3f[] controlPoints, double[] knots, int degree) {
        //check that number of control points in each direction >= (degree+1)
        //Note that if we have equality, then the bspline becomes a bezier curve
        if (controlPoints.length <= degree) {
            throw new IllegalArgumentException("number of control points should be at least equal to degree+1");
        }
//        } else if (knots.length != (controlPoints.length + degree +1)) {
//            //check that length(knots)=length(controlPoints)+degree+1
//            throw new IllegalArgumentException("equality length(knots)=length(controlPoints)+degree+1 for clamped" +
//                    "B-splines is not satisfied");
//        }
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
            BSpline newSpline = this;
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

        //might rewrite this later to clean up the code
        //This is equivalent to to subdivide the bspline
        //at its knot u_i

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
        //insert both u0 and u1 p-1 times
        BSpline newSpline = this;
        if (i==degree) {
            //only need to insert u1
            //Unfortunately it's not only more efficient
            //but its actually needed because my code is very messy
            //and if we don't handle these two corner cases separately
            //we'll get an out of bound exception
            newSpline = newSpline.multipleKnotInsertion(u1, degree-1);
        } else if (i==(controlPoints.length-1)){
            //only need to insert u0
            newSpline = newSpline.multipleKnotInsertion(u0, degree-1);
        } else {
            newSpline = newSpline.multipleKnotInsertion(u0, degree-1).multipleKnotInsertion(u1, degree-1);
        }
        //the bezier points are given by Pi-1, Pi,...,Pi+p-1
        Vector3f[] cp = new Vector3f[degree+1];
        for (int j = 0; j < degree + 1; j++) {
            cp[j] = newSpline.controlPoints[(i-degree)*degree+j];
        }
        return new BezierCurve(cp, degree);

    }

    public Vector3f evaluateNonClamped(double u) {
        //check u is inside spline domain
        if (u < knots[degree-1] || u > knots[controlPoints.length-1]) {
            throw new IllegalArgumentException("Parameter value outside of spline domain");
        }
        //find relevant interval
        int I;
        int r = 0; //multiplicity
        for(I=0; (I+1) < knots.length && knots[I+1]<=u; I++) {
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

    public BSpline knotInsertion(double u) {
        //perform knot insertion
        //based on Bohem's algorithm
        //Return new spline with new knot vectors
        //and new control points
        double[] newKnots = new double[knots.length+1];
        int I;
        for(I=0; (I+1) < knots.length && knots[I+1]<=u; I++) {
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
        return new BSpline(newCP, newKnots, degree);

    }

    public BSpline knotInsertionClamped(double u) {
        //only works on clamped bsplines
        //where first and last knots have degree+1 multiplicity
        Vector3f[] newCP = new Vector3f[controlPoints.length+1];
        double[] newKnots = new double[knots.length+1];
        int I;
        for(I=0; (I+1) < knots.length && knots[I+1]<=u; I++) {
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
        return new BSpline(newCP, newKnots, degree);
    }


    public BSpline multipleKnotInsertion(double t, int h) {
        double[] v = new double[knots.length+h];
        int k = 0; //index of knot span
        int s = 0; //multiplicity
        while(k < knots.length && knots[k]<=t) {
            if (knots[k]==t) s++;
            v[k] = knots[k];
            k++;
        }
        for (int i = 0; i < h; i++) {
            v[k+i] = t; //insert new knots
        }
        for (int i = k+h; i < knots.length+h; i++) {
            v[i] = knots[i-h]; //copy remaining knots after the newly inserted one(s)
        }
        k=k-1; //correct knot span index
        Vector3f[] newPoints = new Vector3f[controlPoints.length+h];
        int p = degree; //only for simplicity
        Vector3f[][] aux = new Vector3f[p+1-s][h+1];
        for (int i = 0; i < p + 1 - s; i++) {
            aux[i][0] = controlPoints[i+(k-p)];
        }
        //calculate new points
        for (int r = 1; r <= h; r++) {
            for (int i = r; i < p+1-s; i++) {
                int j = i+(k-p); //"real" index in global knots indexing
                double a = (t-knots[j])/(knots[j+p-r+1]-knots[j]);
                aux[i][r] = aux[i-1][r-1].mix(aux[i][r-1], a);
            }
        }
        //get the new control points
        for (int i = 0; i < k - p; i++) {
            newPoints[i] = controlPoints[i];
        }
        int i = k-p;
        for (int j = 0; j <= h; j++) {
            newPoints[i++] = aux[j][j];
        }
        for (int j = h+1; j <p+1-s; j++) {
            newPoints[i++] = aux[p-s][j];
        }
        while(i < newPoints.length) {
            newPoints[i] = controlPoints[i-h];
            i++;
        }
        return new BSpline(newPoints, v, degree);
    }

    public Vector3f evaluateClamped(double u) {
        //new version, for clamped bsplines
        //based on the principle of knot insertion
        //In this case, we can use a single vector instead of a matrix
        int k = 0; //index of knot span
        int s = 0; //multiplicity
        while(k < knots.length && knots[k]<=u) {
            if (knots[k]==u) s++;
            k++;
        }
        int p = degree;
        k=k-1;
        if (p < s) {
            //we have to tackle this problem explicitly
            //This happens when multiplicity is greater than degree
            //and going further would give us a zero length vector.
            //Ideally, this should never happen by insertion of new knots,
            //but it will surely happen if we evaluate at the domain limits
            //since those always have p+1 multiplicity in clamped B-Splines.
            //This problem is particularly annoying, since the formula k-s does work
            //for internal knots and if the parameter is equal to the last knot,
            //but it doesnt work if it's equal to the first knot.
            //in that case we need to add 1.
            //I think this problem comes from the fact that our convention use p+1 multiplicity
            //at first and last knot, while p alone would be sufficient. In fact, if we use p multiplicity
            //we can use k-s in every situation, but the code should also work without any explicit check
            //on the relationship between p and s. We would simply have a length 1 vector with only the necessary point
            //(that is already a control point for the B-Spline)
            return u==knots[0] ? controlPoints[0] : controlPoints[k-s];
        }
        //insert u only (p-s) times
        Vector3f[] aux = new Vector3f[p+1-s];
        for (int i = 0; i < p + 1 - s; i++) {
            aux[i] = controlPoints[i+(k-p)];
        }
        for (int r = 1; r <= p-s; r++) {
            for (int i = r; i < p+1-s; i++) {
                int j = i+(k-p); //"real" index in global knots indexing
                double a = (u-knots[j])/(knots[j+p-r+1]-knots[j]);
                aux[i-r] = aux[i-r].mix(aux[i-r+1], a);
            }
        }
        return aux[0];
    }

    public static Vector3f deBoor(Vector3f[] cp, double knots[], double u, int s, int p, int k) {
        //u is the parameter at which we want to evaluate the B-Spline
        //p is degree
        //s is knot multiplicity (multiplicity is defined to be zero if parameter is not a knot)
        //k is index of knot span containing parameter of evaluation
        //This routine assumes that the caller has already chosen the significant control points
        //necessary for evaluation
        if (cp.length == 1) return cp[0];
        Vector3f[] aux = cp.clone(); //to avoid modifying the array passed
        for (int r = 1; r <= p-s; r++) {
            for (int i = r; i < p+1-s; i++) {
                int j = i+(k-p); //"real" index in global knots indexing
                double a = (u-knots[j])/(knots[j+p-r+1]-knots[j]);
                aux[i-r] = aux[i-r].mix(aux[i-r+1], a);
            }
        }
        return aux[0];
    }


    public Vector3f evaluate(double u) {
        if (clamped) {
            return this.evaluateClamped(u);
        } else {
            return this.evaluateNonClamped(u);
        }
    }


    public Vector3f derivative(double u) {
        //calculate derivative at u
        //by creating a new p-1 curve and evaluating that curve
        Vector3f[] points = new Vector3f[controlPoints.length-1];
        double[] newKnots = new double[knots.length-1];
        for (int i = 0; i < newKnots.length; i++) {
            newKnots[i] = knots[i+1];
        }
        for (int i = 0; i < points.length; i++) {
            double h = degree/(knots[i+degree+1]-knots[i+1]);
            points[i] = controlPoints[i+1].add(controlPoints[i].mul(-1));
            points[i] = points[i].mul(h);
        }
        BSpline der = new BSpline(points, newKnots, degree-1);
        return der.evaluate(u);


    }

    public static double[] findParameters(ParameterMethod method, Vector3f[] dataPoints, double a, double b) {
        int n = dataPoints.length;
        double[] t = new double[n];
        if (method==ParameterMethod.CHORD_LENGTH || true) {
            //implement more methods later
            t[0] = 0;
            double[] d = new double[n];
            d[0] = 0;
            for (int i = 1; i < n; i++) {
                d[i] += dataPoints[i].distance(dataPoints[i-1])+d[i-1];
            }
            double l = d[n-1];
            for (int i = 1; i < n-1; i++) {
                t[i] = a + (b-a)*d[i]/l;
            }
            t[n-1] = b;
        }
        return t;
    }

    public static double[] findParameters(Vector3f[] dataPoints, double a, double b) {
        //if not specified, use arc length as method for parameter selection
        int n = dataPoints.length;
        double[] t = new double[n];
        //implement more methods later
        t[0] = 0;
        double[] d = new double[n];
        d[0] = 0;
        for (int i = 1; i < n; i++) {
            d[i] += dataPoints[i].distance(dataPoints[i-1])+d[i-1];
        }
        double l = d[n-1];
        for (int i = 1; i < n-1; i++) {
            t[i] = a + (b-a)*d[i]/l;
        }
        t[n-1] = b;
        return t;
    }

    public static double[] findParameters(Vector3f[][] dataPoints, double a, double b) {
        //if multiple rows are given, take an average of the parameters for each row
        //(just experimenting, no idea if this will work out nicely)
        int n = dataPoints[0].length;
        double[] t = new double[n];
        for (int k=0; k<dataPoints.length; k++) {
            //implement more methods later
            t[0] = 0;
            double[] d = new double[n];
            d[0] = 0;
            for (int i = 1; i < n; i++) {
                d[i] += dataPoints[k][i].distance(dataPoints[k][i-1])+d[i-1];
            }
            double l = d[n-1];
            for (int i = 1; i < n-1; i++) {
                t[i] += a + (b-a)*d[i]/l;
            }
            t[n-1] = b;
        }
        for (int i = 1; i < n-1; i++) {
            t[i] /= dataPoints.length;
        }
        return t;
    }


    public static double[] generateKnots(double t[], int p) {
        int n = t.length;
        double[] knots = new double[n+p+1];
        for (int i = 0; i <= p; i++) {
            knots[i] = 0;
            knots[n+p-i] = 1;
        }
        for (int j = 1; j < n - p; j++) {
            for (int i = j; i < j+p; i++) {
                knots[j+p]+=t[i];
            }
            knots[j+p]/=p;
        }
        return knots;
    }

    public static double[] uniformKnots(int n, int p) {
        double[] knots = new double[n+p+1];
        int s = n+p+1;
        double step = (double)1/(n-p);
        for(int i = 0; i <= p; i++) {
            knots[i] = 0;
        }
        for(int i = p+1; i < n+1; i++) {
            knots[i] = knots[i-1]+step;
        }
        for(int i = n+1; i < n + p + 1; i++) {
            knots[i] = 1;
        }
        return knots;
    }


    public static double[] getSplineBasis(int n, int p, double u, double[] knots) {
        //given u, a vector of m+1 knots (where m satisfies m=n+p+1)
        //n (number of control points aka number of basis functions)
        //and degree p
        //returns the n+1 basis function of degree p evaluated at u

        int m = knots.length;
        double[] b = new double[n];
        if (u==knots[0]) {
            b[0] = 1;
            return b;
        } else if (u==knots[m-1]) {
            b[n-1] = 1;
            return b;
        }

        //find index of knot span
        int k = 0; //index of knot span
        while(k < knots.length && knots[k+1]<=u) {
            k++;
        }
        b[k]=1;
        for (int d = 1; d <= p; d++) {
            b[k-d] = b[k-d+1]*(knots[k+1]-u)/(knots[k+1]-knots[k-d+1]);
            for (int i = k-d+1; i < k; i++) {
                b[i] = b[i]*(u-knots[i])/(knots[i+d]-knots[i])+b[i+1]*(knots[i+d+1]-u)/(knots[i+d+1]-knots[i+1]);
            }
            b[k] = b[k]*(u-knots[k])/(knots[k+d]-knots[k]);
        }
        return b;
    }


    public static BSpline interpolate(Vector3f[] dataPoints, int p) {
        int n = dataPoints.length;
        double[] t = findParameters(ParameterMethod.CHORD_LENGTH,dataPoints, 0, 1);
        double[] knots = generateKnots(t, p);
        //Calculate B-Spline coefficients (basis functions)
        double[][] N = new double[n][n]; //matrix of b spline coefficients
        double[][] D = new double[n][3]; //matrix of data points (3 is because we work in 3d space)
        double[][] P = new double[n][3]; //matrix of control points (solution)
        for (int i = 0; i < n; i++) {
            N[i] = getSplineBasis(n,p,t[i], knots);
            D[i] = dataPoints[i].getArray();
        }
        //get control points
        P = MatrixUtilities.solve(N, D);
        Vector3f[] controlPoints = new Vector3f[n];
        for (int i = 0; i < n; i++) {
            controlPoints[i] = new Vector3f(P[i]);
        }
        //build B-Spline
        BSpline interpolant = new BSpline(controlPoints, knots, p);
        interpolant.t = t;
        return interpolant;
    }





}
