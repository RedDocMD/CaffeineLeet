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
    private final JScrollPane inputScrollPane;
    private final JScrollPane expectedScrollPane;
    private final JScrollPane outputScrollPane;
    private final JButton clearInputButton;
    private final JButton clearExpectedButton;
    private final JButton clearOutputButton;
    private final JButton formatFileButton;
    private final JButton compileFileButton;
    private final JButton runFileButton;

    public MainUI() {
        setTitle("Caffeine Leet");
        setPreferredSize(new Dimension(1000, 700));

        pathField = new JTextField(50);
        browseButton = new JButton("Browse");
        fileTree = new JTree();
        inputArea = new JTextArea();
        expectedArea = new JTextArea();
        outputArea = new JTextArea();
        inputScrollPane = new JScrollPane(inputArea);
        expectedScrollPane = new JScrollPane(expectedArea);
        outputScrollPane = new JScrollPane(outputArea);
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
        add(inputScrollPane,
                new GBC(0, 2, 2, 1)
                        .setFill(GridBagConstraints.BOTH)
                        .setWeight(100, 100)
                        .setInsets(5, 10, 5, 5));
        add(expectedScrollPane,
                new GBC(2, 2, 2, 1)
                        .setFill(GridBagConstraints.BOTH)
                        .setWeight(100, 100)
                        .setInsets(5, 5, 5, 10));
        add(outputScrollPane,
                new GBC(0, 3, 4, 1)
                        .setFill(GridBagConstraints.BOTH)
                        .setWeight(100, 100)
                        .setInsets(5, 10, 5, 10));
        add(clearInputButton, new GBC(0, 4)
                .setFill(GridBagConstraints.NONE)
                .setWeight(100, 0)
                .setAnchor(GridBagConstraints.WEST)
                .setInsets(5, 10, 5, 0));
        add(clearExpectedButton, new GBC(0, 5)
                .setFill(GridBagConstraints.NONE)
                .setWeight(100, 0)
                .setAnchor(GridBagConstraints.WEST)
                .setInsets(5, 10, 5, 0));
        add(clearOutputButton, new GBC(0, 6)
                .setFill(GridBagConstraints.NONE)
                .setWeight(100, 0)
                .setAnchor(GridBagConstraints.WEST)
                .setInsets(5, 10, 10, 0));
        add(formatFileButton, new GBC(3, 4)
                .setFill(GridBagConstraints.NONE)
                .setWeight(100, 0)
                .setAnchor(GridBagConstraints.EAST)
                .setInsets(5, 0, 5, 10));
        add(compileFileButton, new GBC(3, 5)
                .setFill(GridBagConstraints.NONE)
                .setWeight(100, 0)
                .setAnchor(GridBagConstraints.EAST)
                .setInsets(5, 0, 5, 10));
        add(runFileButton, new GBC(3, 6)
                .setFill(GridBagConstraints.NONE)
                .setWeight(100, 0)
                .setAnchor(GridBagConstraints.EAST)
                .setInsets(5, 0, 10, 10));

        pack();
    }
}
