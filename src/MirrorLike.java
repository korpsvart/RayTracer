import java.util.Optional;

public abstract class MirrorLike extends SceneObject{

    /*Objects which only reflect light (no refraction) */

    private double fr = 0.8; //fraction of reflected light

    public MirrorLike(GeometricObject geometricObject) {
        super(geometricObject);
    }

    @Override
    public Optional<IntersectionDataScene> trace(Line3d ray, RayType rayType) {
        Optional<IntersectionDataGeometric> intersectionDataGeometric = this.rayIntersection(ray);
        if (intersectionDataGeometric.isPresent()) {
            GeometricObject geometricObject = intersectionDataGeometric.get().getGeometricObject();
            return Optional.of(new IntersectionDataScene(intersectionDataGeometric.get().getT(), new Diffuse(geometricObject)));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Vector3f computeColor(Vector3f hitPoint, Line3d ray, int rayDepth, Scene currentScene) {
        Vector3f surfaceNormal = this.getSurfaceNormal(hitPoint);
        hitPoint = hitPoint.add(surfaceNormal.mul(Scene.getBias())); //add bias in direction of surface normal
        Vector3f incident = ray.getDirection();
        Vector3f reflectionDir = surfaceNormal.mul(incident.dotProduct(surfaceNormal)*-2).add(incident);
        Line3d reflectionRay = new Line3d(hitPoint, reflectionDir);
        return currentScene.rayTrace(reflectionRay, rayDepth+1).mul(fr);
    }
}
