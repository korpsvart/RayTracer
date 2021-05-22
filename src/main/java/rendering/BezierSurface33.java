package rendering;

public class BezierSurface33 extends GeometricObject {

    //Bezier Surface of degree (3,3)

    //if true use decasteljau to take surface derivative
    //else use bernstein polynomials
    private static boolean DECASTELJAU_DERIVATIVE = true;


    private final Vector3d controlPoints[][];
    private Vector3d[][] controlPointsWorld;
//    private final Vector3f originalCP[][]; //before applying affine transformation

    public BezierSurface33(Vector3d controlPoints[][]) {
        //we should check the matrix has the right size
        this.controlPoints = controlPoints.clone();
    }


    public BezierSurface33(Vector3d controlPoints[], Matrix4D objectToWorld) {
        //in case the points are given in a single array of 16 points
        //(we assume the points are ordered going first left to right
        //and then bottom up
        //but in the internal representation the the first row is the first "column" of points
        this.controlPoints = new Vector3d[4][4];
        this.objectToWorld = objectToWorld;
        for (int i = 0; i < 4; i++) {
            this.controlPoints[0][i] = controlPoints[i*4];
            this.controlPoints[1][i] = controlPoints[i*4+1];
            this.controlPoints[2][i] = controlPoints[i*4+2];
            this.controlPoints[3][i] = controlPoints[i*4+3];
        }
    }

