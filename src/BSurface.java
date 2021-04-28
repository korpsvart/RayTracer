public class BSurface extends GeometricObject {

    //class for Bspline surfaces
    //Only works for clamped surfaces, for simplicity

    private Vector3f[][] controlPoints;
    private Vector3f[][] transposed; //precomputed for performance
    private double[] knotsU;
    private double[] knotsV;
    private int p, q;

    public BSurface(Vector3f[][] controlPoints, double[] knotsU, double[] knotsV, int p, int q, Matrix4D objectToWorld) {
        //check that number of control points in each direction >= (degree+1)
        //Note that if we have equality, then the bspline becomes a bezier curve
        //If equality is satisfied in both directions, then we have a bezier surface
        //check also that fundamental identities are satisfied

        if (controlPoints.length <= p  || controlPoints[0].length <= q) {
            throw new IllegalArgumentException("number of control points should be at least equal to degree+1");
        }
        else if (knotsU.length != (controlPoints.length + p +1) || knotsV.length != (controlPoints[0].length + q +1)) {
            throw new IllegalArgumentException("fundamental identities between knots number, control points number and degree is not satisfied in one or both directions");
        }
        this.objectToWorld = objectToWorld;
        this.controlPoints = new Vector3f[controlPoints.length][controlPoints[0].length];
        if (objectToWorld != null) {
            for (int i = 0; i < controlPoints.length; i++) {
                for (int j = 0; j < controlPoints[i].length; j++) {
                    this.controlPoints[i][j] = controlPoints[i][j].matrixAffineTransform(objectToWorld);
                }
            }
        }
        this.transposed = new Vector3f[controlPoints[0].length][controlPoints.length];
        for (int i = 0; i < controlPoints.length; i++) {
            for (int j = 0; j < controlPoints[0].length; j++) {
                this.transposed[j][i] = this.controlPoints[i][j];
            }
        }
        this.knotsU = knotsU;
        this.knotsV = knotsV;
        this.p = p;
        this.q = q;
    }

    public Vector3f evaluate(double u, double v) {
        //evaluate a B-Spline surface
        //based on B-Spline curve evaluation (akin to the idea used for Bezier surfaces)
        //Improve performance by
        //1) only considering necessary points in the v direction (this doesn't really improve performance
        //as it's done anyway automatically by de Boor's algorithm)
        //2) only considering necessary points in the u direction: this improves performance because
        //it allows us to skip possibly a lot of B-Spline evaluation.

        int c = 0; //index of knot span for u
        int s = 0; //multiplicity for u
        while(c < knotsU.length && knotsU[c]<=u) {
            if (knotsU[c]==u) s++;
            c++;
        }
        c=c-1;

        int d = 0; //index of knot span for v
        int t = 0; //multiplicity for v
        while(d < knotsV.length && knotsV[d]<=v) {
            if (knotsV[d]==v) t++;
            d++;
        }
        d=d-1;
        Vector3f[] auxU;  //points that will be used for evaluation in u direction
        if (p < s) {
            //knot multiplicity is higher than degree
            //This means we only care about one value of i
            auxU = new Vector3f[1];
            int i = u == knotsU[0] ? 0 : c-s;
            if (q < t) {
                auxU[0] = v == knotsV[0] ? controlPoints[i][0] : controlPoints[i][d - t];
            } else {
                Vector3f[] auxV = new Vector3f[q-t+1]; //points for v direction
                for (int j = d-q, l=0; j <= d-t; j++, l++) {
                    auxV[l] = controlPoints[i][j];
                }
                auxU[0] = BSpline.deBoor(auxV, knotsV, v, t, q, d);
            }
        } else {
            auxU = new Vector3f[p-s+1];
            for (int i = c-p, f=0; i <= c-s; i++, f++) {
                if (q < t) {
                    auxU[f] = v == knotsV[0] ? controlPoints[i][0] : controlPoints[i][d - t];
                } else {
                    Vector3f[] auxV = new Vector3f[q-t+1]; //points for v direction
                    for (int j = d-q, l=0; j <= d-t; j++, l++) {
                        auxV[l] = controlPoints[i][j];
                    }
                    auxU[f] = BSpline.deBoor(auxV, knotsV, v, t, q, d);
                }
            }
        }
        return BSpline.deBoor(auxU, knotsU, u, s, p, c);

    }


    public TriangleMesh triangulate(int divs) {

        double du = (knotsU[knotsU.length-1]-knotsU[0])/divs;
        double dv = (knotsV[knotsV.length-1]-knotsV[0])/divs;

        int numVert = (divs+1) * (divs+1);
        Vector3f[] vertex = new Vector3f[numVert];
        Vector3f[] vertexNormal = new Vector3f[numVert];
        int[] vertexIndex = new int[divs*divs*4];
        int[] faceIndex = new int[divs*divs]; //its just gonna be full of 4, since its all quads

        double u,v;
        v = knotsV[0];


        //store all vertices for polygon mesh

        for (int i = 0, k = 0; i <= divs; i++) {
            u=knotsU[0];
            for (int j = 0; j <= divs; j++, k++) {
                vertex[k] = this.evaluate(u, v);
                Vector3f derU = this.derivativeU(u, v);
                Vector3f derV = this.derivativeV(u, v);
                vertexNormal[k] = derU.crossProduct(derV).normalize();
                u+=du;
            }
            v+=dv;
        }

        //create connectivity information in CCW order
        //I'll probably fuck this up in so many ways
        for (int i = 0, k = 0; i < divs; i++) {
            for (int j = 0; j < divs; j++, k++) {
                faceIndex[k] = 4;
                vertexIndex[k*4]=(divs+1)*i+j;
                vertexIndex[k*4+1]=(divs+1)*i+j+1;
                vertexIndex[k*4+2]=(divs+1)*(i+1)+j;
                vertexIndex[k*4+3]=(i+1)*(divs+1)+j+1;
            }
        }

        return new TriangleMesh(faceIndex, vertexIndex, vertex, vertexNormal);

    }

    public Vector3f derivativeV(double u, double v) {
        //calculate derivative with respect to V

        //The same locality principles apply as we saw for evaluation
        //But we consider it only in the u direction here (cause otherwise it would get too complicated
        //and I think the derivative function should take care of everything anyway)
        int c = 0; //index of knot span for u
        int s = 0; //multiplicity for u
        while(c < knotsU.length && knotsU[c]<=u) {
            if (knotsU[c]==u) s++;
            c++;
        }
        c=c-1;
        Vector3f[] auxU;  //points that will be used for evaluation in u direction
        if (p < s) {
            //knot multiplicity is higher than degree
            //This means we only care about one value of i
            auxU = new Vector3f[1];
            int i = u == knotsU[0] ? 0 : c-s;
            BSpline bspline = new BSpline(controlPoints[i], knotsV, q);
            auxU[0] = bspline.derivative(v);
        } else {
            auxU = new Vector3f[p-s+1];
            for (int i = c-p, f=0; i <= c-s; i++, f++) {
                BSpline bspline = new BSpline(controlPoints[i], knotsV, q);
                auxU[f] = bspline.derivative(v);
            }
        }
        return BSpline.deBoor(auxU, knotsU, u, s, p, c);
    }

    public Vector3f derivativeU(double u, double v) {


        int c = 0; //index of knot span for v
        int s = 0; //multiplicity for v
        while(c < knotsV.length && knotsV[c]<=v) {
            if (knotsV[c]==v) s++;
            c++;
        }
        c=c-1;
        Vector3f[] auxV;  //points that will be used for evaluation in u direction
        if (q < s) {
            //knot multiplicity is higher than degree
            //This means we only care about one value of i
            auxV = new Vector3f[1];
            int i = v == knotsV[0] ? 0 : c-s;
            BSpline bspline = new BSpline(transposed[i], knotsU, p);
            auxV[0] = bspline.derivative(u);
        } else {
            auxV = new Vector3f[q-s+1];
            for (int i = c-q, f=0; i <= c-s; i++, f++) {
                BSpline bspline = new BSpline(transposed[i], knotsU, p);
                auxV[f] = bspline.derivative(u);
            }
        }
        return BSpline.deBoor(auxV, knotsV, v, s, q, c);
    }


    public static BSurface interpolate(Vector3f[][] dataPoints, int p, int q, Matrix4D objectToWorld) {
        //find parameters and knot in u direction
        int m = dataPoints.length;
        int n = dataPoints[0].length;
        Vector3f[][] dPTransposed = MatrixUtilities.transpose2(dataPoints);
        double[] s = BSpline.findParameters(dPTransposed, 0, 1);
        double[] u = BSpline.generateKnots(s, p);
        //find parameters in the v direction
        double[] t = BSpline.findParameters(dataPoints, 0, 1);
        double[] v = BSpline.generateKnots(t, q);
        //LUP-decompose first N matrix (to find intermediate control points)
        //First build the matrix
        double[][] N = new double[m][m];
        for (int i = 0; i < m; i++) {
            N[i] = BSpline.getSplineBasis(m, p, s[i],u);
        }
        //decompose
        double[][][] lup = MatrixUtilities.lu(N);
        //calculate intermediate control points Q by spline curve interpolation
        //we can use original dataPoints matrix if we don't care about preserving it
        for (int i = 0; i < n; i++) {
            //setup datapoints column
            double[][] b = new double[m][3];
            for (int j = 0; j < m; j++) {
                b[j] = dataPoints[j][i].getArray();
            }
            //solve
            b = MatrixUtilities.backForwardSubstitution(lup[0], lup[1],lup[2],b);
            //substitute in original dataPoints vector
            for (int j = 0; j < m; j++) {
                dataPoints[j][i] = new Vector3f(b[j]);
            }
        }

        //do something similar but now interpolate by row
        //to get final control ponts

        //build new N matrix
        N = new double[n][n];
        for (int i = 0; i < n; i++) {
            N[i] = BSpline.getSplineBasis(n, q, t[i],v);
        }
        //decompose
        lup = MatrixUtilities.lu(N);
        for (int i = 0; i < n; i++) {
            double[][] b = new double[n][3];
            for (int j = 0; j < n; j++) {
                b[j] = dataPoints[i][j].getArray();
            }
            //solve
            b = MatrixUtilities.backForwardSubstitution(lup[0], lup[1],lup[2],b);
            //substitute in original
            for (int j = 0; j < n; j++) {
                dataPoints[i][j] = new Vector3f(b[j]);
            }
        }

        //build surface
        return new BSurface(dataPoints, u, v, p, q, objectToWorld);

    }

    public BSurface knotInsertionU(double t) {
        int m = controlPoints.length;
        int n = controlPoints[0].length;
        //use the transposed version for easier access
        Vector3f[][] newCP = new Vector3f[n][m+1];
        double[] newKnots = new double[knotsU.length+1];
        BSpline bSpline;
        for (int i = 0; i < n; i++) {
            bSpline = new BSpline(transposed[i],knotsU, p);
            bSpline = bSpline.knotInsertionClamped(t);
            if (i==0) newKnots = bSpline.getKnots(); //checking i==0 just cause it's always the same knot vector
            newCP[i] = bSpline.getControlPoints();
        }

        return new BSurface(MatrixUtilities.transpose2(newCP), newKnots, knotsV, p, q , objectToWorld);
    }


    public BSurface knotInsertionV(double t) {
        int m = controlPoints.length;
        int n = controlPoints[0].length;
        Vector3f[][] newCP = new Vector3f[m][n+1];
        double[] newKnots = new double[knotsV.length+1];
        BSpline bSpline;
        for (int i = 0; i < m; i++) {
            bSpline = new BSpline(controlPoints[i],knotsV, q);
            bSpline = bSpline.knotInsertionClamped(t);
            if (i==0) newKnots = bSpline.getKnots(); //checking i==0 just cause it's always the same knot vector
            newCP[i] = bSpline.getControlPoints();
        }

        return new BSurface(newCP, knotsU, newKnots, p, q , objectToWorld);
    }

    public double[] getKnotsU() {
        return knotsU;
    }

    public void setKnotsU(double[] knotsU) {
        for (int i = 0; i < knotsU.length; i++) {
            this.knotsU[i] = knotsU[i];
        }
    }


    public double[] getKnotsV() {
        return knotsV;
    }

    public void setKnotsV(double[] knotsV) {
        for (int i = 0; i < knotsV.length; i++) {
            this.knotsV[i] = knotsV[i];
        }
    }


    public int getP() {
        return p;
    }

    public int getQ() {
        return q;
    }

    public Vector3f[][] getControlPoints() {
        return controlPoints;
    }

    @Override
    public Matrix4D getObjectToWorld() {
        return super.getObjectToWorld();
    }

    public BSurface editU(int m, int p, double[] knots) {
        if (m < controlPoints.length) return reduceU(p, knots);
        else return extendU(p, knots);
    }

    public BSurface editV(int n, int q, double[] knots) {
        if (n < controlPoints[0].length) return reduceV(q, knots);
        else return extendV(q, knots);
    }


    public BSurface reduceU(int p, double[] knots) {
        //remove the last control point
        //allow specification of new degree and new knots in that direction, if changed
        Vector3f[][] cp = new Vector3f[controlPoints.length-1][controlPoints[0].length];
        for (int i = 0; i < cp.length; i++) {
            for (int j = 0; j < cp[0].length; j++) {
                cp[i][j] = controlPoints[i][j];
            }
        }
        BSurface reduced = new BSurface(cp, knots, knotsV, p, q, objectToWorld);
        return reduced;
    }

    public BSurface reduceV(int q, double[] knots) {
        //remove the last control point
        //allow specification of new degree and new knots in that direction, if changed
        Vector3f[][] cp = new Vector3f[controlPoints.length][controlPoints[0].length-1];
        for (int i = 0; i < cp.length; i++) {
            for (int j = 0; j < cp[0].length; j++) {
                cp[i][j] = controlPoints[i][j];
            }
        }
        BSurface reduced = new BSurface(cp, knotsU, knots, p, q, objectToWorld);
        return reduced;
    }

    public BSurface extendU(int p, double[] knots) {
        //add a new line of control points in u direction
        //default points are generated by slightly changing last polyline direction
        //allow specification of new degree and new knots in that direction, if changed
        Vector3f[][] cp = new Vector3f[controlPoints.length+1][controlPoints[0].length];
        for (int i = 0; i < controlPoints.length; i++) {
            for (int j = 0; j < controlPoints[0].length; j++) {
                cp[i][j] = controlPoints[i][j];
            }
        }
        for (int j = 0; j < cp[0].length; j++) {
            Vector3f direction = cp[cp.length-2][j].add(cp[cp.length-3][j].mul(-1)).normalize();
            cp[cp.length-1][j] = cp[cp.length-2][j].add(direction.mul(0.2));
        }
        BSurface reduced = new BSurface(cp, knots, knotsV, p, q, objectToWorld);
        return reduced;
    }

    public BSurface extendV(int q, double[] knots) {
        //add a new line of control points in u direction
        //default points are generated by slightly changing last polyline direction
        //allow specification of new degree and new knots in that direction, if changed
        Vector3f[][] cp = new Vector3f[controlPoints.length][controlPoints[0].length+1];
        for (int i = 0; i < controlPoints.length; i++) {
            for (int j = 0; j < controlPoints[0].length; j++) {
                cp[i][j] = controlPoints[i][j];
            }
        }
        int n = cp[0].length;
        for (int i = 0; i < cp.length; i++) {
            Vector3f direction = cp[i][n-2].add(cp[i][n-3].mul(-1)).normalize();
            cp[i][n-1] = cp[i][n-2].add(direction.mul(0.2));
        }
        BSurface reduced = new BSurface(cp, knotsU,knots, p, q, objectToWorld);
        return reduced;
    }

}
