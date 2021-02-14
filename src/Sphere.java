import java.awt.*;
import java.util.Optional;

public class Sphere extends GeometricObject {

    private final Vector3f centre;
    private final double radius;


    public Sphere(Vector3f centre, double radius) {
        this.centre = centre;
        this.radius = radius;

    }


    public Vector3f getCentre() {
        return centre;
    }

    public double getRadius() {
        return radius;
    }


    @Override
    public Optional<Double> rayIntersection(Line3d ray) {
        //This version uses a quadratic equation to derive the specific
        //t value to put into the ray parametric equation for the intercept
        double interceptT;
        Vector3f rayOrigin = ray.getPoint();
        Vector3f rayDirection = ray.getDirection();
        double a = 1; //a = |d|^2, but its already stored as a unit vector
        Vector3f vectorCE = this.getCentre().moveTo(rayOrigin);
        double b = rayDirection.mul(2).dotProduct(vectorCE);
        double c = vectorCE.magnitudeSquared() - Math.pow(this.getRadius(), 2);
        double d = Math.pow(b, 2) - 4*a*c;
        if (d >= 0) {
            double t1 = (-b - Math.sqrt(d))/(2*a);
            double t2 = (-b + Math.sqrt(d))/(2*a);
            if (t1 >= 0 && t1 < t2) {
                return Optional.of(t1);
            } else if (t2 >=0) {
                return Optional.of(t2);
            } else {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }


    @Override
    public Vector3f getSurfaceNormal(Vector3f point) {
        return this.centre.moveTo(point).normalize();
    }


}
