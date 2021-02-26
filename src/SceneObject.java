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

    public abstract void addToScene(Scene currentScene, GeometricObject geometricObject);

    @Override
    public Optional<IntersectionDataGeometric> rayIntersection(Line3d ray) {
        return geometricObject.rayIntersection(ray);
    }

    @Override
    public Vector3f getSurfaceNormal(Vector3f point) {
        return geometricObject.getSurfaceNormal(point);
    }

    @Override
    public TriangleMesh triangulate(int divs) {
        return geometricObject.triangulate(divs);
    }

    public abstract Optional<IntersectionDataScene> trace(Line3d ray, RayType rayType);
    public abstract Vector3f computeColor(Vector3f hitPoint, Line3d ray, int rayDepth, Scene currentScene);

    public void triangulateAndAddToScene(Scene currentScene, int divs) {
        TriangleMesh triangleMesh = this.triangulate(divs);
        triangleMesh.addToScene(currentScene, this);
    }

}
