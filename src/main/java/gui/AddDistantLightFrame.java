package main.java.gui;

import main.java.rendering.*;

import javax.swing.*;
import java.awt.*;

class AddDistantLightFrame extends AddLightSourceFrame {
    //only directional data
    private JLabel labelLightDirection = new JLabel("Insert light direction");

    public AddDistantLightFrame(Visualizer visualizer, Scene scene) {
        super(visualizer, scene);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = gridy;
        c.weightx = 0.6;
        c.gridheight = 3;
        c.insets = new Insets(10, 0, 10, 0);
        mainPanel.add(labelLightDirection, c);
        c.weightx = 0.2;
        c.gridheight = 1;
        c.gridx = 1;
        c.gridy = gridy++;
        c.insets = new Insets(10, 0, 0, 0);
        mainPanel.add(x, c);
        c.gridx = 2;
        mainPanel.add(textFieldX, c);
        c.gridx = 1;
        c.gridy = gridy++;
        c.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(y, c);
        c.gridx = 2;
        mainPanel.add(textFieldY, c);
        c.gridx = 1;
        c.gridy = gridy++;
        c.insets = new Insets(0, 0, 10, 0);
        mainPanel.add(z, c);
        c.gridx = 2;
        mainPanel.add(textFieldZ, c);

        addSendButton(gridy++);


        createFrame();
    }

    @Override
    void addParticularLightSource(double intensity, Vector3f color, Vector3f xyz) {
        //remapping intensity over [0,10]
        intensity *= 10;
        DistantLight distantLight = new DistantLight(color, intensity, xyz);
        scene.addLightSource(distantLight);
    }
}
