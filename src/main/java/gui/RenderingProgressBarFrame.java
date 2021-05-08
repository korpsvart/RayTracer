package gui;

import rendering.Scene;
import rendering.SceneListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class RenderingProgressBarFrame extends JFrame implements SceneListener {

    private JProgressBar progressBar;
    private JPanel mainPanel = new JPanel();
    private Scene scene;
    Task task;
    private int width;
    private int height;
    private String format;
    private File saveFile;

    public RenderingProgressBarFrame(Scene scene, int width, int height, String format, File saveFile) {
        this.scene = scene;
        this.width = width;
        this.height = height;
        this.format = format;
        this.saveFile = saveFile;
        scene.addSceneListener(this);
        progressBar = new JProgressBar(0, width*height);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        mainPanel.add(progressBar);
        add(mainPanel);
        this.pack();
        this.setVisible(true);
        task = new Task();
        task.execute();
    }


    @Override
    public void renderingProgressUpdate(long progress) {
        progressBar.setValue((int)progress);
    }

    class Task extends SwingWorker<Void, Void> {

        @Override
        protected Void doInBackground() throws Exception {
            ImageIO.write(scene.renderForOutput(20, width, height), format, saveFile);
            return null;
        }
    }
}
