package gui;

import rendering.*;

import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

class ControlPointsFrame extends JFrame implements ActionListener, WindowListener {

    private final Visualizer visualizer;
    private JLabel[][] labelsCP;
    private TextField[][] textFieldsCP;
    Vector3d[][] controlPoints;
    private Vector3d[][] original;
    int m, n;
    private BasicArrowButton[][] buttonsCP;
    private ControlPointsSurfaceFrame callerFrame;
    private JButton applyButton = new JButton("Apply");

    public ControlPointsFrame(Visualizer visualizer, int m, int n, Vector3d[][] defaultSampleCP, ControlPointsSurfaceFrame callerFrame) {
        setTitle("Edit control or data points");
        this.original = defaultSampleCP;
        this.addWindowListener(this);
        applyButton.addActionListener(this);
        applyButton.setActionCommand("apply");
        this.visualizer = visualizer;
        this.m = m;
        this.n = n;
        controlPoints = new Vector3d[m][n];
        this.callerFrame = callerFrame;
        textFieldsCP = new TextField[m][n];
        buttonsCP = new BasicArrowButton[m][n];

        this.setSize(1200, 1200);
        Panel mainPanel = new Panel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 10, 10, 10);


        //Add control points textfields and buttons
        //we use the matrix actual dimensions instead of (m,n)
        //to handle adding more control points but allowing
        //to keep using the default control points where they can be used
        //(i.e. for B-Spline surfaces if we want to "expand" the already existing surface)
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                //TODO: we don't actually call this method anymore with
                //a bigger dimension than the control points matrix
                //so it needs a bit of refactoring
                if (i < defaultSampleCP.length && j < defaultSampleCP[0].length) {
//                    if (defaultSampleCP[i][j] == null)
//                        defaultSampleCP[i][j] = new Vector3d(0,0,0);
                    textFieldsCP[i][j] = new TextField(Visualizer.extractTextFromVector(defaultSampleCP[i][j]),
                            15);
                    controlPoints[i][j] = defaultSampleCP[i][j];
                } else {
                    textFieldsCP[i][j] = new TextField("{0,0,0}");
                }
                JLabel label = new JLabel("("+i+","+j+")");
                textFieldsCP[i][j].setEditable(false);
                buttonsCP[i][j] = new BasicArrowButton(BasicArrowButton.BOTTOM);
                buttonsCP[i][j].addActionListener(this);
                buttonsCP[i][j].setName(i+","+j);
                buttonsCP[i][j].setActionCommand("edit_cp");
                c.gridx=j*3;
                c.gridy=i;
                mainPanel.add(label, c);
                c.gridx=j*3+1;
                mainPanel.add(textFieldsCP[i][j], c);
                c.gridx=j*3+2;
                mainPanel.add(buttonsCP[i][j], c);
            }
        }
        Panel buttonPanel = new Panel();
        c.gridy++;
        c.gridx = 0;
        c.gridwidth=n*3;
        buttonPanel.add(applyButton, CENTER_ALIGNMENT);
        mainPanel.add(buttonPanel,c);
        this.add(mainPanel);
        this.pack();
    }

    public ControlPointsFrame(Visualizer visualizer, Vector3d[][] cp, ControlPointsSurfaceFrame callerFrame) {
        this(visualizer, cp.length, cp[0].length, cp, callerFrame);
    }

    public BasicArrowButton[][] getButtonsCP() {
        return buttonsCP;
    }

    public TextField[][] getTextFieldsCP() {
        return textFieldsCP;
    }

    public Vector3d[][] getControlPoints() {
        return controlPoints;
    }

    @Override
    public void windowOpened(WindowEvent windowEvent) {

    }

    @Override
    public void windowClosing(WindowEvent windowEvent) {
        controlPoints = original;
        callerFrame.setEnabled(true);
        this.dispose();
    }

    @Override
    public void windowClosed(WindowEvent windowEvent) {

    }

    @Override
    public void windowIconified(WindowEvent windowEvent) {

    }

    @Override
    public void windowDeiconified(WindowEvent windowEvent) {

    }

    @Override
    public void windowActivated(WindowEvent windowEvent) {

    }

    @Override
    public void windowDeactivated(WindowEvent windowEvent) {

    }

    public void setCP(int i, int j, Vector3d data) {
        controlPoints[i][j] = data;
        textFieldsCP[i][j].setText(Visualizer.extractTextFromVector(data));
//        textFieldsCP[i][j].setText(Visualizer.extractTextFromVector(data));
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "edit_cp":
                BasicArrowButton button = (BasicArrowButton) e.getSource();
                String[] pos = button.getName().split(",");
                int i = Integer.parseInt(pos[0]);
                int j = Integer.parseInt(pos[1]);
                Vector3d currentData = controlPoints[i][j];
                VectorEditFrame vectorEditFrame = new VectorEditFrame(i, j, currentData,this);
                break;
            case "apply":
                callerFrame.setEnabled(true);
                callerFrame.updateControlPoints(controlPoints);
                setVisible(false);
                this.dispose();
                break;
        }
    }



}
