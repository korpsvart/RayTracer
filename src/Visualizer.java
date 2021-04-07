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
            "Diffuse", "Phong", "Mirror-Like", "Transparent"
    };
    private final static String[] lightTypes = {
            "Point light", "Distant light"
    };
    private final static Map<String, LightType> lightTypesMap = Map.of(
            "Point light", LightType.POINT_LIGHT, "Distant light", LightType.DISTANT_LIGHT
    );
    private final static Map<String, MaterialType> materialsMap = Map.of(
            "Diffuse",MaterialType.DIFFUSE, "Phong", MaterialType.PHONG,
            "Mirror-Like", MaterialType.MIRRORLIKE,
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
            AddBoxFrame addBoxFrame = new AddBoxFrame(this.scene);
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

    abstract class AddLightSourceFrame extends Frame implements ActionListener {

        protected Scene scene;

        private JLabel colorLabel = new JLabel("Select light color");
        protected Color currentColor = Color.white;
        private JLabel intensityLabel = new JLabel("Insert light intensity (recommended in range (1,200))");
        protected TextField textFieldIntensity = new TextField("100", 10);
        protected Panel mainPanel = new Panel(new GridBagLayout());
        protected JColorChooser colorChooser;
        protected Button colorButton = new Button("Choose color");
        protected TextField colorTextField = new TextField("(255,255,255)", 10);
        protected Panel colorSubPanel = createChooseRGBPanel();
        protected JLabel lightType = new JLabel("Choose light type");
        protected JComboBox comboBoxLightType = new JComboBox();
        private JLabel labelLightPosition = new JLabel("Insert light position");
        private JLabel x = new JLabel("x");
        private JLabel y = new JLabel("y");
        private JLabel z = new JLabel("z");
        private TextField textFieldXPosition = new TextField("1", 10);
        private TextField textFieldYPosition = new TextField("0", 10);
        private TextField textFieldZPosition = new TextField("-1", 10);
        private JLabel labelLightDirection = new JLabel("Insert light direction");
        private TextField textFieldXDirection = new TextField("1", 10);
        private TextField textFieldYDirection = new TextField("0", 10);
        private TextField textFieldZDirection = new TextField("-1", 10);

        public AddLightSourceFrame(Scene scene) {
            this.scene = scene;

            GridBagConstraints c = new GridBagConstraints();
            int gridy = 0;

            c.gridy=gridy++;
            c.gridwidth=2;
            mainPanel.add(intensityLabel, c);
            c.gridx=2;
            c.gridwidth=1;
            mainPanel.add(textFieldIntensity, c);

            c.gridy=gridy++;
            mainPanel.add(colorSubPanel, c);





        }

        private void addLightPropertiesSubPanel() {
        }


        private Panel createChooseRGBPanel() {
            GridBagConstraints c = new GridBagConstraints();
            Panel chooseRGBPanel = new Panel(new GridBagLayout());
            JLabel materialPropertyLabel = new JLabel("Select color:");
            c.gridy=0;
            c.gridx=0;
            c.weightx=0.5;
            c.gridwidth=1;
            chooseRGBPanel.add(materialPropertyLabel, c);
            JLabel rgbLabel = new JLabel("RGB:");
            c.gridx=1;
            c.weightx=0.2;
            TextField currentAlbedoLabel = new TextField("(255,255,255)");
            this.colorTextField = currentAlbedoLabel;
            currentAlbedoLabel.setEditable(false);
            c.gridx=2;
            c.weightx=0.2;
            chooseRGBPanel.add(currentAlbedoLabel, c);
            Button openColorChooserButton = new Button("Pick a color");
            this.colorButton = openColorChooserButton;
            openColorChooserButton.setActionCommand("colorChoose");
            openColorChooserButton.addActionListener(this);
            c.gridx=3;
            c.weightx=0.2;
            chooseRGBPanel.add(openColorChooserButton, c);
            return chooseRGBPanel;
        }


    }



    abstract class AddObjectFrame extends Frame implements  ActionListener {

        protected TextField albedoTextField;
        protected JColorChooser colorChooser;
        protected Button albedoButton;
        protected TextField iorText;
        protected JLabel materialLabel;
        protected Scene scene;
        protected Panel mainPanel;
        protected int materialSubPanelGridy;
        protected Panel albedoSubPanel;
        protected Panel transparentSubPanel;
        protected Color currentColor = Color.white;
        protected JComboBox materialComboBox;

        public AddObjectFrame(Scene scene) {

            GridBagConstraints c = new GridBagConstraints();
            Panel panel = new Panel(new GridBagLayout());
            this.mainPanel = panel;
            this.scene = scene;
            this.materialLabel = new JLabel("Select type of material");
            JComboBox comboBoxMaterial = new JComboBox(materials);
            this.materialComboBox = comboBoxMaterial;
            materialComboBox.addActionListener(this);
            //define albedo subpanel
            albedoSubPanel = createChooseRGBPanel();


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


        }

        private Panel createChooseRGBPanel() {
            GridBagConstraints c = new GridBagConstraints();
            Panel chooseRGBPanel = new Panel(new GridBagLayout());
            JLabel materialPropertyLabel = new JLabel("Select albedo:");
            c.gridy=0;
            c.gridx=0;
            c.weightx=0.5;
            c.gridwidth=1;
            chooseRGBPanel.add(materialPropertyLabel, c);
            JLabel rgbLabel = new JLabel("RGB:");
            c.gridx=1;
            c.weightx=0.2;
            TextField currentAlbedoLabel = new TextField("(255,255,255)");
            this.albedoTextField = currentAlbedoLabel;
            currentAlbedoLabel.setEditable(false);
            c.gridx=2;
            c.weightx=0.2;
            chooseRGBPanel.add(currentAlbedoLabel, c);
            Button openColorChooserButton = new Button("Select albedo");
            this.albedoButton = openColorChooserButton;
            openColorChooserButton.setActionCommand("colorChoose");
            openColorChooserButton.addActionListener(this);
            c.gridx=3;
            c.weightx=0.2;
            chooseRGBPanel.add(openColorChooserButton, c);
            return chooseRGBPanel;
        }

        protected void addMaterialComboBox(int gridy) {
            GridBagConstraints c = new GridBagConstraints();
            c.gridy=gridy;
            c.gridx=0;
            c.gridwidth=2;
            c.weightx=0.6;
            mainPanel.add(materialLabel, c);
            c.gridx=2;
            c.weightx=0.6;
            mainPanel.add(materialComboBox, c);
        }

        protected void addMaterialPropertySubPanel(MaterialType materialType, int gridy) {
            this.materialSubPanelGridy = gridy;
            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(10, 0, 10, 0);
            c.gridy=gridy;
            c.gridx=0;
            c.weightx=0;
            c.gridwidth=3;
            switch (materialType) {
                case DIFFUSE: case PHONG:
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

        protected void addSendButton(int gridy) {
            //Add button to send data
            GridBagConstraints c = new GridBagConstraints();
            Button sendButton = new Button("Create");
            sendButton.setActionCommand("create");
            sendButton.addActionListener(this);
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridy=gridy;
            c.gridx=1;
            c.weightx=0;
            c.gridwidth=1;
            c.insets = new Insets(10, 0, 10, 0);
            mainPanel.add(sendButton, c);


            add(mainPanel);
            pack();
            setVisible(true);
            setSize(800, 400);
        }

        protected void addSceneObject(GeometricObject geometricObject, MaterialType materialType, Vector3f albedo, double ior) {
            switch (materialType) {
                case DIFFUSE:
                    Diffuse diffuse = new Diffuse(geometricObject);
                    diffuse.setAlbedo(albedo);
                    scene.addSceneObject(diffuse);
                    break;
                case PHONG:
                    Phong phong = new Phong(geometricObject);
                    phong.setAlbedo(albedo);
                    scene.addSceneObject(phong);
                    break;
                case MIRRORLIKE:
                    scene.addSceneObject(new MirrorLike(geometricObject));
                    break;
                case TRANSPARENT:
                    MirrorTransparent mirrorTransparent = new MirrorTransparent(geometricObject);
                    mirrorTransparent.setIor(ior);
                    scene.addSceneObject(mirrorTransparent);
                    break;
            }
        }

        public void triangulateAndAddSceneObject(GeometricObject geometricObject, MaterialType materialType, Vector3f albedo,
                                                 double ior, int divs) {
            switch (materialType) {
                case DIFFUSE:
                    Diffuse diffuse = new Diffuse(geometricObject);
                    diffuse.setAlbedo(albedo);
                    scene.triangulateAndAddSceneObject(diffuse, divs);
                    break;
                case PHONG:
                    Phong phong = new Phong(geometricObject);
                    phong.setAlbedo(albedo);
                    scene.triangulateAndAddSceneObject(phong, divs);
                    break;
                case MIRRORLIKE:
                    scene.triangulateAndAddSceneObject(new MirrorLike(geometricObject), divs);
                    break;
                case TRANSPARENT:
                    MirrorTransparent mirrorTransparent = new MirrorTransparent(geometricObject);
                    mirrorTransparent.setIor(ior);
                    scene.triangulateAndAddSceneObject(mirrorTransparent, divs);
                    break;
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
                case "colorChoose":
                    JColorChooser colorChooser = new JColorChooser(currentColor);
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
                    albedoTextField.setText("("+color.getRed()+","+color.getGreen()+","+color.getBlue()+")");
                    break;
                case "comboBoxChanged":
                    JComboBox cb = (JComboBox)e.getSource();
                    MaterialType materialType = materialsMap.get(cb.getSelectedItem());
                    switch (materialType) {
                        case DIFFUSE:
                            addMaterialPropertySubPanel(MaterialType.DIFFUSE, materialSubPanelGridy);
                            break;
                        case PHONG:
                            addMaterialPropertySubPanel(MaterialType.PHONG, materialSubPanelGridy);
                            break;
                        case TRANSPARENT:
                            addMaterialPropertySubPanel(MaterialType.TRANSPARENT, materialSubPanelGridy);
                            break;
                        case MIRRORLIKE:
                            addMaterialPropertySubPanel(MaterialType.MIRRORLIKE, materialSubPanelGridy);
                    }
                    break;
            }
        }
    }


    class AddSphereFrame extends AddObjectFrame implements WindowListener{


        private TextField posX, posY, posZ;
        private TextField textRadius;
        private Color currentColor = Color.white;
        private JColorChooser colorChooser;


        public AddSphereFrame(Scene scene) {
            super(scene);

            addWindowListener(this);
            setTitle("Add sphere");
            GridBagConstraints c = new GridBagConstraints();

            //Start of position input graphics
            JLabel positionLabel = new JLabel("Insert position:");
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx=0;
            c.gridy=0;
            c.gridheight=3;
            c.weightx=0.6;
            mainPanel.add(positionLabel, c);
            JLabel x = new JLabel("X");
            JLabel y = new JLabel("Y");
            JLabel z = new JLabel("Z");
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx=1;
            c.gridy=0;
            c.gridheight=1;
            c.weightx=0.1;
            mainPanel.add(x, c);
            TextField textFieldX = new TextField("0",10);
            posX = textFieldX;
            textFieldX.addActionListener(this);
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx=2;
            c.gridy=0;
            c.gridheight=1;
            c.weightx=0.3;
            mainPanel.add(textFieldX, c);

            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx=1;
            c.gridy=1;
            c.gridheight=1;
            c.weightx=0.1;
            mainPanel.add(y, c);
            TextField textFieldY = new TextField("0",10);
            posY = textFieldY;
            textFieldY.addActionListener(this);
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx=2;
            c.gridy=1;
            c.gridheight=1;
            c.weightx=0.3;
            mainPanel.add(textFieldY, c);

            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx=1;
            c.gridy=2;
            c.gridheight=1;
            c.weightx=0.1;
            mainPanel.add(z, c);
            TextField textFieldZ = new TextField("-5",10);
            posZ = textFieldZ;
            textFieldZ.addActionListener(this);
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx=2;
            c.gridy=2;
            c.gridheight=1;
            c.weightx=0.3;
            mainPanel.add(textFieldZ, c);
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
            mainPanel.add(radiusLabel, c);
            TextField textFieldRadius = new TextField("0.3", 10);
            textRadius = textFieldRadius;
            textFieldRadius.addActionListener(this);
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx=2;
            c.gridy=3;
            c.gridheight=1;
            c.gridwidth=1;
            c.weightx=0.4;
            mainPanel.add(textFieldRadius, c);
            //end of radius input graphics

            //material label and combo box
            addMaterialComboBox(4);


            //material property
            //(changes according to selected option) - default is albedo (for diffuse objects)
            //add current active subpanel (default is the diffuse one)
            addMaterialPropertySubPanel(MaterialType.DIFFUSE, 5);
            //end of material property


            //add send button
            addSendButton(6);

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
            super.actionPerformed(e);
            switch(e.getActionCommand()){
                case "create":
                    MaterialType mType = materialsMap.get(materialComboBox.getSelectedItem());
                    Vector3f position = new Vector3f(Double.parseDouble(posX.getText()),
                            Double.parseDouble(posY.getText()), Double.parseDouble(posZ.getText()));
                    double radius = Double.parseDouble(textRadius.getText());
                    String albedoVal = albedoTextField.getText().substring(1, albedoTextField.getText().length()-1);
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
            addSceneObject(sphere, materialType, albedo, ior);
        }
    }

    class AddBoxFrame extends AddObjectFrame {

        //text field creation
        private TextField textFieldXMin = new TextField("-0.4",10);
        private TextField textFieldYMin = new TextField("-0.4",10);
        private TextField textFieldZMin = new TextField("0.4",10);
        private TextField textFieldXMax = new TextField("0.4",10);
        private TextField textFieldYMax = new TextField("0.4",10);
        private TextField textFieldZMax = new TextField("-0.4",10);
        private TextField textFieldXTranslation = new TextField("0", 10);
        private TextField textFieldYTranslation = new TextField("0", 10);
        private TextField textFieldZTranslation = new TextField("-4", 10);
        private TextField textFieldXRotation = new TextField("0", 10);
        private TextField textFieldYRotation = new TextField("0", 10);
        private TextField textFieldZRotation = new TextField("0", 10);

        public AddBoxFrame(Scene scene) {
            super(scene);

            //labels creation
            JLabel minLabel = new JLabel("Insert min extension coordinates");
            JLabel maxLabel = new JLabel("Insert max extension coordinates");
            JLabel translationLabel = new JLabel("Insert origin (translation to world coordinates)");
            JLabel rotationLabel = new JLabel("Insert rotation around the three main axis");
            JLabel x = new JLabel("X");
            JLabel y = new JLabel("Y");
            JLabel z = new JLabel("Z");
            JLabel x2 = new JLabel("X");
            JLabel y2 = new JLabel("Y");
            JLabel z2 = new JLabel("Z");
            JLabel x3 = new JLabel("X");
            JLabel y3 = new JLabel("Y");
            JLabel z3 = new JLabel("Z");
            JLabel x4 = new JLabel("X");
            JLabel y4 = new JLabel("Y");
            JLabel z4 = new JLabel("Z");




            int gridy = 0;
            GridBagConstraints c = new GridBagConstraints();

            //adding components to main panel

            //min extension
            c.fill = GridBagConstraints.HORIZONTAL;
            c.insets = new Insets(10, 0, 10, 0);
            c.gridy=gridy;
            c.gridx=0;
            c.gridwidth = 1;
            c.gridheight = 3;
            c.weightx = 0.6;
            mainPanel.add(minLabel, c);

            c.insets = new Insets(10, 0, 0, 0);
            c.gridx=1;
            c.gridy=gridy++;
            c.gridwidth=1;
            c.gridheight=1;
            c.weightx=0.2;
            mainPanel.add(x, c);
            c.gridx=2;
            mainPanel.add(textFieldXMin, c);

            c.insets = new Insets(0, 0, 0, 0);
            c.gridy=gridy++;
            c.gridx=1;
            mainPanel.add(y, c);
            c.gridx=2;
            mainPanel.add(textFieldYMin, c);

            c.gridy=gridy++;
            c.gridx=1;
            mainPanel.add(z, c);
            c.gridx=2;
            c.insets = new Insets(0, 0, 10, 0);
            mainPanel.add(textFieldZMin, c);


            //max extension
            c.insets = new Insets(10, 0, 10, 0);
            c.gridy=gridy;
            c.gridx=0;
            c.gridwidth = 1;
            c.gridheight = 3;
            c.weightx = 0.6;
            mainPanel.add(maxLabel, c);

            c.insets = new Insets(10, 0, 0, 0);
            c.gridx=1;
            c.gridy=gridy++;
            c.gridwidth=1;
            c.gridheight=1;
            c.weightx=0.2;
            mainPanel.add(x2, c);
            c.gridx=2;
            mainPanel.add(textFieldXMax, c);

            c.insets = new Insets(0, 0, 0, 0);
            c.gridy=gridy++;
            c.gridx=1;
            mainPanel.add(y2, c);
            c.gridx=2;
            mainPanel.add(textFieldYMax, c);

            c.gridy=gridy++;
            c.gridx=1;
            mainPanel.add(z2, c);
            c.gridx=2;
            c.insets = new Insets(0, 0, 10, 0);
            mainPanel.add(textFieldZMax, c);

            //translation
            c.insets = new Insets(10, 0, 10, 0);
            c.gridy=gridy;
            c.gridx=0;
            c.gridwidth = 1;
            c.gridheight = 3;
            c.weightx = 0.6;
            mainPanel.add(translationLabel, c);

            c.insets = new Insets(10, 0, 0, 0);
            c.gridx=1;
            c.gridy=gridy++;
            c.gridwidth=1;
            c.gridheight=1;
            c.weightx=0.2;
            mainPanel.add(x3, c);
            c.gridx=2;
            mainPanel.add(textFieldXTranslation, c);

            c.insets = new Insets(0, 0, 0, 0);
            c.gridy=gridy++;
            c.gridx=1;
            mainPanel.add(y3, c);
            c.gridx=2;
            mainPanel.add(textFieldYTranslation, c);

            c.gridy=gridy++;
            c.gridx=1;
            mainPanel.add(z3, c);
            c.gridx=2;
            c.insets = new Insets(0, 0, 10, 0);
            mainPanel.add(textFieldZTranslation, c);

            //rotation
            c.insets = new Insets(10, 0, 10, 0);
            c.gridy=gridy;
            c.gridx=0;
            c.gridwidth = 1;
            c.gridheight = 3;
            c.weightx = 0.6;
            mainPanel.add(rotationLabel, c);

            c.insets = new Insets(10, 0, 0, 0);
            c.gridx=1;
            c.gridy=gridy++;
            c.gridwidth=1;
            c.gridheight=1;
            c.weightx=0.2;
            mainPanel.add(x4, c);
            c.gridx=2;
            mainPanel.add(textFieldXRotation, c);

            c.insets = new Insets(0, 0, 0, 0);
            c.gridy=gridy++;
            c.gridx=1;
            mainPanel.add(y4, c);
            c.gridx=2;
            mainPanel.add(textFieldYRotation, c);

            c.gridy=gridy++;
            c.gridx=1;
            mainPanel.add(z4, c);
            c.gridx=2;
            c.insets = new Insets(0, 0, 10, 0);
            mainPanel.add(textFieldZRotation, c);

            c.insets = new Insets(10, 0, 10, 0);
            c.gridwidth=1;
            c.gridx=1;
            c.gridy=gridy++;
            c.weightx=0;
            mainPanel.add(materialComboBox, c);

            addMaterialComboBox(gridy++);
            addMaterialPropertySubPanel(MaterialType.DIFFUSE, gridy++);

            addSendButton(gridy);

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            super.actionPerformed(e);
            switch (e.getActionCommand()) {
                case "create":
                    MaterialType mType = materialsMap.get(materialComboBox.getSelectedItem());
                    Vector3f min = new Vector3f(Double.parseDouble(textFieldXMin.getText()),
                            Double.parseDouble(textFieldYMin.getText()), Double.parseDouble(textFieldZMin.getText()));
                    Vector3f max = new Vector3f(Double.parseDouble(textFieldXMax.getText()),
                            Double.parseDouble(textFieldYMax.getText()), Double.parseDouble(textFieldZMax.getText()));
                    Vector3f translation = new Vector3f(Double.parseDouble(textFieldXTranslation.getText()),
                            Double.parseDouble(textFieldYTranslation.getText()), Double.parseDouble(textFieldZTranslation.getText()));
                    Vector3f rotation = new Vector3f(Double.parseDouble(textFieldXRotation.getText()),
                            Double.parseDouble(textFieldYRotation.getText()), Double.parseDouble(textFieldZRotation.getText()));
                    String albedoVal = albedoTextField.getText().substring(1, albedoTextField.getText().length()-1);
                    String[] albedoA = albedoVal.split(",");
                    Vector3f albedo = new Vector3f(Arrays.stream(albedoA).mapToDouble((x) -> Double.parseDouble(x)).toArray());
                    albedo = albedo.mul((double)1/255);
                    double ior = Double.parseDouble(iorText.getText());
                    createBox(min, max, translation, rotation, mType, albedo, ior);
                    renderScene(this.scene);
                    break;
            }

        }

        private void createBox(Vector3f min, Vector3f max, Vector3f translation, Vector3f rotation, MaterialType materialType,
                               Vector3f albedo, double ior) {
            double anglex = rotation.getX();
            double angley = rotation.getY();
            double anglez = rotation.getZ();
            Matrix3D rotationMatrix = Matrix3D.rotationAroundzAxis(anglez).matrixMult(
                    Matrix3D.rotationAroundyAxis(angley)
            ).matrixMult(Matrix3D.rotationAroundxAxis(anglex));
            Matrix4D objectToWorld = new Matrix4D(rotationMatrix, translation);
            PhysicalBox box = new PhysicalBox(min, max, objectToWorld);
            triangulateAndAddSceneObject(box, materialType, albedo, ior, -1);
        }


    }



}
