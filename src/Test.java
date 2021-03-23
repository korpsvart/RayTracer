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


        //testing spline evaluation
        //correct
        double knots[] = new double[]{
                0, 2, 4, 6
        };
        Vector3f controlPoints[] = new Vector3f[]{
                new Vector3f(0, 0, 0),
                new Vector3f(8, 8, 0),
                new Vector3f(8, 0, 0)
        };
        BSpline bspline = new BSpline(controlPoints, knots, 2);
        System.out.println(bspline.evaluate(3));

        //testing knot insertion
        //using the spline above, we try to insert a new node at u=3
        //correct
        BSpline bspline2 = bspline.knotInsertion(3);

        //testing extracting bezier from the above bspline
        //there's only one bezier curve contained
        //and its the one that covers [2,4]
        //i.e. i=1 is the number of the interval
        //correct
        BezierCurve extracted = bspline.extractBezier(1);
        System.out.println(extracted);


        //testing clamped quadratic bspline
        //with i=2 it should give the same result as the above bspline
        //i=1 and i=3 should be valid ranges too
        //Correct!
        controlPoints = new Vector3f[]{
                new Vector3f(-3, -2, 0),
                new Vector3f(0, 0, 0),
                new Vector3f(8, 8, 0),
                new Vector3f(8, 0, 0),
                new Vector3f(10, -2, 0)
        };
        knots = new double[] {
                0, 0, 0, 2, 4, 6, 6, 6
        };
        BSpline clampedQuadratic = new BSpline(controlPoints, knots, 2);
        clampedQuadratic.setClamped(true);
        extracted = clampedQuadratic.extractBezier(1);
        System.out.println("aaaa");


        //another test for clamped bsplines
        //in this case we use a bspline of degree 3
        //with 5 control points
        //this should contain two bezier curves, for i=0,1
        //We could make this a lot more efficient by noting that the
        //same knot insertion routine already gives us the control points for both bezier curves
        //in this case. Also, we could improve performance even more by writing a specific more efficient
        //routine which handles knot insertion for already existing multiple knots (which don't requires
        //any control point replacement, but a simple interpolation!)
        BSpline cubicBSpline;
        controlPoints = new Vector3f[]{
                new Vector3f(0, 0, 0),
                new Vector3f(4, 0, 0),
                new Vector3f(5, -1, 0),
                new Vector3f(3, -2, 0),
                new Vector3f(-1, -2, 0)
        };
        knots = new double[]{
                0,0,0,0,2,4,4,4,4
        };
        cubicBSpline = new BSpline(controlPoints, knots, 3);
        cubicBSpline.setClamped(true);
        BezierCurve b1 = cubicBSpline.extractBezier(0);
        BezierCurve b2 = cubicBSpline.extractBezier(1);
        System.out.println("aaaa");

    }
}
