package gui;

import rendering.MonteCarloSampling;
import rendering.Scene;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class SettingsFrame extends JFrame implements ActionListener {

    private JPanel mainPanel = new JPanel(new GridBagLayout());
    private Visualizer visualizer;
    private Scene scene;
    GridBagConstraints c = new GridBagConstraints();
    JCheckBox indirectDiffuseCB;
    JCheckBox shadowMirrorCB;
    TextField samplesNTextField;
    JButton applyButton;
    TextField widthTextField;
    TextField heightTextField;
    TextField maxDepthTextField;
    public SettingsFrame(Visualizer visualizer, Scene scene) {
        c.fill = GridBagConstraints.HORIZONTAL;
        setTitle("Settings");
        this.visualizer = visualizer;
        this.scene = scene;
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        //resolution section
        JLabel resolutionLabel = new JLabel("Resolution");
        JLabel widthLabel = new JLabel("Width");
        JLabel heightLabel = new JLabel("Height");
        widthTextField = new TextField(10);
        heightTextField = new TextField(10);
        widthTextField.setText(String.valueOf(scene.getWidth()));
        heightTextField.setText(String.valueOf(scene.getHeight()));
        c.gridy++;
        mainPanel.add(resolutionLabel, c);
        c.gridy++;
        mainPanel.add(widthLabel, c);
        c.gridx=1;
        mainPanel.add(widthTextField, c);
        c.gridy++;
        c.gridx=0;
        mainPanel.add(heightLabel, c);
        c.gridx=1;
        mainPanel.add(heightTextField, c);
        c.gridy++;
        c.gridx=0;
        c.gridwidth=2;
        c.insets  = new Insets(20,0,0,0);
        mainPanel.add(new JSeparator(SwingConstants.HORIZONTAL), c);
        c.gridy++;
        c.gridwidth=1;




        //indirect diffuse section
        JLabel indirectDiffuse = new JLabel("Simulate indirect diffuse light: ");
        indirectDiffuseCB = new JCheckBox();
        indirectDiffuseCB.setActionCommand("indirect_diffuse");
        indirectDiffuseCB.addActionListener(this);
        JLabel samplesN = new JLabel("Number of samples: ");
        samplesNTextField = new TextField(10);
        if (Scene.isSimulateIndirectDiffuse()) {
            indirectDiffuseCB.setSelected(true);
        } else {
            samplesNTextField.setEnabled(false);
        }
        samplesNTextField.setText(String.valueOf(MonteCarloSampling.getSamplingN()));
        mainPanel.add(indirectDiffuse,c);
        c.gridx=1;
        mainPanel.add(indirectDiffuseCB,c);
        c.insets  = new Insets(0,0,0,0);
        c.gridy++;
        c.gridx=0;
        mainPanel.add(samplesN, c);
        c.gridx=1;
        mainPanel.add(samplesNTextField, c);
        c.gridy++;

        c.gridx=0;
        c.gridwidth=2;
        c.insets  = new Insets(20,0,0,0);
        mainPanel.add(new JSeparator(SwingConstants.HORIZONTAL), c);
        c.gridy++;
        c.gridwidth=1;

        //Max ray depth section
        JLabel maxDepthLabel = new JLabel("Max ray depth");
        maxDepthTextField = new TextField(String.valueOf(scene.getMaxRayDepth()));

        c.gridx=0;
        mainPanel.add(maxDepthLabel, c);
        c.gridx=1;
        mainPanel.add(maxDepthTextField, c);
        c.gridy++;


        c.gridx=0;
        c.gridwidth=2;
        c.insets  = new Insets(20,0,0,0);
        mainPanel.add(new JSeparator(SwingConstants.HORIZONTAL), c);
        c.gridy++;
        c.gridwidth=1;

        //shadow on mirror objects section
        c.insets = new Insets(0,0,0,0);
        JLabel shadowMirrorLabel = new JLabel("Cast shadows on reflective objects: ");
        shadowMirrorCB = new JCheckBox();
        shadowMirrorCB.setActionCommand("shadow_mirror");
        shadowMirrorCB.addActionListener(this);
        mainPanel.add(shadowMirrorLabel, c);
        c.gridx=1;
        mainPanel.add(shadowMirrorCB, c);

        if (scene.isShadowMirror()) {
            shadowMirrorCB.setSelected(true);
        }

        c.gridy++;

        c.insets = new Insets(20, 0, 0, 0);




        //apply button
        applyButton = new JButton("Apply");
        applyButton.setActionCommand("apply");
        applyButton.addActionListener(this);

        c.gridx=0;
        c.weightx=1;
        c.gridwidth=2;
        mainPanel.add(applyButton, c);
        c.gridy++;


        this.add(mainPanel);
        this.pack();
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "indirect_diffuse":
                if (indirectDiffuseCB.isSelected())
                    samplesNTextField.setEnabled(true);
                else
                    samplesNTextField.setEnabled(false);
                break;
            case "apply":
                    if (indirectDiffuseCB.isSelected()) {
                        Scene.setSimulateIndirectDiffuse(true);
                        try {
                            MonteCarloSampling.setSamplingN(Integer.parseInt(samplesNTextField.getText()));
                        } catch (NumberFormatException exception) {
                            JOptionPane.showMessageDialog(this,
                                    "Only numeric integer input is accepted!",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                            throw exception;
                        }
                    } else {
                        Scene.setSimulateIndirectDiffuse(false);
                    }
                    if (shadowMirrorCB.isSelected()) {
                        scene.setShadowMirror(true);
                    }
                try {
                    scene.setMaxRayDepth(Integer.parseInt(maxDepthTextField.getText()));
                    int width = Integer.parseInt(widthTextField.getText());
                    int height = Integer.parseInt(heightTextField.getText());
                    if (width == scene.getWidth() && height == scene.getHeight()) {
                        //avoid re-creation of visualizer
                        visualizer.renderScene(scene);
                    } else {
                        scene.setWidth(width);
                        scene.setHeight(height);
                        visualizer.dispose();
                        scene.setImg(new BufferedImage(scene.getWidth(), scene.getHeight(), BufferedImage.TYPE_INT_RGB));
                        Visualizer visualizer1 = new Visualizer(scene);
                        visualizer1.renderScene(scene);
                        visualizer1.setVisible(true);
                    }
                    this.dispose();
                } catch (NumberFormatException exception) {
                    JOptionPane.showMessageDialog(this,
                            "Only numeric integer input is accepted!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    throw exception;
                }

                break;
        }
    }
}
