package rendering;

import java.util.List;
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
        Vector3d reflectionColor =  currentScene.rayTraceWithBVH(reflectionRay, rayDepth+1).mul(fr);
        if (currentScene.isShadowMirror()) {
            reflectionColor = accountForVisibility(currentScene, hitPoint, reflectionColor, this);
        }
        return reflectionColor;
    }

    public static Vector3d accountForVisibility(Scene currentScene, Vector3d hitPoint, Vector3d reflectionColor, SceneObject sceneObject) {
        //check if light is blocked by another object
        //if it is, then make the color darker
        List<LightSource> lightSourceList = currentScene.getLightSources();
        int n = lightSourceList.size();
        Vector3d dimAmount = reflectionColor.mul(-(double)1/n);
        for (LightSource lightSource:
                lightSourceList) {
            LightSource.LightInfo lightInfo = lightSource.getDirectionAndDistance(hitPoint);
            Vector3d lDir = lightInfo.getLightDir();
            double distance = lightInfo.getDistance();
            Vector3d hitPoint3 = hitPoint.add(lDir.mul(10e-3)); //adding depth bias
            Ray shadowRay = new Ray(hitPoint3, lDir);
            boolean visibility = currentScene.checkVisibility(shadowRay, distance, sceneObject);
            if (!visibility) {
                //compute color
                //(now using square rolloff)
                reflectionColor = reflectionColor.add(dimAmount);
            }
        }
        return reflectionColor;
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
