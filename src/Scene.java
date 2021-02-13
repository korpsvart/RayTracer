import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Optional;



public class Scene {

    private static final int MAX_RAY_DEPTH = 10; //max depth of ray tracing recursion
    private static final double bias = 10e-6; //bias for shadow acne
    private static final double AIR_IOR = 1; //air index of refraction, considered as vacuum for simplicity

    private static boolean backFaceCulling = true;
    private final int width;
    private final int height;
    private final double fieldOfView;
    private final BufferedImage img;
    private final Vector3f cameraPosition;
    private ArrayList<SceneObject> sceneObjects;
    private ArrayList<PointLight> pointLights;

    public void addPointLight(PointLight pointLight) {
        pointLights.add(pointLight);
    }

    private final Vector3f backgroundColor; //default to black

    public static boolean isBackFaceCulling() {
        return backFaceCulling;
    }

    public Scene(int width, int height, double fieldOfView, BufferedImage img, Vector3f cameraPosition, Vector3f backgroundColor) {
        this.width = width;
        this.height = height;
        this.fieldOfView = fieldOfView;
        this.img = img;
        this.cameraPosition = cameraPosition;
        this.backgroundColor = backgroundColor;
        this.pointLights = new ArrayList<>();
        this.sceneObjects = new ArrayList<>();
    }

    public Scene(int width, int height, double fieldOfView, BufferedImage img, Vector3f cameraPosition) {
        this.width = width;
        this.height = height;
        this.fieldOfView = fieldOfView;
        this.img = img;
        this.cameraPosition = cameraPosition;
        this.backgroundColor = new Vector3f(0, 0, 0);
        this.pointLights = new ArrayList<>();
        this.sceneObjects = new ArrayList<>();
    }

    public void addSceneObject(SceneObject sceneObject) {
        sceneObjects.add(sceneObject);
    }

