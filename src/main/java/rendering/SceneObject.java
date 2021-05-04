package rendering;
import java.util.Optional;

public abstract class SceneObject{


    /*Scene Object, decorating (more like wrapping) a GeometricObject,
    adding material properties to them.
    This class should be extended by class like Diffuse, MirrorLike ecc...
     */

    private GeometricObject geometricObject;

    private SceneObject topLevelSceneObject;

    public GeometricObject getGeometricObject() {
        return geometricObject;
    }

    public boolean isUseBVH() {
        return geometricObject.isUseBVH();
    }

    public SceneObject(GeometricObject geometricObject) {
        //wrap the geometric object passed
        this.geometricObject = geometricObject;
        if (geometricObject instanceof TriangleMesh.Triangle) {
            topLevelSceneObject = ((TriangleMesh.Triangle)geometricObject).getTriangleMesh().getSceneObject();
        } else {
            topLevelSceneObject = this;
        }
    }

    public Vector3f[] getLocalCartesianSystem(Vector3f point, double u, double v) {
        return geometricObject.getLocalCartesianSystem(point, u, v);
    }


    public Optional<IntersectionData> rayIntersection(Line3d ray) {
        return geometricObject.rayIntersection(ray);
    }


    public Vector3f getSurfaceNormal(Vector3f point, double u, double v) {
        return geometricObject.getSurfaceNormal(point, u, v);
    }


    public TriangleMesh triangulate(int divs) {
        return geometricObject.triangulate(divs);

    }

    public abstract Optional<IntersectionData> trace(Line3d ray, RayType rayType);
    public abstract Vector3f computeColor(IntersectionData intersectionData, Line3d ray, int rayDepth, Scene currentScene);

    public abstract void addTrianglesToScene(Scene currentScene, TriangleMesh triangleMesh);

    public BoundingVolume getBoundingVolume() {
        BoundingVolume b = this.geometricObject.getBoundingVolume();
        return BoundingVolume.linkToObject(b, this);
    }

    abstract String getTypeName();

    @Override
    public String toString() {
        return getTypeName() + geometricObject.toString();
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SceneObject) {
            return topLevelSceneObject== ((SceneObject) obj).topLevelSceneObject;
        }
        return false;
    }
}
