package gui;

import rendering.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;

abstract class AddObjectFrame extends Frame implements ActionListener, WindowListener {


    private final Visualizer visualizer;
    protected TextField albedoTextField;
    protected JColorChooser colorChooser;
    protected Button albedoButton;
    protected TextField iorText;
    protected JLabel materialLabel;
    protected Scene scene;
    protected Panel mainPanel;
    protected int materialSubPanelGridy;
    protected Panel albedoSubPanel;
    protected TextField textFieldXTranslation = new TextField("0", 10);
    protected TextField textFieldYTranslation = new TextField("0", 10);
    protected TextField textFieldZTranslation = new TextField("-4", 10);
    protected TextField textFieldXRotation = new TextField("0", 10);
    protected TextField textFieldYRotation = new TextField("0", 10);
    protected TextField textFieldZRotation = new TextField("0", 10);
    protected TextField textFieldXScaling = new TextField("1", 10);
    protected TextField textFieldYScaling = new TextField("1", 10);
    protected TextField textFieldZScaling = new TextField("1", 10);
    protected Panel otwSubPanel = createOTWSubPanel();
    protected Panel transparentSubPanel;
    protected Color currentColor = Color.white;
    protected JComboBox materialComboBox;
    protected int divs = 16;
    private final JLabel divisionLabel = new JLabel("Number of subdivisions for triangulation: ");
    private final TextField divisionTextField = new TextField(10);

    public AddObjectFrame(Visualizer visualizer, Scene scene) {
        this.visualizer = visualizer;

        addWindowListener(this);
        GridBagConstraints c = new GridBagConstraints();
        Panel panel = new Panel(new GridBagLayout());
        this.mainPanel = panel;
        this.scene = scene;
        this.materialLabel = new JLabel("Select type of material");
        JComboBox comboBoxMaterial = new JComboBox(Visualizer.materials);
        this.materialComboBox = comboBoxMaterial;
        materialComboBox.addActionListener(this);
        //define albedo subpanel
        albedoSubPanel = createChooseRGBPanel();


        //define transparent subpanel
        this.transparentSubPanel = new Panel(new GridBagLayout());
        JLabel iorLabel = new JLabel("Insert index of refraction");
        c.gridy = 0;
        c.gridx = 0;
        c.weightx = 0.8;
        c.gridwidth = 2;
        transparentSubPanel.add(iorLabel, c);
        TextField iorTextField = new TextField("1.5");
        this.iorText = iorTextField;
        c.gridx = 2;
        c.gridwidth = 1;
        c.weightx = 0.2;
        transparentSubPanel.add(iorTextField, c);


    }


    protected void addOTWSubPanel(int gridy) {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = gridy;
        c.weightx=1;
        c.gridwidth=3;
        mainPanel.add(otwSubPanel, c);
    }

    protected void setDefaultOTW(Vector3f translation, Vector3f rotation) {
        textFieldXTranslation.setText(Double.toString(translation.getX()));
        textFieldYTranslation.setText(Double.toString(translation.getY()));
        textFieldZTranslation.setText(Double.toString(translation.getZ()));
        textFieldXRotation.setText(Double.toString(rotation.getX()));
        textFieldYRotation.setText(Double.toString(rotation.getX()));
        textFieldZRotation.setText(Double.toString(rotation.getX()));
    }

    protected void setDefaultOTW(Vector3f translation, Vector3f rotation, Vector3f scaling) {
        textFieldXTranslation.setText(Double.toString(translation.getX()));
        textFieldYTranslation.setText(Double.toString(translation.getY()));
        textFieldZTranslation.setText(Double.toString(translation.getZ()));
        textFieldXRotation.setText(Double.toString(rotation.getX()));
        textFieldYRotation.setText(Double.toString(rotation.getX()));
        textFieldZRotation.setText(Double.toString(rotation.getX()));
        textFieldXRotation.setText(Double.toString(scaling.getX()));
        textFieldYRotation.setText(Double.toString(scaling.getX()));
        textFieldZRotation.setText(Double.toString(scaling.getX()));
    }

