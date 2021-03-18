import java.util.Scanner;


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

        Vector3f[] dataPoints1 = new Vector3f[]{
                new Vector3f(0, 0, -5),
                new Vector3f(2, 0.5, -5),
                new Vector3f(4, 0.3, -5)
        };
        Vector3f[] dataPoints2 = new Vector3f[] {
                new Vector3f(0.3, 3, -6),
                new Vector3f(2.2, 2.8, -6),
                new Vector3f(4.2, 3.1, -6)
        };
        Vector3f[] tangent = new Vector3f[] {
                new Vector3f(1, 1, 0),
                new Vector3f(1, 0, 0),
                new Vector3f(1, -1, 0)
        };
        Vector3f[] tangent2 = new Vector3f[] {
                new Vector3f(1, 2, 0),
                new Vector3f(1, -1, 0),
                new Vector3f(1, -2, 0)
        };

        Vector3f[] tangent3 = new Vector3f[]{
                new Vector3f(-0.2, 1, 0),
                new Vector3f(0.3, 1, 0)
        };

        BezierSpline3 spline1 = BezierSpline3.piecewiseCubicInterpolation(dataPoints1, tangent);
        BezierSpline3 spline2 = BezierSpline3.piecewiseCubicInterpolation(dataPoints2, tangent2);
        BezierSpline3 spline3 = BezierSpline3.piecewiseCubicInterpolation(new Vector3f[]{dataPoints1[0], dataPoints2[0]}, tangent3);
        BezierSpline3 spline4 = BezierSpline3.piecewiseCubicInterpolation(new Vector3f[]{dataPoints1[1], dataPoints2[1]}, tangent3);
        BezierSpline3 spline5 = BezierSpline3.piecewiseCubicInterpolation(new Vector3f[]{dataPoints1[2], dataPoints2[2]}, tangent3);
        BezierSurface33 surface1 = BezierSurface33.fillWithCoons(new BezierCurve3[]{
                spline1.getCurves()[0],
                spline2.getCurves()[0],
                spline3.getCurves()[0],
                spline4.getCurves()[0]
        });
        BezierSurface33 surface2 = BezierSurface33.fillWithCoons(new BezierCurve3[]{
                spline1.getCurves()[1],
                spline2.getCurves()[1],
                spline4.getCurves()[0],
                spline5.getCurves()[0]
        });

    }
}
