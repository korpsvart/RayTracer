package gui;

import rendering.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

class KnotsEditFrame extends JFrame implements WindowListener, ActionListener {

    private int l; //number of editable knots (equal to number of knots - degree*2)
    private boolean addedKnot = false;
    private String direction;
    private BSurface bSurface;
    private AddBSplineSurfaceFrame callerFrame;
    private double[] knots;
    private int degree;
    private TextField[] textFields;
    private Panel mainPanel = new Panel(new GridBagLayout());
    private JButton insertKnotButton = new JButton("Insert new knot");
    private JButton applyButton = new JButton("Apply changes");

    public KnotsEditFrame(BSurface bSurface, AddBSplineSurfaceFrame callerFrame, String direction) {
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setTitle("Edit knots");
        this.callerFrame = callerFrame;
        this.bSurface = bSurface;
        this.direction = direction;
        if (direction.equals("u")) {
            this.degree = bSurface.getP();
            this.knots = bSurface.getKnotsU();
        } else {
            this.degree = bSurface.getQ();
            this.knots = bSurface.getKnotsV();
        }
        addWindowListener(this);
        insertKnotButton.setActionCommand("insert_knot");
        insertKnotButton.addActionListener(this);
        applyButton.setActionCommand("apply");
        applyButton.addActionListener(this);
        int knotsLength = knots.length;

        l = knotsLength - 2 * degree;
        textFields = new TextField[l];
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 60, 10, 60);
        int gridy = 0;
        for (int i = 0; i < l; i++, gridy++) {
            c.gridy = gridy;
            textFields[i] = new TextField(Double.toString(knots[i + degree]));
            mainPanel.add(textFields[i], c);
        }
        textFields[0].setEditable(false);
        textFields[l - 1].setEditable(false);
        c.gridy++;
        mainPanel.add(insertKnotButton, c);
        c.gridy++;
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
    public void actionPerformed(ActionEvent actionEvent) {
        switch (actionEvent.getActionCommand()) {
            //first update the spline knots, in case they have been modified before
            //the insert new knot button is pressed
            case "insert_knot": handleKnotInsertion();
                break;
            case "apply": handleApply();
                break;
        }
    }

    private void handleKnotInsertion() {
        for (int i = 0; i < l; i++) {
            try {
                this.knots[i + degree] = Double.parseDouble(textFields[i].getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Only numeric input is accepted!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                throw e;
            }
            if (direction.equals("u")) {
                bSurface.setKnotsU(this.knots);
            } else {
                bSurface.setKnotsV(this.knots);
            }
        }
        double newKnot = 0;
        try {
            newKnot = Double.parseDouble(JOptionPane.showInputDialog("Insert new knot value (inside (0, 1) range)"));
            if (newKnot <= 0 || newKnot >= 1)
                throw new NumberFormatException("value outside of permitted range");
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(this,
                    "Invalid input",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            throw exception;
        } catch (HeadlessException e) {
            e.printStackTrace();
        }
        if (direction.equals("u")) {
            bSurface = bSurface.knotInsertionU(newKnot);
        } else {

            bSurface = bSurface.knotInsertionV(newKnot);
        }
        addedKnot = true;
        this.dispose();
    }

    private void handleApply() {
        if (addedKnot) {
            callerFrame.setBSurface(bSurface);
        } else {
            for (int i = 0; i < l; i++) {
                try {
                    double knot = Double.parseDouble(textFields[i].getText());
                    if (knot <= 0 || knot >= 1) throw new NumberFormatException("value outside of permitted range");
                    this.knots[i + degree] = knot;
                } catch (NumberFormatException exception) {
                    JOptionPane.showMessageDialog(this,
                            "Invalid input",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    throw exception;
                }
                if (direction.equals("u")) {
                    bSurface.setKnotsU(this.knots);
                } else {
                    bSurface.setKnotsV(this.knots);
                }

            }
        }
        setVisible(false);
        this.dispose();
    }
}
