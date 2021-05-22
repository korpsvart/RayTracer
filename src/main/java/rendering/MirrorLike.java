package rendering;

import java.util.Optional;

public class MirrorLike extends SceneObject{

    /*Objects which only reflect light (no refraction) */

    private double fr = 0.8; //fraction of reflected light

    public MirrorLike(GeometricObject geometricObject) {
        super(geometricObject);
    }

    @Override
    public Optional<IntersectionData> trace(Ray ray, RayType rayType) {
        if (rayType == RayType.INDIRECT_DIFFUSE) {
            return Optional.empty();
        } else {
            return this.rayIntersection(ray);
        }
    }


    @Override
    public Vector3d computeColor(IntersectionData intersectionData, Ray ray, int rayDepth, Scene currentScene) {
        Double t = intersectionData.getT();
        Double u = intersectionData.getU();
        Double v = intersectionData.getV();
        Vector3d hitPoint = ray.getPoint().add(ray.getDirection().mul(t));
        Vector3d surfaceNormal = this.getSurfaceNormal(hitPoint, u, v);
        hitPoint = hitPoint.add(surfaceNormal.mul(Scene.getBias())); //add bias in direction of surface normal
        Vector3d incident = ray.getDirection();
        Vector3d reflectionDir = surfaceNormal.mul(incident.dotProduct(surfaceNormal)*-2).add(incident);
        Ray reflectionRay = new Ray(hitPoint, reflectionDir);
        return currentScene.rayTraceWithBVH(reflectionRay, rayDepth+1).mul(fr);
    }

    @Override
    public void addTrianglesToScene(Scene currentScene, TriangleMesh triangleMesh) {
        for (TriangleMesh.Triangle triangle :
                triangleMesh.getTriangles()) {
            currentScene.addSceneObject(new MirrorLike(triangle));
        }
    }

    @Override
    String getTypeName() {
        return "Mirror-like";
    }


}
