package rendering;

public class BezierSplineSurface {

    private double uNodes[];
    private double vNodes[];
    private BezierSurface33[] patches;

//    public static BezierSpline3 splineInterpolation(int m, int n, BezierSpline3[][] splines) {
//        //create surface by interpolating given bezier splines
//        //The approach is this:
//        //every patch can be considered as a Coons patch, defined by 16 vectors
//        //12 of these are easily computed from the composing bezier curves
//        //the 4 missing are the twist vectors, which can be estimated in various ways.
//        //We use Adini's twist method, which guarantees C1 surfaces.
//        //Once we obtain the twist vectors, we exploit the relationship between these vectors
//        //and the internal control points of the bezier surfaces.
//        //This allows to keep using always the same "primitive" (degree (3,3) bezier surfaces)
//        //instead of implementing Coons patches explicitly
//
//        BezierSurface33[] patches = new BezierSurface33[(n-1)*(m-1)];
//
//        for (int i = 0; i < m-1; i++) {
//            for (int j = 0; j < n - 1; j++) {
//                BezierCurve3[] patchCurves = new BezierCurve3[4];
//                patchCurves[0] = splines[0][i].getCurves()[j];
//                patchCurves[1] = splines[0][i+1].getCurves()[j];
//                patchCurves[2] = splines[1][j].getCurves()[i];
//                patchCurves[3] = splines[1][j+1].getCurves()[i];
//
//            }
//        }
//    }
//
//    private Vector3f adiniTwist(BezierSurface33[][] patches, int i, int j) {
//
//
//    }
//
//
}