    public void render() {
        double aspectRatio = (double)width/height;
        double scale = Math.tan(Math.toRadians(fieldOfView)/2); //scaling due to fov
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                double x = (2*(i+0.5)/width - 1)*aspectRatio*scale;
                double y = (1-2*(j+0.5)/height)*scale;
                Vector3f rayDirection = new Vector3f(x,y,-1);
                Line3d ray = new Line3d(cameraPosition, rayDirection);
                Vector3f color = rayTrace(ray, 0); //default ior is that of air
                //which can be considered as vacuum for simplicity
                Color color1 = color.vectorToColor();
                img.setRGB(i,j,color1.getRGB());
            }
        }
    }

    public Vector3f rayTrace(Line3d ray, int rayDepth) {
        if (rayDepth > MAX_RAY_DEPTH) {
            return this.backgroundColor;
        }
        double interceptMin = Double.POSITIVE_INFINITY;
        SceneObject objectFound = null;
        boolean found = false;
        for (SceneObject sO: sceneObjects
        ) {
            Optional<Double> interceptT = sO.rayIntersection(ray);
            if (interceptT.isPresent() && (interceptT.get() < interceptMin)) {
                found = true;
                objectFound = sO;
                interceptMin = interceptT.get();
            }
        }
        if (found) {
            Vector3f hitPoint = ray.getPoint().add(ray.getDirection().mul(interceptMin));
            Vector3f surfaceNormal = objectFound.getSurfaceNormal(hitPoint);
            //add bias for shadow acne in direction of surface normal
            if (objectFound.isDiffuse()) {
                //shading perfect diffuse surfaces using albedo, light intensity and cosine
                //Do this for each light in the scene
                //and sum all the contributions
                //First, verify there's no obstacle between point and light!
                hitPoint = hitPoint.add(surfaceNormal.mul(bias)); //bias in direction of normal
                double objectAlbedo = objectFound.getAlbedo();
                Vector3f finalColor = new Vector3f(0f,0f,0f);
                for (PointLight pLight:
                     pointLights) {
                    Vector3f lDir = hitPoint.moveTo(pLight.getPosition());
                    double distance = lDir.magnitude();
                    lDir = lDir.normalize();
                    Line3d shadowRay = new Line3d(hitPoint, lDir);
                    found = false;
                    for (SceneObject sO :
                            sceneObjects) {
                        if (!objectFound.equals(sO) && !objectFound.isDiffuse()) { //fix later by replacing with isReflectionRefraction
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
                            Optional<Double> tIntecept = sO.rayIntersection(shadowRay);
                            if (tIntecept.isPresent() && tIntecept.get() < distance) {
                                found = true;
                            }
                        }
                    }
                    if (!found) {
                        //compute color
                        //(now using square rolloff)
                        double facingRatio = Math.max(0, surfaceNormal.dotProduct(lDir)); //we still need this
                        Vector3f lightColor = Vector3f.colorToVector(pLight.getColor());
                        double intensity = pLight.getIntensity() * facingRatio / Math.pow(distance, 2);
                        lightColor = lightColor.mul(intensity);
                        finalColor = finalColor.add(lightColor);
                    }
                }
                finalColor = finalColor.mul(objectAlbedo/(4*Math.pow(Math.PI, 2)));
                return finalColor;
            } else {
                //compute reflection and refraction
                double ior2 = objectFound.getIor();
                return reflectionRefraction(hitPoint, ray.getDirection(), surfaceNormal, AIR_IOR, ior2, rayDepth);
            }
        } else {
            return backgroundColor;
        }

    }




    public void render2(Sphere sphere, Color color) {
        //Not working!!
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                double u = (double) i/width;
                double v = (double) j/height;
                double x = (u-0.5)*width;
                double y = (v-0.5)*height;
                Vector3f rayDirection = new Vector3f(x,y,-1);
                Line3d ray = new Line3d(cameraPosition, rayDirection);
                Optional<Vector3f> intercept = ray.sphereRayIntersectV2(sphere);
                if (intercept.isPresent()) {
                    img.setRGB(i, j, color.getRGB());
                }
            }
        }
    }

    public Vector3f reflectionRefraction(Vector3f hitPoint, Vector3f incident, Vector3f surfaceNormal, double ior1, double ior2, int rayDepth) {
        //compute refraction and reflection

        //compute ratio of reflected and refracted light
        //using fresnel equation

        Vector3f reflectionDir = surfaceNormal.mul(incident.dotProduct(surfaceNormal)*-2).add(incident);
        Vector3f refractionDir;
        double fr; //ratio of reflected light
        double c1 = incident.dotProduct(surfaceNormal);
        if (c1 < 0) {
            c1 = -c1;
        } else {
            //we are going outside the object
            //reverse normal
            //(dont invert c1 cause its already positive)
            //Also swap indexes of refraction!
            double temp = ior1;
            ior1=ior2;
            ior2=temp;
            surfaceNormal = surfaceNormal.mul(-1);
        }
        double eta = ior1 / ior2;
        double c = 1 - Math.pow(ior1/ior2, 2)*(1-Math.pow(c1, 2));
        Vector3f hitPointRefl = hitPoint.add(surfaceNormal.mul(bias)); //bias in direction of normal
        Vector3f hitPointRefr = hitPoint.add(surfaceNormal.mul(-bias)); //bias in direction opposite of normal
        if (c < 0) {
            //incident angle is greater then critical angle:
            //total internal reflection.
            //We don't need to compute refraction direction
            return rayTrace(new Line3d(hitPointRefl, reflectionDir), rayDepth+1);
        } else {
            double c2 = Math.sqrt(c);
            //compute refraction direction
            refractionDir = incident.mul(eta).add(surfaceNormal.mul(eta*c1 - c2));
            //compute fr using Fresnel equations
            double cosRefr = -refractionDir.dotProduct(surfaceNormal); //cosine of angle of refraction
            assert (cosRefr>0);
            double x12 = ior1*cosRefr;
            double x21 = ior2*c1;
            double fr_parallel = Math.pow((x21-x12)/(x21+x12), 2);
            double fr_perpendicular = Math.pow((x12-x21)/(x12+x21), 2);
            fr = (fr_parallel+fr_perpendicular)/2;
            Vector3f reflectionColor = rayTrace(new Line3d(hitPointRefl, reflectionDir), rayDepth+1).mul(fr);
            Vector3f refractionColor = rayTrace(new Line3d(hitPointRefr, refractionDir), rayDepth+1).mul(1-fr);
            return reflectionColor.add(refractionColor);
        }

    }

/*    //useless
    //replaced by the method above
    public Optional<Vector3f> computeRefaction(Vector3f incident, Vector3f surfaceNormal, double ior1, double ior2) {
        //ior1 = index of refraction of first material
        //ior2 = index of refraction of second material (entered by light)
        //return empty if no refraction
        double eta = ior1 / ior2;
        double c1 = incident.dotProduct(surfaceNormal);
        if (c1 < 0) {
            c1 = -c1;
        } else {
            //we are going outside the object
            //reverse normal
            //(dont invert c1 cause its already positive)
            surfaceNormal = surfaceNormal.mul(-1);
        }
        double c = 1 - Math.pow(ior1/ior2, 2)*(1-Math.pow(c1, 2));
        if (c < 0) {
            //incident angle is greater then critical angle
            return Optional.empty();
        }
        double c2 = Math.sqrt(c);
        Vector3f t = incident.mul(eta).add(surfaceNormal.mul(eta*c1 - c2));
        return Optional.of(t);
    }*/



/*    public double getIntensity(Sphere sphere) {
        for (LightSource lS :
                lightSources) {
            double intensity = lS.getIntensity();
            double position = lS.getPosition();

        }
    }*/
}
