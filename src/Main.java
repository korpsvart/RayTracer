import com.sun.jdi.Mirror;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicLong;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public class Main {

    public static AtomicLong getRayTriangleTests() {
        return rayTriangleTests;
    }

    public static AtomicLong getRayTriangleIntersections() {
        return rayTriangleIntersections;
    }

    //variables for evaluating performance
    //and acceleration structures
    private static AtomicLong rayTriangleTests = new AtomicLong(0);
    private static AtomicLong rayTriangleIntersections = new AtomicLong(0);

    private static BezierSurface33[] testC1Patches() {
        Vector3f[] dataPoints1 = new Vector3f[]{
                new Vector3f(0, 0, -5),
                new Vector3f(0.5, 0.3, -5),
                new Vector3f(1, 0.1, -5)
        };
        Vector3f[] dataPoints2 = new Vector3f[] {
                new Vector3f(0.2, 1.3, -5.2),
                new Vector3f(0.5, 1.1, -5.2),
                new Vector3f(1, 1.3, -5.2)
        };
        Vector3f[] tangent = new Vector3f[] {
                new Vector3f(1, 1, 0),
                new Vector3f(1, 0, 0),
                new Vector3f(1, -1, 0)
        };
        Vector3f[] tangent2 = new Vector3f[] {
                new Vector3f(1, -2, 0),
                new Vector3f(1, 1, 0),
                new Vector3f(1, -2, 0)
        };

        Vector3f[] tangent3 = new Vector3f[]{
                new Vector3f(-0.2, 1, 0),
                new Vector3f(0.3, 1, 0)
        };

        BezierSpline3 spline1 = BezierSpline3.piecewiseCubicInterpolation(dataPoints1, tangent);
        BezierSpline3 spline2 = BezierSpline3.piecewiseCubicInterpolation(dataPoints2, tangent2);
        BezierSpline3 spline3 = BezierSpline3.piecewiseCubicInterpolation(new Vector3f[]{dataPoints1[0], dataPoints2[0]}, tangent3);
        BezierSpline3 spline4 = BezierSpline3.piecewiseCubicInterpolation(new Vector3f[]{dataPoints1[1], dataPoints2[1]}, tangent3);
        BezierSpline3 spline5 = BezierSpline3.piecewiseCubicInterpolation(new Vector3f[]{dataPoints1[2], dataPoints2[2]}, tangent3);
        BezierSurface33 surface1 = BezierSurface33.fillWithCoons(new BezierCurve3[]{
                spline1.getCurves()[0],
                spline2.getCurves()[0],
                spline3.getCurves()[0],
                spline4.getCurves()[0]
        });
        BezierSurface33 surface2 = BezierSurface33.fillWithCoons(new BezierCurve3[]{
                spline1.getCurves()[1],
                spline2.getCurves()[1],
                spline4.getCurves()[0],
                spline5.getCurves()[0]
        });
        boolean g1 = surface1.checkG1(surface2);
        return new BezierSurface33[]{surface1, surface2};
    }

    public static BSurface testBSSurface() {
        int p = 3;
        int q = 3;
        Vector3f[][] controlPointsSurface = {
                {
                        new Vector3f(-1, -1, -4),
                        new Vector3f(-0.9, -0.8, -4.2),
                        new Vector3f(-0.8, -0.6, -4.2),
                        new Vector3f(-0.8, -0.2, -4.3),
                        new Vector3f(-0.7, 0, -4.2)
                },
                {
                        new Vector3f(-0.5, -1.2, -4.5),
                        new Vector3f(-0.4, -0.9, -4.6),
                        new Vector3f(-0.43, -0.7, -4.6),
                        new Vector3f(-0.38, -0.3, -4.5),
                        new Vector3f(-0.34, -0.1, -4.7)
                },
                {
                        new Vector3f(-0.2, -1, -4.1),
                        new Vector3f(-0.25, -0.8, -4.9),
                        new Vector3f(-0.27, -0.5, -4.7),
                        new Vector3f(-0.21, -0.4, -4.9),
                        new Vector3f(-0.18, -0.2, -5)
                },
                {
                        new Vector3f(0, -1, -4.5),
                        new Vector3f(0.1, -0.7, -4.6),
                        new Vector3f(-0.1, -0.5, -4.2),
                        new Vector3f(0, -0.2, -4.1),
                        new Vector3f(0.1, 0, -4.3)
                },
                {
                        new Vector3f(0.3, -1.3, -4.2),
                        new Vector3f(0.4, -1, -4.8),
                        new Vector3f(0.4, -0.8, -4.3),
                        new Vector3f(0.6, -0.5, -4.0),
                        new Vector3f(0.5, -0.2, -4.1)
                },
        };
        double[] knotsU = {0, 0, 0, 0, 0.5, 1, 1, 1, 1};
        double[] knotsV = knotsU.clone();
        Matrix4D objectToWorld = new Matrix4D(Matrix3D.identity, new Vector3f(0, 1.4, -1));
        return new BSurface(controlPointsSurface, knotsU, knotsV, p, q, objectToWorld);
    }


    public static void main(String[] args) throws Exception {

        //We project rays through the center of each pixel
        //If ray intersect with sphere, we color that pixel
        //Camera is placed at the origin, facing direction of negative z
        //(0, 0, -1)
        //Screen is placed at z=-1

        int width = 1024;
        int height = 768;
        double fieldOfView = 39.6; //in degrees
        Vector3f cameraPosition = new Vector3f(0,0,0);
        BufferedImage img = new BufferedImage(width, height, TYPE_INT_RGB);

        Scene scene = new Scene(width, height, fieldOfView, img, cameraPosition, new Vector3f(0.1f, 0.3f, 0.6f));


        //Sample sphere
        Color color1 = new Color(1f,1f,1f);
        Color color2 = new Color(0.8f,0.6f,0.3f);
        Color color3 = new Color(0.4f,0.9f,0.1f);
        Diffuse diffuseSphere1 = new Diffuse(new Sphere(new Vector3f(0.3,0,-4), 0.2));
        diffuseSphere1.setAlbedo(new Vector3f(0, 0.2, 0.9));
        MirrorTransparent transparentSphere = new MirrorTransparent(new Sphere(new Vector3f(-0.5, 0.5, -8), 1));
        Diffuse diffuseSphere2 = new Diffuse(new Sphere(new Vector3f(1, 0.2, -8), 1.2));
        transparentSphere.setIor(1.5);
        Sphere sphere3 = new Sphere(new Vector3f(2.6, 0.3, -7), 0.6);
        Sphere sphere4 = new Sphere(new Vector3f(0, -2, -7), 0.5);
        Sphere sphere5 = new Sphere(new Vector3f(0, -2, -4), 0.3);
        Sphere sphere6 = new Sphere(new Vector3f(0, -2, -11), 0.6);
        Diffuse diffuseSphere3 = new Diffuse(sphere3);
        Diffuse diffuseSphere4 = new Diffuse(sphere4);
        Diffuse diffuseSphere5 = new Diffuse(sphere5);
        Diffuse diffuseSphere6 = new Diffuse(sphere6);


        Plane3d plane1 = new Plane3d(new Vector3f(0, -1, 0), new Vector3f(0, 1, 0));
        Plane3d plane2 = new Plane3d(new Vector3f(0, -1, 0), new Vector3f(0, 1, 0));
        MirrorTransparent transparentPlane = new MirrorTransparent(plane1);
        Diffuse diffusePlane = new Diffuse(plane2);
        transparentPlane.setIor(1.3);
        scene.addSceneObject(diffuseSphere1);
//        scene.addSceneObject(transparentSphere);
        diffuseSphere2.setAlbedo(new Vector3f(0.7, 0, 0));
//        scene.addSceneObject(diffuseSphere2);
//        scene.addSceneObject(diffuseSphere3);
//        scene.addSceneObject(diffuseSphere4);
//        scene.addSceneObject(diffuseSphere5);
//        scene.addSceneObject(diffuseSphere6);
//        scene.addSceneObject(transparentPlane);
//        scene.addSceneObject(diffusePlane);
//        scene.addSceneObject(triangle1);
        Vector3f[][] controlPoints = {
                new Vector3f[] { new Vector3f(0, 0, -5),
                        new Vector3f(0, 0.5, -5),
                        new Vector3f(0.1, 0.8, -5),
                        new Vector3f(0, 1, -5),},

                       new Vector3f[] {                new Vector3f(0.3, 0, -5.5),
                               new Vector3f(0.3, 0.6, -5.2),
                               new Vector3f(0.4, 0.7, -5.5),
                               new Vector3f(0.3, 0.9, -5.5),
                       },
                new Vector3f[] {
                        new Vector3f(0.7, 0.4, -5.7),
                        new Vector3f(0.5, 0.5, -5.3),
                        new Vector3f(0.7, 0.8, -5.7),
                        new Vector3f(0.8, 1, -5.5),
                },
                new Vector3f[] {
                        new Vector3f(1, 0.2, -5.7),
                        new Vector3f(1, 0.4, -5.5),
                        new Vector3f(1.1, 0.7, -5.3),
                        new Vector3f(0.9, 1.2, -5.8)
                }
        };

//        BezierSurface33 bezierSurface = new BezierSurface33(controlPoints);
//        BezierSurface33 bezierSurface = BezierSurface33.fillWithCoons(controlPoints);
//        BezierPatchesData teaPotData = BezierPatchesData.createTeapot();
//        Matrix4D teaPotOTW = BezierPatchesData.getTeapotOTW();
//        Matrix4D teaPotCTW = BezierPatchesData.getTeapotCTW();
////        scene.setCameraToWorld(teaPotCTW);
//        BezierSurface33[] teaPotPatches = teaPotData.getSurfaces(teaPotOTW);
//        for (BezierSurface33 patch :
//                teaPotPatches) {
//            Diffuse diffusePatch = new Diffuse(patch);
//            diffusePatch.setAlbedo(new Vector3f(0.3, 0, 0.51));
//            scene.triangulateAndAddSceneObject(diffusePatch, 16);
//        }
//        Diffuse transparentBezier = new Diffuse(bezierSurface);
//        transparentBezier.setAlbedo(new Vector3f(0.2, 0.5, 0.1));
////        transparentBezier.setIor(1.5);
//        scene.triangulateAndAddSceneObject(transparentBezier, 16);
//        BezierSurface33[] patches = testC1Patches();
//        for (BezierSurface33 patch :
//                patches) {
//            Diffuse diffusePatch = new Diffuse(patch);
//            diffusePatch.setAlbedo(new Vector3f(0.3, 0, 0.51));
//            scene.triangulateAndAddSceneObject(diffusePatch, 16);
//        }
        //Testing B-Spline surfaces
        BSurface bSurface = testBSSurface();
        Diffuse diffuseSurface = new Diffuse(bSurface);
        diffuseSurface.setAlbedo(new Vector3f(0.3, 0, 0.51));
        scene.triangulateAndAddSceneObject(diffuseSurface, 12);
        PointLight pointLight1 = new PointLight(color1, 200, new Vector3f(0.5, 0.6, -4.5));
        PointLight pointLight2 = new PointLight(color2, 200, new Vector3f(-0.6, 1.3, -9));
        PointLight pointLight3 = new PointLight(color1, 150, new Vector3f(0.7, 0, -3));
        Matrix3D lightRotation = new Matrix3D(new double[][] {
            {0,0,-1},
            {0,0,1},
            {0, 0,-1}
        });
        DistantLight distantLight1 = new DistantLight(color1, 10, new Matrix4D(lightRotation, new Vector3f(0, 0, 0)));
//        scene.addPointLight(pointLight1);
//        scene.addPointLight(pointLight2);
        scene.addLightSource(pointLight3);
//        scene.addLightSource(distantLight1);

        //create BVH
        //Set minBound and maxBound to contain whole renderable scene

        Vector3f minBound = new Vector3f(-1000, -1000, -1000);
        Vector3f maxBound = new Vector3f(1000, 1000, 1000);
        scene.setBVH();
        long start = 0;
        start = System.nanoTime();
        scene.render(20);
        long renderTime = System.nanoTime() - start;


        Visualizer visualizer = new Visualizer(scene);

        System.out.println("Ray number: " + width*height); //divide to get seconds
        System.out.println("Rendering time: " + renderTime/10e9);
        System.out.println("Number of ray-triangle tests: " + rayTriangleTests);
        System.out.println("Number of ray-triangle intersections: " + rayTriangleIntersections);
        System.out.println("Hit-ratio: " + (double)rayTriangleIntersections.longValue()/rayTriangleTests.longValue()*100); //hit ratio as percentage
    }



}
