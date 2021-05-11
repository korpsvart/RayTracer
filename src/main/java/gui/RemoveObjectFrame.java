package gui;

import rendering.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class RemoveObjectFrame extends JFrame implements ActionListener, ListSelectionListener {

    DefaultListModel<SceneObject> listModel = new DefaultListModel();
    private static final String removeString = "Remove";
    private static final String editString = "Edit";
    private Visualizer visualizer;
    private Scene scene;
    JList list;
    
    public RemoveObjectFrame(Visualizer visualizer, Scene scene) {
        this.visualizer = visualizer;
        this.scene = scene;
        ArrayList<SceneObject> sceneObjects = scene.getTopLevelSceneObjects();
        listModel.addAll(sceneObjects);

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
        SceneObject sceneObject;
        switch (e.getActionCommand()) {
            case "Remove":
                sceneObject = (SceneObject) list.getSelectedValue();
                scene.removeSceneObject(sceneObject);
                listModel.remove(list.getSelectedIndex());
                visualizer.renderScene(scene);
                break;
            case "Edit":
                sceneObject = (SceneObject) list.getSelectedValue();
                GeometricObject geometricObject = sceneObject.getGeometricObject();
                if (geometricObject instanceof BezierSurface33) {
                    AddBezierSurface addBezierSurface = new AddBezierSurface(visualizer, scene,
                            sceneObject);
                } else if (geometricObject instanceof Sphere) {
                    AddSphereFrame addSphereFrame = new AddSphereFrame(visualizer, scene, sceneObject);
                } else if (geometricObject instanceof PhysicalBox) {
                    AddBoxFrame addBoxFrame = new AddBoxFrame(visualizer, scene, sceneObject);
                }  else if (geometricObject instanceof BSurface) {
                    AddBSplineSurfaceFrame addBSplineSurfaceFrame = new AddBSplineSurfaceFrame(visualizer, scene, sceneObject);
                } else if (geometricObject instanceof BezierPatchesData) {
                    AddTeapotFrame addTeapotFrame = new AddTeapotFrame(visualizer, scene, sceneObject);
                }
                this.dispose();
        }
    }

    private void rebuildListModel(int selectedIndex) {
        //remove object from listModel at selected index
        //and rebuild list model with new scene objects
        listModel.remove(selectedIndex);
        listModel.addAll(scene.getTopLevelSceneObjects());
        list = new JList(listModel);
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {

    }
}
