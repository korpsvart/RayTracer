package rendering;

public class Ray {

    //I should probably rename this to Ray

    private final Vector3d point;
    private final Vector3d direction;

    public Ray(Vector3d point, Vector3d direction) {
        this.point = point;
        this.direction = direction.normalize();
    }

    public Vector3d getPoint() {
        return point;
    }

    public Vector3d getDirection() {
        return direction;
    }

    public double pointRayDistance(Vector3d q) {
        //currently unused, but could be useful in future
        Vector3d u = q.add(this.point.mul(-1));
        double dP = u.dotProduct(this.direction);
        if (dP > 0) {
            Vector3d puv = u.project(this.direction);
            Vector3d q1 = this.point.add(puv);
            return q.add(q1.mul(-1)).magnitude();
        } else {
            //point is behind the ray
            //we return just the distance |p-q|
            //with minus sign to inform that the point
            //is behind the ray
            return -q.add(this.point.mul(-1)).magnitude();
        }

    }


}
