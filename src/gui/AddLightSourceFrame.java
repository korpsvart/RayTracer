package gui;

import rendering.Scene;
import rendering.Vector3f;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Arrays;

abstract class AddLightSourceFrame extends Frame implements ActionListener, WindowListener {

    private final Visualizer visualizer;
    protected Scene scene;

    private JLabel colorLabel = new JLabel("Select light color");
    protected Color currentColor = Color.white;
    private JLabel intensityLabel = new JLabel("Insert light intensity (number inside [0,1])");
    protected TextField textFieldIntensity = new TextField("0.7", 10);
    protected Panel mainPanel = new Panel(new GridBagLayout());
    protected JColorChooser colorChooser;
    protected Button colorButton = new Button("Choose color");
    protected TextField colorTextField = new TextField("(255,255,255)", 10);
    protected Panel colorSubPanel = createChooseRGBPanel();
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
        this.addWindowListener(this);

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
        mainPanel.add(colorSubPanel, c);

    }

    private Panel createChooseRGBPanel() {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 0, 10, 0);
        Panel chooseRGBPanel = new Panel(new GridBagLayout());
        JLabel materialPropertyLabel = new JLabel("Select color:");
        c.gridy = 0;
        c.gridx = 0;
        c.weightx = 0.5;
        c.gridwidth = 1;
        chooseRGBPanel.add(materialPropertyLabel, c);
        JLabel rgbLabel = new JLabel("RGB:");
        c.gridx = 1;
        c.weightx = 0.2;
        TextField currentAlbedoLabel = new TextField("(255,255,255)");
        this.colorTextField = currentAlbedoLabel;
        currentAlbedoLabel.setEditable(false);
        c.gridx = 2;
        c.weightx = 0.2;
        chooseRGBPanel.add(currentAlbedoLabel, c);
        Button openColorChooserButton = new Button("Pick a color");
        this.colorButton = openColorChooserButton;
        openColorChooserButton.setActionCommand("colorChoose");
        openColorChooserButton.addActionListener(this);
        c.gridx = 3;
        c.weightx = 0.2;
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

    protected void addLightSource() {
        double x = Double.parseDouble(textFieldX.getText());
        double y = Double.parseDouble(textFieldY.getText());
        double z = Double.parseDouble(textFieldZ.getText());
        Vector3f xyz = new Vector3f(x, y, z);
        double intensity = Double.parseDouble(textFieldIntensity.getText());
        String colorVal = colorTextField.getText().substring(1, colorTextField.getText().length() - 1);
        String[] colorA = colorVal.split(",");
        Vector3f color = new Vector3f(Arrays.stream(colorA).mapToDouble((h) -> Double.parseDouble(h)).toArray());
        color = color.mul((double) 1 / 255);
        addParticularLightSource(intensity, color, xyz);
        visualizer.renderScene(scene);
    }

    abstract void addParticularLightSource(double intensity, Vector3f color, Vector3f xyz);

    protected void addSendButton(int gridy) {
        //Add button to send data
        GridBagConstraints c = new GridBagConstraints();
        Button sendButton = new Button("Create");
        sendButton.setActionCommand("create");
        sendButton.addActionListener(this);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = gridy;
        c.gridx = 1;
        c.weightx = 0;
        c.gridwidth = 1;
        c.insets = new Insets(10, 0, 10, 0);
        mainPanel.add(sendButton, c);
    }
}
