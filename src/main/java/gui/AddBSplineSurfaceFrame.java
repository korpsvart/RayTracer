package gui;

import rendering.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;

class AddBSplineSurfaceFrame extends ControlPointsSurfaceFrame implements ChangeListener {

    private final Visualizer visualizer;
    private ControlPointsFrame controlPointsFrame;
    private BSurface bSurface;
    //        private int p,q; //degrees in both directions. Default is 3
//        private int m,n; //number of control points in both directions
//        private int s; //number of knots for u parameter
//        private int t; //number of knots for v parameter
//        private double[] knotsU;
//        private double[] knotsV;
    private JSpinner spinnerP;
    private JSpinner spinnerQ;
    private JSpinner spinnerM;
    private JSpinner spinnerN;
    private JLabel jLabelP = new JLabel("Degree for u parameter");
    private JLabel jLabelQ = new JLabel("Degree for v parameter");
    private JLabel jLabelM = new JLabel("Number of control points for u parameter");
    private JLabel jLabelN = new JLabel("Number of control points for v parameter");
    private Button buttonCP = new Button("Edit control points");
    private Button buttonKnotsU = new Button("Edit knots for u parameter");
    private Button buttonKnotsV = new Button("Edit knots for v parameter");


    public AddBSplineSurfaceFrame(Visualizer visualizer, Scene scene) {
        super(visualizer, scene);
        this.visualizer = visualizer;
        initializeDataWithSample();
        buttonCP.setActionCommand("open_edit_cp");
        buttonCP.addActionListener(this);
        buttonKnotsU.setActionCommand("open_edit_knots_u");
        buttonKnotsU.addActionListener(this);
        buttonKnotsV.setActionCommand("open_edit_knots_v");
        buttonKnotsV.addActionListener(this);
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
        c.gridx = 1;
        mainPanel.add(spinnerP, c);

        c.gridx = 0;
        c.gridy = gridy++;
        mainPanel.add(jLabelQ, c);
        c.gridx = 1;
        mainPanel.add(spinnerQ, c);

        c.gridx = 0;
        c.gridy = gridy++;
        mainPanel.add(jLabelM, c);
        c.gridx = 1;
        mainPanel.add(spinnerM, c);

        c.gridx = 0;
        c.gridy = gridy++;
        mainPanel.add(jLabelN, c);
        c.gridx = 1;
        mainPanel.add(spinnerN, c);


        c.gridy = gridy++;
        c.gridx = 0;
        mainPanel.add(buttonCP, c);
        c.gridx = 1;
        mainPanel.add(buttonKnotsU, c);
        c.gridx = 2;
        mainPanel.add(buttonKnotsV, c);


        addOTWSubPanel(gridy++);
        addMaterialComboBox(gridy++);
        addMaterialPropertySubPanel(MaterialType.DIFFUSE, gridy++);
        addSendButton(gridy++);

        setSizeToContent(3, gridy, 280, 70);
    }

    @Override
    GeometricObject createGeometricObject() {

        return new BSurface(bSurface.getControlPoints(), bSurface.getKnotsU(), bSurface.getKnotsV(),
                bSurface.getP(), bSurface.getQ(), getOTWMatrix());
    }

    public void updateControlPoints(Vector3f[][] cp) {
        bSurface = new BSurface(cp, bSurface.getKnotsU(), bSurface.getKnotsV(),
                bSurface.getP(), bSurface.getQ(), Matrix4D.identity);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        switch (e.getActionCommand()) {
            case "open_edit_cp":
                controlPointsFrame = new ControlPointsFrame(visualizer, bSurface.getControlPoints(), this);
                controlPointsFrame.setVisible(true);
                break;
            case "open_edit_knots_u":
                KnotsEditFrame knotsEditFrame = new KnotsEditFrame(bSurface, this, "u");
                break;
            case "open_edit_knots_v":
                KnotsEditFrame knotsEditFrame1 = new KnotsEditFrame(bSurface, this, "v");
                break;
        }
    }

    private void initializeDataWithSample() {
        Vector3f[][] sampleCP = SampleShapes.getBSplineSample1CP();
        Matrix4D sampleOTW = SampleShapes.getBSplineSample1OTW();
        double[] u = SampleShapes.getBSplineSample1U();
        double[] v = SampleShapes.getBSplineSample1V();
        int p = SampleShapes.getBSplineSample1P();
        int q = SampleShapes.getBSplineSample1Q();
        bSurface = new BSurface(sampleCP, u, v,
                p, q, Matrix4D.identity);
        controlPointsFrame = new ControlPointsFrame(visualizer, sampleCP, this);

        //update spinners
        SpinnerNumberModel spinnerModelM = new SpinnerNumberModel(sampleCP.length, 3, 10, 1);
        SpinnerNumberModel spinnerModelN = new SpinnerNumberModel(sampleCP[0].length, 3, 10, 1);
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
        setDefaultOTW(sampleOTW.getC(), new Vector3f(0, 0, 0));
    }

