public class BezierSurface33 {

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



    public TriangleMesh triangulate(int divs) {
        double du;
        double dv = du = (float)1 / divs; //grid resolution

        int numVert = (divs+1) * (divs+1);
        Vector3f[] vertex = new Vector3f[numVert];
        int[] vertexIndex = new int[divs*divs*4];
        int[] faceIndex = new int[divs*divs]; //its just gonna be full of 4, since its all quads

        double u,v;
        u = v = 0;


        //store all vertices for polygon mesh
        for (int i = 0, k = 0; i <= divs; i++) {
            u=0;
            for (int j = 0; j <= divs; j++, k++) {
                vertex[k] = this.evaluate(u, v);
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
        return new TriangleMesh(faceIndex, vertexIndex, vertex, boundingBox);

    }




}
