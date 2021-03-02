import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class Visualizer extends Frame implements ActionListener {

    private Scene scene;
    private SceneCanvas sceneCanvas;

    public Visualizer(Scene scene) {


        this.scene = scene;
        BufferedImage sceneToRender = this.scene.getImg();
        setSize(sceneToRender.getWidth()+100, sceneToRender.getHeight());
        SceneCanvas c = new SceneCanvas(sceneToRender);
        this.sceneCanvas = c;
        c.setBounds(0, 0, sceneToRender.getWidth(), sceneToRender.getHeight());
        add(c);
        Button b = new CommandButton(this,"right");
        b.setBounds(sceneToRender.getWidth()+25, sceneToRender.getHeight()/2, 50, 50);
        add(b);
        setLayout(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("right")) {
            Matrix4D currentCTW = scene.getCameraToWorld();
            Vector3f c = currentCTW.getC();
            this.scene.setCameraToWorld(c.add(new Vector3f(-0.05, 0, 0)), new Vector3f(0, 0, -1));
            this.scene.render();
            this.sceneCanvas.repaint();
            System.out.println("Potamocero forense");
        }
    }

    class CommandButton extends Button {

        public CommandButton(Visualizer visualizer, String label) {
            super(label);
            addActionListener(visualizer);
        }


    }

}
