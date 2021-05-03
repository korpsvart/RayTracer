package main.java.rendering;

public class BezierSurface {


    public static Vector3f deCasteljauV1(Vector3f controlPoints[][], double u, double v) {
        //evaluate a generic (h,k) degree Bezier Surface by using DeCasteljau
        //algorithm on curves
        int h = controlPoints.length;
        int k = controlPoints[0].length;
        Vector3f curveControlPoints[] = new Vector3f[h];
        for (int i = 0; i < h; i++) {
            curveControlPoints[i] = BezierCurve.deCasteljau(controlPoints[i], k, v);
        }
        return BezierCurve.deCasteljau(curveControlPoints, h, u);
    }
}
