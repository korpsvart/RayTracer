package rendering;
import java.util.Optional;

public class Plane3d extends GeometricObject {

    protected final Vector3d point;
    protected final Vector3d e1;
    protected final Vector3d e2;
    protected final Vector3d n;
    //It would be sufficient to store only n or e1 and e2
    //Right now i don't know which form will be more useful


    public Plane3d(Vector3d point, Vector3d e1, Vector3d e2) throws ArithmeticException {
        this.triangulated = false;
        this.point = point;
        try {
            this.e1 = e1.normalize();
            this.e2 = e2.normalize();
        } catch (ArithmeticException e) {
            throw new ArithmeticException("Error, one or more of the vectors defining the plane is the zero vector");
        }
        this.n = e1.crossProduct(e2);
    }


    public Plane3d(Vector3d point, Vector3d n) throws Exception {
        //If I did everything right
        //This version should also guarantee that e1, e2, n is a castesian reference frame
        this.triangulated = false;
        this.point = point;
        this.n = n.normalize();
        double a = this.n.getX();
        double b = this.n.getY();
        double c = this.n.getZ();
        double d = -(a*this.point.getX() + b*this.point.getY() + c*this.point.getZ());
        Vector3d e2;
        if (a!=0) {
            e2 = this.point.moveTo(new Vector3d(-d/a, 0, 0));
        } else if (b!=0) {
            e2 = this.point.moveTo(new Vector3d(0, -d/b, 0));
        } else if (c!=0) {
            e2 = this.point.moveTo(new Vector3d(0, 0, -d/c));
        } else {
            throw new Exception("Error! a,b,c cannot be all zeros when defining a plane!");
        }
        this.e2 = e2.normalize();
        this.e1 = this.n.crossProduct(this.e2); //should be already normalized
    }

    public Plane3d(double a, double b, double c, double d) throws Exception {
        this.triangulated = false;
        Plane3d p = cartesianToParametric(a,b,c,d);
        this.point = p.point;
        this.e1 = p.e1;
        this.e2 = p.e2;
        this.n = e1.crossProduct(e2);
    }

    public static Plane3d cartesianToParametric(double a, double b, double c, double d) throws Exception {
        if (a!=0) {
            return new Plane3d(new Vector3d(-d/a, 0, 0), new Vector3d(b/a, 1, 0).normalize(), new Vector3d(-c/a, 0, 1).normalize());
        } else if (b!=0) {
            return new Plane3d(new Vector3d(0, -d/b, 0), new Vector3d(1, -a/b, 0).normalize(), new Vector3d(0, -c/b, 1).normalize());
        } else if (c!=0) {
            return new Plane3d(new Vector3d(0, 0, -d/c), new Vector3d(1, 0, -a/c).normalize(), new Vector3d(0, 1, -b/c).normalize());
        } else {
            throw new Exception("Error! a,b,c cannot be all zeros when defining a plane!");
        }
    }

    public Optional<IntersectionData> rayIntersection(Ray ray) {
        Vector3d v = ray.getDirection();
        double dotProduct = v.dotProduct(this.n);

        if (Math.abs(dotProduct) < 10e-6) {
            return Optional.empty();
        } else {
            double t = ray.getPoint().moveTo(this.point).dotProduct(this.n) / dotProduct;
            return t>=0 ? Optional.of(new IntersectionData(t)) : Optional.empty();
        }

    }


    @Override
    public Vector3d getSurfaceNormal(Vector3d point, double u, double v) {
        return this.n;
    }


    @Override
    public String toString() {
        return "Plane; " + "Translation: " + getTranslationData() + "; " + "Rotation: " + getRotationData();
    }

}