    private void setControlPointsFrameDefault(int m, int n) {
        controlPointsFrame = new ControlPointsFrame(visualizer, bSurface.getControlPoints(), this);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        //how does b-spline editing works:
        //Knots: you can edit the existing knots, but only when knots different from 0 and 1 exist
        //you can also insert a new knot, which will increase the number of control points in that direction
        //(the degree is left unchanged)
        //There is no option to remove existing knots
        //Degree: you can increase or decrease the degree. Changing the degree WILL NOT preserve knots information
        //(knots will be reset to uniform spacing).
        //As long as the degree doesn't reach the number of control points, increasing it will not change the
        //number of control points nor will modify the existing control points.
        //When the degree reaches the number of control points, it's necessary to create a new control point.
        //The newly added control point is not chosen by the user, but it's generated with the following procedure:
        //The program will require the user to input a new knot value. This knot is used to perform a knot insertion,
        //which consequently will add a new point to the spline. After this, the knots will be reset to uniform when
        //actually increasing the degree, so the knot inserted by the user will only affect where the surface
        //will be slightly changed.
        //Control points: adding control points can be done in two ways (three ways if we count the "indirect" one):
        //-by inserting new knots (control points added "inside" the surface)
        //-by increasing the number of control points (control points are "appended" at the start/end)
        //-by increasing the degree, when this causes a knot insertion (see above explanation)
        //Only the first way will preserve the existing knot spacing. Appending new control points will reset
        //the existing knot spacing, making it uniform.
        double[] newKnots;
        int p, q;
        if (e.getSource() instanceof JSpinner) {
            JSpinner source = (JSpinner) e.getSource();
            switch (source.getName()) {
                case "p":
                    int pNew = (int) source.getValue();
                    int m = bSurface.getControlPoints().length;
                    if (pNew == m) {
                        double newKnot = Double.parseDouble(JOptionPane.showInputDialog("Insert new knot value (inside (0, 1) range)"));
                        bSurface = bSurface.knotInsertionU(newKnot);
                        newKnots = BSpline.uniformKnots(m + 1, pNew);
                    } else {
                        newKnots = BSpline.uniformKnots(m, pNew);
                    }
                    bSurface = new BSurface(bSurface.getControlPoints(), newKnots, bSurface.getKnotsV(),
                            pNew, bSurface.getQ(), Matrix4D.identity);
                    spinnerM.removeChangeListener(this);
                    spinnerM.setValue(bSurface.getControlPoints().length);
                    spinnerM.addChangeListener(this);
                    break;
                case "q":
                    int qNew = (int) source.getValue();
                    int n = bSurface.getControlPoints()[0].length;
                    if (qNew == n) {
                        double newKnot = Double.parseDouble(JOptionPane.showInputDialog("Insert new knot value (inside (0, 1) range)"));
                        bSurface = bSurface.knotInsertionV(newKnot);
                        newKnots = BSpline.uniformKnots(n + 1, qNew);
                    } else {
                        newKnots = BSpline.uniformKnots(n, qNew);
                    }
                    bSurface = new BSurface(bSurface.getControlPoints(), bSurface.getKnotsU(), newKnots,
                            bSurface.getP(), qNew, Matrix4D.identity);
                    spinnerN.removeChangeListener(this);
                    spinnerN.setValue(bSurface.getControlPoints()[0].length);
                    spinnerN.addChangeListener(this);
                    break;
                case "m":
                    //if m is decreasing, it could be necessary to decrease the degree number
                    //anyway, we need to adjust the knot vector length
                    int mNew = (int) source.getValue();
                    if (mNew == bSurface.getControlPoints().length) {
                        spinnerP.removeChangeListener(this);
                        spinnerP.setValue(spinnerP.getPreviousValue());
                        spinnerP.addChangeListener(this);
                    }
                    p = (int) spinnerP.getValue();
                    newKnots = BSpline.uniformKnots(mNew, p);
                    bSurface = bSurface.editU(mNew, p, newKnots);
                    break;
                case "n":
                    int nNew = (int) source.getValue();
                    if (nNew == bSurface.getControlPoints()[0].length) {
                        spinnerQ.removeChangeListener(this);
                        spinnerQ.setValue(spinnerQ.getPreviousValue());
                        spinnerQ.addChangeListener(this);
                    }
                    q = (int) spinnerQ.getValue();
                    newKnots = BSpline.uniformKnots(nNew, q);
                    bSurface = bSurface.editV(nNew, q, newKnots);
                    break;
            }
        }
    }

    public void setBSurface(BSurface bSurface) {
        this.bSurface = bSurface;
    }
}
