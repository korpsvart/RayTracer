package rendering;

public class BoundingBox {

    private Vector3f min;
    private Vector3f max;
    private Vector3f centroid;

    public BoundingBox(Vector3f min, Vector3f max) {
        this.min = min;
        this.max = max;
    }

    public BoundingBox(Vector3f vertex[]) {
        double xMin = Double.POSITIVE_INFINITY;
        double yMin = Double.POSITIVE_INFINITY;
        double zMin = Double.POSITIVE_INFINITY;
        double xMax = Double.NEGATIVE_INFINITY;
        double yMax = Double.NEGATIVE_INFINITY;
        double zMax = Double.NEGATIVE_INFINITY;
        for (Vector3f point:
                vertex) {
            if (point.getX() < xMin) xMin = point.getX();
            if (point.getY() < yMin) yMin = point.getY();
            if (point.getZ() < zMin) zMin = point.getZ();
            if (point.getX() > xMax) xMax = point.getX();
            if (point.getY() > yMax) yMax = point.getY();
            if (point.getZ() > zMax) zMax = point.getZ();
        }
        this.min = new Vector3f(xMin, yMin, zMin);
        this.max = new Vector3f(xMax, yMax, zMax);
    }



    public static boolean rayBoxIntersection(Vector3f min, Vector3f max, Line3d ray) {

        double tmin, tmax; //actual t values of intersection points

        //Not really readable right now. Will comment later maybe
        Vector3f inverseDirection = ray.getDirection().invert();
        //signs are used for swapping if ray direction is opposite to bounds order
        int sign_x = inverseDirection.getX() >= 0 ? 0 : 1;
        int sign_y = inverseDirection.getY() >= 0 ? 0 : 1;
        int sign_z = inverseDirection.getZ() >= 0 ? 0 : 1;

        Vector3f bound[] = {min, max};

        double tmin_x = (bound[sign_x].getX() - ray.getPoint().getX()) * inverseDirection.getX();
        double tmax_x = (bound[1-sign_x].getX() - ray.getPoint().getX()) * inverseDirection.getX();


        double tmin_y = (bound[sign_y].getY() - ray.getPoint().getY()) * inverseDirection.getY();
        double tmax_y = (bound[1-sign_y].getY() - ray.getPoint().getY()) * inverseDirection.getY();


        if ((tmax_x < tmin_y) || (tmax_y < tmin_x)) {
            return false;
        }

        //select largest t for tmin
        if (tmin_x < tmin_y) {
            tmin = tmin_y;
        } else {
            tmin = tmin_x;
        }

        //select smallest t for tmax
        if (tmax_x < tmin_y) {
            tmax = tmax_x;
        } else {
            tmax = tmax_y;
        }

        double tmin_z = (bound[sign_z].getZ() - ray.getPoint().getZ()) * inverseDirection.getZ();
        double tmax_z = (bound[1-sign_z].getZ() - ray.getPoint().getZ()) * inverseDirection.getZ();


        if (tmax_z < tmin) {
            return false;
        }

        //select largest t for tmin
        if (tmin < tmin_z) {
            tmin = tmin_z;
        }

        //select smallest t for tmax
        if (tmax > tmax_z) {
            tmax = tmax_z;
        }

        //return tmin, tmax if needed
        //for now I will just return true

        return true;

    }

    public Vector3f getCentroid() {
        if (centroid == null) {
            centroid = new Vector3f(
                    min.add(max).getX()/2,
                    min.add(max).getY()/2,
                    min.add(max).getZ()/2
            );
        }
        return centroid;

    }

    public boolean rayIntersection(Line3d ray) {
        return BoundingBox.rayBoxIntersection(this.min, this.max, ray);
    }
}
