package gui;

import rendering.Scene;
import rendering.Vector3f;

abstract class ControlPointsSurfaceFrame extends AddObjectFrame {

    public ControlPointsSurfaceFrame(Visualizer visualizer, Scene scene) {
        super(visualizer, scene);
    }

    abstract void updateControlPoints(Vector3f[][] cp);
}
