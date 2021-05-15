package gui;

import javax.swing.*;

public abstract class SavingTask extends RenderingTask {

    private RenderingProgressBarFrame renderingProgressBarFrame;

    public void setRenderingProgressBarFrame(RenderingProgressBarFrame renderingProgressBarFrame) {
        this.renderingProgressBarFrame = renderingProgressBarFrame;
    }

    public SavingTask() {
        super(null);
    }

    @Override
    protected void done() {
        renderingProgressBarFrame.dispose();
    }
}
