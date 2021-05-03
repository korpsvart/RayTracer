package gui;

import rendering.*;

import javax.swing.plaf.basic.BasicArrowButton;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

class ControlPointsFrame extends Frame implements WindowListener, ActionListener {

    private final Visualizer visualizer;
    private TextField[][] textFieldsCP;
    int m, n;
    private BasicArrowButton[][] buttonsCP;
    private ControlPointsSurfaceFrame callerFrame;

    public ControlPointsFrame(Visualizer visualizer, int m, int n, Vector3f[][] defaultSampleCP, ControlPointsSurfaceFrame callerFrame) {
        this.visualizer = visualizer;
        this.m = m;
        this.n = n;
        this.callerFrame = callerFrame;
        addWindowListener(this);
        textFieldsCP = new TextField[m][n];
        buttonsCP = new BasicArrowButton[m][n];

        this.setSize(1200, 1200);
        Panel mainPanel = new Panel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 10, 10, 10);
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
                    textFieldsCP[i][j] = new TextField("{"+defaultSampleCP[i][j].getX()+
                            ","+defaultSampleCP[i][j].getY()+
                            ","+defaultSampleCP[i][j].getZ()+"}", 15);
                } else {
                    textFieldsCP[i][j] = new TextField("{0,0,0}");
                }
                textFieldsCP[i][j].setEditable(false);
                buttonsCP[i][j] = new BasicArrowButton(BasicArrowButton.BOTTOM);
                buttonsCP[i][j].addActionListener(this);
                buttonsCP[i][j].setName(i+","+j);
                buttonsCP[i][j].setActionCommand("edit_cp");
                c.gridx=j*2;
                c.gridy=i;
                mainPanel.add(textFieldsCP[i][j], c);
                c.gridx=j*2+1;
                mainPanel.add(buttonsCP[i][j], c);
            }
        }
        this.add(mainPanel);
        this.pack();
    }

    public ControlPointsFrame(Visualizer visualizer, Vector3f[][] cp, ControlPointsSurfaceFrame callerFrame) {
        this(visualizer, cp.length, cp[0].length, cp, callerFrame);
    }

    public BasicArrowButton[][] getButtonsCP() {
        return buttonsCP;
    }

    public TextField[][] getTextFieldsCP() {
        return textFieldsCP;
    }

    @Override
    public void windowOpened(WindowEvent windowEvent) {

    }

    @Override
    public void windowClosing(WindowEvent windowEvent) {
        Vector3f[][] cp = new Vector3f[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                cp[i][j] = Visualizer.extractVectorFromTextField(getTextFieldsCP()[i][j]);
            }
        }
        callerFrame.updateControlPoints(cp);
        setVisible(false);
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

    public void setCP(int i, int j, Vector3f data) {
        textFieldsCP[i][j].setText(Visualizer.extractTextFromVector(data));
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "edit_cp":
                BasicArrowButton button = (BasicArrowButton) e.getSource();
                String[] pos = button.getName().split(",");
                int i = Integer.parseInt(pos[0]);
                int j = Integer.parseInt(pos[1]);
                Vector3f currentData = Visualizer.extractVectorFromTextField(textFieldsCP[i][j]);
                VectorEditFrame vectorEditFrame = new VectorEditFrame(i, j, currentData,this);

        }
    }


}
