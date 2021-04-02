import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class Visualizer extends Frame implements ActionListener, WindowListener, KeyListener, AWTEventListener {

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
        setFocusable(true);
        setFocusTraversalKeysEnabled(true);
        this.getToolkit().addAWTEventListener(this, AWTEvent.KEY_EVENT_MASK);
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
            this.scene.render(20);
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

    @Override
    public void keyTyped(KeyEvent e) {
        System.out.println("blip blop");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("blip blop");
        if (e.getExtendedKeyCode()==KeyEvent.VK_RIGHT) {
            Matrix4D currentCTW = scene.getCameraToWorld();
            Vector3f c = currentCTW.getC();
            this.scene.setCameraToWorld(c.add(new Vector3f(-0.05, 0, 0)), new Vector3f(0, 0, -1));
            this.scene.setBVH();
            this.scene.render(20);
            this.sceneCanvas.repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("blip blop");
    }

    @Override
    public void eventDispatched(AWTEvent event) {
        if (event instanceof KeyEvent) {
            KeyEvent key = (KeyEvent) event;
            if (key.getID() == KeyEvent.KEY_PRESSED) { //Handle key presses
                if (key.getExtendedKeyCode() == KeyEvent.VK_RIGHT) {
                    scene.getCamera().translate(new Vector3f(0.1, 0, 0));
                    this.scene.setBVH();
                    this.scene.render(20);
                    this.sceneCanvas.repaint();
                } else if (key.getExtendedKeyCode() == KeyEvent.VK_LEFT) {
                    scene.getCamera().translate(new Vector3f(-0.1, 0, 0));
                    this.scene.setBVH();
                    this.scene.render(20);
                    this.sceneCanvas.repaint();
                } else if (key.getExtendedKeyCode() == KeyEvent.VK_UP) {
                    scene.getCamera().translate(new Vector3f(0, 0.1, 0));
                    this.scene.setBVH();
                    this.scene.render(20);
                    this.sceneCanvas.repaint();
                } else if (key.getExtendedKeyCode() == KeyEvent.VK_DOWN) {
                    scene.getCamera().translate(new Vector3f(0, -0.1, 0));
                    this.scene.setBVH();
                    this.scene.render(20);
                    this.sceneCanvas.repaint();
                }
                else if (key.getExtendedKeyCode() == KeyEvent.VK_W) {
                    scene.getCamera().translate(new Vector3f(0, 0, 0.1));
                    this.scene.setBVH();
                    this.scene.render(20);
                    this.sceneCanvas.repaint();
                }
                else if (key.getExtendedKeyCode() == KeyEvent.VK_S) {
                    scene.getCamera().translate(new Vector3f(0, 0, -0.1));
                    this.scene.setBVH();
                    this.scene.render(20);
                    this.sceneCanvas.repaint();
                } else if (key.getExtendedKeyCode() == KeyEvent.VK_Q) {
                    scene.getCamera().rotateY(-2); //degrees
                    this.scene.setBVH();
                    this.scene.render(20);
                    this.sceneCanvas.repaint();
                }else if (key.getExtendedKeyCode() == KeyEvent.VK_E) {
                    scene.getCamera().rotateY(2); //degrees
                    this.scene.setBVH();
                    this.scene.render(20);
                    this.sceneCanvas.repaint();
                }else if (key.getExtendedKeyCode() == KeyEvent.VK_R) {
                    scene.getCamera().rotateX(2); //degrees
                    this.scene.setBVH();
                    this.scene.render(20);
                    this.sceneCanvas.repaint();
                }else if (key.getExtendedKeyCode() == KeyEvent.VK_F) {
                    scene.getCamera().rotateX(-2); //degrees
                    this.scene.setBVH();
                    this.scene.render(20);
                    this.sceneCanvas.repaint();
                }
                key.consume();
            }
        }
    }

    class CommandButton extends Button {

        public CommandButton(Visualizer visualizer, String label) {
            super(label);
            addActionListener(visualizer);
        }


    }

}
