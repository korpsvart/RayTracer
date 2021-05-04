package gui;


import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Map;

import rendering.*;

public class Visualizer extends Frame implements ActionListener, WindowListener, KeyListener, AWTEventListener {

    private Scene scene;
    private SceneCanvas sceneCanvas;
    final static String[] materials = {
            "Diffuse", "Phong", "Mirror-Like", "Transparent"
    };
    private final static String[] lightTypes = {
            "Point light", "Distant light"
    };
    private final static Map<String, LightType> lightTypesMap = Map.of(
            "Point light", LightType.POINT_LIGHT, "Distant light", LightType.DISTANT_LIGHT
    );
    final static Map<String, MaterialType> materialsMap = Map.of(
            "Diffuse", MaterialType.DIFFUSE, "Phong", MaterialType.PHONG,
            "Mirror-Like", MaterialType.MIRRORLIKE,
            "Transparent", MaterialType.TRANSPARENT);

    public Visualizer(Scene scene) {


        this.scene = scene;
        BufferedImage sceneToRender = this.scene.getImg();

        setSize(sceneToRender.getWidth(), sceneToRender.getHeight());
        SceneCanvas c = new SceneCanvas(sceneToRender);
        this.sceneCanvas = c;
        c.setBounds(0, 0, sceneToRender.getWidth(), sceneToRender.getHeight());
        add(c);
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Options");
        Menu settingsMenu = new Menu("Settings");
        MenuItem changeSettings = new MenuItem("Change settings");
        settingsMenu.add(changeSettings);
        changeSettings.setActionCommand("settings");
        menuBar.add(settingsMenu);
        settingsMenu.addActionListener(this);
        Menu addFigureSubMenu = new Menu("Add figure...");
        MenuItem removeFigureMenuItem = new MenuItem("Remove figure");
        removeFigureMenuItem.setActionCommand("remove");
        Menu addLightMenu = new Menu("Add light");
        MenuItem pointLightMenuItem = new MenuItem("Point light");
        pointLightMenuItem.setActionCommand("add_point_light");
        MenuItem distantLightMenuItem = new MenuItem("Distant light");
        distantLightMenuItem.setActionCommand("add_distant_light");
        MenuItem removeLightMenuItem = new MenuItem("Remove light");
        removeLightMenuItem.setActionCommand("remove_light");
        MenuItem sphereMenuItem = new MenuItem("Sphere");
        sphereMenuItem.setActionCommand("add_sphere");
        MenuItem boxMenuitem = new MenuItem("Box");
        boxMenuitem.setActionCommand("add_box");
        MenuItem planeMenuitem = new MenuItem("Plane");
        planeMenuitem.setActionCommand("add_plane");
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
        addFigureSubMenu.add(planeMenuitem);
        planeMenuitem.addActionListener(this);
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
        addLightMenu.add(pointLightMenuItem);
        pointLightMenuItem.addActionListener(this);
        addLightMenu.add(distantLightMenuItem);
        distantLightMenuItem.addActionListener(this);
        menu.add(addFigureSubMenu);
        menu.add(removeFigureMenuItem);
        menu.add(addLightMenu);
        menu.add(removeLightMenuItem);
        removeFigureMenuItem.addActionListener(this);
        removeLightMenuItem.addActionListener(this);
        menuBar.add(menu);
        this.setMenuBar(menuBar);
        setFocusable(true);
        setFocusTraversalKeysEnabled(true);
        this.getToolkit().addAWTEventListener(this, AWTEvent.KEY_EVENT_MASK);
        setLayout(null);
        addWindowListener(this);
        setResizable(false);
        setVisible(true);

    }

    public static Vector3f extractVectorFromTextField(TextField textField) {
        String vectorVal = textField.getText().substring(1, textField.getText().length()-1);
        String[] vectorA = vectorVal.split(",");
        return new Vector3f(Arrays.stream(vectorA).mapToDouble((h) -> Double.parseDouble(h)).toArray());
    }

