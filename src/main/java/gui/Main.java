package gui;

import rendering.Scene;
import rendering.Vector3f;

import java.awt.image.BufferedImage;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public class Main {

    public static void main(String[] args) {
        int width = 1024;
        int height = 650;
        double fieldOfView = 39.6; //in degrees
        Vector3f cameraPosition = new Vector3f(0,0,0);
        BufferedImage img = new BufferedImage(width, height, TYPE_INT_RGB);

        Scene scene = new Scene(width, height, fieldOfView, img, cameraPosition, new Vector3f(0.1f, 0.3f, 0.6f));

        scene.setBVH();
        scene.render(20);

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Visualizer(scene);
            }
        });
    }
}
