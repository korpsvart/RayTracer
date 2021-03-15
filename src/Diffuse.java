import java.util.ArrayList;
import java.util.Optional;

public class Diffuse extends SceneObject
{
    private Vector3f albedo = new Vector3f(0.3, 0.3, 0.3); //albedo is only considered for diffuse objects

    public Diffuse(GeometricObject geometricObject) {
        super(geometricObject);
    }

    public void setAlbedo(Vector3f albedo) {
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
        Vector3f hitPoint2 = hitPoint.add(surfaceNormal.mul(Scene.getBias())); //adding normal bias
        Vector3f finalColor = new Vector3f(0f,0f,0f);
        Vector3f directDiffuse = new Vector3f(0, 0, 0);
        for (PointLight pLight:
                pointLights) {
            Vector3f lDir = hitPoint2.moveTo(pLight.getPosition());
            double distance = lDir.magnitude();
            lDir = lDir.normalize();
            Vector3f hitPoint3 = hitPoint2.add(lDir.mul(10e-3)); //adding depth bias
            Line3d shadowRay = new Line3d(hitPoint3, lDir);
            boolean visibility = currentScene.checkVisibility(shadowRay, distance, this);
            if (visibility) {
                //compute color
                //(now using square rolloff)
                double facingRatio = Math.max(0, surfaceNormal.dotProduct(lDir)); //we still need this
                Vector3f lightColor = Vector3f.colorToVector(pLight.getColor());
                double intensity = pLight.getIntensity() * facingRatio / Math.pow(distance, 2);
                lightColor = lightColor.mul(intensity);
                directDiffuse = directDiffuse.add(lightColor);
            }
        }
        //we already divide by pi here instead of later
        //this includes both square rolloff and BRDF
        directDiffuse = directDiffuse.mul(1/(4*Math.pow(Math.PI, 2)));
        if (currentScene.isSimulateIndirectDiffuse()) {
            Vector3f[] lcs = this.getLocalCartesianSystem(hitPoint, u, v);
            Vector3f indirectDiffuse = MonteCarloSampling.calculateIndirectDiffuse(currentScene, hitPoint2, lcs, rayDepth);
            finalColor = directDiffuse.add(indirectDiffuse.mul(2)).elementWiseMul(albedo);
        } else {
            finalColor = directDiffuse.elementWiseMul(albedo);
        }
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
