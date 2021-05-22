package gui;

import rendering.*;


class AddPlaneFrame extends AddObjectFrame {

    private Vector3d normal = new Vector3d(0, 1, 0); //default is y axis
    private Vector3d position = new Vector3d(0, -0.5, 0); //default is y=-3 (other coords don't matter)

    public AddPlaneFrame(Visualizer visualizer, Scene scene) {
        super(visualizer, scene);
        setDefaultOTW(position, new Vector3d(0, 0, 0));
        createMainPanel();
    }

    private void createMainPanel() {
        int gridy = 0;
        addOTWSubPanel(gridy++);
        addMaterialComboBox(gridy++);
        addMaterialPropertySubPanel(gridy++);
        addSendButton(gridy++);
        this.pack();
    }

    public AddPlaneFrame(Visualizer visualizer, Scene scene, SceneObject defaultObject) {
        super(visualizer, scene, defaultObject);
        createMainPanel();
    }

    @Override
    GeometricObject createGeometricObject() {
        Vector3d n = normal.matrixAffineTransform(getOTWMatrix().getA());
        Vector3d p = getOTWMatrix().getC();
        try {
            return new Plane3d(p, n);
        } catch (Exception e) {
            System.out.println("Invalid plane");
            //TODO: handle this
            return null;
        }
    }
}
