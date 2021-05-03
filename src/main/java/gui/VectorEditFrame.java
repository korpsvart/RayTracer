package main.java.gui;


import main.java.rendering.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

class VectorEditFrame extends Frame implements WindowListener {

    protected JLabel x = new JLabel("x");
    protected JLabel y = new JLabel("y");
    protected JLabel z = new JLabel("z");
    protected TextField textFieldX;
    protected TextField textFieldY;
    protected TextField textFieldZ;
    private ControlPointsFrame mainFrame;
    int i, j;

    public VectorEditFrame(int i, int j, Vector3f data, ControlPointsFrame mainFrame) {
        addWindowListener(this);
        this.i = i;
        this.j = j;
        textFieldX = new TextField(Double.toString(data.getX()), 10);
        textFieldY = new TextField(Double.toString(data.getY()), 10);
        textFieldZ = new TextField(Double.toString(data.getZ()), 10);
        this.mainFrame = mainFrame;

        Panel mainPanel = new Panel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        int gridy = 0;

        c.insets = new Insets(20, 20, 20, 20);
        c.gridy = gridy++;
        mainPanel.add(x, c);
        c.gridx = 1;
        mainPanel.add(textFieldX, c);

        c.gridy = gridy++;
        c.gridx = 0;
        mainPanel.add(y, c);
        c.gridx = 1;
        mainPanel.add(textFieldY, c);

        c.gridy = gridy++;
        c.gridx = 0;
        mainPanel.add(z, c);
        c.gridx = 1;
        mainPanel.add(textFieldZ, c);


        this.add(mainPanel);
        this.pack();
        setVisible(true);
    }


    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        Double x = Double.parseDouble(textFieldX.getText());
        Double y = Double.parseDouble(textFieldY.getText());
        Double z = Double.parseDouble(textFieldZ.getText());
        mainFrame.setCP(i, j, new Vector3f(x, y, z));
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
}
