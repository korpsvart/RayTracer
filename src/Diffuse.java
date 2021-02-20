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
        ArrayList<PointLight> pointLights = currentScene.getPointLights();
        ArrayList<SceneObject> sceneObjects = currentScene.getSceneObjects();
        Vector3f surfaceNormal = this.getSurfaceNormal(hitPoint);
        //shading perfect diffuse surfaces using albedo, light intensity, cosine and square rolloff
        //Do this for each light in the scene
        //and sum all the contributions
        //First, verify there's no obstacle between point and light!
        hitPoint = hitPoint.add(surfaceNormal.mul(Scene.getBias())); //bias in direction of normal
        Vector3f finalColor = new Vector3f(0f,0f,0f);
        for (PointLight pLight:
                pointLights) {
            Vector3f lDir = hitPoint.moveTo(pLight.getPosition());
            double distance = lDir.magnitude();
            lDir = lDir.normalize();
            Line3d shadowRay = new Line3d(hitPoint, lDir);
            boolean visibility = true;
            for (SceneObject sO :
                    sceneObjects) {
                    Optional<IntersectionDataScene> tIntecept = sO.trace(shadowRay, RayType.SHADOW);
                    if (tIntecept.isPresent() && tIntecept.get().getT() < distance) {
                        visibility = false;
                    }
            }
            if (visibility) {
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
}
