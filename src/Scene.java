import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Optional;



public class Scene {

    private static final int MAX_RAY_DEPTH = 10; //max depth of ray tracing recursion

    public static double getBias() {
        return bias;
    }

    private static final double bias = 10e-6; //bias for shadow acne
    private static final double AIR_IOR = 1; //air index of refraction, considered as vacuum for simplicity

    private static boolean backFaceCulling = true;
    private final int width;
    private final int height;
    private final double fieldOfView;
    private final BufferedImage img;
    private final Vector3f cameraPosition;

    public ArrayList<SceneObject> getSceneObjects() {
        return sceneObjects;
    }

    public ArrayList<PointLight> getPointLights() {
        return pointLights;
    }

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
        for (SceneObject sO: sceneObjects
        ) {
            Optional<Double> interceptT = sO.rayIntersection(ray);
            if (interceptT.isPresent() && (interceptT.get() < interceptMin)) {
                objectFound = sO;
                interceptMin = interceptT.get();
            }
        }
        if (objectFound != null) {
            Vector3f hitPoint = ray.getPoint().add(ray.getDirection().mul(interceptMin));
            return objectFound.computeColor(hitPoint,ray,rayDepth,this);
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
