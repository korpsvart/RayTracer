public class BezierCurve {


    public static Vector3f deCasteljau(Vector3f[] controlPoints, int degree, double t) {
        //Generic degree Bezier Curve evaluation with DeCasteljau algorithm
        //(Recursive version)
        //degree must always match length(controlPoints)-1
        if (degree==1) {
            return controlPoints[0].mul(1-t).add(controlPoints[1].mul(t));
        }
        Vector3f controlPointsNext[] = new Vector3f[degree];
        for (int i = 0; i < degree; i++) {
            controlPointsNext[0] = controlPoints[i].mul(1-t).add(controlPoints[i+1].mul(t));
        }
        return deCasteljau(controlPointsNext, degree-1, t);
    }
}
