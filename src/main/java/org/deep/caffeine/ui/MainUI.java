package org.deep.caffeine.ui;

import org.deep.caffeine.lang.Cpp;
import org.deep.caffeine.lang.Language;
import org.deep.caffeine.model.EmptyFileTreeNode;
import org.deep.caffeine.model.FileTreeNode;
import org.deep.caffeine.model.InterfaceModel;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

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
    private final InterfaceModel interfaceModel;

    private static final Language[] LANGUAGES = {new Cpp()};

    public MainUI() {
        setTitle("Caffeine Leet");
        setPreferredSize(new Dimension(800, 800));

        interfaceModel = new InterfaceModel();

        pathField = new JTextField(50);
        browseButton = new JButton("Browse");
        var fileTreeModel = new FileTreeModel();
        fileTree = new JTree(fileTreeModel);
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
        // Setup visuals
        var layout = new GridBagLayout();
        setLayout(layout);

        var inputScrollPane = new JScrollPane(inputArea);
        var expectedScrollPane = new JScrollPane(expectedArea);
        var outputScrollPane = new JScrollPane(outputArea);
        var fileTreeScrollPane = new JScrollPane(fileTree);

        var pathPanel = new JPanel();
        var pathPanelLayout = new BorderLayout(5, 0);
        pathPanel.setLayout(pathPanelLayout);
        var pathLabel = new JLabel("Directory:", JLabel.RIGHT);
        pathPanel.add(pathLabel, BorderLayout.WEST);
        pathPanel.add(pathField, BorderLayout.CENTER);
        add(pathPanel,
                new GBC(0, 0, 3, 1)
                        .setFill(GridBagConstraints.HORIZONTAL)
                        .setWeight(100, 0)
                        .setInsets(10, 10, 5, 10));
        add(browseButton,
                new GBC(3, 0)
                        .setFill(GridBagConstraints.NONE)
                        .setWeight(100, 0)
                        .setAnchor(GridBagConstraints.WEST)
                        .setInsets(10, 5, 5, 10));

        var etched = BorderFactory.createEtchedBorder();
        fileTreeScrollPane.setBorder(etched);
        fileTree.setVisibleRowCount(8);
        add(fileTreeScrollPane,
                new GBC(0, 1, 4, 1)
                        .setFill(GridBagConstraints.HORIZONTAL)
                        .setWeight(100, 0)
                        .setInsets(10, 10, 5, 10));

        var inputPanel = new JPanel();
        var inputPaneLayout = new GridLayout(0, 2, 5, 0);
        inputPanel.setLayout(inputPaneLayout);
        etched = BorderFactory.createEtchedBorder();
        inputScrollPane.setBorder(BorderFactory.createTitledBorder(etched, "Input"));
        inputPanel.add(inputScrollPane);
        etched = BorderFactory.createEtchedBorder();
        expectedScrollPane.setBorder(BorderFactory.createTitledBorder(etched, "Expected Output"));
        inputPanel.add(expectedScrollPane);
        add(inputPanel, new GBC(0, 2, 4, 1)
                .setFill(GridBagConstraints.BOTH)
                .setWeight(100, 100)
                .setInsets(10, 10, 5, 10));

        etched = BorderFactory.createEtchedBorder();
        outputScrollPane.setBorder(BorderFactory.createTitledBorder(etched, "Output"));
        add(outputScrollPane,
                new GBC(0, 3, 4, 1)
                        .setFill(GridBagConstraints.BOTH)
                        .setWeight(100, 100)
                        .setInsets(5, 10, 5, 10));

        var leftButtonPanel = new JPanel();
        var leftGridLayout = new GridLayout(3, 1, 0, 5);
        leftButtonPanel.setLayout(leftGridLayout);
        leftButtonPanel.add(clearInputButton);
        leftButtonPanel.add(clearExpectedButton);
        leftButtonPanel.add(clearOutputButton);
        add(leftButtonPanel, new GBC(0, 4)
                .setFill(GridBagConstraints.NONE)
                .setWeight(100, 0)
                .setAnchor(GridBagConstraints.WEST)
                .setInsets(10, 10, 10, 0));

        var rightButtonPanel = new JPanel();
        var rightGridLayout = new GridLayout(3, 1, 0, 5);
        rightButtonPanel.setLayout(rightGridLayout);
        rightButtonPanel.add(formatFileButton);
        rightButtonPanel.add(compileFileButton);
        rightButtonPanel.add(runFileButton);
        add(rightButtonPanel, new GBC(3, 4)
                .setFill(GridBagConstraints.NONE)
                .setWeight(100, 0)
                .setAnchor(GridBagConstraints.EAST)
                .setInsets(10, 0, 10, 10));

        // Setup listeners
        pathField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                interfaceModel.setMainDirectory(pathField.getText().trim());
                if (interfaceModel.isMainDirectoryValid())
                    pathField.setForeground(Color.BLACK);
                else pathField.setForeground(Color.RED);
                fileTree.updateUI();
            }
        });

        var directoryChooser = new JFileChooser();
        directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        browseButton.addActionListener(e -> {
            directoryChooser.setCurrentDirectory(interfaceModel.getFileChooserDirectory());
            var result = directoryChooser.showOpenDialog(MainUI.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                var dir = directoryChooser.getSelectedFile().getPath();
                interfaceModel.setMainDirectory(dir);
                pathField.setText(dir);
                fileTree.updateUI();
            }
        });

        disableFileButtons();
        fileTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        fileTree.addTreeSelectionListener(e -> {
            var node = (FileTreeNode) fileTree.getLastSelectedPathComponent();
            if (node == null) {
                interfaceModel.setSelectedFile(null);
                interfaceModel.setSelectedFileLanguage(null);
                disableFileButtons();
            } else if (node.isLeaf()) {
                var file = node.getFile();
                interfaceModel.setSelectedFile(file);
                interfaceModel.setSelectedFileLanguage(null);
                for (var lang : LANGUAGES) {
                    if (lang.hasFile(file.getName())) {
                        interfaceModel.setSelectedFileLanguage(lang);
                        enableFileButtons();
                        break;
                    }
                }
            }
        });

        runFileButton.addActionListener(e -> {
            var file = interfaceModel.getSelectedFile();
            var lang = interfaceModel.getSelectedFileLanguage();
            if (lang != null) {
                try {
                    var result = lang.compile(file, false);
                    if (result.getExitCode() != 0) {
                        outputArea.setText(result.getStderrValue());
                    } else {
                        assert(result.getCreatedFile().isPresent());
                        var compiledFile = result.getCreatedFile().get();
                        result = lang.run(compiledFile, inputArea.getText());
                        if (result.getExitCode() == 0) {
                            outputArea.setText(result.getStdoutValue());
                        } else {
                            outputArea.setText(result.getStderrValue());
                        }
                    }
                } catch (IOException | InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });

        pack();
    }

    private void disableFileButtons() {
        formatFileButton.setEnabled(false);
        compileFileButton.setEnabled(false);
        runFileButton.setEnabled(false);
    }

    private void enableFileButtons() {
        formatFileButton.setEnabled(true);
        compileFileButton.setEnabled(true);
        runFileButton.setEnabled(true);
    }

    class FileTreeModel implements TreeModel {
        private final EventListenerList listenerList = new EventListenerList();

        @Override
        public Object getRoot() {
            var dir = interfaceModel.getMainDirectory();
            return interfaceModel.isMainDirectoryValid() ? new FileTreeNode(dir, true) : EmptyFileTreeNode.INSTANCE;
        }

        @Override
        public Object getChild(Object parent, int index) {
            var parentNode = (FileTreeNode) parent;
            var children = parentNode.children();
            return children.get(index);
        }

        @Override
        public int getChildCount(Object parent) {
            var parentNode = (FileTreeNode) parent;
            return parentNode.childCount();
        }

        @Override
        public boolean isLeaf(Object node) {
            var nodeThing = (FileTreeNode) node;
            return nodeThing.isLeaf();
        }

        @Override
        public void valueForPathChanged(TreePath path, Object newValue) {

        }

        @Override
        public int getIndexOfChild(Object parent, Object child) {
            if (parent == null || child == null)
                return -1;
            var parentNode = (FileTreeNode) parent;
            var childNode = (FileTreeNode) child;
            var rootNode = (FileTreeNode) getRoot();
            if (rootNode.hasChild(parentNode) && rootNode.hasChild(childNode) && parentNode.hasChild(childNode)) {
                var children = parentNode.children();
                for (int i = 0; i < children.size(); ++i) {
                    var childFileNode = children.get(i);
                    if (childFileNode.equals(childNode))
                        return i;
                }
            }
            return -1;
        }

        @Override
        public void addTreeModelListener(TreeModelListener l) {
            listenerList.add(TreeModelListener.class, l);
        }

        @Override
        public void removeTreeModelListener(TreeModelListener l) {
            listenerList.remove(TreeModelListener.class, l);
        }
    }
}
