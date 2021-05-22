package gui;

import rendering.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;

class AddSurfaceInterpolationFrame extends ControlPointsSurfaceFrame implements ChangeListener {

    private Visualizer visualizer;
    private ControlPointsFrame controlPointsFrame;
    private int p, q; //degrees in both directions. Default is 3
    private int m, n; //number of control points in both directions
    private JSpinner spinnerP;
    private JSpinner spinnerQ;
    private JSpinner spinnerM;
    private JSpinner spinnerN;

    private JLabel jLabelP = new JLabel("Degree for u parameter");
    private JLabel jLabelQ = new JLabel("Degree for v parameter");
    private JLabel jLabelM = new JLabel("Number of data points for u parameter");
    private JLabel jLabelN = new JLabel("Number of data points for v parameter");
    private JButton buttonCP = new JButton("Edit data points");

    public AddSurfaceInterpolationFrame(Visualizer visualizer, Scene scene) {
        super(visualizer, scene);
        initializeDataWithSample();
        createMainPanel();
    }

    private void createMainPanel() {
        buttonCP.setActionCommand("open_edit_cp");
        buttonCP.addActionListener(this);
        spinnerP.addChangeListener(this);
        spinnerQ.addChangeListener(this);
        spinnerM.addChangeListener(this);
        spinnerN.addChangeListener(this);
        int gridy = 0;
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 10, 10, 10);

        c.gridx = 0;
        c.gridy = gridy++;
        mainPanel.add(jLabelP, c);
        c.gridx = 2;
        mainPanel.add(spinnerP, c);

        c.gridx = 0;
        c.gridy = gridy++;
        mainPanel.add(jLabelQ, c);
        c.gridx = 2;
        mainPanel.add(spinnerQ, c);

        c.gridx = 0;
        c.gridy = gridy++;
        mainPanel.add(jLabelM, c);
        c.gridx = 2;
        mainPanel.add(spinnerM, c);

        c.gridx = 0;
        c.gridy = gridy++;
        mainPanel.add(jLabelN, c);
        c.gridx = 2;
        mainPanel.add(spinnerN, c);

        c.gridy = gridy++;
        c.gridx = 0;
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(buttonCP);
        c.gridwidth = 3;
        mainPanel.add(buttonPanel, c);

        addOTWSubPanel(gridy++);
        addMaterialComboBox(gridy++);
        addMaterialPropertySubPanel(gridy++);
        addDivsPanel(gridy++);
        addSendButton(gridy++);


        setSizeToContent(3, gridy, 250, 70);
    }

    public AddSurfaceInterpolationFrame(Visualizer visualizer, Scene scene, SceneObject defaultSceneObject) {
        super(visualizer, scene, defaultSceneObject);
        BSurface bSurface = (BSurface) defaultSceneObject.getGeometricObject();
        initializeDataWithSample(bSurface);
        createMainPanel();
    }

    @Override
    void updateControlPoints(Vector3d[][] cp) {
//        for (int i = 0; i < m; i++) {
//            for (int j = 0; j < n; j++) {
//                controlPointsFrame.controlPoints[i][j] = cp[i][j];
//            }
//        }
    }

    @Override
    GeometricObject createGeometricObject() {
        int m = (int) spinnerM.getValue();
        int n = (int) spinnerN.getValue();
        int p = (int) spinnerP.getValue();
        int q = (int) spinnerQ.getValue();
        Vector3d[][] dataPoints = new Vector3d[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                dataPoints[i][j] = controlPointsFrame.getControlPoints()[i][j];
            }
        }
