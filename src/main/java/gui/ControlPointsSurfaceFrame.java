package gui;

import rendering.*;

abstract class ControlPointsSurfaceFrame extends AddObjectFrame {



    public ControlPointsSurfaceFrame(Visualizer visualizer, Scene scene) {
        super(visualizer, scene);
    }

    public ControlPointsSurfaceFrame(Visualizer visualizer, Scene scene, SceneObject defaultSceneObject) {
        super(visualizer, scene, defaultSceneObject);
    }

    abstract void updateControlPoints(Vector3d[][] cp);


    public void showControlPoints(Vector3d[][] cp, Matrix4D otw) {
        for (int i = 0; i <cp.length; i++) {
            for (int j = 0; j < cp[0].length; j++) {
                PhysicalPoint physicalPoint = new PhysicalPoint(otw.transformVector(cp[i][j]));
                ConceptualObject conceptualObject = new ConceptualObject(physicalPoint);
                scene.addSceneObject(conceptualObject);
            }
        }
    }


}
