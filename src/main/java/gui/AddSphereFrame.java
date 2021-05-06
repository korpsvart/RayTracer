package gui;

import rendering.*;

import javax.swing.*;
import java.awt.*;

class AddSphereFrame extends AddObjectFrame {


    private TextField posX, posY, posZ;
    private Sphere defaultSphere;
    private TextField textRadius;
    private Color currentColor = Color.white;
    private JColorChooser colorChooser;


    public AddSphereFrame(Visualizer visualizer, Scene scene) {
        super(visualizer, scene);

        addSphereFrameConstructor();

    }

    private void addSphereFrameConstructor() {
        setTitle("Add sphere");
        GridBagConstraints c = new GridBagConstraints();

        //Start of position input graphics
        JLabel positionLabel = new JLabel("Insert position:");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 3;
        c.weightx = 0.6;
        mainPanel.add(positionLabel, c);
        JLabel x = new JLabel("X");
        JLabel y = new JLabel("Y");
        JLabel z = new JLabel("Z");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 1;
        c.weightx = 0.1;
        mainPanel.add(x, c);
        TextField textFieldX = new TextField("0", 10);
        posX = textFieldX;
        textFieldX.addActionListener(this);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 0;
        c.gridheight = 1;
        c.weightx = 0.3;
        mainPanel.add(textFieldX, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        c.gridheight = 1;
        c.weightx = 0.1;
        mainPanel.add(y, c);
        TextField textFieldY = new TextField("0", 10);
        posY = textFieldY;
        textFieldY.addActionListener(this);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 1;
        c.gridheight = 1;
        c.weightx = 0.3;
        mainPanel.add(textFieldY, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 2;
        c.gridheight = 1;
        c.weightx = 0.1;
        mainPanel.add(z, c);
        TextField textFieldZ = new TextField("-5", 10);
        posZ = textFieldZ;
        textFieldZ.addActionListener(this);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 2;
        c.gridheight = 1;
        c.weightx = 0.3;
        mainPanel.add(textFieldZ, c);
        //end of position input graphics

        //Start of radius input graphics
        JLabel radiusLabel = new JLabel("Insert radius:");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 3;
        c.gridheight = 1;
        c.gridwidth = 2;
        c.weightx = 0.6;
        c.insets = new Insets(20, 0, 20, 0);
        mainPanel.add(radiusLabel, c);
        TextField textFieldRadius = new TextField("0.3", 10);
        textRadius = textFieldRadius;
        textFieldRadius.addActionListener(this);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 3;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 0.4;
        mainPanel.add(textFieldRadius, c);
        //end of radius input graphics

        //material label and combo box
        addMaterialComboBox(4);


        //material property
        //(changes according to selected option) - default is albedo (for diffuse objects)
        //add current active subpanel (default is the diffuse one)
        addMaterialPropertySubPanel(5);
        //end of material property


        //add send button
        addSendButton(6);
        setSizeToContent(3, 7, 200, 60);
    }

    public AddSphereFrame(Visualizer visualizer, Scene scene, SceneObject defaultSceneObject) {
        super(visualizer, scene, defaultSceneObject);
        addSphereFrameConstructor();
        defaultSphere = (Sphere)defaultSceneObject.getGeometricObject();
        posX.setText(String.valueOf(defaultSphere.getCentre().getX()));
        posY.setText(String.valueOf(defaultSphere.getCentre().getY()));
        posZ.setText(String.valueOf(defaultSphere.getCentre().getZ()));
        textRadius.setText(String.valueOf(defaultSphere.getRadius()));
    }

    protected GeometricObject createGeometricObject() {
        Vector3f position = new Vector3f(Double.parseDouble(posX.getText()),
                Double.parseDouble(posY.getText()), Double.parseDouble(posZ.getText()));
        double radius = Double.parseDouble(textRadius.getText());
        Sphere sphere = new Sphere(position, radius);
        return sphere;
    }
}
