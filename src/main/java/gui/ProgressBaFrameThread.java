package gui;

import rendering.Scene;

import javax.swing.*;

public class ProgressBaFrameThread extends SwingWorker<Void, Void> {
    private Scene scene;
    private int width;
    private int height;

    public ProgressBaFrameThread(Scene scene, int width, int height) {
        this.scene = scene;
        this.width = width;
        this.height = height;

    }

    @Override
    protected Void doInBackground() throws Exception {
        return null;
    }
}
