import java.util.Optional;

public abstract class SceneObject extends GeometricObject {

    /*Scene Object, decorating (more like wrapping) a GeometricObject,
    adding material properties to them.
    This class should be extended by class like Diffuse, MirrorLike ecc...
     */

    private GeometricObject geometricObject;
    private boolean useBVH = false; //set to true if you wish to use bvh to accelerate intersection
    //1)do not use bvh acceleration with plane
    //2)it's not recommended for simple object, such as spheres

    public void setUseBVH(boolean useBVH) {
        this.useBVH = useBVH;
    }

    public boolean isUseBVH() {
        return useBVH;
    }

    public SceneObject(GeometricObject geometricObject) {
        //wrap the geometric object passed
        this.geometricObject = geometricObject;
    }

    @Override
    public Optional<IntersectionData> rayIntersection(Line3d ray) {
        return geometricObject.rayIntersection(ray);
    }

    @Override
    public Vector3f getSurfaceNormal(Vector3f point, double u, double v) {
        return geometricObject.getSurfaceNormal(point, u, v);
    }

    @Override
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

}