    private Panel createOTWSubPanel() {
        Panel panel = new Panel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        int gridy = 0;
        JLabel translationLabel = new JLabel("Insert origin (translation to world coordinates)");
        JLabel rotationLabel = new JLabel("Insert rotation around the three main axis");
        JLabel scalingLabel = new JLabel("Insert scaling");
        JLabel x = new JLabel("X");
        JLabel y = new JLabel("Y");
        JLabel z = new JLabel("Z");
        JLabel x2 = new JLabel("X");
        JLabel y2 = new JLabel("Y");
        JLabel z2 = new JLabel("Z");
        JLabel x3 = new JLabel("X");
        JLabel y3 = new JLabel("Y");
        JLabel z3 = new JLabel("Z");


        //translation

        c.insets = new Insets(10, 0, 10, 0);
        c.gridy = gridy;
        c.gridx = 0;
        c.gridwidth = 1;
        c.gridheight = 3;
        c.weightx = 0.6;
        panel.add(translationLabel, c);

        c.insets = new Insets(10, 0, 0, 0);
        c.gridx = 1;
        c.gridy = gridy++;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0.2;
        panel.add(x, c);
        c.gridx = 2;
        panel.add(textFieldXTranslation, c);

        c.insets = new Insets(0, 0, 0, 0);
        c.gridy = gridy++;
        c.gridx = 1;
        panel.add(y, c);
        c.gridx = 2;
        panel.add(textFieldYTranslation, c);

        c.gridy = gridy++;
        c.gridx = 1;
        panel.add(z, c);
        c.gridx = 2;
        c.insets = new Insets(0, 0, 10, 0);
        panel.add(textFieldZTranslation, c);

        //rotation
        c.insets = new Insets(10, 0, 10, 0);
        c.gridy = gridy;
        c.gridx = 0;
        c.gridwidth = 1;
        c.gridheight = 3;
        c.weightx = 0.6;
        panel.add(rotationLabel, c);

        c.insets = new Insets(10, 0, 0, 0);
        c.gridx = 1;
        c.gridy = gridy++;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0.2;
        panel.add(x2, c);
        c.gridx = 2;
        panel.add(textFieldXRotation, c);

        c.insets = new Insets(0, 0, 0, 0);
        c.gridy = gridy++;
        c.gridx = 1;
        panel.add(y2, c);
        c.gridx = 2;
        panel.add(textFieldYRotation, c);

        c.gridy = gridy++;
        c.gridx = 1;
        panel.add(z2, c);
        c.gridx = 2;
        c.insets = new Insets(0, 0, 10, 0);
        panel.add(textFieldZRotation, c);


        //rotation
        c.insets = new Insets(10, 0, 10, 0);
        c.gridy = gridy;
        c.gridx = 0;
        c.gridwidth = 1;
        c.gridheight = 3;
        c.weightx = 0.6;
        panel.add(scalingLabel, c);

        c.insets = new Insets(10, 0, 0, 0);
        c.gridx = 1;
        c.gridy = gridy++;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0.2;
        panel.add(x3, c);
        c.gridx = 2;
        panel.add(textFieldXScaling, c);

        c.insets = new Insets(0, 0, 0, 0);
        c.gridy = gridy++;
        c.gridx = 1;
        panel.add(y3, c);
        c.gridx = 2;
        panel.add(textFieldYScaling, c);

        c.gridy = gridy++;
        c.gridx = 1;
        panel.add(z3, c);
        c.gridx = 2;
        c.insets = new Insets(0, 0, 10, 0);
        panel.add(textFieldZScaling, c);
        return panel;
    }

    private Panel createChooseRGBPanel() {
        GridBagConstraints c = new GridBagConstraints();
        Panel chooseRGBPanel = new Panel(new GridBagLayout());
        JLabel materialPropertyLabel = new JLabel("Select albedo:");
        c.gridy = 0;
        c.gridx = 0;
        c.weightx = 0.5;
        c.gridwidth = 1;
        chooseRGBPanel.add(materialPropertyLabel, c);
        JLabel rgbLabel = new JLabel("RGB:");
        c.gridx = 1;
        c.weightx = 0.2;
        TextField currentAlbedoLabel = new TextField("(255,255,255)");
        this.albedoTextField = currentAlbedoLabel;
        currentAlbedoLabel.setEditable(false);
        c.gridx = 2;
        c.weightx = 0.2;
        chooseRGBPanel.add(currentAlbedoLabel, c);
        Button openColorChooserButton = new Button("Select albedo");
        this.albedoButton = openColorChooserButton;
        openColorChooserButton.setActionCommand("colorChoose");
        openColorChooserButton.addActionListener(this);
        c.gridx = 3;
        c.weightx = 0.2;
        chooseRGBPanel.add(openColorChooserButton, c);
        return chooseRGBPanel;
    }

