package gui;

import rendering.*;

class AddTeapotFrame extends AddObjectFrame {

    public AddTeapotFrame(Visualizer visualizer, Scene scene) {
        super(visualizer, scene);
        initializeWithTeapotData();
        int gridy = 0;
        addOTWSubPanel(gridy++);
        addMaterialComboBox(gridy++);
        addMaterialPropertySubPanel(MaterialType.DIFFUSE, gridy++);
        addSendButton(gridy++);

    }

    private void initializeWithTeapotData() {
        BezierPatchesData bpd = SampleShapes.getTeapot();
        setDefaultOTW(bpd.getObjectToWorld().getC(), new Vector3f(0, 0, 0));
    }

    @Override
    GeometricObject createGeometricObject() {
        BezierPatchesData bezierPatchesData = SampleShapes.getTeapot();
        bezierPatchesData.setObjectToWorld(getOTWMatrix());
        return bezierPatchesData;
    }

}
