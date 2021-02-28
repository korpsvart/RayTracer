public class BezierSurface33 extends GeometricObject {

    //Bezier Surface of degree (3,3)


    private final Vector3f controlPoints[][];

    public BezierSurface33(Vector3f controlPoints[][]) {
        //we should check the matrix has the right size
        this.controlPoints = controlPoints.clone();
    }


    public Vector3f evaluate(double u, double v) {
        //This version is based on the fact
        //that for a fixed v value we get a
        //degree 3 bezier curve, which we can
        //then evaluate with u
        //The bezier curves are evaluated using DeCasteljau algorithm
        Vector3f curveControlPoints[] = new Vector3f[4];
        for (int i = 0; i < 4; i++) {
            //We evaluate each of this curve at v, getting 4 control points
            //This defines the Bezier Curve we can evaluate at u
            //to get the point on the surface P(u, v)
            BezierCurve3 c = new BezierCurve3(this.controlPoints[i]);
            curveControlPoints[i] = c.evaluate(v);
        }
        BezierCurve3 p = new BezierCurve3(curveControlPoints);
        return p.evaluate(u);
    }


    @Override
    public TriangleMesh triangulate(int divs) {
        double du;
        double dv = du = (float)1 / divs; //grid resolution

        int numVert = (divs+1) * (divs+1);
        Vector3f[] vertex = new Vector3f[numVert];
        Vector3f[] vertexNormal = new Vector3f[numVert];
        int[] vertexIndex = new int[divs*divs*4];
        int[] faceIndex = new int[divs*divs]; //its just gonna be full of 4, since its all quads

        double u,v;
        u = v = 0;


        //store all vertices for polygon mesh
        for (int i = 0, k = 0; i <= divs; i++) {
            u=0;
            for (int j = 0; j <= divs; j++, k++) {
                Vector3f[] x = this.evaluateAndDerivative(u, v);
                Vector3f normal = x[1].crossProduct(x[2]).normalize();
                vertex[k] = x[0];
                vertexNormal[k] = normal;
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
                vertexIndex[k*4+2]=(divs+1)*(i+1)+j+1;
                vertexIndex[k*4+3]=(i+1)*(divs+1)+j;
            }
        }


        Vector3f cPoints[] = new Vector3f[16];
        for (int i = 0; i < controlPoints.length; i++) {
            for (int j = 0; j < controlPoints[0].length; j++) {
                cPoints[i*4+j]=controlPoints[i][j];
            }
        }
        BoundingBox boundingBox = new BoundingBox(cPoints);
        return new TriangleMesh(faceIndex, vertexIndex, vertex, vertexNormal, boundingBox);

    }


    public Vector3f derivativeU(double u, double v) {
        //this version, like the evaluate method,
        //is based on the fact that for a fixed v value
        //we get a degree 3 bezier curve, depending only on u
        //we can then take the derivative by taking the derivative
        //of that curve
        BezierCurve3 bezierCurve3;
        Vector3f curveControlPoints[] = new Vector3f[4];
        for (int i = 0; i < 4; i++) {
            bezierCurve3 = new BezierCurve3(controlPoints[i]);
            curveControlPoints[i] = bezierCurve3.evaluate(v);
        }
        bezierCurve3 = new BezierCurve3(curveControlPoints);
        return bezierCurve3.derivative(u);
    }


    public Vector3f derivativeV(double u, double v) {
        //the idea is the same used for derivativeV
        //except that now we fix the u value
        //and take the derivative with respect to v

        BezierCurve3 bezierCurve3;
        Vector3f curveControlPoints[] = new Vector3f[4];
        for (int i = 0; i < 4; i++) {
            bezierCurve3 = new BezierCurve3(controlPoints[0][i], controlPoints[1][i], controlPoints[2][i], controlPoints[3][i]);
            curveControlPoints[i] = bezierCurve3.evaluate(u);
        }
        bezierCurve3 = new BezierCurve3(curveControlPoints);
        return bezierCurve3.derivative(v);
    }

    public Vector3f[] evaluateAndDerivative(double u, double v) {
        //Evaluate at (u,v) and take derivative
        //at (u,v).
        //Calling this should be a little more efficient than
        //calling first evaluate and then derivativeU and derivativeV
        //separately.
        //This is because using DeCasteljau algorithm for curves
        //we can evaluate both the curve and the tangent vector
        //at the same time, but for a Bezier Surface we can exploit this
        //for only one of the two derivatives

        Vector3f curveControlPoints[] = new Vector3f[4];
        for (int i = 0; i < 4; i++) {
            //We evaluate each of this curve at v, getting 4 control points
            //This defines the Bezier Curve we can evaluate at u
            //to get the point on the surface P(u, v)
            BezierCurve3 c = new BezierCurve3(this.controlPoints[i]);
            curveControlPoints[i] = c.evaluate(v);
        }
        BezierCurve3 p = new BezierCurve3(curveControlPoints);
        Vector3f[] x = p.evaluateAndDerivative(u);
        return new Vector3f[]{x[0], x[1], derivativeV(u, v)};



    }



}
