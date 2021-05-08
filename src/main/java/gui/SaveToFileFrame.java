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
                String fileName = JOptionPane.showInputDialog("Insert file name: ");
                saveToFile(fileName);
                break;
        }
    }


    private void saveToFile(String fileName) {
        String format = (String)formats.getSelectedItem();
        int targetWidth = Integer.parseInt(widthTextField.getText());
        int targetHeight = Integer.parseInt(heightTextField.getText());
        File saveFile = new File(fileName+"."+format);
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(saveFile);
        int rval = chooser.showSaveDialog(formats);
        if (rval == JFileChooser.APPROVE_OPTION) {
            saveFile = chooser.getSelectedFile();
            try {
                ImageIO.write(scene.renderForOutput(20, targetWidth, targetHeight), format, saveFile);
            } catch (IOException ex) {
            }
        }
    }
}
