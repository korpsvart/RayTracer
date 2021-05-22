package rendering;
import java.util.Optional;

public class Sphere extends GeometricObject {

    private final Vector3d centre;
    private final double radius;


    public Sphere(Vector3d centre, double radius) {
        this.triangulated = false;
        this.centre = centre;
        this.radius = radius;

    }


    public Vector3d getCentre() {
        return centre;
    }

    public double getRadius() {
        return radius;
    }


    @Override
    public Optional<IntersectionData> rayIntersection(Ray ray) {
        //This version uses a quadratic equation to derive the specific
        //t value to put into the ray parametric equation for the intercept
        double interceptT;
        Vector3d rayOrigin = ray.getPoint();
        Vector3d rayDirection = ray.getDirection();
        double a = 1; //a = |d|^2, but its already stored as a unit vector
        Vector3d vectorCE = this.getCentre().moveTo(rayOrigin);
        double b = rayDirection.mul(2).dotProduct(vectorCE);
        double c = vectorCE.magnitudeSquared() - Math.pow(this.getRadius(), 2);
        double d = Math.pow(b, 2) - 4*a*c;
        if (d >= 0) {
            double t1 = (-b - Math.sqrt(d))/(2*a);
            double t2 = (-b + Math.sqrt(d))/(2*a);
            if (t1 >= 0 && t1 < t2) {
                return Optional.of(new IntersectionData(t1));
            } else if (t2 >=0) {
                return Optional.of(new IntersectionData(t2));
            } else {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }


    @Override
    public Vector3d getSurfaceNormal(Vector3d point, double u, double v) {
        return this.centre.moveTo(point).normalize();
    }


    @Override
    public String toString() {
        return "Sphere; " + "Centre: " + getCentre() + ";" + "Radius: " + getRadius();
    }
}
