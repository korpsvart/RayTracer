package rendering;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;


public class Scene{

    private int maxRayDepth = 4; //max depth of ray tracing recursion
    private AtomicLong renderingProgress = new AtomicLong(0);
    private static  Vector3f MIN_BOUND = new Vector3f(-10e6, -10e6, -10e6);
    private static  Vector3f MAX_BOUND = new Vector3f(10e6, 10e6, 10e6);


    private static boolean simulateIndirectDiffuse = false;
    private static boolean useEnvironmentLight = false;

    public static boolean isSimulateIndirectDiffuse() {
        return simulateIndirectDiffuse;
    }

    public static double getBias() {
        return bias;
    }

    private static final double bias = 10e-6; //bias for shadow acne
    private static final double AIR_IOR = 1; //air index of refraction
    // , considered as vacuum for simplicity

    private static boolean backFaceCulling = true;
    private int width;
    private int height;
    private final double fieldOfView;

    public void setImg(BufferedImage img) {
        this.img = img;
    }

    private BufferedImage img;
    private List<SceneListener> sceneListenerList = new ArrayList<>();
    private final Camera camera;
    private final Vector3f cameraPosition;
    //we save last camera orientation. Rays are always generated with default position and orientation and then
    //transformed. However, we need to store the last camera orientation to handle successive inputs
    //asking for camera orientation change
    private  Vector3f cameraOrientation = new Vector3f(0, 0, -1); //default orientation
    private Matrix4D cameraToWorld;
    private BVH BVH;
    private ArrayList<SceneObject> sceneObjects;
    private ArrayList<SceneObject> topLevelSceneObjects = new ArrayList<>();
    private ArrayList<SceneObject> nonBVHSceneObjects;
    private ArrayList<SceneObject> sceneObjectsBVH;
    private ArrayList<LightSource> lightSources;
    private EnvironmentLight environmentLight = new EnvironmentLight(new Vector3f(1, 1, 1), 0.3f);
    public ArrayList<SceneObject> getSceneObjects() {
        return sceneObjects;
    }


    public static void setSimulateIndirectDiffuse(boolean simulateIndirectDiffuse) {
        Scene.simulateIndirectDiffuse = simulateIndirectDiffuse;
    }

    public static boolean isUseEnvironmentLight() {
        return useEnvironmentLight;
    }

    public static void setUseEnvironmentLight(boolean useEnvironmentLight) {
        Scene.useEnvironmentLight = useEnvironmentLight;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }



    public ArrayList<SceneObject> getTopLevelSceneObjects() {
        return topLevelSceneObjects;
    }

    public ArrayList<LightSource> getLightSources() {
        return lightSources;
    }

