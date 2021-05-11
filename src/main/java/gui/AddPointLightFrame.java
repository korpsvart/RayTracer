package gui;

import rendering.*;

import javax.swing.*;
import java.awt.*;

class AddPointLightFrame extends AddLightSourceFrame {
    //only positional data
    private JLabel labelLightPosition = new JLabel("Insert light position");
    private boolean removeMode = false;
    private PointLight defaultPointLight;

    public AddPointLightFrame(Visualizer visualizer, Scene scene) {
        super(visualizer,scene);
        createMainPanel();
    }

    private void createMainPanel() {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 0, 10, 0);
        c.gridy = gridy;
        c.weightx = 0.6;
        c.gridheight = 3;
        mainPanel.add(labelLightPosition, c);
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

    public AddPointLightFrame(Visualizer visualizer, Scene scene, PointLight pointLight) {
        super(visualizer, scene, pointLight);
        defaultPointLight = pointLight;
        removeMode = true;
        textFieldX.setText(String.valueOf(defaultPointLight.getPosition().getX()));
        textFieldY.setText(String.valueOf(defaultPointLight.getPosition().getY()));
        textFieldZ.setText(String.valueOf(defaultPointLight.getPosition().getZ()));
        textFieldIntensity.setText(String.valueOf(defaultPointLight.getNormalizedIntensity()));
        createMainPanel();
    }



    @Override
    LightSource getParticularLightSource(double intensity, Vector3f color, Vector3f xyz) {
        //remapping intensity over [0,150]
        intensity *= 150;
        if (removeMode) {
            scene.removeLightSource(defaultPointLight);
        }
        PointLight pointLight = new PointLight(color, intensity, xyz);
        return pointLight;
    }


}
