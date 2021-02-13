import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public class Main {


    public static void main(String[] args) throws Exception {

        //We project rays through the center of each pixel
        //If ray intersect with sphere, we color that pixel
        //Camera is placed at the origin, facing direction of negative z
        //(0, 0, -1)
        //Screen is placed at z=-1

        int width = 3840;
        int height = 2160;
        double fieldOfView = 39.6; //in degrees
        Vector3f cameraPosition = new Vector3f(0,0,0);
        BufferedImage img = new BufferedImage(width, height, TYPE_INT_RGB);

        Scene scene = new Scene(width, height, fieldOfView, img, cameraPosition, new Vector3f(0.7f, 0.3f, 0.8f));

        //Sample sphere
        Color color1 = new Color(1f,1f,1f);
        Color color2 = new Color(0.8f,0.6f,0.3f);
        Color color3 = new Color(0.4f,0.9f,0.1f);
        Sphere sphere1 = new Sphere(new Vector3f(1,0,-10), 1, color1, true);
        Sphere sphere2 = new Sphere(new Vector3f(-0.5, 0.5, -8), 1, color2, false);
        Sphere sphere4 = new Sphere(new Vector3f(-3, 2, -10), 1.6, color2, true);
        sphere2.setIor(1.5);
        Sphere sphere3 = new Sphere(new Vector3f(-2, -2, -7), 0.6, color2, true);
        Sphere sphere5 = new Sphere(new Vector3f(0, -2, -7), 0.5, color2, true);
        Sphere sphere6 = new Sphere(new Vector3f(0, -2, -4), 0.3, color2, true);
        Sphere sphere7 = new Sphere(new Vector3f(0, -2, -11), 0.6, color2, true);

        Triangle triangle1 = new Triangle(new Vector3f(-1.8,1,-7), new Vector3f(0,1,-7), new Vector3f(-1,1.6,-7));
        PlaneObject plane1 = new PlaneObject(new Vector3f(0, -1, 0), new Vector3f(0, 1, 0), color2, false);
        PlaneObject plane2 = new PlaneObject(new Vector3f(0, -3, 0), new Vector3f(0, 1, 0), color2, true);
        plane1.setIor(1.3);
        scene.addSceneObject(sphere1);
        scene.addSceneObject(sphere2);
        scene.addSceneObject(sphere3);
        scene.addSceneObject(sphere4);
        scene.addSceneObject(sphere5);
        scene.addSceneObject(sphere6);
        scene.addSceneObject(sphere7);
        scene.addSceneObject(plane1);
        scene.addSceneObject(plane2);
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
//        bezierSurface.triangulate(32).makeTriangles(scene);
        PointLight pointLight1 = new PointLight(color1, 200, new Vector3f(0.5, 0.6, -4.5));
        PointLight pointLight2 = new PointLight(color2, 200, new Vector3f(-0.6, 1.3, -9));
        PointLight pointLight3 = new PointLight(color3, 200, new Vector3f(1, 1.5, -4));
        scene.addPointLight(pointLight1);
        scene.addPointLight(pointLight2);
        scene.addPointLight(pointLight3);
        scene.render();

        File outputImg = new File("img.png");
        try {
            ImageIO.write(img, "png", outputImg);
        } catch (IOException e) {
            System.out.println("errore nel salvataggio immagine");
        }

    }

}
