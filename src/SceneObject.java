import java.awt.*;
import java.util.Optional;

public abstract class SceneObject extends GeometricObject {

    /*Scene Object, decorating (more like wrapping) a GeometricObject,
    adding material properties to them.
    This class should be extended by class like Diffuse, MirrorLike ecc...
     */

    private GeometricObject geometricObject;

    public SceneObject(GeometricObject geometricObject) {
        //wrap the geometric object passed
        this.geometricObject = geometricObject;
    }

    @Override
    public Optional<Double> rayIntersection(Line3d ray) {
        return geometricObject.rayIntersection(ray);
    }

    @Override
    public Vector3f getSurfaceNormal(Vector3f point) {
        return geometricObject.getSurfaceNormal(point);
    }

    public abstract Optional<Double> trace(Line3d ray, RayType rayType);
    public abstract Vector3f computeColor(Vector3f hitPoint, Line3d ray, int rayDepth, Scene currentScene);

}
