import java.util.Optional;

public abstract class GeometricObject {

    /* Geometric object, not considering its material properties
    with respect to light. This class can be directly inherited by geometric
    objects, which only needs to supply methods regarding geometric properties.
    (This acts like the "Component" in a Decorator Pattern, the concrete components
    are the geometric objects, which you can decorate to add "material" properties to them.
    You MUST add material properties to be able to render them in the Scene).
     */

    public abstract Optional<IntersectionDataGeometric> rayIntersection(Line3d ray);
    public abstract Vector3f getSurfaceNormal(Vector3f point);
    public boolean boxCheck(Line3d ray) {
        //default implementation for objects
        //for which box check is overkill
        return true;
    }
}
