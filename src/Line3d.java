import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Optional;

public class Line3d {



    private final Vector3f point;
    private final Vector3f direction;

    public Line3d(Vector3f point, Vector3f direction) {
        this.point = point;
        this.direction = direction.normalize();
    }

    public Vector3f getPoint() {
        return point;
    }

    public Vector3f getDirection() {
        return direction;
    }

    public double pointRayDistance(Vector3f q) {
        Vector3f u = q.add(this.point.mul(-1));
        double dP = u.dotProduct(this.direction);
        if (dP > 0) {
            Vector3f puv = u.project(this.direction);
            Vector3f q1 = this.point.add(puv);
            return q.add(q1.mul(-1)).magnitude();
        } else {
            //point is behind the ray
            //we return just the distance |p-q|
            //with minus sign to inform that the point
            //is behind the ray
            return -q.add(this.point.mul(-1)).magnitude();
        }

    }

    public ArrayList<Vector3f> sphereRayIntersect(Sphere sphere) {
        ArrayList<Vector3f> intercepts = new ArrayList<Vector3f>();
        Vector3f pq = sphere.getCentre().add(this.point.mul(-1));
        double distance = this.pointRayDistance(sphere.getCentre());
        if (distance <= 0) {
            //sphere centre is behind the ray
            //We check if the ray origin is inside the sphere
            if (Math.abs(distance) > sphere.getRadius()) {
                //No intersection
            } else if (Math.abs(distance) == sphere.getRadius()){
                //Ray origin is on the sphere surface
                //The origin is the intercept
                intercepts.add(this.point);
            } else {
                //Ray origin is inside the sphere
                //we project pq onto the line corresponding to the ray
                Vector3f pq1 = pq.project(this.direction);
                Vector3f q1 = this.point.add(pq1);
                distance = sphere.getCentre().add(q1.mul(-1)).magnitude();
                double pq1Length = q1.add(this.point.mul(-1)).magnitude();
                double q1xLength = Math.sqrt(Math.pow(sphere.getRadius(), 2) - Math.pow(distance, 2));
                Vector3f x = q1.add(this.direction.mul(q1xLength));
                intercepts.add(x);
            }
        } else {
            if (Math.abs(distance) > sphere.getRadius()) {
                //No intersection
            } else if (Math.abs(distance) == sphere.getRadius()) {
                //Intersection is the projection of sphere centre
                Vector3f uv = pq.project(this.direction);
                Vector3f x = this.point.add(uv);
                intercepts.add(x);
            } else {
                if (pq.magnitude() > sphere.getRadius()) {
                    //origin of ray is outside sphere
                    Vector3f pq1 = pq.project(this.direction);
                    double q1x1Length = Math.sqrt(Math.pow(sphere.getRadius(), 2) - Math.pow(distance, 2));
                    double px1Length = pq1.magnitude() - q1x1Length;
                    Vector3f x1 = this.point.add(this.direction.mul(px1Length));
                    Vector3f x2 = x1.add(this.direction.mul(2*q1x1Length));
                    intercepts.add(x1);
                    intercepts.add(x2);
                } else {
                    //origin of ray is inside sphere
                    Vector3f pq1 = pq.project(this.direction);
                    double q1x1Length = Math.sqrt(Math.pow(sphere.getRadius(), 2) - Math.pow(distance, 2));
                    double px1Length = pq1.magnitude() + q1x1Length;
                    Vector3f x1 = this.point.add(this.direction.mul(px1Length));
                    intercepts.add(x1);
                }

            }
        }

        return intercepts;
    }


    public Optional<Vector3f> sphereRayIntersectV2(Sphere sphere) {
        //This version uses a quadratic equation to derive the specific
        //t value to put into the ray parametric equation for the intercept
        Vector3f intercept = null;
        double a = 1; //a = |d|^2, but its already stored as a unit vector
        Vector3f vectorCE = sphere.getCentre().moveTo(this.point);
        double b = this.direction.mul(2).dotProduct(vectorCE);
        double c = vectorCE.magnitudeSquared() - Math.pow(sphere.getRadius(), 2);
        double d = Math.pow(b, 2) - 4*a*c;
        if (d >= 0) {
            double t1 = (-b - Math.sqrt(d))/(2*a);
            double t2 = (-b + Math.sqrt(d))/(2*a);
            double t = Math.min(t1, t2);
            intercept = this.point.add(this.direction.mul(t));
        }
        return Optional.ofNullable(intercept);
    }


}
