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
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Options");
        Menu addFigureSubMenu = new Menu("Add figure...");
        MenuItem removeFigureMenuItem = new MenuItem("Remove figure");
        removeFigureMenuItem.setActionCommand("remove");
        MenuItem sphereMenuItem = new MenuItem("Sphere");
        sphereMenuItem.setActionCommand("add_sphere");
        MenuItem boxMenuitem = new MenuItem("Box");
        boxMenuitem.setActionCommand("add_box");
        MenuItem bezierItem = new MenuItem("Bezier Surface");
        bezierItem.setActionCommand("add_bezSurf");
        MenuItem bSplineItem = new MenuItem("B-Spline Surface");
        bSplineItem.setActionCommand("add_bSplineSurf");
        MenuItem bInterpolateItem = new MenuItem("B-Spline interpolation");
        bInterpolateItem.setActionCommand("add_bSplineInterp");
        Menu samplesSubMenu = new Menu("Sample shapes");
        MenuItem teapotItem = new MenuItem("Teapot (Bezier patches)");
        teapotItem.setActionCommand("add_teapot");
        MenuItem donutItem = new MenuItem("Donut (B-Spline surface)");
        donutItem.setActionCommand("add_donut");
        addFigureSubMenu.add(sphereMenuItem);
        sphereMenuItem.addActionListener(this);
        addFigureSubMenu.add(boxMenuitem);
        boxMenuitem.addActionListener(this);
        addFigureSubMenu.add(bezierItem);
        bezierItem.addActionListener(this);
        addFigureSubMenu.add(bSplineItem);
        bSplineItem.addActionListener(this);
        addFigureSubMenu.add(bInterpolateItem);
        bInterpolateItem.addActionListener(this);
        addFigureSubMenu.add(samplesSubMenu);
        samplesSubMenu.add(teapotItem);
        teapotItem.addActionListener(this);
        samplesSubMenu.add(donutItem);
        donutItem.addActionListener(this);
        menu.add(addFigureSubMenu);
        menu.add(removeFigureMenuItem);
        removeFigureMenuItem.addActionListener(this);
        menuBar.add(menu);
        this.setMenuBar(menuBar);
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
        addWindowListener(this);
        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand()=="add_sphere") {
            AddBoxFrame addBoxFrame = new AddBoxFrame();
        } else if (e.getActionCommand()=="add_box") {
            System.out.println("henlo box");
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
        if (event instanceof KeyEvent && isFocused()) {
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

    class AddBoxFrame extends Frame implements WindowListener, ActionListener {

        public AddBoxFrame() {

            addWindowListener(this);
            setTitle("Add sphere");
            Panel panel = new Panel(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            JLabel positionLabel = new JLabel("Insert position:");
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx=0;
            c.gridy=0;
            c.gridheight=3;
            c.insets = new Insets(0, 10, 0, 10);
            c.weightx=0.6;
            panel.add(positionLabel, c);
            JLabel x = new JLabel("X");
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx=1;
            c.gridy=0;
            c.gridheight=1;
            c.weightx=0.1;
            panel.add(x, c);
            TextField textFieldX = new TextField(50);
            textFieldX.addActionListener(this);
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx=2;
            c.gridy=0;
            c.gridheight=1;
            c.weightx=0.2;
            panel.add(textFieldX, c);
            JLabel y = new JLabel("Y");
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx=1;
            c.gridy=1;
            c.gridheight=1;
            panel.add(y, c);
            c.weightx=0.1;
            TextField textFieldY = new TextField(50);
            textFieldY.addActionListener(this);
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx=2;
            c.gridy=1;
            c.gridheight=1;
            c.weightx=0.2;
            panel.add(textFieldY, c);
            JLabel z = new JLabel("Z");
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx=1;
            c.gridy=3;
            c.gridheight=1;
            c.weightx=0.1;
            panel.add(z, c);
            TextField textFieldZ = new TextField(50);
            textFieldY.addActionListener(this);
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx=2;
            c.gridy=3;
            c.gridheight=1;
            c.weightx=0.2;
            panel.add(textFieldZ, c);
            add(panel);
            pack();
            setVisible(true);
            setSize(400, 500);
            textFieldX.requestFocusInWindow();
        }

        @Override
        public void windowOpened(WindowEvent e) {

        }

        @Override
        public void windowClosing(WindowEvent e) {
            setVisible(false);
            this.dispose();
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
        public void actionPerformed(ActionEvent e) {

        }
    }

}
