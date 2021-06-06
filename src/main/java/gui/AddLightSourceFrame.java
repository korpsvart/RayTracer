package gui;

import rendering.*;


import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

abstract class AddLightSourceFrame extends JFrame implements ActionListener {

    private final Visualizer visualizer;
    protected Scene scene;

    private JLabel colorLabel = new JLabel("Select light color");
    protected Color currentColor = Color.white;
    private JLabel intensityLabel = new JLabel("Insert light intensity (number inside [0,1])");
    protected TextField textFieldIntensity = new TextField("0.7", 10);
    protected Panel mainPanel = new Panel(new GridBagLayout());
    protected JColorChooser colorChooser;
    protected JButton colorButton = new JButton("Choose color");
    protected TextField colorTextField = new TextField("(255,255,255)", 10);
    protected Panel colorSubPanel;
    protected int gridy = 0;
    protected JLabel x = new JLabel("x");
    protected JLabel y = new JLabel("y");
    protected JLabel z = new JLabel("z");
    protected TextField textFieldX = new TextField("1", 10);
    protected TextField textFieldY = new TextField("0", 10);
    protected TextField textFieldZ = new TextField("-1", 10);


    public AddLightSourceFrame(Visualizer visualizer, Scene scene) {
        this.visualizer = visualizer;
        this.scene = scene;
        createMainPanel();

    }

    private void createMainPanel() {
        setTitle("Add light");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        colorSubPanel = createChooseRGBPanel();

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;


        c.insets = new Insets(10, 0, 10, 0);
        c.gridy = gridy++;
        c.gridwidth = 2;
        mainPanel.add(intensityLabel, c);
        c.gridx = 2;
        c.gridwidth = 1;
        mainPanel.add(textFieldIntensity, c);

        c.gridy = gridy++;
        c.gridx=0;
        c.gridwidth=3;
        mainPanel.add(colorSubPanel, c);
        this.pack();
    }


    public AddLightSourceFrame(Visualizer visualizer, Scene scene, LightSource defaultLightSource) {
        this.visualizer = visualizer;
        this.scene = scene;
        currentColor = defaultLightSource.getColor().vectorToColor();
        createMainPanel();
    }

    private Panel createChooseRGBPanel() {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 0, 10, 0);
        Panel chooseRGBPanel = new Panel(new GridBagLayout());
        JLabel materialPropertyLabel = new JLabel("Select color:");
        c.gridy = 0;
        c.gridx = 0;
        c.weightx = 0.8;
        c.gridwidth = 1;
        chooseRGBPanel.add(materialPropertyLabel, c);
        JLabel rgbLabel = new JLabel("RGB:");
        c.gridx = 1;
        c.weightx = 0.2;
        TextField currentAlbedoLabel = new TextField("(" + currentColor.getRed() + "," + currentColor.getGreen() + "," + currentColor.getBlue() + ")");
        this.colorTextField = currentAlbedoLabel;
        currentAlbedoLabel.setEditable(false);
        c.gridx = 1;
        c.weightx = 0.1;
        chooseRGBPanel.add(currentAlbedoLabel, c);
        JButton openColorChooserButton = new JButton("Pick a color");
        this.colorButton = openColorChooserButton;
        openColorChooserButton.setActionCommand("colorChoose");
        openColorChooserButton.addActionListener(this);
        c.gridx = 2;
        c.weightx = 0.1;
        chooseRGBPanel.add(openColorChooserButton, c);
        return chooseRGBPanel;
    }

    public void createFrame() {
        add(mainPanel);
        pack();
        setSize(400, 400);
        setVisible(true);
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
                colorTextField.setText("(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ")");
                break;
            case "create":
                this.addLightSource();
                break;
        }
    }


    protected void addLightSource() {
        double x = Double.parseDouble(textFieldX.getText());
        double y = Double.parseDouble(textFieldY.getText());
        double z = Double.parseDouble(textFieldZ.getText());
        Vector3d xyz = new Vector3d(x, y, z);
        double intensity = Double.parseDouble(textFieldIntensity.getText());
        if (intensity < 0 || intensity > 1) {
            JOptionPane.showMessageDialog(this,
                    "Intensity value must be inside [0,1]",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            throw new IllegalArgumentException("Intensity value must be inside [0,1]");
        }
        String colorVal = colorTextField.getText().substring(1, colorTextField.getText().length() - 1);
        String[] colorA = colorVal.split(",");
        Vector3d color = new Vector3d(Arrays.stream(colorA).mapToDouble((h) -> Double.parseDouble(h)).toArray());
        color = color.mul((double) 1 / 255);
        LightSource lightSource = getParticularLightSource(intensity, color, xyz);
        lightSource.setNormalizedIntensity(intensity);
        scene.addLightSource(lightSource);
        visualizer.renderSceneWithoutRebuildingBVH(scene);
        this.dispose();
    }

    abstract LightSource getParticularLightSource(double intensity, Vector3d color, Vector3d xyz);

    protected void addSendButton(int gridy) {
        //Add button to send data
        GridBagConstraints c = new GridBagConstraints();
        JButton sendButton = new JButton("Create");
        sendButton.setActionCommand("create");
        sendButton.addActionListener(this);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = gridy;
        c.gridx = 0;
        c.weightx = 0;
        c.gridwidth = 3;
        c.insets = new Insets(10, 0, 10, 0);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(sendButton);
        mainPanel.add(buttonPanel, c);
    }
}
