import java.awt.*;
import java.util.Optional;

public class Triangle extends GeometricObject {

    //Vertices must be given in CCW ordering

    private final Vector3f v0, v1, v2;

    public Triangle(Vector3f v0, Vector3f v1, Vector3f v2) {
        this.v0 = v0;
        this.v1 = v1;
        this.v2 = v2;
    }

    @Override
    public Optional<Double> rayIntersection2(Line3d ray) {
        //Using local coordinates u,v
        //solving linear system with Cramer's rule
        //(akin to Moller-Trumbore algorithm)

        Main.getRayTriangleTests().getAndIncrement();

        Vector3f e1 = v0.moveTo(v1);
        Vector3f e2 = v0.moveTo(v2);
        Vector3f n = e1.crossProduct(e2);

        Vector3f rayDirOpposite = ray.getDirection().mul(-1);

        double denom = rayDirOpposite.dotProduct(n);

        if (Math.abs(denom) < 10e-6) {
            //if denom is really close to 0 it means
            //ray direction and normal are perpendicular
            //(t goes to infinity)
            return Optional.empty();
        }

        if (Scene.isBackFaceCulling() && denom < 0) {
            //if denom is < 0
            //oject is backfacing
            //don't show if back face culling is enalbed
            return Optional.empty();
        }

        Vector3f b = v0.moveTo(ray.getPoint());
        Vector3f q = b.crossProduct(ray.getDirection());

        double u = e2.dotProduct(q) / denom;

        if (u < 0 || u > 1) {
            return Optional.empty();
        }

        double v = e1.mul(-1).dotProduct(q) / denom;

        if (v < 0 || (u + v) > 1) {
            return Optional.empty();
        } else {
            Main.getRayTriangleIntersections().getAndIncrement();
            return Optional.of(b.dotProduct(n) / denom);
        }


    }

    @Override
    public Vector3f getSurfaceNormal(Vector3f point) {
        Vector3f e1 = v0.moveTo(v1);
        Vector3f e2 = v0.moveTo(v2);
        return e1.crossProduct(e2).normalize();
    }
}