    public static String extractTextFromVector(Vector3f v) {
        return "{"+v.getX()+","+v.getY()+","+v.getZ()+"}";

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("add_sphere")) {
            AddSphereFrame addSphereFrame = new AddSphereFrame(this,this.scene);
        } else if (e.getActionCommand().equals("add_box")) {
            AddBoxFrame addBoxFrame = new AddBoxFrame(this, this.scene);
        } else if (e.getActionCommand().equals("add_point_light")) {
            AddPointLightFrame addPointLightFrame = new AddPointLightFrame(this, this.scene);
        } else if (e.getActionCommand().equals("add_distant_light")) {
            AddDistantLightFrame addDistantLightFrame = new AddDistantLightFrame(this, this.scene);
        } else if (e.getActionCommand().equals("add_bezSurf")) {
            AddBezierSurface addBezierSurface = new AddBezierSurface(this, this.scene);
        } else if (e.getActionCommand().equals("add_bSplineSurf"))  {
            AddBSplineSurfaceFrame addBSplineSurfaceFrame = new AddBSplineSurfaceFrame(this, this.scene);
        } else if (e.getActionCommand().equals("add_bSplineInterp"))  {
            AddSurfaceInterpolationFrame addSurfaceInterpolationFrame = new AddSurfaceInterpolationFrame(this, this.scene);
        } else if (e.getActionCommand().equals("add_teapot")) {
            AddTeapotFrame addTeapotFrame = new AddTeapotFrame(this, this.scene);
        }  else if (e.getActionCommand().equals("add_donut")) {
            AddDonutFrame addDonutFrame = new AddDonutFrame(this, this.scene);
        }  else if(e.getActionCommand().equals("add_plane")) {
            AddPlaneFrame addPlaneFrame = new AddPlaneFrame(this, this.scene);
        } else if (e.getActionCommand().equals("remove")) {
            RemoveObjectFrame removeObjectFrame = new RemoveObjectFrame(this, this.scene);
        }  else if (e.getActionCommand().equals("remove_light")) {
            RemoveLightFrame removeLightFrame = new RemoveLightFrame(this, this.scene);
        }   else if (e.getActionCommand().equals("settings")) {
            SettingsFrame settingsFrame = new SettingsFrame(this, this.scene);
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
            renderScene(this.scene);
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
                    renderScene(this.scene);
                } else if (key.getExtendedKeyCode() == KeyEvent.VK_LEFT) {
                    scene.getCamera().translate(new Vector3f(-0.1, 0, 0));
                    renderScene(this.scene);
                } else if (key.getExtendedKeyCode() == KeyEvent.VK_UP) {
                    scene.getCamera().translate(new Vector3f(0, 0.1, 0));
                    renderScene(this.scene);
                } else if (key.getExtendedKeyCode() == KeyEvent.VK_DOWN) {
                    scene.getCamera().translate(new Vector3f(0, -0.1, 0));
                    renderScene(this.scene);
                }
                else if (key.getExtendedKeyCode() == KeyEvent.VK_W) {
                    scene.getCamera().translate(new Vector3f(0, 0, -0.1));
                    renderScene(this.scene);
                }
                else if (key.getExtendedKeyCode() == KeyEvent.VK_S) {
                    scene.getCamera().translate(new Vector3f(0, 0, 0.1));
                    renderScene(this.scene);
                } else if (key.getExtendedKeyCode() == KeyEvent.VK_Q) {
                    scene.getCamera().rotateY(2); //degrees
                    renderScene(this.scene);
                }else if (key.getExtendedKeyCode() == KeyEvent.VK_E) {
                    scene.getCamera().rotateY(-2); //degrees
                    renderScene(this.scene);
                }else if (key.getExtendedKeyCode() == KeyEvent.VK_R) {
                    scene.getCamera().rotateX(2); //degrees
                    renderScene(this.scene);
                }else if (key.getExtendedKeyCode() == KeyEvent.VK_F) {
                    scene.getCamera().rotateX(-2); //degrees
                    renderScene(this.scene);
                }
                key.consume();
            }
        }
    }

    public void renderScene(Scene scene) {
        this.scene.setBVH();
        this.scene.render(20);

        this.sceneCanvas.repaint();
    }

    class CommandButton extends Button {

        public CommandButton(Visualizer visualizer, String label) {
            super(label);
            addActionListener(visualizer);
        }
    }


}


