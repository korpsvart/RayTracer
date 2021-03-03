public class BoundingVolume {

    private static final int planeSetNormalNumber = 7;
    private static final Vector3f[] planeSetNormal = {
            new Vector3f(1, 0, 0),
            new Vector3f(0, 1, 0),
            new Vector3f(0, 0, 1),
            new Vector3f(Math.sqrt(3)/3, Math.sqrt(3)/3, Math.sqrt(3)/3),
            new Vector3f(-Math.sqrt(3)/3, Math.sqrt(3)/3, Math.sqrt(3)/3),
            new Vector3f(-Math.sqrt(3)/3, -Math.sqrt(3)/3, Math.sqrt(3)/3),
            new Vector3f(Math.sqrt(3)/3, -Math.sqrt(3)/3, Math.sqrt(3)/3)
    };

    private double dNear[] = new double[planeSetNormalNumber];
    private double dFar[] = new double[planeSetNormalNumber];

    public BoundingVolume(Vector3f vertex[]) {
        for (int i = 0; i < planeSetNormalNumber; i++) {
            double dNearTemp = Double.POSITIVE_INFINITY;
            double dFarTemp = Double.NEGATIVE_INFINITY;
            for (int j = 0; j < vertex.length; j++) {
                double d = planeSetNormal[i].dotProduct(vertex[j]);
                dNearTemp = Math.min(d, dNearTemp);
                dFarTemp = Math.max(d, dFarTemp);
            }
            dNear[i] = dNearTemp;
            dFar[i] = dFarTemp;
        }
    }

    public boolean intersect(Line3d ray) {
        double tNear = Double.NEGATIVE_INFINITY;
        double tFar = Double.POSITIVE_INFINITY;
        Vector3f origin = ray.getPoint();
        Vector3f r = ray.getDirection();
        for (int i = 0; i < planeSetNormalNumber; i++) {
            double num = planeSetNormal[i].dotProduct(origin);
            double den = planeSetNormal[i].dotProduct(r);
            double tn = (dNear[i] - num) / den;
            double tf = (dFar[i] - num) / den;
            if (den < 0) {
                //swap
                double temp = tn;
                tn = tf;
                tf = temp;
            }
            if (tn > tNear) tNear = tn;
            if (tf < tFar) tFar = tf;
            if (tNear > tFar) return false;
        }
        return true;
    }


}
