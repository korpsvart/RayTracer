package gui;

import rendering.*;

import javax.swing.*;
import java.awt.*;

class AddBoxFrame extends AddObjectFrame {

    //text field creation
    private TextField textFieldXMin = new TextField("-0.2", 10);
    private TextField textFieldYMin = new TextField("-0.2", 10);
    private TextField textFieldZMin = new TextField("-0.2", 10);
    private TextField textFieldXMax = new TextField("0.2", 10);
    private TextField textFieldYMax = new TextField("0.2", 10);
    private TextField textFieldZMax = new TextField("0.2", 10);


    public AddBoxFrame(Visualizer visualizer, Scene scene) {
        super(visualizer, scene);

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
        c.gridy = gridy;
        c.gridx = 0;
        c.gridwidth = 1;
        c.gridheight = 3;
        c.weightx = 0.6;
        mainPanel.add(minLabel, c);

        c.insets = new Insets(10, 0, 0, 0);
        c.gridx = 1;
        c.gridy = gridy++;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0.2;
        mainPanel.add(x, c);
        c.gridx = 2;
        mainPanel.add(textFieldXMin, c);

        c.insets = new Insets(0, 0, 0, 0);
        c.gridy = gridy++;
        c.gridx = 1;
        mainPanel.add(y, c);
        c.gridx = 2;
        mainPanel.add(textFieldYMin, c);

        c.gridy = gridy++;
        c.gridx = 1;
        mainPanel.add(z, c);
        c.gridx = 2;
        c.insets = new Insets(0, 0, 10, 0);
        mainPanel.add(textFieldZMin, c);


        //max extension
        c.insets = new Insets(10, 0, 10, 0);
        c.gridy = gridy;
        c.gridx = 0;
        c.gridwidth = 1;
        c.gridheight = 3;
        c.weightx = 0.6;
        mainPanel.add(maxLabel, c);

        c.insets = new Insets(10, 0, 0, 0);
        c.gridx = 1;
        c.gridy = gridy++;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0.2;
        mainPanel.add(x2, c);
        c.gridx = 2;
        mainPanel.add(textFieldXMax, c);

        c.insets = new Insets(0, 0, 0, 0);
        c.gridy = gridy++;
        c.gridx = 1;
        mainPanel.add(y2, c);
        c.gridx = 2;
        mainPanel.add(textFieldYMax, c);

        c.gridy = gridy++;
        c.gridx = 1;
        mainPanel.add(z2, c);
        c.gridx = 2;
        c.insets = new Insets(0, 0, 10, 0);
        mainPanel.add(textFieldZMax, c);

        addOTWSubPanel(gridy++);

        c.insets = new Insets(10, 0, 10, 0);
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = gridy++;
        c.weightx = 0;
        mainPanel.add(materialComboBox, c);

        addMaterialComboBox(gridy++);
        addMaterialPropertySubPanel(MaterialType.DIFFUSE, gridy++);

        addSendButton(gridy);
        setSizeToContent(3, gridy, 200, 60);

    }


    @Override
    GeometricObject createGeometricObject() {
        Vector3f min = new Vector3f(Double.parseDouble(textFieldXMin.getText()),
                Double.parseDouble(textFieldYMin.getText()), Double.parseDouble(textFieldZMin.getText()));
        Vector3f max = new Vector3f(Double.parseDouble(textFieldXMax.getText()),
                Double.parseDouble(textFieldYMax.getText()), Double.parseDouble(textFieldZMax.getText()));
        PhysicalBox box = new PhysicalBox(min, max, getOTWMatrix());
        return box;
    }


}
