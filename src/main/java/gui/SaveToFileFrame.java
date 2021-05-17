package gui;

import rendering.Scene;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.TreeSet;

public class SaveToFileFrame extends JFrame implements ActionListener {

    private final TextField widthTextField;
    private final TextField heightTextField;
    Panel mainPanel = new Panel();
    Scene scene;
    private final JComboBox formats;

    public SaveToFileFrame(Scene scene) {
        setTitle("Save");
        this.scene = scene;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel saveAsLabel = new JLabel("Save as");
        JLabel resolutionLabel = new JLabel("Resolution");
        JLabel widthLabel = new JLabel("Width");
        JLabel heightLabel = new JLabel("Height");
        widthTextField = new TextField(10);
        heightTextField = new TextField(10);
        widthTextField.setText(String.valueOf(scene.getWidth()));
        heightTextField.setText(String.valueOf(scene.getHeight()));
        formats = new JComboBox(getFormats());
        formats.setActionCommand("Formats");
        formats.addActionListener(this);
        JButton saveButton = new JButton("Save");
        saveButton.setActionCommand("save");
        saveButton.addActionListener(this);
        mainPanel.add(saveAsLabel);
        mainPanel.add(formats);
        mainPanel.add(resolutionLabel);
        mainPanel.add(widthLabel);
        mainPanel.add(widthTextField);
        mainPanel.add(heightLabel);
        mainPanel.add(heightTextField);
        mainPanel.add(saveButton);
        this.add(mainPanel);
        this.pack();
        this.setVisible(true);
    }


    private static String[] getFormats() {
            String[] formats = ImageIO.getWriterFormatNames();
            TreeSet<String> formatSet = new TreeSet<String>();
            for (String s : formats) {
                formatSet.add(s.toLowerCase());
            }
            return formatSet.toArray(new String[0]);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "save":
                saveToFile();
                break;
        }
    }


    private void saveToFile() {
        String format = (String)formats.getSelectedItem();
        int targetWidth = 0;
        int targetHeight = 0;
        try {
            targetWidth = Integer.parseInt(widthTextField.getText());
            targetHeight = Integer.parseInt(heightTextField.getText());
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(this,
                    "Only numeric integer input is accepted!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            throw exception;
        }

            final int width = targetWidth;
            final int height = targetHeight;
            String fileName = JOptionPane.showInputDialog("Insert file name: ");
            File saveFile = new File(fileName+"."+format);
            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(saveFile);
            int rval = chooser.showSaveDialog(formats);
            if (rval == JFileChooser.APPROVE_OPTION) {
                final File approvedFile = chooser.getSelectedFile();
                if (targetWidth == scene.getWidth() && targetHeight == scene.getHeight()) {
                    //avoid re-rendering
                    try {
                        ImageIO.write(scene.getImg(), format, approvedFile);
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(this,
                                "Error when writing to file",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    RenderingProgressBarFrame renderingProgressBarFrame =
                            new RenderingProgressBarFrame(scene, targetWidth*targetHeight,
                                    new SavingTask() {
                                        @Override
                                        protected Void doInBackground() throws Exception {
                                            ImageIO.write(scene.renderForOutput(20, width,
                                                    height), format, approvedFile);
                                            return null;
                                        }

                                    });
                }
            }
    }
}
