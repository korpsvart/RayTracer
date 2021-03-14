import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Optional;



public class Scene {

    private static final int MAX_RAY_DEPTH = 3; //max depth of ray tracing recursion
    private static  Vector3f MIN_BOUND = new Vector3f(-10e6, -10e6, -10e6);
    private static  Vector3f MAX_BOUND = new Vector3f(10e6, 10e6, 10e6);
    private static boolean SIMULATE_INDIRECT_DIFFUSE = true;
    private static boolean USE_ENVIRONMENT_LIGHT = true;

    public static boolean isSimulateIndirectDiffuse() {
        return SIMULATE_INDIRECT_DIFFUSE;
    }

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
    private Matrix4D cameraToWorld;
    private BVH BVH;
    private ArrayList<SceneObject> sceneObjects;
    private ArrayList<SceneObject> nonBVHSceneObjects;
    private ArrayList<SceneObject> sceneObjectsBVH;
    private ArrayList<PointLight> pointLights;
    private EnvironmentLight environmentLight = new EnvironmentLight(new Color(1f, 1f, 1f), 0.3f);

    public ArrayList<SceneObject> getSceneObjects() {
        return sceneObjects;
    }

    public ArrayList<PointLight> getPointLights() {
        return pointLights;
    }

    public BVH getBVH() {
        return BVH;
    }



    public void addPointLight(PointLight pointLight) {
        pointLights.add(pointLight);
    }

    private final Vector3f backgroundColor; //default to black

    public static boolean isBackFaceCulling() {
        return backFaceCulling;
    }

    public void setCameraToWorld(Matrix4D cameraToWorld) {
        this.cameraToWorld = cameraToWorld;
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
        this.nonBVHSceneObjects = new ArrayList<>();
        this.sceneObjectsBVH = new ArrayList<>();
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
        this.nonBVHSceneObjects = new ArrayList<>();
        this.sceneObjectsBVH = new ArrayList<>();
        setCameraToWorld(cameraPosition, new Vector3f(0, 0, -1));
    }

    public void addSceneObject(SceneObject sceneObject) {
        if (sceneObject.isUseBVH()) {
            sceneObjectsBVH.add(sceneObject);
        } else {
            nonBVHSceneObjects.add(sceneObject);
        }
        sceneObjects.add(sceneObject);
    }

    public ArrayList<SceneObject> getSceneObjectsBVH() {
        return sceneObjectsBVH;
    }

    public void triangulateAndAddSceneObject(SceneObject sceneObject, int divs) {
        TriangleMesh triangleMesh = sceneObject.triangulate(divs);
        sceneObject.addTrianglesToScene(this, triangleMesh);
    }

    public void setBVH() {
        //this should be called for each rendering if the scene is not static
        this.BVH = new BVH(this, Scene.MIN_BOUND, Scene.MAX_BOUND);
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
                Vector3f color = rayTraceWithBVH(ray, 0);
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
            return objectFound.computeColor(intersectionDataMin,ray,rayDepth,this);
        } else {
            return backgroundColor;
        }
    }


    public Vector3f rayTraceWithBVH(Line3d ray, int rayDepth) {
        if (rayDepth > MAX_RAY_DEPTH) {
            return this.backgroundColor;
        }
        Optional<IntersectionDataPlusObject> intersectionDataPlusObject = calculateIntersection(ray, RayType.PRIMARY);
        if (intersectionDataPlusObject.isPresent()) {
            return intersectionDataPlusObject.get().getSceneObject().computeColor(intersectionDataPlusObject.get().getIntersectionData(), ray, rayDepth, this);
        } else {
            return backgroundColor;
        }
    }

    public Vector3f rayTraceWithBVH(Line3d ray, int rayDepth, RayType rayType) {
        //same as above but allow caller to specify ray type
        if (rayDepth > MAX_RAY_DEPTH) {
            if (USE_ENVIRONMENT_LIGHT && rayType==RayType.INDIRECT_DIFFUSE) {
                return Vector3f.colorToVector(environmentLight.getColor()).mul(environmentLight.getIntensity());
            } else {
                return this.backgroundColor;
            }
        }
        Optional<IntersectionDataPlusObject> intersectionDataPlusObject = calculateIntersection(ray, rayType);
        if (intersectionDataPlusObject.isPresent()) {
            return intersectionDataPlusObject.get().getSceneObject().computeColor(intersectionDataPlusObject.get().getIntersectionData(), ray, rayDepth, this);
        } else {
            if (USE_ENVIRONMENT_LIGHT && rayType==RayType.INDIRECT_DIFFUSE) {
                return Vector3f.colorToVector(environmentLight.getColor()).mul(environmentLight.getIntensity());
            } else {
                return this.backgroundColor;
            }
        }
    }

    public Optional<IntersectionDataPlusObject> calculateIntersection(Line3d ray, RayType rayType) {
        double interceptMin = Double.POSITIVE_INFINITY;
        SceneObject objectFound = null;
        IntersectionData intersectionDataMin = null;
        //intersect using BVH
        Optional<IntersectionDataPlusObject> intersectionDataPlusObject = this.BVH.intersect(ray, rayType);
        if (intersectionDataPlusObject.isPresent()) {
            interceptMin = intersectionDataPlusObject.get().getT();
            objectFound = intersectionDataPlusObject.get().getSceneObject();
            intersectionDataMin = intersectionDataPlusObject.get().getIntersectionData();
        }
        //intersect non-BVH objects
        for (SceneObject sO:
                nonBVHSceneObjects) {
            Optional<IntersectionData> interceptT = sO.trace(ray, rayType);
            if (interceptT.isPresent() && (interceptT.get().getT() < interceptMin)) {
                objectFound = sO;
                interceptMin = interceptT.get().getT();
                intersectionDataMin = interceptT.get();
            }
        }
        if (objectFound != null) {
            return Optional.of(new IntersectionDataPlusObject(intersectionDataMin, objectFound));
        } else {
            return Optional.empty();
        }
    }

    public boolean checkVisibility(Line3d ray, double maxDistance, SceneObject caller) {
        //similar to calculateIntersection
        //but used to check visibility for diffuse objects
        //thus it can be made more efficient by stopping as
        //soon as we find an object with t < maxDistance
        //and returning only a boolean value
        if (!BVH.checkVisibility(ray, maxDistance, caller)) {
            return false;
        }
        for (SceneObject sO :
                nonBVHSceneObjects) {
            if (sO != caller) {
                Optional<IntersectionData> interceptT = sO.trace(ray, RayType.SHADOW);
                if (interceptT.isPresent() && (interceptT.get().getT() < maxDistance)) {
                    return false;
                }
            }
        }
        return true;
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