import java.util.Optional;

public class BoundingVolume {

    private static final int planeSetNormalNumber = 7;
    private static Vector3f[] planeSetNormal = {
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



    private SceneObject sceneObject;

    public SceneObject getSceneObject() {
        return sceneObject;
    }

    public void setSceneObject(SceneObject sceneObject) {
        this.sceneObject = sceneObject;
    }


    public BoundingVolume() {
        for (int i = 0; i < planeSetNormalNumber; i++) {
            dNear[i] = Double.POSITIVE_INFINITY;
            dFar[i] = Double.NEGATIVE_INFINITY;
        }
    }

    public static BoundingVolume linkToObject(BoundingVolume boundingVolume, SceneObject sceneObject) {
        //create new bounding volume from existing one,
        //linking it to a particular object
        BoundingVolume b = new BoundingVolume();
        b.dNear = boundingVolume.getdNear();
        b.dFar = boundingVolume.getdFar();
        b.sceneObject = sceneObject;
        return b;
    }

    public static BoundingVolume createNullBoundingvolume() {
        BoundingVolume boundingVolume = new BoundingVolume();
        for (int i = 0; i < planeSetNormalNumber; i++) {
            boundingVolume.dNear[i] = -10e8;
            boundingVolume.dFar[i] = 10e8;
        }
        return boundingVolume;
    }

    public BoundingVolume(SceneObject sceneObject) {
        this();
        this.sceneObject = sceneObject;
    }

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

    public BoundingVolume(Vector3f vertex[], SceneObject sceneObject) {
        this(vertex);
        this.sceneObject = sceneObject;
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

    public Optional<Double> intersect(Line3d ray, double[][] precalculated) {
        //make use of precalculated dot products
        //to enhance performance
        //Also returns t
        double tNear = Double.NEGATIVE_INFINITY;
        double tFar = Double.POSITIVE_INFINITY;
        Vector3f origin = ray.getPoint();
        Vector3f r = ray.getDirection();
        for (int i = 0; i < planeSetNormalNumber; i++) {
            double num = precalculated[i][0];
            double den = precalculated[i][1];
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
            if (tNear > tFar) return Optional.empty();
        }
        //if tNear is negative while tFar is positive, it means
        //ray was casted from inside the box, so we should return
        //tFar instead of tNear
        double t = (tNear < 0 && tFar >= 0) ? tFar : tNear;
        return Optional.of(t);
    }

    public void extendBy(BoundingVolume b) {
        for (int i = 0; i < planeSetNormalNumber; i++) {
            if (this.dNear[i] > b.dNear[i]) this.dNear[i] = b.dNear[i];
            if (this.dFar[i] < b.dFar[i]) this.dFar[i] = b.dFar[i];
        }
    }

    public double[] getdFar() {
        return dFar;
    }

    public double[] getdNear() {
        return dNear;
    }

    public static double[][] precalculateForIntersection(Line3d ray) {
        Vector3f o = ray.getDirection();
        Vector3f r = ray.getDirection();
        double res[][] = new double[planeSetNormalNumber][2];
        for (int i = 0; i < planeSetNormalNumber; i++) {
            res[i][0] = planeSetNormal[i].dotProduct(o); //first element is always numerator
            res[i][1] = planeSetNormal[i].dotProduct(r); //second element is denominator
        }
        return res;
    }
}
