package main.java.rendering;

import java.util.ArrayList;
import java.util.Optional;

public class Diffuse extends SceneObject
{
    private Vector3f albedo = new Vector3f(0.3, 0.3, 0.3); //albedo is only considered for diffuse objects


    public Vector3f getAlbedo() {
        return albedo;
    }

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
        ArrayList<LightSource> lightSources = currentScene.getLightSources();
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
        for (LightSource lightSource:
                lightSources) {
            LightSource.LightInfo lightInfo = lightSource.getDirectionAndDistance(hitPoint2);
            Vector3f lDir = lightInfo.getLightDir();
            double distance = lightInfo.getDistance();
            Vector3f hitPoint3 = hitPoint2.add(lDir.mul(10e-3)); //adding depth bias
            Line3d shadowRay = new Line3d(hitPoint3, lDir);
            boolean visibility = currentScene.checkVisibility(shadowRay, distance, this);
            if (visibility) {
                //compute color
                //(now using square rolloff)
                double facingRatio = Math.max(0, surfaceNormal.dotProduct(lDir)); //we still need this
                Vector3f lightColor = lightSource.illuminate(distance).mul(facingRatio);
                directDiffuse = directDiffuse.add(lightColor);
            }
        }
        //brdf for purely diffuse objects is albedo/pi
        //we don't divide indirectDiffuse by pi cause we didnt multiply it before by the pdf
        directDiffuse = directDiffuse.mul(1/Math.PI);
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
            Diffuse diffuse = new Diffuse(triangle);
            diffuse.setAlbedo(this.albedo);
            currentScene.addSceneObject(diffuse);
        }
    }


}
