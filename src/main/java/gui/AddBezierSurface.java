package gui;

import rendering.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

class AddBezierSurface extends ControlPointsSurfaceFrame {

    private BezierSurface33 defaultBezierSurface;
    private ControlPointsFrame controlPointsFrame;
    private JButton buttonCP = new JButton("Edit control points");


    public AddBezierSurface(Visualizer visualizer, Scene scene) {

        super(visualizer, scene);
        controlPointsFrame = new ControlPointsFrame(visualizer, SampleShapes.getBezierSurfaceSampleCP(), this);
        createMainPanel();

    }

    private void createMainPanel() {
        JPanel buttonPanel = new JPanel();
        buttonCP.setActionCommand("open_edit_cp");
        buttonCP.addActionListener(this);
        buttonPanel.add(buttonCP);
        int gridy = 0;
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = gridy++;
        c.gridwidth=3;

        addOTWSubPanel(gridy++);
        addMaterialComboBox(gridy++);
        addMaterialPropertySubPanel(gridy++);
        addDivsPanel(gridy++);
        c.gridy=gridy++;
        mainPanel.add(buttonPanel, c);
        addSendButton(gridy++);
//        setSizeToContent(3, gridy, 200, 100);
        this.pack();
    }

    public AddBezierSurface(Visualizer visualizer, Scene scene, SceneObject defaultObject) {
        super(visualizer, scene, defaultObject);
        this.defaultBezierSurface = (BezierSurface33)defaultObject.getGeometricObject();
        controlPointsFrame = new ControlPointsFrame(visualizer, defaultBezierSurface.getControlPoints(), this);
        createMainPanel();

    }

    @Override
    void updateControlPoints(Vector3d[][] cp) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        switch (e.getActionCommand()) {
            case "open_edit_cp":
                controlPointsFrame = new ControlPointsFrame(visualizer, controlPointsFrame.getControlPoints(), this);
                this.setEnabled(false);
                controlPointsFrame.setVisible(true);
                break;

        }
    }


    @Override
    GeometricObject createGeometricObject() {
        Vector3d[][] cp = new Vector3d[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                cp[i][j] = controlPointsFrame.getControlPoints()[i][j];
            }
        }
//        showControlPoints(cp, getOTWMatrix());
        return new BezierSurface33(cp, getOTWMatrix());
    }

}
