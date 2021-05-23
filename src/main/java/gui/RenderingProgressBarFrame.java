package gui;

import rendering.Scene;
import rendering.SceneListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

public class RenderingProgressBarFrame extends JFrame implements SceneListener {

    private JProgressBar progressBar;
    private JPanel mainPanel = new JPanel();
    RenderingTask renderingTask;


    public RenderingProgressBarFrame(Scene scene, int maxProgressBar, RenderingTask task) {
        setLocationRelativeTo(null);
        setUndecorated(true);
        scene.addSceneListener(this);
        progressBar = new JProgressBar(0, maxProgressBar);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        mainPanel.add(progressBar);
        add(mainPanel);
        this.pack();
        this.setVisible(true);
        this.renderingTask = task;
        renderingTask.setRenderingProgressBarFrame(this);
        task.execute();
    }


    @Override
    public void renderingProgressUpdate(long progress) {
        progressBar.setValue((int)progress);
    }

}
