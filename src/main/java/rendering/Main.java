package rendering;

import gui.*;

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



    public static void main(String[] args) throws Exception {

        //We project rays through the center of each pixel
        //If ray intersect with sphere, we color that pixel
        //Camera is placed at the origin, facing direction of negative z
        //(0, 0, -1)
        //Screen is placed at z=-1

        int width = 1024;
        int height = 650;
        double fieldOfView = 39.6; //in degrees
        Vector3f cameraPosition = new Vector3f(0,0,0);
        BufferedImage img = new BufferedImage(width, height, TYPE_INT_RGB);

        Scene scene = new Scene(width, height, fieldOfView, img, cameraPosition, new Vector3f(0.1f, 0.3f, 0.6f));

        Vector3f color1 = new Vector3f(1,1,1);
        Vector3f color2 = new Vector3f(0.8,0.6,0.3);


        Plane3d plane1 = new Plane3d(new Vector3f(0, -1, 0), new Vector3f(0, 1, 0));
        Plane3d plane2 = new Plane3d(new Vector3f(0, -1, 0), new Vector3f(0, 1, 0));
        MirrorTransparent transparentPlane = new MirrorTransparent(plane1);
        transparentPlane.setIor(1.3);


        PointLight pointLight3 = new PointLight(color1, 150, new Vector3f(0, 0, 0));
        Matrix3D lightRotation = new Matrix3D(new double[][] {
            {0,0,-1},
            {0,0,1},
            {0, 0,-1}
        });
        scene.addLightSource(pointLight3);

        scene.setBVH();
        long start = 0;
        start = System.nanoTime();
        scene.render(20);
        long renderTime = System.nanoTime() - start;


        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Visualizer(scene);
            }
        });
//        Visualizer visualizer = new Visualizer(scene);

        System.out.println("Ray number: " + width*height); //divide to get seconds
        System.out.println("Rendering time: " + renderTime/10e9);
        System.out.println("Number of ray-triangle tests: " + rayTriangleTests);
        System.out.println("Number of ray-triangle intersections: " + rayTriangleIntersections);
        System.out.println("Hit-ratio: " + (double)rayTriangleIntersections.longValue()/rayTriangleTests.longValue()*100); //hit ratio as percentage
    }



}
