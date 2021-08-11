package org.deep.caffeine.ui;

import javax.swing.*;
import java.awt.*;

public class MainUI extends JFrame {
    private final JTextField pathField;
    private final JButton browseButton;
    private final JTree fileTree;
    private final JTextArea inputArea;
    private final JTextArea expectedArea;
    private final JTextArea outputArea;
    private final JButton clearInputButton;
    private final JButton clearExpectedButton;
    private final JButton clearOutputButton;
    private final JButton formatFileButton;
    private final JButton compileFileButton;
    private final JButton runFileButton;

    public MainUI() {
        setTitle("Caffeine Leet");
        setPreferredSize(new Dimension(800, 800));

        pathField = new JTextField(50);
        browseButton = new JButton("Browse");
        fileTree = new JTree();
        inputArea = new JTextArea();
        expectedArea = new JTextArea();
        outputArea = new JTextArea();

        clearInputButton = new JButton("Clear Input");
        clearExpectedButton = new JButton("Clear Expected");
        clearOutputButton = new JButton("Clear Output");
        formatFileButton = new JButton("Format File");
        compileFileButton = new JButton("Compile File");
        runFileButton = new JButton("Run File");

        initLayout();
    }

    private void initLayout() {
        var layout = new GridBagLayout();
        setLayout(layout);

        var inputScrollPane = new JScrollPane(inputArea);
        var expectedScrollPane = new JScrollPane(expectedArea);
        var outputScrollPane = new JScrollPane(outputArea);

        add(pathField,
                new GBC(0, 0, 3, 1)
                        .setFill(GridBagConstraints.HORIZONTAL)
                        .setWeight(100, 0)
                        .setInsets(10, 10, 5, 5));
        add(browseButton,
                new GBC(3, 0)
                        .setFill(GridBagConstraints.NONE)
                        .setWeight(100, 0)
                        .setAnchor(GridBagConstraints.WEST)
                        .setInsets(10, 5, 5, 10));
        add(fileTree,
                new GBC(0, 1, 4, 1)
                        .setFill(GridBagConstraints.BOTH)
                        .setWeight(100, 50)
                        .setInsets(5, 10, 5, 10));

        var inputPane = new JPanel();
        var inputPaneLayout = new GridLayout(0, 2, 5, 0);
        inputPane.setLayout(inputPaneLayout);
        inputPane.add(inputScrollPane);
        inputPane.add(expectedScrollPane);
        add(inputPane, new GBC(0, 2, 4, 1)
                .setFill(GridBagConstraints.BOTH)
                .setWeight(100, 100)
                .setInsets(5, 10, 5, 10));

        add(outputScrollPane,
                new GBC(0, 3, 4, 1)
                        .setFill(GridBagConstraints.BOTH)
                        .setWeight(100, 100)
                        .setInsets(5, 10, 5, 10));

        var leftButtonPane = new JPanel();
        var leftGridLayout = new GridLayout(3, 1, 0, 5);
        leftButtonPane.setLayout(leftGridLayout);
        leftButtonPane.add(clearInputButton);
        leftButtonPane.add(clearExpectedButton);
        leftButtonPane.add(clearOutputButton);
        add(leftButtonPane, new GBC(0, 4)
                .setFill(GridBagConstraints.NONE)
                .setWeight(100, 0)
                .setAnchor(GridBagConstraints.WEST)
                .setInsets(5, 10, 10, 0));

        var rightButtonPane = new JPanel();
        var rightGridLayout = new GridLayout(3, 1, 0, 5);
        rightButtonPane.setLayout(rightGridLayout);
        rightButtonPane.add(formatFileButton);
        rightButtonPane.add(compileFileButton);
        rightButtonPane.add(runFileButton);
        add(rightButtonPane, new GBC(3, 4)
                .setFill(GridBagConstraints.NONE)
                .setWeight(100, 0)
                .setAnchor(GridBagConstraints.EAST)
                .setInsets(5, 0, 10, 10));

        pack();
    }
}
