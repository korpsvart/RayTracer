import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;

public class Visualizer extends Frame implements ActionListener, WindowListener {

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
        Button rightButton = new CommandButton(this,"right");
        Button closeButton = new CommandButton(this, "close");
        rightButton.setBounds(sceneToRender.getWidth()+25, sceneToRender.getHeight()/2, 50, 50);
        closeButton.setBounds(sceneToRender.getWidth()+25, sceneToRender.getHeight()/2+75, 50, 50);
        add(rightButton);
        add(closeButton);
        setLayout(null);
        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("right")) {
            Matrix4D currentCTW = scene.getCameraToWorld();
            Vector3f c = currentCTW.getC();
            this.scene.setCameraToWorld(c.add(new Vector3f(-0.05, 0, 0)), new Vector3f(0, 0, -1));
            this.scene.setBVH();
            this.scene.render();
            this.sceneCanvas.repaint();
        } else if (e.getActionCommand().equals("close")) {
            setVisible(false);
            this.dispose();
            System.exit(0);
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        setVisible(false);
        this.dispose();
        System.exit(0);
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    class CommandButton extends Button {

        public CommandButton(Visualizer visualizer, String label) {
            super(label);
            addActionListener(visualizer);
        }


    }

}
