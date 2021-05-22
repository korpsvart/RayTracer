package rendering;

import java.util.ArrayList;
import java.util.Optional;

public class Phong extends SceneObject{

    private Vector3d albedo = new Vector3d(0.3, 0.3, 0.3);
    private double kd = 0.95; //diffuse component coeff
    private double ks = 1-kd; //specular component coeff
    private int n = 20; //specular exponent
    //for a more realistic visual effect
    //ks should increase as n increases
    //because the specular reflection is more "focused"


    public Phong(GeometricObject geometricObject) {
        super(geometricObject);
    }


    public void setAlbedo(Vector3d albedo) {
        this.albedo = albedo;
    }

    @Override
    public Optional<IntersectionData> trace(Ray ray, RayType rayType) {
        return this.rayIntersection(ray);
    }

    @Override
    public Vector3d computeColor(IntersectionData intersectionData, Ray ray, int rayDepth, Scene currentScene) {
        ArrayList<LightSource> lightSources = currentScene.getLightSources();
        ArrayList<SceneObject> sceneObjects = currentScene.getSceneObjects();
        Double t = intersectionData.getT();
        Double u = intersectionData.getU();
        Double v = intersectionData.getV();
        Vector3d hitPoint = ray.getPoint().add(ray.getDirection().mul(t));
        Vector3d surfaceNormal = this.getSurfaceNormal(hitPoint, u, v);
        //shading perfect diffuse surfaces using albedo, light intensity, cosine and square rolloff
        //Do this for each light in the scene
        //and sum all the contributions
        //First, verify there's no obstacle between point and light!
        Vector3d hitPoint2 = hitPoint.add(surfaceNormal.mul(Scene.getBias())); //adding normal bias
        Vector3d finalDiffuse;
        Vector3d specularComponent = new Vector3d(0, 0, 0);
        Vector3d directDiffuse = new Vector3d(0, 0, 0);
        for (LightSource lightSource:
                lightSources) {
            LightSource.LightInfo lightInfo = lightSource.getDirectionAndDistance(hitPoint2);
            Vector3d lDir = lightInfo.getLightDir();
            double distance = lightInfo.getDistance();
            Vector3d hitPoint3 = hitPoint2.add(lDir.mul(10e-3)); //adding depth bias
            Ray shadowRay = new Ray(hitPoint3, lDir);
            boolean visibility = currentScene.checkVisibility(shadowRay, distance, this);
            if (visibility) {
                //compute diffuse component
                double facingRatio = Math.max(0, surfaceNormal.dotProduct(lDir)); //we still need this
                Vector3d lightColor = lightSource.illuminate(distance).mul(facingRatio);
                directDiffuse = directDiffuse.add(lightColor);
                //compute specular component
                lDir = lDir.mul(-1);
                Vector3d reflectionDir = surfaceNormal.mul(lDir.dotProduct(surfaceNormal)*-2).add(lDir);
                double c = Math.max(0,Math.pow(ray.getDirection().mul(-1).dotProduct(reflectionDir), n));
                specularComponent = specularComponent.add(lightColor.mul(c));
            }
        }
        //brdf for purely diffuse objects is albedo/pi
        //we don't divide indirectDiffuse by pi cause we didnt multiply it before by the pdf
        directDiffuse = directDiffuse.mul(1/Math.PI);
        if (currentScene.isSimulateIndirectDiffuse()) {
            Vector3d[] lcs = this.getLocalCartesianSystem(hitPoint, u, v);
            Vector3d indirectDiffuse = MonteCarloSampling.calculateIndirectDiffuse(currentScene, hitPoint2, lcs, rayDepth);
            finalDiffuse = directDiffuse.add(indirectDiffuse.mul(2)).elementWiseMul(albedo);
        } else {
            finalDiffuse = directDiffuse.elementWiseMul(albedo);
        }
        return finalDiffuse.mul(kd).add(specularComponent.mul(ks));
    }


    @Override
    public void addTrianglesToScene(Scene currentScene, TriangleMesh triangleMesh) {
        for (TriangleMesh.Triangle triangle :
                triangleMesh.getTriangles()) {
            Phong phong = new Phong(triangle);
            phong.setAlbedo(this.albedo);
            currentScene.addSceneObject(phong);
        }
    }

    @Override
    String getTypeName() {
        return "Phong";
    }

    public Vector3d getAlbedo() {
        return albedo;
    }
}
