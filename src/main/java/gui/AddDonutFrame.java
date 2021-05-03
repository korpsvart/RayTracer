package gui;

import rendering.*;

class AddDonutFrame extends AddSurfaceInterpolationFrame {

    public AddDonutFrame(Visualizer visualizer, Scene scene) {
        super(visualizer, scene);
    }

    @Override
    protected Vector3f[][] getSampleDP() {
        return SampleShapes.getInterpolatingSurfaceDonutDP();
    }

    @Override
    protected Matrix4D getSampleOTW() {
        return SampleShapes.getInterpolatingSurfaceDonutOTW();
    }

    @Override
    protected int getSampleP() {
        return SampleShapes.getInterpolatingSurfaceDonutP();
    }

    @Override
    protected int getSampleQ() {
        return SampleShapes.getInterpolatingSurfaceDonutQ();
    }
}
