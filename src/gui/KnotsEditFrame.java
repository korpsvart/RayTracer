package gui;

import rendering.BSurface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

class KnotsEditFrame extends Frame implements WindowListener, ActionListener {

    private int l; //number of editable knots (equal to number of knots - degree*2)
    private boolean addedKnot = false;
    private String direction;
    private BSurface bSurface;
    private AddBSplineSurfaceFrame callerFrame;
    private double[] knots;
    private int degree;
    private TextField[] textFields;
    private Panel mainPanel = new Panel(new GridBagLayout());
    private Button insertKnotButton = new Button("Insert new knot");

    public KnotsEditFrame(BSurface bSurface, AddBSplineSurfaceFrame callerFrame, String direction) {
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
        int knotsLength = knots.length;

        l = knotsLength - 2 * degree;
        textFields = new TextField[l];
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        int gridy = 0;
        for (int i = 0; i < l; i++, gridy++) {
            c.gridy = gridy;
            textFields[i] = new TextField(Double.toString(knots[i + degree]));
            mainPanel.add(textFields[i], c);
        }
        textFields[0].setEditable(false);
        textFields[l - 1].setEditable(false);
        mainPanel.add(insertKnotButton);
        this.add(mainPanel);
        this.pack();
        this.setSize(300, 50 * l);
        setVisible(true);
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        if (addedKnot) {
            callerFrame.setBSurface(bSurface);
        } else {
            for (int i = 0; i < l; i++) {
                this.knots[i + degree] = Double.parseDouble(textFields[i].getText());
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
        if (actionEvent.getActionCommand().equals("insert_knot")) {
            //first update the spline knots, in case they have been modified before
            //the insert new knot button is pressed
            for (int i = 0; i < l; i++) {
                this.knots[i + degree] = Double.parseDouble(textFields[i].getText());
                if (direction.equals("u")) {
                    bSurface.setKnotsU(this.knots);
                } else {
                    bSurface.setKnotsV(this.knots);
                }
            }
            double newKnot = Double.parseDouble(JOptionPane.showInputDialog("Insert new knot value (inside (0, 1) range)"));
            if (direction.equals("u")) {
                bSurface = bSurface.knotInsertionU(newKnot);
            } else {

                bSurface = bSurface.knotInsertionV(newKnot);
            }
            addedKnot = true;
            this.windowClosing(null);
        }
    }
}
