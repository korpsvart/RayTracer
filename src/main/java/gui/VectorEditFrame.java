package gui;

import rendering.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.math.RoundingMode;
import java.text.DecimalFormat;

class VectorEditFrame extends JFrame implements WindowListener, ActionListener {

    protected JLabel x = new JLabel("x");
    protected JLabel y = new JLabel("y");
    protected JLabel z = new JLabel("z");
    protected TextField textFieldX;
    protected TextField textFieldY;
    protected TextField textFieldZ;
    private ControlPointsFrame mainFrame;
    private JButton applyButton = new JButton("Apply");
    int i, j;

    public VectorEditFrame(int i, int j, Vector3f data, ControlPointsFrame mainFrame) {
        this.setTitle("Edit point or vector");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        applyButton.setActionCommand("apply");
        applyButton.addActionListener(this);
        addWindowListener(this);
        this.i = i;
        this.j = j;
        textFieldX = new TextField(Double.toString(data.getX()), 10);
        textFieldY = new TextField(Double.toString(data.getY()), 10);
        textFieldZ = new TextField(Double.toString(data.getZ()), 10);
        this.mainFrame = mainFrame;

        Panel mainPanel = new Panel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        int gridy = 0;

        c.insets = new Insets(10, 10, 10, 5);
        c.gridy = gridy++;
        mainPanel.add(x, c);
        c.gridx = 2;
        mainPanel.add(textFieldX, c);

        c.gridy = gridy++;
        c.gridx = 0;
        mainPanel.add(y, c);
        c.gridx = 2;
        mainPanel.add(textFieldY, c);

        c.gridy = gridy++;
        c.gridx = 0;
        mainPanel.add(z, c);
        c.gridx = 2;
        mainPanel.add(textFieldZ, c);

        c.gridy++;
        c.gridx=1;
        mainPanel.add(applyButton,c);


        this.add(mainPanel);
        this.pack();
        setVisible(true);
    }


    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

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
        switch (e.getActionCommand()) {
            case "apply":
                Double x = null;
                Double y = null;
                Double z = null;
                try {
                    x = Double.parseDouble(textFieldX.getText());
                    y = Double.parseDouble(textFieldY.getText());
                    z = Double.parseDouble(textFieldZ.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Only numeric input is accepted!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    throw ex;
                }
                mainFrame.setCP(i, j, new Vector3f(x, y, z));
                setVisible(false);
                this.dispose();
                break;
        }
    }
}
