import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Map;

public class Visualizer extends Frame implements ActionListener, WindowListener, KeyListener, AWTEventListener {

    private Scene scene;
    private SceneCanvas sceneCanvas;
    private final static String[] materials = {
            "Diffuse", "Mirror-Like", "Transparent"
    };
    private final static Map<String, MaterialType> materialsMap = Map.of(
            "Diffuse",MaterialType.DIFFUSE, "Mirror-Like", MaterialType.MIRRORLIKE,
            "Transparent", MaterialType.TRANSPARENT);

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
            AddSphereFrame addSphereFrame = new AddSphereFrame(this.scene);
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
                    scene.getCamera().translate(new Vector3f(0, 0, 0.1));
                    renderScene(this.scene);
                }
                else if (key.getExtendedKeyCode() == KeyEvent.VK_S) {
                    scene.getCamera().translate(new Vector3f(0, 0, -0.1));
                    renderScene(this.scene);
                } else if (key.getExtendedKeyCode() == KeyEvent.VK_Q) {
                    scene.getCamera().rotateY(-2); //degrees
                    renderScene(this.scene);
                }else if (key.getExtendedKeyCode() == KeyEvent.VK_E) {
                    scene.getCamera().rotateY(2); //degrees
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

    private void renderScene(Scene scene) {
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

    class AddSphereFrame extends Frame implements WindowListener, ActionListener {


        private TextField albedoLabel;
        private Scene scene;
        private TextField posX, posY, posZ;
        private TextField textRadius;
        private Panel mainPanel;
        private TextField iorText;
        private Button albedoButton;
        private Panel albedoSubPanel;
        private Panel transparentSubPanel;
        private Color currentColor = Color.white;
        private JColorChooser colorChooser;
        private JComboBox materialComboBox;

        public AddSphereFrame(Scene scene) {

            this.scene = scene;

            addWindowListener(this);
            setTitle("Add sphere");
            Panel panel = new Panel(new GridBagLayout());
            this.mainPanel = panel;
            GridBagConstraints c = new GridBagConstraints();

            //Start of position input graphics
            JLabel positionLabel = new JLabel("Insert position:");
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx=0;
            c.gridy=0;
            c.gridheight=3;
            c.weightx=0.6;
            panel.add(positionLabel, c);
            JLabel x = new JLabel("X");
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx=1;
            c.gridy=0;
            c.gridheight=1;
            c.weightx=0.1;
            panel.add(x, c);
            TextField textFieldX = new TextField("0",10);
            posX = textFieldX;
            textFieldX.addActionListener(this);
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx=2;
            c.gridy=0;
            c.gridheight=1;
            c.weightx=0.3;
            panel.add(textFieldX, c);
            JLabel y = new JLabel("Y");
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx=1;
            c.gridy=1;
            c.gridheight=1;
            c.weightx=0.1;
            panel.add(y, c);
            TextField textFieldY = new TextField("0",10);
            posY = textFieldY;
            textFieldY.addActionListener(this);
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx=2;
            c.gridy=1;
            c.gridheight=1;
            c.weightx=0.3;
            panel.add(textFieldY, c);
            JLabel z = new JLabel("Z");
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx=1;
            c.gridy=2;
            c.gridheight=1;
            c.weightx=0.1;
            panel.add(z, c);
            TextField textFieldZ = new TextField("-5",10);
            posZ = textFieldZ;
            textFieldZ.addActionListener(this);
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx=2;
            c.gridy=2;
            c.gridheight=1;
            c.weightx=0.3;
            panel.add(textFieldZ, c);
            //end of position input graphics

            //Start of radius input graphics
            JLabel radiusLabel = new JLabel("Insert radius:");
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx=0;
            c.gridy=3;
            c.gridheight=1;
            c.gridwidth=2;
            c.weightx=0.6;
            c.insets = new Insets(20, 0, 20, 0);
            panel.add(radiusLabel, c);
            TextField textFieldRadius = new TextField("0.3", 10);
            textRadius = textFieldRadius;
            textFieldRadius.addActionListener(this);
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx=2;
            c.gridy=3;
            c.gridheight=1;
            c.gridwidth=1;
            c.weightx=0.4;
            panel.add(textFieldRadius, c);
            //end of radius input graphics

            //start of material input graphics

            JLabel materialLabel = new JLabel("Select type of material");
            c.gridy=4;
            c.gridx=0;
            c.gridwidth=2;
            c.weightx=0.6;
            panel.add(materialLabel, c);
            JComboBox comboBoxMaterial = new JComboBox(materials);
            this.materialComboBox = comboBoxMaterial;
            c.gridx=2;
            c.weightx=0.6;
            panel.add(comboBoxMaterial, c);
            comboBoxMaterial.addActionListener(this);

            //material property
            //(changes according to selected option) - default is albedo (for diffuse objects)

            //define albedo subpanel
            this.albedoSubPanel = new Panel(new GridBagLayout());
            JLabel materialPropertyLabel = new JLabel("Select albedo:");
            c.gridy=0;
            c.gridx=0;
            c.weightx=0.5;
            c.gridwidth=1;
            albedoSubPanel.add(materialPropertyLabel, c);
            JLabel rgbLabel = new JLabel("RGB:");
            c.gridx=1;
            c.weightx=0.2;
            TextField currentAlbedoLabel = new TextField("(255,255,255)");
            this.albedoLabel = currentAlbedoLabel;
            currentAlbedoLabel.setEditable(false);
            c.gridx=2;
            c.weightx=0.2;
            albedoSubPanel.add(currentAlbedoLabel, c);
            Button openColorChooserButton = new Button("Select albedo");
            this.albedoButton = openColorChooserButton;
            openColorChooserButton.setActionCommand("colorChoose");
            openColorChooserButton.addActionListener(this);
            c.gridx=3;
            c.weightx=0.2;
            albedoSubPanel.add(openColorChooserButton, c);


            //define transparent subpanel
            this.transparentSubPanel = new Panel(new GridBagLayout());
            JLabel iorLabel = new JLabel("Insert index of refraction");
            c.gridy=0;
            c.gridx=0;
            c.weightx=0.8;
            c.gridwidth=2;
            transparentSubPanel.add(iorLabel, c);
            TextField iorTextField = new TextField("1.5");
            this.iorText = iorTextField;
            c.gridx=2;
            c.gridwidth=1;
            c.weightx=0.2;
            transparentSubPanel.add(iorTextField, c);

            //add current active subpanel (default is the diffuse one)
            addMaterialPropertySubPanel(panel, MaterialType.DIFFUSE);
            //end of material property

            //Add button to send data
            Button sendButton = new Button("Create");
            sendButton.setActionCommand("create");
            sendButton.addActionListener(this);
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridy=6;
            c.gridx=1;
            c.weightx=0;
            c.gridwidth=1;
            panel.add(sendButton, c);


            add(panel);
            pack();
            setVisible(true);
            setSize(800, 400);
            textFieldX.requestFocusInWindow();
        }


        private void addMaterialPropertySubPanel(Panel mainPanel,MaterialType materialType) {
            GridBagConstraints c = new GridBagConstraints();
            c.gridy=5;
            c.gridx=0;
            c.weightx=0;
            c.gridwidth=3;
            switch (materialType) {
                case DIFFUSE:
                    mainPanel.remove(this.transparentSubPanel);
                    mainPanel.add(this.albedoSubPanel, c);
                    mainPanel.revalidate();
                    mainPanel.repaint();
                    break;
                case TRANSPARENT:
                    mainPanel.remove(this.albedoSubPanel);
                    mainPanel.add(this.transparentSubPanel, c);
                    mainPanel.revalidate();
                    mainPanel.repaint();
                    break;
                case MIRRORLIKE:
                    mainPanel.remove(this.transparentSubPanel);
                    mainPanel.remove(this.albedoSubPanel);
                    mainPanel.revalidate();
                    mainPanel.repaint();
            }
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
            switch(e.getActionCommand()){
                case "colorChoose":
                    JColorChooser colorChooser = new JColorChooser();
                    this.colorChooser = colorChooser;
                    colorChooser.setPreviewPanel(new JPanel());
                    AbstractColorChooserPanel[] panels = colorChooser.getChooserPanels();
                    for (AbstractColorChooserPanel panel :
                            panels) {
                        panel.setColorTransparencySelectionEnabled(false);
                    }
                    JDialog dialog = JColorChooser.createDialog(this, "Pick color",
                            true, colorChooser, this, this);
                    dialog.setVisible(true);
                    break;
                case "OK":
                    //event generated when a color is chosen
                    Color color = this.colorChooser.getColor();
                    albedoLabel.setText("("+color.getRed()+","+color.getGreen()+","+color.getBlue()+")");
                    break;
                case "comboBoxChanged":
                    JComboBox cb = (JComboBox)e.getSource();
                    MaterialType materialType = materialsMap.get(cb.getSelectedItem());
                    switch (materialType) {
                        case DIFFUSE:
                            addMaterialPropertySubPanel(mainPanel,MaterialType.DIFFUSE);
                            break;
                        case TRANSPARENT:
                            addMaterialPropertySubPanel(mainPanel, MaterialType.TRANSPARENT);
                            break;
                        case MIRRORLIKE:
                            addMaterialPropertySubPanel(mainPanel, MaterialType.MIRRORLIKE);
                    }
                    break;
                case "create":
                    MaterialType mType = materialsMap.get(materialComboBox.getSelectedItem());
                    Vector3f position = new Vector3f(Double.parseDouble(posX.getText()),
                            Double.parseDouble(posY.getText()), Double.parseDouble(posZ.getText()));
                    double radius = Double.parseDouble(textRadius.getText());
                    String albedoVal = albedoLabel.getText().substring(1, albedoLabel.getText().length()-1);
                    String[] albedoA = albedoVal.split(",");
                    Vector3f albedo = new Vector3f(Arrays.stream(albedoA).mapToDouble((x) -> Double.parseDouble(x)).toArray());
                    albedo = albedo.mul((double)1/255);
                    double ior = Double.parseDouble(iorText.getText());
                    createSphere(position,radius,mType,albedo,ior);
                    renderScene(this.scene);
            }
        }

        private void createSphere(Vector3f position, double radius, MaterialType materialType, Vector3f albedo, double ior) {
            Sphere sphere = new Sphere(position, radius);
            switch (materialType) {
                case DIFFUSE:
                    Diffuse diffuse = new Diffuse(sphere);
                    diffuse.setAlbedo(albedo);
                    scene.addSceneObject(diffuse);
                    break;
                case MIRRORLIKE:
                    scene.addSceneObject(new MirrorLike(sphere));
                    break;
                case TRANSPARENT:
                    MirrorTransparent mirrorTransparent = new MirrorTransparent(sphere);
                    mirrorTransparent.setIor(ior);
                    scene.addSceneObject(mirrorTransparent);
                    break;
            }
        }
    }

}
