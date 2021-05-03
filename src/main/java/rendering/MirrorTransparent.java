package rendering;
import java.util.Optional;

public class MirrorTransparent extends SceneObject {

    /* Objects which react to light both with
    reflection and refraction (they have some degree
    of transparency)
     */

    private double ior = 1;
    private final static double AIR_IOR = 1;

    public MirrorTransparent(GeometricObject geometricObject) {
        super(geometricObject);
    }

    public MirrorTransparent(GeometricObject geometricObject, double ior) {
        super(geometricObject);
        this.ior = ior;
    }

    public double getIor() {
        return ior;
    }

    public void setIor(double ior) {
        this.ior = ior;
    }



    @Override
    public Optional<IntersectionData> trace(Line3d ray, RayType rayType) {
        if (rayType==RayType.SHADOW && rayType == RayType.INDIRECT_DIFFUSE) {
            /*Details about the implementation:
            A classic problem is when we are looking at diffuse objects which are
            behind reflection/refraction objects (i.e. objects that have some degree of transparency),
            but the path from the diffuse objects to the light sources is blocked by the transparent object(s).
            As an example: imagine looking at the rocks on the bottom of a lake (rocks are the diffuse objects,
            water is transparent).
            The problem is: if we check visibility by tracing the path of shadow ray and checking if this has some
            intersection with ANY other objects, we won't be able to see the rocks because light is getting "blocked"
            by water, which makes little sense.
            I think a correct solution would be to raytrace again when intersecting diffuse objects "on the bottom",
            the ray would then exit the pool of water and so on. However, I think this would not work with our current
            implementation, which uses a simplified model of reality (and even if it did, it would make everything slower).
            A naive solution would be, thus, just to exclude objects which have some degree of transparency from our visibility check
            (this is the one currently implemented).
            One could be reasonably concerned that this solution doesn't take into account the "distortion" caused by the angle of
            refraction when exiting the pool of water (i.e. we end up only considering the refraction when entering the water).
            However, I think the current approach could be justified in some way by the nature of diffuse object, which (at least ideally)
            should reflect light uniformly in all directions, thus "evening out" the distortion caused by refraction.
             */
            return Optional.empty();
        } else {
            return this.rayIntersection(ray);
        }
    }

    @Override
    public Vector3f computeColor(IntersectionData intersectionData, Line3d ray, int rayDepth, Scene currentScene) {
        Double t = intersectionData.getT();
        Double u = intersectionData.getU();
        Double v = intersectionData.getV();
        Vector3f hitPoint = ray.getPoint().add(ray.getDirection().mul(t));
        Vector3f surfaceNormal = this.getSurfaceNormal(hitPoint, u, v);
        return reflectionRefraction(hitPoint, ray.getDirection(), surfaceNormal, AIR_IOR, this.ior, rayDepth, currentScene);
    }


    public Vector3f reflectionRefraction(Vector3f hitPoint, Vector3f incident, Vector3f surfaceNormal, double ior1, double ior2, int rayDepth,
                                         Scene currentScene) {

        //compute refraction and reflection

        //compute ratio of reflected and refracted light
        //using fresnel equation

        Vector3f reflectionDir = surfaceNormal.mul(incident.dotProduct(surfaceNormal)*-2).add(incident);
        Vector3f refractionDir;
        double fr; //ratio of reflected light
        double cosi = incident.dotProduct(surfaceNormal);
        if (cosi < 0) {
            cosi = -cosi;
        } else {
            //we are going outside the object
            //reverse normal
            //(dont invert cosi cause its already positive)
            //Also swap indexes of refraction!
            double temp = ior1;
            ior1=ior2;
            ior2=temp;
            surfaceNormal = surfaceNormal.mul(-1);
        }
        double eta = ior1 / ior2;
        double c = 1 - Math.pow(eta, 2)*(1-Math.pow(cosi, 2));
        Vector3f hitPointRefl = hitPoint.add(surfaceNormal.mul(Scene.getBias())); //bias in direction of normal
        Vector3f hitPointRefr = hitPoint.add(surfaceNormal.mul(-Scene.getBias())); //bias in direction opposite of normal
        if (c < 0) {
            //incident angle is greater then critical angle:
            //total internal reflection.
            //We don't need to compute refraction direction
            return currentScene.rayTraceWithBVH(new Line3d(hitPointRefl, reflectionDir), rayDepth+1);
        } else {
            double c2 = Math.sqrt(c);
            //compute refraction direction
            refractionDir = incident.mul(eta).add(surfaceNormal.mul(eta*cosi - c2));
            //compute fr using Fresnel equations
            double cost = -refractionDir.dotProduct(surfaceNormal); //cosine of angle of refraction
            assert (cost>0);
            double x11 = ior1*cosi;
            double x22 = ior2*cost;
            double x12 = ior1*cost;
            double x21 = ior2*cosi;
            double rS = Math.pow((x11-x22)/(x11+x22), 2);
            double rP = Math.pow((x12-x21)/(x12+x21), 2);
            fr = (rS+rP)/2;
            Vector3f reflectionColor = currentScene.rayTraceWithBVH(new Line3d(hitPointRefl, reflectionDir), rayDepth+1).mul(fr);
            Vector3f refractionColor = currentScene.rayTraceWithBVH(new Line3d(hitPointRefr, refractionDir), rayDepth+1).mul(1-fr);
            return reflectionColor.add(refractionColor);
        }

    }

    @Override
    public void addTrianglesToScene(Scene currentScene, TriangleMesh triangleMesh) {
        for (TriangleMesh.Triangle triangle :
                triangleMesh.getTriangles()) {
            currentScene.addSceneObject(new MirrorTransparent(triangle, this.ior));
        }
    }

    @Override
    String getTypeName() {
        return "Mirror-transparent";
    }


}
