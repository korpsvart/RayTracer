package gui;

import rendering.*;

import java.awt.*;
import java.awt.event.ActionEvent;

class AddBezierSurface extends ControlPointsSurfaceFrame {

    private BezierSurface33 defaultBezierSurface;
    private ControlPointsFrame controlPointsFrame;
    private Button buttonCP = new Button("Edit control points");


    public AddBezierSurface(Visualizer visualizer, Scene scene) {

        super(visualizer, scene);
        controlPointsFrame = new ControlPointsFrame(visualizer, SampleShapes.getBezierSurfaceSampleCP(), this);
        createMainPanel();

    }

    private void createMainPanel() {
        buttonCP.setActionCommand("open_edit_cp");
        buttonCP.addActionListener(this);
        int gridy = 0;
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = gridy++;
        mainPanel.add(buttonCP, c);
        addOTWSubPanel(gridy++);
        addMaterialComboBox(gridy++);
        addMaterialPropertySubPanel(gridy++);
        addDivsPanel(gridy++);
        addSendButton(gridy++);
        setSizeToContent(3, gridy, 200, 100);
    }

    public AddBezierSurface(Visualizer visualizer, Scene scene, SceneObject defaultObject) {
        super(visualizer, scene, defaultObject);
        this.defaultBezierSurface = (BezierSurface33)defaultObject.getGeometricObject();
        controlPointsFrame = new ControlPointsFrame(visualizer, defaultBezierSurface.getControlPoints(), this);
        createMainPanel();

    }

    @Override
    void updateControlPoints(Vector3f[][] cp) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        switch (e.getActionCommand()) {
            case "open_edit_cp":
                controlPointsFrame.setVisible(true);
                break;

        }
    }


    @Override
    GeometricObject createGeometricObject() {
        Vector3f[][] cp = new Vector3f[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                cp[i][j] = Visualizer.extractVectorFromTextField(controlPointsFrame.getTextFieldsCP()[i][j]);
            }
        }
//        showControlPoints(cp, getOTWMatrix());
        return new BezierSurface33(cp, getOTWMatrix());
    }

}
