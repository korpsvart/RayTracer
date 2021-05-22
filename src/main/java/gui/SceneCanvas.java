package gui;

import rendering.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class SceneCanvas extends Canvas {

    private BufferedImage sceneToRender;

    public SceneCanvas(BufferedImage sceneToRender) {
        setBackground(Color.gray);
        this.sceneToRender = sceneToRender;
    }
    public void paint(Graphics g)  {
        g.drawImage(sceneToRender, 0, 0, new TextArea());
    }


}
