package gui;

import rendering.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class RemoveLightFrame extends JFrame implements ActionListener, ListSelectionListener {

    DefaultListModel<LightSource> listModel = new DefaultListModel();
    private static final String removeString = "Remove";
    private static final String editString = "Edit";
    private Visualizer visualizer;
    private Scene scene;
    JList list;

    public RemoveLightFrame(Visualizer visualizer, Scene scene) {
        setTitle("Edit lights");
        this.visualizer = visualizer;
        this.scene = scene;
        listModel.addAll(scene.getLightSources());

        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(5);
        JScrollPane listScrollPane = new JScrollPane(list);

        JButton removeButton = new JButton(removeString);
        removeButton.setActionCommand(removeString);
        removeButton.addActionListener(this);

        JButton editButton = new JButton(editString);
        editButton.setActionCommand(editString);
        editButton.addActionListener(this);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane,
                BoxLayout.LINE_AXIS));
        buttonPane.add(removeButton);
        buttonPane.add(editButton);
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        add(listScrollPane, BorderLayout.CENTER);
        add(buttonPane, BorderLayout.PAGE_END);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Display the window.
        this.pack();
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        LightSource lightSource;
        switch (e.getActionCommand()) {
            case "Remove":
                lightSource = (LightSource) list.getSelectedValue();
                scene.removeLightSource(lightSource);
                listModel.remove(list.getSelectedIndex());
                visualizer.renderSceneWithoutRebuildingBVH(scene);
                this.dispose();
                break;
            case "Edit":
                lightSource = (LightSource) list.getSelectedValue();
                if (lightSource instanceof PointLight) {
                    AddPointLightFrame addPointLightFrame = new AddPointLightFrame(visualizer, scene,
                            (PointLight)lightSource);
                } else if (lightSource instanceof DistantLight) {
                    AddDistantLightFrame addDistantLightFrame = new AddDistantLightFrame(visualizer, scene,
                            (DistantLight) lightSource);
                }
                this.dispose();
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {

    }
}