    public BezierSurface33 affineTransform(Matrix4D transformation) {
        //apply affine transformation to bezier surface
        //by applying it to the control points
        //This assumes we don't actually use the object to world matrix
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                controlPoints[i][j] = controlPoints[i][j].matrixAffineTransform(transformation);
            }
        }
        return this;
    }


    public BezierSurface33(Vector3d controlPoints[][], Matrix4D objectToWorld) {
        //in case the points are given in a single array of 16 points
        //(we assume the points are ordered going first left to right
        //and then bottom up
        //but in the internal representation the the first row is the first "column" of points
        this.controlPoints = new Vector3d[4][4];
        this.objectToWorld = objectToWorld;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                this.controlPoints[i][j] = controlPoints[i][j];
            }
        }
    }


    public Vector3d evaluate(double u, double v) {
        //This version is based on the fact
        //that for a fixed v value we get a
        //degree 3 bezier curve, which we can
        //then evaluate with u
        //The bezier curves are evaluated using DeCasteljau algorithm
        Vector3d curveControlPoints[] = new Vector3d[4];
        for (int i = 0; i < 4; i++) {
            //We evaluate each of this curve at v, getting 4 control points
            //This defines the Bezier Curve we can evaluate at u
            //to get the point on the surface P(u, v)
            BezierCurve3 c = new BezierCurve3(this.controlPointsWorld[i]);
            curveControlPoints[i] = c.evaluate(v);
        }
        BezierCurve3 p = new BezierCurve3(curveControlPoints);
        return p.evaluate(u);
    }


    @Override
    public TriangleMesh triangulate() {

        controlPointsWorld = new Vector3d[controlPoints.length][controlPoints[0].length];

        for (int i = 0; i < controlPoints.length; i++) {
            for (int j = 0; j < controlPoints[0].length; j++) {
                controlPointsWorld[i][j] = controlPoints[i][j].matrixAffineTransform(objectToWorld);
            }
        }
        double du;
        double dv = du = (float)1 / divs; //grid resolution

        int numVert = (divs+1) * (divs+1);
        Vector3d[] vertex = new Vector3d[numVert];
        Vector3d[] vertexNormal = new Vector3d[numVert];
        int[] vertexIndex = new int[divs*divs*4];
        int[] faceIndex = new int[divs*divs]; //its just gonna be full of 4, since its all quads

        double u,v;
        u = 0; v = 0;


        //store all vertices for polygon mesh
        if (DECASTELJAU_DERIVATIVE) {
            for (int i = 0, k = 0; i <= divs; i++) {
                u=0;
                for (int j = 0; j <= divs; j++, k++) {
                    Vector3d[] x = this.evaluateAndDerivative(u, v);
                    Vector3d normal = x[1].crossProduct(x[2]).normalize();
                    vertex[k] = x[0];
                    vertexNormal[k] = normal;
                    u+=du;
                }
                v+=dv;
            }
        } else {
            for (int i = 0, k = 0; i <= divs; i++) {
                u=0;
                for (int j = 0; j <= divs; j++, k++) {
                    Vector3d x = this.evaluate(u, v);
                    Vector3d derivativeU = this.derivativeU_v2(u, v);
                    Vector3d derivativeV = this.derivativeV_v2(u, v);
                    Vector3d normal = derivativeU.crossProduct(derivativeV).normalize();
                    vertex[k] = x;
                    vertexNormal[k] = normal;
                    u+=du;
                }
                v+=dv;
            }
        }

        //create connectivity information in CCW order
        for (int i = 0, k = 0; i < divs; i++) {
            for (int j = 0; j < divs; j++, k++) {
                faceIndex[k] = 4;
                vertexIndex[k*4]=(divs+1)*i+j;
                vertexIndex[k*4+1]=(divs+1)*i+j+1;
                vertexIndex[k*4+2]=(divs+1)*(i+1)+j;
                vertexIndex[k*4+3]=(i+1)*(divs+1)+j+1;
            }
        }


        Vector3d cPoints[] = new Vector3d[16];
        for (int i = 0; i < controlPoints.length; i++) {
            for (int j = 0; j < controlPoints[0].length; j++) {
                cPoints[i*4+j]=controlPointsWorld[i][j];
            }
        }
        BoundingVolume boundingVolume = new BoundingVolume(cPoints);
        return new TriangleMesh(faceIndex, vertexIndex, vertex, vertexNormal, boundingVolume);

    }


    public Vector3d derivativeU(double u, double v) {
        //this version, like the evaluate method,
        //is based on the fact that for a fixed v value
        //we get a degree 3 bezier curve, depending only on u
        //we can then take the derivative by taking the derivative
        //of that curve
        BezierCurve3 bezierCurve3;
        Vector3d curveControlPoints[] = new Vector3d[4];
        for (int i = 0; i < 4; i++) {
            bezierCurve3 = new BezierCurve3(controlPointsWorld[i]);
            curveControlPoints[i] = bezierCurve3.evaluate(v);
        }
        bezierCurve3 = new BezierCurve3(curveControlPoints);
        return bezierCurve3.derivative(u);
    }

    public Vector3d derivativeU_v2(double u, double v) {
        //same as above, but we evaluate the curve derivative using bernstein polynomials
        BezierCurve3 bezierCurve3;
        Vector3d curveControlPoints[] = new Vector3d[4];
        for (int i = 0; i < 4; i++) {
            bezierCurve3 = new BezierCurve3(controlPointsWorld[i]);
            curveControlPoints[i] = bezierCurve3.evaluate(v);
        }
        bezierCurve3 = new BezierCurve3(curveControlPoints);
        return bezierCurve3.derivative_v2(u);
    }




    public Vector3d derivativeV(double u, double v) {
        //the idea is the same used for derivativeV
        //except that now we fix the u value
        //and take the derivative with respect to v

        BezierCurve3 bezierCurve3;
        Vector3d curveControlPoints[] = new Vector3d[4];
        for (int i = 0; i < 4; i++) {
            bezierCurve3 = new BezierCurve3(controlPointsWorld[0][i], controlPointsWorld[1][i], controlPointsWorld[2][i], controlPointsWorld[3][i]);
            curveControlPoints[i] = bezierCurve3.evaluate(u);
        }
        bezierCurve3 = new BezierCurve3(curveControlPoints);
        return bezierCurve3.derivative(v);
    }

    public Vector3d derivativeV_v2(double u, double v) {
        //same as above, but we take the curve derivative using bernstein polynomials
        BezierCurve3 bezierCurve3;
        Vector3d curveControlPoints[] = new Vector3d[4];
        for (int i = 0; i < 4; i++) {
            bezierCurve3 = new BezierCurve3(controlPointsWorld[0][i], controlPointsWorld[1][i], controlPointsWorld[2][i], controlPointsWorld[3][i]);
            curveControlPoints[i] = bezierCurve3.evaluate(u);
        }
        bezierCurve3 = new BezierCurve3(curveControlPoints);
        return bezierCurve3.derivative_v2(v);
    }

    public Vector3d[] evaluateAndDerivative(double u, double v) {
        //Evaluate at (u,v) and take derivative
        //at (u,v).
        //Calling this should be a little more efficient than
        //calling first evaluate and then derivativeU and derivativeV
        //separately.
        //This is because using DeCasteljau algorithm for curves
        //we can evaluate both the curve and the tangent vector
        //at the same time, but for a Bezier Surface we can exploit this
        //for only one of the two derivatives

        Vector3d curveControlPoints[] = new Vector3d[4];
        for (int i = 0; i < 4; i++) {
            //We evaluate each of this curve at v, getting 4 control points
            //This defines the Bezier Curve we can evaluate at u
            //to get the point on the surface P(u, v)
            BezierCurve3 c = new BezierCurve3(this.controlPointsWorld[i]);
            curveControlPoints[i] = c.evaluate(v);
        }
        BezierCurve3 p = new BezierCurve3(curveControlPoints);
        Vector3d[] x = p.evaluateAndDerivative(u);
        return new Vector3d[]{x[0], x[1], derivativeV(u, v)};



    }

    public static BezierSurface33 fillWithCoons(BezierCurve3[] bezierCurve3) {
        //this version accepts directly 4 degree 3 bezier curves (boundary curves)
        //its assumed that bezierCurve3[0] contains lower u curve
        //[1] contains upper u curve
        //[2] contains left v curve
        //[3] contains right v curve
        //The function returns a complete patch by filling the missing internal points
        //using coons method (Coons patches)
        Vector3d[][] controlPoints = new Vector3d[4][4];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                controlPoints[j][i*3] = bezierCurve3[i].getControlPoints()[j];
            }
            controlPoints[i*3] = bezierCurve3[i+2].getControlPoints();
        }
        return fillWithCoons(controlPoints);
    }

    public static BezierSurface33 fillWithCoons(Vector3d[][] cP) {
        //same as above but accent controlPoints matrix with missing values
        //clone the matrix in case the original matrix already contains the missing values
        //and we are just experimenting
        Vector3d[][] controlPoints = cP.clone();
        Vector3d ruledU, ruledV, bilinear;
        for (int i = 1; i < 3; i++) {
            for (int j = 1; j < 3; j++) {
                ruledU = getRuledSurfaceValueU(controlPoints, i, j);
                ruledV = getRuledSurfaceValueU(controlPoints, i, j);
                bilinear = getBilinearInterpolant(controlPoints, j, i); //check this for errors
                controlPoints[i][j] = ruledU.add(ruledV).add(bilinear.mul(-1));
            }
        }
        return new BezierSurface33(controlPoints);
    }

    private static Vector3d getBilinearInterpolant(Vector3d[][] controlPoints, int i, int j) {
        Vector3d c1 = controlPoints[0][0].mix(controlPoints[3][0],(double)j/3);
        Vector3d c2 = controlPoints[0][3].mix(controlPoints[3][3],(double)j/3);
        return c1.mix(c2, (double)i/3);
    }

    private static Vector3d getRuledSurfaceValueU(Vector3d[][] controlPoints, int i, int j) {
        return controlPoints[0][j].mix(controlPoints[3][j],(double)i/3);
    }

    private static Vector3d getRuledSurfaceValueV(Vector3d[][] controlPoints, int i, int j) {
        return controlPoints[i][0].mix(controlPoints[i][3],(double)j/3);
    }


    public boolean checkC1(BezierSurface33 b) {
        boolean c1 = true;
        BezierCurve3 curve1, curve2;
        for (int i = 0; i < 4; i++) {
            curve1 = new BezierCurve3(this.controlPoints[0][i], this.controlPoints[1][i], this.controlPoints[2][i], this.controlPoints[3][i]);
            curve2 = new BezierCurve3(b.controlPoints[0][i], b.controlPoints[1][i], b.controlPoints[2][i], b.controlPoints[3][i]);
            c1 = c1 && curve1.checkC1(curve2);
        }
        return c1;
    }

    public boolean checkG1(BezierSurface33 b) {
        boolean g1 = true;
        BezierCurve3 curve1, curve2;
        for (int i = 0; i < 4; i++) {
            curve1 = new BezierCurve3(this.controlPoints[0][i], this.controlPoints[1][i], this.controlPoints[2][i], this.controlPoints[3][i]);
            curve2 = new BezierCurve3(b.controlPoints[0][i], b.controlPoints[1][i], b.controlPoints[2][i], b.controlPoints[3][i]);
            g1 = g1 && curve1.checkG1(curve2);
        }
        return g1;
    }


    public Vector3d[][] getControlPoints() {
        return controlPoints;
    }


    @Override
    public String toString() {
        return "Bézier surface; " + "Translation: " + getTranslationData() + "; " + "Rotation: " + getRotationData();
    }
}
