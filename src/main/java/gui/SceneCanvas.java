package gui;

import rendering.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import org.imgscalr.Scalr;

public class SceneCanvas extends Canvas {

    private BufferedImage sceneToRender;

    public SceneCanvas(BufferedImage sceneToRender) {
        setBackground(Color.gray);
        this.sceneToRender = sceneToRender;
    }
    public void paint(Graphics g)  {
        BufferedImage resized;
        try {
            resized = resizeImage(sceneToRender, 800,
                    800);
        } catch (Exception e) {
            System.out.println("Error during resizing of image");
            resized = sceneToRender;
            e.printStackTrace();
        }

        g.drawImage(sceneToRender, 0, 0, new TextArea());
    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws Exception {
        return Scalr.resize(originalImage, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, targetWidth, targetHeight, Scalr.OP_ANTIALIAS);
    }

}