//        showControlPoints(dataPoints, getOTWMatrix());
        return BSurface.interpolate(dataPoints, p, q, getOTWMatrix());
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() instanceof JSpinner) {
            JSpinner source = (JSpinner) e.getSource();
            switch (source.getName()) {
                case "p":
                    int pNew = (int) source.getValue();
                    if (pNew > p) {
                        p = pNew;
                        if (m - pNew < 1) {
                            m++;
                            spinnerM.setValue(spinnerM.getNextValue());
                        }
                    } else {
                        p = pNew;
                        //decreasing degree
                    }
                    break;
                case "q":
                    int qNew = (int) source.getValue();
                    if (qNew > q) {
                        q = qNew;
                        if (n - qNew < 1) {
                            n++;
                            spinnerN.setValue(spinnerN.getNextValue());
                        }
                    } else {
                        //decreasing degree
                        q = qNew;
                    }
                    break;
                case "m":
                    //if m is decreasing, it could be necessary to decrease the degree number
                    //anyway, we need to adjust the knot vector length
                    int mNew = (int) source.getValue();
                    if (mNew < m) {
                        m = mNew;
                        if (mNew - p < 1) {
                            p--;
                            spinnerP.setValue(spinnerP.getPreviousValue());
                        }
                    } else {
                        m = mNew;
                    }
                    setControlPointsFrameDefault(m, n);
                    break;
                case "n":
                    int nNew = (int) source.getValue();
                    if (nNew < n) {
                        n = nNew;
                        if (nNew - q < 1) {
                            q--;
                            spinnerQ.setValue(spinnerQ.getPreviousValue());
                        }
                    } else {
                        n = nNew;
                    }
                    setControlPointsFrameDefault(m, n);
                    break;
            }
        }
    }

    protected Vector3d[][] getSampleDP() {
        return SampleShapes.getBSurfaceInterpolationSample1CP();
    }

    protected Matrix4D getSampleOTW() {
        return SampleShapes.getBSurfaceInterpolationSample1OTW();
    }

    protected int getSampleP() {
        return SampleShapes.getBSurfaceInterpolationSample1P();
    }

    protected int getSampleQ() {
        return SampleShapes.getBSurfaceInterpolationSample1Q();
    }


    private void initializeDataWithSample() {
        Vector3d[][] sampleCP = getSampleDP();
        Matrix4D sampleOTW = getSampleOTW();
        this.m = sampleCP.length;
        this.n = sampleCP[0].length;
        controlPointsFrame = new ControlPointsFrame(visualizer, m, n, sampleCP, this);
        this.p = getSampleP();
        this.q = getSampleQ();

        //update spinners
        SpinnerNumberModel spinnerModelM = new SpinnerNumberModel(m, 3, 100, 1);
        SpinnerNumberModel spinnerModelN = new SpinnerNumberModel(n, 3, 100, 1);
        SpinnerNumberModel spinnerModelP = new SpinnerNumberModel(p, 2, 9, 1);
        SpinnerNumberModel spinnerModelQ = new SpinnerNumberModel(q, 2, 9, 1);
        spinnerM = new JSpinner(spinnerModelM);
        spinnerM.setName("m");
        spinnerN = new JSpinner(spinnerModelN);
        spinnerN.setName("n");
        spinnerP = new JSpinner(spinnerModelP);
        spinnerP.setName("p");
        spinnerQ = new JSpinner(spinnerModelQ);
        spinnerQ.setName("q");

        //update otw
        //For now I'm not considering rotation
        //since I should implement a method that translates
        //rotation matrix back to rotation angle around each axis
        setDefaultOTW(sampleOTW.getC(), new Vector3d(0, 0, 0));
    }

    private void initializeDataWithSample(BSurface bSurface) {
        Vector3d[][] sampleCP = getSampleDP();
        Matrix4D sampleOTW = bSurface.getObjectToWorld();
        this.m = sampleCP.length;
        this.n = sampleCP[0].length;
        controlPointsFrame = new ControlPointsFrame(visualizer, m, n, sampleCP, this);
        this.p = bSurface.getP();
        this.q = bSurface.getQ();

        //update spinners
        SpinnerNumberModel spinnerModelM = new SpinnerNumberModel(m, 3, 100, 1);
        SpinnerNumberModel spinnerModelN = new SpinnerNumberModel(n, 3, 100, 1);
        SpinnerNumberModel spinnerModelP = new SpinnerNumberModel(p, 2, 9, 1);
        SpinnerNumberModel spinnerModelQ = new SpinnerNumberModel(q, 2, 9, 1);
        spinnerM = new JSpinner(spinnerModelM);
        spinnerM.setName("m");
        spinnerN = new JSpinner(spinnerModelN);
        spinnerN.setName("n");
        spinnerP = new JSpinner(spinnerModelP);
        spinnerP.setName("p");
        spinnerQ = new JSpinner(spinnerModelQ);
        spinnerQ.setName("q");

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        switch (e.getActionCommand()) {
            case "open_edit_cp":
                controlPointsFrame = new ControlPointsFrame(visualizer, m, n, controlPointsFrame.getControlPoints(), this);
                this.setEnabled(false);
                controlPointsFrame.setVisible(true);
                break;
        }
    }

    private void setControlPointsFrameDefault(int m, int n) {
        controlPointsFrame = new ControlPointsFrame(visualizer, m, n, SampleShapes.getBSurfaceInterpolationSample1CP(), this);
    }
}
