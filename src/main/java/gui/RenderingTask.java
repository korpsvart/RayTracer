package gui;

import javax.swing.*;

public abstract class RenderingTask extends SwingWorker<Void,Void> {

    private RenderingProgressBarFrame renderingProgressBarFrame;
    private SceneCanvas sceneCanvas;

    public RenderingTask(SceneCanvas sceneCanvas) {
        this.sceneCanvas = sceneCanvas;
    }

    public void setRenderingProgressBarFrame(RenderingProgressBarFrame renderingProgressBarFrame) {
        this.renderingProgressBarFrame = renderingProgressBarFrame;
    }

    @Override
    protected void done() {
        renderingProgressBarFrame.dispose();
        sceneCanvas.repaint();
    }
}
