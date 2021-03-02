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

    private static boolean backFaceCulling = true;
    private final int width;
    private final int height;
    private final double fieldOfView;
    private final BufferedImage img;
    private final Vector3f cameraPosition;
    private Matrix4D cameraToWorld;

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
        setCameraToWorld(cameraPosition, new Vector3f(0, 0, -1));
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
        setCameraToWorld(cameraPosition, new Vector3f(0, 0, -1));
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
        Vector3f cameraPositionWorld = this.cameraToWorld.transformVector(cameraPosition);
        Matrix3D cTWForVectors = cameraToWorld.getA();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                double x = (2*(i+0.5)/width - 1)*aspectRatio*scale;
                double y = (1-2*(j+0.5)/height)*scale;
                Vector3f rayDirection = new Vector3f(x,y,-1);
                Vector3f rayDirectionWorld = cTWForVectors.transformVector(rayDirection).normalize();
                Line3d ray = new Line3d(cameraPositionWorld, rayDirectionWorld);
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
        IntersectionData intersectionDataMin = null;
        SceneObject objectFound = null;
        for (SceneObject sO: sceneObjects
        ) {
            Optional<IntersectionData> interceptT = sO.trace(ray, RayType.PRIMARY);
            if (interceptT.isPresent() && (interceptT.get().getT() < interceptMin)) {
                objectFound = sO;
                interceptMin = interceptT.get().getT();
                intersectionDataMin = interceptT.get();
            }
        }
        if (objectFound != null) {
//            Vector3f hitPoint = ray.getPoint().add(ray.getDirection().mul(interceptMin));
            return objectFound.computeColor(intersectionDataMin,ray,rayDepth,this);
        } else {
            return backgroundColor;
        }
    }

    public void setCameraToWorld(Vector3f from, Vector3f to) {
        Vector3f aux = new Vector3f(0, 1, 0);

        Vector3f forward = from.add(to.mul(-1)).normalize();
        Vector3f right = aux.normalize().crossProduct(forward);
        Vector3f up = forward.crossProduct(right);
        Vector3f translation = from;

        this.cameraToWorld = new Matrix4D(new Matrix3D(new Vector3f[]{right, up, forward}, Matrix3D.COL_VECTOR), translation);

    }

    public BufferedImage getImg() {
        return img;
    }

    public Matrix4D getCameraToWorld() {
        return cameraToWorld;
    }
}