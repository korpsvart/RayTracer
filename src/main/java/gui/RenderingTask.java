package gui;

import javax.swing.*;

public abstract class RenderingTask extends SwingWorker<Void,Void> {

    RenderingProgressBarFrame renderingProgressBarFrame;

    public void setRenderingProgressBarFrame(RenderingProgressBarFrame renderingProgressBarFrame) {
        this.renderingProgressBarFrame = renderingProgressBarFrame;
    }

    @Override
    protected void done() {
        renderingProgressBarFrame.dispose();
    }
}
