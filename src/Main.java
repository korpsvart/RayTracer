import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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

        int width = 1280;
        int height = 1024;
        double fieldOfView = 39.6; //in degrees
        Vector3f cameraPosition = new Vector3f(0,0,0);
        BufferedImage img = new BufferedImage(width, height, TYPE_INT_RGB);

        Scene scene = new Scene(width, height, fieldOfView, img, cameraPosition, new Vector3f(0.7f, 0.3f, 0.8f));


        //Sample sphere
        Color color1 = new Color(1f,1f,1f);
        Color color2 = new Color(0.8f,0.6f,0.3f);
        Color color3 = new Color(0.4f,0.9f,0.1f);
        Diffuse diffuseSphere1 = new Diffuse(new Sphere(new Vector3f(1,0,-10), 1));
        MirrorTransparent transparentSphere = new MirrorTransparent(new Sphere(new Vector3f(-0.5, 0.5, -8), 1));
        Diffuse diffuseSphere2 = new Diffuse(new Sphere(new Vector3f(-3, 2, -10), 1.6));
        transparentSphere.setIor(1.5);
        Sphere sphere3 = new Sphere(new Vector3f(-2, -2, -7), 0.6);
        Sphere sphere4 = new Sphere(new Vector3f(0, -2, -7), 0.5);
        Sphere sphere5 = new Sphere(new Vector3f(0, -2, -4), 0.3);
        Sphere sphere6 = new Sphere(new Vector3f(0, -2, -11), 0.6);
        Diffuse diffuseSphere3 = new Diffuse(sphere3);
        Diffuse diffuseSphere4 = new Diffuse(sphere4);
        Diffuse diffuseSphere5 = new Diffuse(sphere5);
        Diffuse diffuseSphere6 = new Diffuse(sphere6);


        Triangle triangle1 = new Triangle(new Vector3f(-1.8,1,-7), new Vector3f(0,1,-7), new Vector3f(-1,1.6,-7));
        Plane3d plane1 = new Plane3d(new Vector3f(0, -1, 0), new Vector3f(0, 1, 0));
        Plane3d plane2 = new Plane3d(new Vector3f(0, -3, 0), new Vector3f(0, 1, 0));
        MirrorTransparent transparentPlane = new MirrorTransparent(plane1);
        Diffuse diffusePlane = new Diffuse(plane2);
        transparentPlane.setIor(1.3);
//        scene.addSceneObject(diffuseSphere1);
//        scene.addSceneObject(transparentSphere);
//        scene.addSceneObject(diffuseSphere2);
//        scene.addSceneObject(diffuseSphere3);
//        scene.addSceneObject(diffuseSphere4);
//        scene.addSceneObject(diffuseSphere5);
//        scene.addSceneObject(diffuseSphere6);
        scene.addSceneObject(transparentPlane);
        scene.addSceneObject(diffusePlane);
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
        BezierSurface33 bezierSurface = new BezierSurface33(controlPoints);
        long start = 0;
        start = System.nanoTime();
        Diffuse triangleMesh = new Diffuse(bezierSurface.triangulate(16));
        long triangulationTime = System.nanoTime() - start;
        scene.addSceneObject(triangleMesh);
        PointLight pointLight1 = new PointLight(color1, 200, new Vector3f(0.5, 0.6, -4.5));
        PointLight pointLight2 = new PointLight(color2, 200, new Vector3f(-0.6, 1.3, -9));
        PointLight pointLight3 = new PointLight(color3, 200, new Vector3f(1, 1.5, -4));
        scene.addPointLight(pointLight1);
        scene.addPointLight(pointLight2);
        scene.addPointLight(pointLight3);

        //calculate render time
        //(note that this is a very bad way of benchmarking)
        start = System.nanoTime();
        scene.render();
        long renderTime = System.nanoTime() - start;

        File outputImg = new File("img.png");
        try {
            ImageIO.write(img, "png", outputImg);
        } catch (IOException e) {
            System.out.println("errore nel salvataggio immagine");
        }

        System.out.println("Rendering time: " + renderTime/10e9); //divide to get seconds
        System.out.println("Ray number: " + width*height); //divide to get seconds
        System.out.println("Triangulation time: " + triangulationTime/10e9);
        System.out.println("Number of ray-triangle tests: " + rayTriangleTests);
        System.out.println("Number of ray-triangle intersections: " + rayTriangleIntersections);
        System.out.println("Hit-ratio: " + (double)rayTriangleIntersections.longValue()/rayTriangleTests.longValue()*100); //hit ratio as percentage
    }

}
