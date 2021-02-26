import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Optional;



public class Scene {

    private static final int MAX_RAY_DEPTH = 5; //max depth of ray tracing recursion

    public static double getBias() {
        return bias;
    }

    private static final double bias = 10e-6; //bias for shadow acne
    private static final double AIR_IOR = 1; //air index of refraction, considered as vacuum for simplicity

    private static boolean backFaceCulling = false;
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

    public void triangulateAndAddSceneObject(SceneObject sceneObject, int divs) {
        TriangleMesh triangleMesh = sceneObject.triangulate(divs);
        sceneObject.addTrianglesToScene(this, triangleMesh);
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
            Optional<Double> interceptT = sO.trace(ray, RayType.PRIMARY);
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

}
