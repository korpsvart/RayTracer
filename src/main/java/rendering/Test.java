package rendering;
public class Test {


    public static void main(String[] args) {
//        int l = 4;
//        double z = -5;
//        Vector3f[] cP = {
//                new Vector3f(0,0,z),
//                new Vector3f(0.3,0.3,z),
//                new Vector3f(0.5,0.2,z),
//                new Vector3f(0.7,-1,z),
//                new Vector3f(1, -0.2, z),
//                new Vector3f(1.2, -1, z),
//                new Vector3f(1.4, 0, z),
//                new Vector3f(1.6, -1.2, z),
//                new Vector3f(1.7, -0.5, z),
//                new Vector3f(1.8, -1.2, z)
//        };
//        double[] nodes = new double[5];
//        for (int i = 0; i < 5; i++) {
//            nodes[i] = (double)i/4;
//        }
//        BezierSpline3 bezierSpline3 = BezierSpline3.CreateC1Spline(l, nodes, cP);
//        Vector3f result = bezierSpline3.evaluate((double)1/4);
//        System.out.println("aaaa");

//        Vector3f dataPoints[] = new Vector3f[]{
//                new Vector3f(0, 0, 0),
//                new Vector3f(2, 2, 0),
//                new Vector3f(3, 1, 0),
//                new Vector3f(4, 1.5, 0)
//        };
//        Vector3f tangents[] = new Vector3f[] {
//                new Vector3f(1, 1, 0),
//                new Vector3f(2, -1, 0),
//                new Vector3f(2, 1, 0),
//                new Vector3f(3, -1, 0)
//        };
//        BezierSpline3 spline = BezierSpline3.piecewiseCubicInterpolation(dataPoints, tangents);
//        Vector3f derivativeTest = spline.getCurves()[0].derivative(0);
//        //check C1 continuity
//        boolean c1Continuity = spline.getCurves()[0].checkC1(spline.getCurves()[1]);
//        System.out.println("aaaaa");

//        Vector3f[] dataPoints1 = new Vector3f[]{
//                new Vector3f(0, 0, -5),
//                new Vector3f(2, 0.5, -5),
//                new Vector3f(4, 0.3, -5)
//        };
//        Vector3f[] dataPoints2 = new Vector3f[] {
//                new Vector3f(0.3, 3, -6),
//                new Vector3f(2.2, 2.8, -6),
//                new Vector3f(4.2, 3.1, -6)
//        };
//        Vector3f[] tangent = new Vector3f[] {
//                new Vector3f(1, 1, 0),
//                new Vector3f(1, 0, 0),
//                new Vector3f(1, -1, 0)
//        };
//        Vector3f[] tangent2 = new Vector3f[] {
//                new Vector3f(1, 2, 0),
//                new Vector3f(1, -1, 0),
//                new Vector3f(1, -2, 0)
//        };
//
//        Vector3f[] tangent3 = new Vector3f[]{
//                new Vector3f(-0.2, 1, 0),
//                new Vector3f(0.3, 1, 0)
//        };
//
//        BezierSpline3 spline1 = BezierSpline3.piecewiseCubicInterpolation(dataPoints1, tangent);
//        BezierSpline3 spline2 = BezierSpline3.piecewiseCubicInterpolation(dataPoints2, tangent2);
//        BezierSpline3 spline3 = BezierSpline3.piecewiseCubicInterpolation(new Vector3f[]{dataPoints1[0], dataPoints2[0]}, tangent3);
//        BezierSpline3 spline4 = BezierSpline3.piecewiseCubicInterpolation(new Vector3f[]{dataPoints1[1], dataPoints2[1]}, tangent3);
//        BezierSpline3 spline5 = BezierSpline3.piecewiseCubicInterpolation(new Vector3f[]{dataPoints1[2], dataPoints2[2]}, tangent3);
//        BezierSurface33 surface1 = BezierSurface33.fillWithCoons(new BezierCurve3[]{
//                spline1.getCurves()[0],
//                spline2.getCurves()[0],
//                spline3.getCurves()[0],
//                spline4.getCurves()[0]
//        });
//        BezierSurface33 surface2 = BezierSurface33.fillWithCoons(new BezierCurve3[]{
//                spline1.getCurves()[1],
//                spline2.getCurves()[1],
//                spline4.getCurves()[0],
//                spline5.getCurves()[0]
//        });


//        //testing spline evaluation
//        //correct
//        double knots[] = new double[]{
//                0, 2, 4, 6
//        };
//        Vector3f controlPoints[] = new Vector3f[]{
//                new Vector3f(0, 0, 0),
//                new Vector3f(8, 8, 0),
//                new Vector3f(8, 0, 0)
//        };
//        BSpline bspline = new BSpline(controlPoints, knots, 2);
//        bspline.setClamped(false);
//        System.out.println(bspline.evaluate(3));
//
//        //testing knot insertion
//        //using the spline above, we try to insert a new node at u=3
//        //correct
//        BSpline bspline2 = bspline.knotInsertion(3);
//
//        //testing extracting bezier from the above bspline
//        //there's only one bezier curve contained
//        //and its the one that covers [2,4]
//        //i.e. i=1 is the number of the interval
//        //correct
//        BezierCurve extracted = bspline.extractBezier(1);
//        System.out.println(extracted);
//
//
//        //testing clamped quadratic bspline
//        //with i=2 it should give the same result as the above bspline
//        //i=1 and i=3 should be valid ranges too
//        //Correct!
//        controlPoints = new Vector3f[]{
//                new Vector3f(-3, -2, 0),
//                new Vector3f(0, 0, 0),
//                new Vector3f(8, 8, 0),
//                new Vector3f(8, 0, 0),
//                new Vector3f(10, -2, 0)
//        };
//        knots = new double[] {
//                0, 0, 0, 2, 4, 6, 6, 6
//        };
//        BSpline clampedQuadratic = new BSpline(controlPoints, knots, 2);
//        clampedQuadratic.setClamped(true);
//        extracted = clampedQuadratic.extractBezier(1);
//        System.out.println("aaaa");
//
//
//        //another test for clamped bsplines
//        //in this case we use a bspline of degree 3
//        //with 5 control points
//        //this should contain two bezier curves, for i=0,1
//        //We could make this a lot more efficient by noting that the
//        //same knot insertion routine already gives us the control points for both bezier curves
//        //in this case. Also, we could improve performance even more by writing a specific more efficient
//        //routine which handles knot insertion for already existing multiple knots (which don't requires
//        //any control point replacement, but a simple interpolation!)
//        BSpline cubicBSpline;
//        controlPoints = new Vector3f[]{
//                new Vector3f(0, 0, 0),
//                new Vector3f(4, 0, 0),
//                new Vector3f(5, -1, 0),
//                new Vector3f(3, -2, 0),
//                new Vector3f(-1, -2, 0)
//        };
//        knots = new double[]{
//                0,0,0,0,2,4,4,4,4
//        };
//        cubicBSpline = new BSpline(controlPoints, knots, 3);
//        cubicBSpline.setClamped(true);
//        BezierCurve b1 = cubicBSpline.extractBezier(0);
//        BezierCurve b2 = cubicBSpline.extractBezier(1);
//        Vector3f evaluation = cubicBSpline.evaluateClamped(3);
//        System.out.println("aaaa");
//        //testing the new multiple knot insertion routine
//
//
//
//        //B-spline surface test
//        int p = 3;
//        int q = 3;
//        Vector3f[][] controlPointsSurface = {
//                {
//                    new Vector3f(-1, -1, -4),
//                        new Vector3f(-0.9, -0.8, -4.2),
//                        new Vector3f(-0.8, -0.6, -4.2),
//                        new Vector3f(-0.8, -0.2, -4.3),
//                        new Vector3f(-0.7, 0, -4.2)
//                },
//                {
//                        new Vector3f(-0.5, -1.2, -4.5),
//                        new Vector3f(-0.4, -0.9, -4.6),
//                        new Vector3f(-0.43, -0.7, -4.6),
//                        new Vector3f(-0.38, -0.3, -4.5),
//                        new Vector3f(-0.34, -0.1, -4.7)
//                },
//                {
//                        new Vector3f(-0.2, -1, -4.1),
//                        new Vector3f(-0.25, -0.8, -4.9),
//                        new Vector3f(-0.27, -0.5, -4.7),
//                        new Vector3f(-0.21, -0.4, -4.9),
//                        new Vector3f(-0.18, -0.2, -5)
//                },
//                {
//                        new Vector3f(0, -1, -4.5),
//                        new Vector3f(0.1, -0.7, -4.6),
//                        new Vector3f(-0.1, -0.5, -4.2),
//                        new Vector3f(0, -0.2, -4.1),
//                        new Vector3f(0.1, 0, -4.3)
//                },
//                {
//                        new Vector3f(0.3, -1.3, -4.2),
//                        new Vector3f(0.4, -1, -4.8),
//                        new Vector3f(0.4, -0.8, -4.3),
//                        new Vector3f(0.6, -0.5, -4.0),
//                        new Vector3f(0.5, -0.2, -4.1)
//                },
//        };
//        double[] knotsU = {0, 0, 0, 0, 0.5, 1, 1, 1, 1};
//        double[] knotsV = knotsU.clone();
//        Matrix4D objectToWorld = new Matrix4D(Matrix3D.identity, new Vector3f(0, 1, 0));
//        BSurface bSurface = new BSurface(controlPointsSurface, knotsU, knotsV, p, q, objectToWorld);
//        Vector3f evaluationSurface = bSurface.evaluate(0, 0);
//        System.out.println("aaaaaaa");
//
//        //derivative test
//        Vector3f der = cubicBSpline.derivative(0);
//        System.out.println("aaaa");
//
//        //U-derivative for surface test
//        Vector3f derU = bSurface.derivativeV(0, 0);
//        Vector3f derV = bSurface.derivativeU(0, 0);
//        System.out.println("aaaa");

        //testing equation solver using LU decomposition
        double[][] a = new double[][]{
                {3, 2, 1},
                {1, 2, 3},
                {2, 1, 3}
        };
        double[][] b = new double[][]{
                {3, 6},
                {4, 1},
                {5, 2}
        };
        double[][] result = MatrixUtilities.solve(a, b);


        //test arc length parameters generation
        Vector3f dataPoints[] = new Vector3f[]{
                new Vector3f(0, 0, 0),
                new Vector3f(1, 2, 0),
                new Vector3f(3, 4, 0),
                new Vector3f(4, 0, 0)
        };
        double[] t = BSpline.findParameters(BSpline.ParameterMethod.CHORD_LENGTH, dataPoints, 0, 1);
        System.out.println("aaa");

        //testing knot vector generation
        double[] t2 = new double[]{
                0, 1 / 4f, 1 / 3f, 2 / 3f, 3 / 4f, 1
        };
        double[] knots = BSpline.generateKnots(t2, 3);
        System.out.println("aaaa");


        //testing b-spline coefficients computation


        //test B-SPline interpolation
        Vector3f[] dp = new Vector3f[]{
                new Vector3f(0, 0, -5),
                new Vector3f(0.8, 0.3, -5.2),
                new Vector3f(1.2, 0.6, -5.4),
                new Vector3f(1.5, 0.2, -5)
        };
        BSpline interpolant = BSpline.interpolate(dp, 2);
        double[] params = interpolant.getT();
        //verify the spline interpolates the points
        //by evaluating it at the corresponding parameters
        for (int i = 0; i < dp.length; i++) {
            Vector3f value = interpolant.evaluate(params[i]);
            System.out.println(dp[i].equals(value));
        }
        System.out.println("aaaa");


        //testing B-spline interpolating surface
        Vector3f[][] dataPointsSurface = {
                {
                        new Vector3f(-1, -1, -4),
                        new Vector3f(-0.6, -0.8, -4.2),
                        new Vector3f(-0.4, -0.9, -4.2),
                        new Vector3f(-0.3, -0.8, -4.3),
                },
                {
                        new Vector3f(-1, -0.7, -4.5),
                        new Vector3f(-0.6, -0.6, -4.6),
                        new Vector3f(-0.4, -0.7, -4.6),
                        new Vector3f(-0.2, -0.6, -4.5),
                },
                {
                        new Vector3f(-1.2, -0.4, -4.1),
                        new Vector3f(-0.8, -0.5, -4.9),
                        new Vector3f(-0.7, -0.4, -4.7),
                        new Vector3f(-0.5, -0.5, -4.9),
                },
                {
                        new Vector3f(-0.9, -0.2, -4.5),
                        new Vector3f(-0.7, -0.1, -4.6),
                        new Vector3f(-0.6, -0.2, -4.2),
                        new Vector3f(-0.4, -0, -4.1),
                }
        };
        BSurface interpolantSurface = BSurface.interpolate(MatrixUtilities.transpose2(dataPointsSurface), 2, 2, Matrix4D.identity);
        System.out.println("aaaeoeo");


        //test camera rotation
        //seems to be working, ignoring some floating point error
        Camera camera = new Camera();
        camera.rotateX(90);
        System.out.println("aaaa");
        //test translation after rotation
        //according to local coordinate system!
        //ok, its working
        camera.translate(new Vector3f(0, 0, 4));
        System.out.println("eeeoo");

    }
}