    protected void addMaterialComboBox(int gridy) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = gridy;
        c.gridx = 0;
        c.gridwidth = 2;
        c.weightx = 0.6;
        mainPanel.add(materialLabel, c);
        c.gridx = 2;
        c.weightx = 0.6;
        mainPanel.add(materialComboBox, c);
    }

    protected void addMaterialPropertySubPanel(MaterialType materialType, int gridy) {
        this.materialSubPanelGridy = gridy;
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 0, 10, 0);
        c.gridy = gridy;
        c.gridx = 0;
        c.weightx = 0;
        c.gridwidth = 3;
        switch (materialType) {
            case DIFFUSE:
            case PHONG:
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
//        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = gridy;
        c.gridx = 1;
        c.weightx = 0;
        c.gridwidth = 1;
        c.insets = new Insets(10, 0, 10, 0);
        mainPanel.add(sendButton, c);


        add(mainPanel);
        pack();
        setVisible(true);
    }

    abstract GeometricObject createGeometricObject();

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
                albedoTextField.setText("(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ")");
                break;
            case "comboBoxChanged":
                JComboBox cb = (JComboBox) e.getSource();
                MaterialType materialType = Visualizer.materialsMap.get(cb.getSelectedItem());
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
            case "create":
                MaterialType mType = Visualizer.materialsMap.get(materialComboBox.getSelectedItem());
                String albedoVal = albedoTextField.getText().substring(1, albedoTextField.getText().length() - 1);
                String[] albedoA = albedoVal.split(",");
                Vector3f albedo = new Vector3f(Arrays.stream(albedoA).mapToDouble((x) -> Double.parseDouble(x)).toArray());
                albedo = albedo.mul((double) 1 / 255);
                double ior = Double.parseDouble(iorText.getText());
                GeometricObject geometricObject = createGeometricObject();
                if (geometricObject.isTriangulated()) {
                    try {
                        divs = Integer.parseInt(divisionTextField.getText());
                    } catch (NumberFormatException exception) {
                        divs = 16;
                    }
                    triangulateAndAddSceneObject(geometricObject, mType, albedo, ior, divs);
                } else {
                    addSceneObject(geometricObject, mType, albedo, ior);
                }
                visualizer.renderScene(this.scene);
                break;
        }
    }

    protected Vector3f getTranslation() {
        //I add a little shift in x direction to translation
        //because if the box is exactly in (0,0,z) position there's an annoying triangulation visual effect
        //(read the ray triangle intersection routine inside TriangleMesh for a detailed explanation)
        return new Vector3f(Double.parseDouble(textFieldXTranslation.getText()) + 10e-6,
                Double.parseDouble(textFieldYTranslation.getText()), Double.parseDouble(textFieldZTranslation.getText()));
    }

    protected Vector3f getRotation() {
        return new Vector3f(Double.parseDouble(textFieldXRotation.getText()),
                Double.parseDouble(textFieldYRotation.getText()), Double.parseDouble(textFieldZRotation.getText()));
    }

    protected Vector3f getScaling() {
        return new Vector3f(Double.parseDouble(textFieldXScaling.getText()),
                Double.parseDouble(textFieldYScaling.getText()), Double.parseDouble(textFieldZScaling.getText()));
    }

    protected Matrix4D getOTWMatrix() {
        Vector3f translation = getTranslation();
        Vector3f rotation = getRotation();
        Vector3f scaling = getScaling();
        double anglex = rotation.getX();
        double angley = rotation.getY();
        double anglez = rotation.getZ();
        Matrix3D rotationMatrix = Matrix3D.rotationAroundzAxis(anglez).matrixMult(
                Matrix3D.rotationAroundyAxis(angley)
        ).matrixMult(Matrix3D.rotationAroundxAxis(anglex));
        Matrix3D scalingMatrix = new Matrix3D(new Vector3f[]{
                new Vector3f(scaling.getX(), 0, 0),
                new Vector3f(0, scaling.getY(), 0),
                new Vector3f(0, 0, scaling.getZ())
        }, Matrix3D.COL_VECTOR);
        Matrix3D a = scalingMatrix.matrixMult(rotationMatrix);
        Matrix4D objectToWorld = new Matrix4D(a, translation);
        return objectToWorld;
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

    protected void setSizeToContent(int gridx, int gridy, int scalex, int scaley) {
        setSize((gridx)*scalex, (gridy)*scaley);
    }


    protected void addDivsPanel(int gridy) {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = gridy;
        mainPanel.add(divisionLabel, c);
        divisionTextField.setText(String.valueOf(divs));
        mainPanel.add(divisionTextField, c);
        divisionTextField.addActionListener(this);
    }
}
