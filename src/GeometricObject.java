import java.util.Optional;

public abstract class GeometricObject {

    /* Geometric object, not considering its material properties
    with respect to light. This class can be directly inherited by geometric
    objects, which only needs to supply methods regarding geometric properties.
    (This acts like the "Component" in a Decorator Pattern, the concrete components
    are the geometric objects, which you can decorate to add "material" properties to them.
    You MUST add material properties to be able to render them in the Scene).
     */

    public Optional<IntersectionData> rayIntersection2(Line3d ray) {
        //default implementation
        //for objects for which we are not able to calculate directly intersection
        //(i.e. bezier surfaces, which are always triangulated)

        return Optional.empty();
    }

    public Optional<IntersectionData> rayIntersection(Line3d ray) {
        //this is the version which should be always called from outside
        //which always includes the bounding box check
        if (this.boundingVolumeCheck(ray)) {
            return this.rayIntersection2(ray);
        } else {
            return Optional.empty();
        }
    }

    public Vector3f getSurfaceNormal(Vector3f point, double u, double v) {
        return null;
    }

    public boolean boxCheck(Line3d ray) {
        //default implementation for objects
        //for which box check is overkill
        return true;
    }

    public boolean boundingVolumeCheck(Line3d ray) {
        return true;
    }

    public TriangleMesh triangulate(int divs) {
        return null;
    }

    public BoundingVolume getBoundingVolume() {
        //default implementation for objects for which
        //we don't want to implement bounding volumes
        return BoundingVolume.createNullBoundingvolume();
    }

}
