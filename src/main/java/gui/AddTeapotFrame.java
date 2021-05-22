package gui;

import rendering.*;

class AddTeapotFrame extends AddObjectFrame {

    public AddTeapotFrame(Visualizer visualizer, Scene scene) {
        super(visualizer, scene);
        setDivs(4);
        initializeWithTeapotData();
        createMainPanel();
    }

    private void createMainPanel() {

        int gridy = 0;
        addOTWSubPanel(gridy++);
        addMaterialComboBox(gridy++);
        addMaterialPropertySubPanel(gridy++);
        addDivsPanel(gridy++);
        addSendButton(gridy++);
        this.pack();
    }

    public AddTeapotFrame(Visualizer visualizer, Scene scene, SceneObject defaultObject) {
        super(visualizer, scene, defaultObject);
        createMainPanel();
    }

    private void initializeWithTeapotData() {
        BezierPatchesData bpd = SampleShapes.getTeapot();
        setDefaultOTW(bpd.getObjectToWorld().getC(), new Vector3d(0, 0, 0));
    }

    @Override
    GeometricObject createGeometricObject() {
        BezierPatchesData bezierPatchesData = SampleShapes.getTeapot();
        bezierPatchesData.setObjectToWorld(getOTWMatrix());
        return bezierPatchesData;
    }

}
