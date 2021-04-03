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

        Color color1 = new Color(1f,1f,1f);
        Color color2 = new Color(0.8f,0.6f,0.3f);
        Color color3 = new Color(0.4f,0.9f,0.1f);


        Plane3d plane1 = new Plane3d(new Vector3f(0, -1, 0), new Vector3f(0, 1, 0));
        Plane3d plane2 = new Plane3d(new Vector3f(0, -1, 0), new Vector3f(0, 1, 0));
        MirrorTransparent transparentPlane = new MirrorTransparent(plane1);
        Diffuse diffusePlane = new Diffuse(plane2);
        transparentPlane.setIor(1.3);

//        BezierPatchesData teaPot = SampleShapes.getTeapot();
//        BezierSurface33[] teaPotPatches = teaPot.getSurfaces();
//        for (BezierSurface33 patch :
//                teaPotPatches) {
//            Diffuse diffusePatch = new Diffuse(patch);
//            diffusePatch.setAlbedo(new Vector3f(0.3, 0, 0.51));
//            scene.triangulateAndAddSceneObject(diffusePatch, 8);
//        }
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

        //test parallelepiped rendering
        Matrix4D boxToWorld = new Matrix4D(
                Matrix3D.rotationAroundArbitraryAxis(30, new Vector3f(1,1,1)), new Vector3f(-0.6, 0.8, -5)
        );
        PhysicalBox box = new PhysicalBox(new Vector3f(0, 0, 0), new Vector3f(0.5, 0.7, -0.7), boxToWorld);
        Diffuse diffuseBox = new Diffuse(box);
        diffuseBox.setAlbedo(new Vector3f(0.5, 0, 0.2));
        scene.triangulateAndAddSceneObject(diffuseBox, -1); //divs makes no sense here


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