    public BVH getBVH() {
        return BVH;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void removeSceneObject(SceneObject sceneObject) {
        sceneObjects.removeIf((s1) -> s1.equals(sceneObject));
        nonBVHSceneObjects.removeIf((s1) -> s1.equals(sceneObject));
        sceneObjectsBVH.removeIf((s1) -> s1.equals(sceneObject));
        topLevelSceneObjects.removeIf((s1) -> s1.equals(sceneObject));
    }


    public void addLightSource(LightSource lightSource) {
        lightSources.add(lightSource);
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
        this.lightSources = new ArrayList<LightSource>();
        this.sceneObjects = new ArrayList<>();
        this.nonBVHSceneObjects = new ArrayList<>();
        this.sceneObjectsBVH = new ArrayList<>();
        this.camera = new Camera();
        setCameraToWorld(cameraPosition, cameraOrientation);
}

    public Scene(int width, int height, double fieldOfView, BufferedImage img, Vector3f cameraPosition) {
        this.width = width;
        this.height = height;
        this.fieldOfView = fieldOfView;
        this.img = img;
        this.cameraPosition = cameraPosition;
        this.backgroundColor = new Vector3f(0, 0, 0);
        this.lightSources = new ArrayList<LightSource>();
        this.sceneObjects = new ArrayList<>();
        this.nonBVHSceneObjects = new ArrayList<>();
        this.sceneObjectsBVH = new ArrayList<>();
        this.camera = new Camera();
        setCameraToWorld(cameraPosition, cameraOrientation);
    }

    public void addSceneObject(SceneObject sceneObject) {
        if (sceneObject.isUseBVH()) {
            sceneObjectsBVH.add(sceneObject);
        } else {
            nonBVHSceneObjects.add(sceneObject);

        }
        sceneObjects.add(sceneObject);
        if (!(sceneObject.getGeometricObject() instanceof TriangleMesh.Triangle))
            topLevelSceneObjects.add(sceneObject);
    }

    public ArrayList<SceneObject> getSceneObjectsBVH() {
        return sceneObjectsBVH;
    }

    public void triangulateAndAddSceneObject(SceneObject sceneObject) {
        TriangleMesh triangleMesh = sceneObject.triangulate();
        triangleMesh.setSceneObject(sceneObject);
        topLevelSceneObjects.add(sceneObject);
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


    public void render(int divs) {
        //employs screen subdivision parallelism
        renderingProgress.set(0);
        double aspectRatio = (double)width/height;
        double scale = Math.tan(Math.toRadians(fieldOfView)/2); //scaling due to fov

        ExecutorService executor = Executors.newCachedThreadPool();

        int stepX = Math.floorDiv(width, divs);
        int stepY = Math.floorDiv(height, divs);
        for (int i = 0; i < divs; i++) {
            for (int j = 0; j < divs; j++) {
                int x = i*stepX;
                int y = j*stepY;
                BufferedImage subImg = img.getSubimage(x, y, stepX, stepY);
                executor.submit(() -> {
                    screenAreaRendering(subImg,x,y,stepX, stepY,aspectRatio, scale, this, camera, width, height);
                });
            }
            if (height % divs != 0) {
                int x = i*stepX;
                int y = divs*stepY;
                BufferedImage subImg = img.getSubimage(x, y, stepX, height%divs);
                executor.submit(() -> {
                    screenAreaRendering(subImg, x,y,stepX, height%divs,aspectRatio, scale, this, camera, width, height);
                });
            }
        }
        if (width % divs != 0) {
            for (int j = 0; j < divs; j++) {
                int x = stepX*divs;
                int y = j*divs;
                BufferedImage subImg = img.getSubimage(x, y, width%divs, stepY);
                executor.submit(() -> {
                    screenAreaRendering(subImg, x, y, width%divs, stepY,aspectRatio, scale, this, camera, width, height);
                });
            }
            if (height % divs != 0) {
                int x = stepX*divs;
                int y = stepY*divs;
                BufferedImage subImg = img.getSubimage(stepX * divs, stepY*divs, width % divs, height%divs);
                executor.submit(() -> {
                    screenAreaRendering(subImg, x,y,height%divs, height%divs,aspectRatio, scale, this, camera, width, height);
                });
            }
        }
        executor.shutdown();
        try {
            executor.awaitTermination(200, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public Vector3f rayTrace(Line3d ray, int rayDepth) {
        if (rayDepth == maxRayDepth) {
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
        if (rayDepth == maxRayDepth) {
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
        //TODO: check this method
        if (rayDepth == maxRayDepth) {
            if (rayType==RayType.INDIRECT_DIFFUSE) {
                if (useEnvironmentLight) {
                    return environmentLight.getColor().mul(environmentLight.getIntensity());
                } else {
                    return new Vector3f(0,0,0);
                }
            } else {
                return this.backgroundColor;
            }
        }
        Optional<IntersectionDataPlusObject> intersectionDataPlusObject = calculateIntersection(ray, rayType);
        if (intersectionDataPlusObject.isPresent()) {
            return intersectionDataPlusObject.get().getSceneObject().computeColor(intersectionDataPlusObject.get().getIntersectionData(), ray, rayDepth, this);
        } else {
            if (rayType==RayType.INDIRECT_DIFFUSE) {
                if (useEnvironmentLight) {
                    return environmentLight.getColor().mul(environmentLight.getIntensity());
                } else {
                    return new Vector3f(0,0,0);
                }
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
        cameraOrientation = to;

    }

    public BufferedImage getImg() {
        return img;
    }

    public Matrix4D getCameraToWorld() {
        return cameraToWorld;
    }

    protected static void screenAreaRendering(BufferedImage img, int startX, int startY, int w, int h, double aspectRatio, double scale,
                                              Scene currentScene, Camera camera, int totalWidth, int totalHeight) {

        int width = totalWidth;
        int height = totalHeight;
        for (int i = startX; i < w+startX; i++) {
            for (int j = startY; j < h+startY; j++) {
                double x = (2*(i+0.5)/width - 1)*aspectRatio*scale;
                double y = (1-2*(j+0.5)/height)*scale;
                Vector3f rayDirection = new Vector3f(x,y,-1);
                Vector3f rayDirectionWorld = camera.convertToFixedSystem(rayDirection).normalize();
                Line3d ray = new Line3d(camera.getPosition(), rayDirectionWorld);
                Vector3f color = currentScene.rayTraceWithBVH(ray, 0);
                Color color1 = color.vectorToColor();
                img.setRGB(i-startX,j-startY,color1.getRGB());
                currentScene.updateProgress();
            }
        }
    }

    public void removeLightSource(LightSource lightSource) {
        lightSources.remove(lightSource);
    }


    public Vector3f getCameraOrientation() {
        return cameraOrientation;
    }

    public Camera getCamera() {
        return camera;
    }


    public BufferedImage renderForOutput(int divs, int width, int height) {
        renderingProgress.set(0);
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        //employs screen subdivision parallelism
        double aspectRatio = (double)width/height;
        double scale = Math.tan(Math.toRadians(fieldOfView)/2); //scaling due to fov

        ExecutorService executor = Executors.newCachedThreadPool();

        int stepX = Math.floorDiv(width, divs);
        int stepY = Math.floorDiv(height, divs);
        for (int i = 0; i < divs; i++) {
            for (int j = 0; j < divs; j++) {
                int x = i*stepX;
                int y = j*stepY;
                BufferedImage subImg = img.getSubimage(x, y, stepX, stepY);
                executor.submit(() -> {
                    screenAreaRendering(subImg,x,y,stepX, stepY,aspectRatio, scale, this, camera, width, height);
                });
            }
            if (height % divs != 0) {
                int x = i*stepX;
                int y = divs*stepY;
                BufferedImage subImg = img.getSubimage(x, y, stepX, height%divs);
                executor.submit(() -> {
                    screenAreaRendering(subImg, x,y,stepX, height%divs,aspectRatio, scale, this, camera, width , height);
                });
            }
        }
        if (width % divs != 0) {
            for (int j = 0; j < divs; j++) {
                int x = stepX*divs;
                int y = j*divs;
                BufferedImage subImg = img.getSubimage(x, y, width%divs, stepY);
                executor.submit(() -> {
                    screenAreaRendering(subImg, x, y, width%divs, stepY,aspectRatio, scale, this, camera, width ,height);
                });
            }
            if (height % divs != 0) {
                int x = stepX*divs;
                int y = stepY*divs;
                BufferedImage subImg = img.getSubimage(stepX * divs, stepY*divs, width % divs, height%divs);
                executor.submit(() -> {
                    screenAreaRendering(subImg, x,y,height%divs, height%divs,aspectRatio, scale, this, camera, width, height);
                });
            }
        }
        executor.shutdown();
        try {
            executor.awaitTermination(50, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return img;
    }

    private void updateProgress() {
        long progress = renderingProgress.addAndGet(1);
        for (SceneListener sceneListener :
                sceneListenerList) {
            sceneListener.renderingProgressUpdate(progress);
        }
    }

    public void addSceneListener(SceneListener sceneListener) {
        sceneListenerList.add(sceneListener);
    }

    public int getMaxRayDepth() {
        return maxRayDepth;
    }

    public void setMaxRayDepth(int maxRayDepth) {
        this.maxRayDepth = maxRayDepth;
    }
}