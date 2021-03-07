import java.util.ArrayList;
import java.util.Optional;

public class Diffuse extends SceneObject
{
    private double albedo = 0.3; //albedo is only considered for diffuse objects

    public Diffuse(GeometricObject geometricObject) {
        super(geometricObject);
    }

    public void setAlbedo(double albedo) {
        this.albedo = albedo;
    }

    @Override
    public Optional<IntersectionData> trace(Line3d ray, RayType rayType) {
        return this.rayIntersection(ray);
    }

    @Override
    public Vector3f computeColor(IntersectionData intersectionData, Line3d ray, int rayDepth, Scene currentScene) {
        ArrayList<PointLight> pointLights = currentScene.getPointLights();
        ArrayList<SceneObject> sceneObjects = currentScene.getSceneObjects();
        Double t = intersectionData.getT();
        Double u = intersectionData.getU();
        Double v = intersectionData.getV();
        Vector3f hitPoint = ray.getPoint().add(ray.getDirection().mul(t));
        Vector3f surfaceNormal = this.getSurfaceNormal(hitPoint, u, v);
        //shading perfect diffuse surfaces using albedo, light intensity, cosine and square rolloff
        //Do this for each light in the scene
        //and sum all the contributions
        //First, verify there's no obstacle between point and light!
        hitPoint = hitPoint.add(surfaceNormal.mul(Scene.getBias())); //adding normal bias
        Vector3f finalColor = new Vector3f(0f,0f,0f);
        for (PointLight pLight:
                pointLights) {
            Vector3f lDir = hitPoint.moveTo(pLight.getPosition());
            double distance = lDir.magnitude();
            lDir = lDir.normalize();
            Vector3f hitPoint2 = hitPoint.add(lDir.mul(10-3)); //adding depth bias
            Line3d shadowRay = new Line3d(hitPoint2, lDir);
            boolean visibility = true;
//            for (SceneObject sO :
//                    sceneObjects) {
//                    Optional<IntersectionData> tIntecept = sO.trace(shadowRay, RayType.SHADOW);
//                    if (tIntecept.isPresent() && tIntecept.get().getT() < distance) {
//                        visibility = false;
//                        break;
//                    }
//            }
            Optional<IntersectionDataPlusObject> obstacle = currentScene.getBVH().intersect(shadowRay, RayType.SHADOW);
            if (!obstacle.isPresent()) {
                //compute color
                //(now using square rolloff)
                double facingRatio = Math.max(0, surfaceNormal.dotProduct(lDir)); //we still need this
                Vector3f lightColor = Vector3f.colorToVector(pLight.getColor());
                double intensity = pLight.getIntensity() * facingRatio / Math.pow(distance, 2);
                lightColor = lightColor.mul(intensity);
                finalColor = finalColor.add(lightColor);
            }
        }
        finalColor = finalColor.mul(albedo/(4*Math.pow(Math.PI, 2)));
        return finalColor;
    }

    @Override
    public void addTrianglesToScene(Scene currentScene, TriangleMesh triangleMesh) {
        for (TriangleMesh.Triangle triangle :
                triangleMesh.getTriangles()) {
            currentScene.addSceneObject(new Diffuse(triangle));
        }
    }


}
