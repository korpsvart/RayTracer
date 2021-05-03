package rendering;

public class Line3d {

    //I should probably rename this to Ray

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
        //currently unused, but could be useful in future
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


}
