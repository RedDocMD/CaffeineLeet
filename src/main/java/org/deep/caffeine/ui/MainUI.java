package org.deep.caffeine.ui;

import com.github.difflib.text.*;
import com.google.gson.*;
import org.deep.caffeine.cache.*;
import org.deep.caffeine.lang.*;
import org.deep.caffeine.model.*;
import org.jsoup.*;
import org.jsoup.nodes.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.*;
import java.util.stream.*;

public class MainUI extends JFrame {
    private static final Language[] LANGUAGES = {new Cpp(), new Go(), new Python(), new Rust()};
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
    private final JButton debugCompileButton;
    private final JButton runFileButton;
    private final JButton runAndDiffButton;
    private final JButton refreshButton;
    private final InterfaceModel interfaceModel;
    private final Cache cache;
    private final File cacheFile;
    private final Gson gson;
    private final Font iosevkaHeavy;

    public MainUI() throws IOException, FontFormatException {
        setTitle("Caffeine Leet");
        setPreferredSize(new Dimension(800, 800));

        interfaceModel = new InterfaceModel();
        var homeDir = new File(System.getProperty("user.home"));
        cacheFile = new File(homeDir, ".caffeine_leet.cache");
        Cache tempCache = null;
        gson = new Gson();
        if (cacheFile.exists() && cacheFile.isFile()) {
            try {
                var content = Files.readString(Path.of(cacheFile.getPath()));
                tempCache = gson.fromJson(content, Cache.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (tempCache == null)
            tempCache = new Cache();
        cache = tempCache;
        interfaceModel.setMainDirectory(cache.getDirectory());

        var baseIosevkaRegular = Font.createFont(Font.TRUETYPE_FONT,
                Objects.requireNonNull(getClass().getResourceAsStream("/iosevka-etoile-regular.ttf")));
        Font iosevkaRegular = baseIosevkaRegular.deriveFont(13.0f);
        var baseIosevkaHeavy = Font.createFont(Font.TRUETYPE_FONT,
                Objects.requireNonNull(getClass().getResourceAsStream("/iosevka-etoile-heavy.ttf")));
        iosevkaHeavy = baseIosevkaHeavy.deriveFont(13.0f);

        pathField = new JTextField(50);
        browseButton = new JButton("Browse");
        var fileTreeModel = new FileTreeModel();
        fileTree = new JTree(fileTreeModel);

        pathField.setFont(iosevkaRegular);
        browseButton.setFont(iosevkaHeavy);
        fileTree.setFont(iosevkaRegular);

        inputArea = new JTextArea();
        expectedArea = new JTextArea();
        outputArea = new JTextArea();

        var baseFiraCodeRegular = Font.createFont(Font.TRUETYPE_FONT,
                Objects.requireNonNull(getClass().getResourceAsStream("/FiraCode-Regular.ttf")));
        Font firaCodeRegular = baseFiraCodeRegular.deriveFont(14.0f);
        inputArea.setFont(firaCodeRegular);
        expectedArea.setFont(firaCodeRegular);
        outputArea.setFont(firaCodeRegular);

        clearInputButton = new JButton("Clear Input");
        clearExpectedButton = new JButton("Clear Expected");
        clearOutputButton = new JButton("Clear Output");
        formatFileButton = new JButton("Format File");
        compileFileButton = new JButton("Compile File");
        debugCompileButton = new JButton("Debug Compile File");
        runFileButton = new JButton("Run File");
        runAndDiffButton = new JButton("Run and Diff File");
        refreshButton = new JButton("Refresh");

        clearInputButton.setFont(iosevkaHeavy);
        clearExpectedButton.setFont(iosevkaHeavy);
        clearOutputButton.setFont(iosevkaHeavy);
        formatFileButton.setFont(iosevkaHeavy);
        compileFileButton.setFont(iosevkaHeavy);
        debugCompileButton.setFont(iosevkaHeavy);
        runFileButton.setFont(iosevkaHeavy);
        runAndDiffButton.setFont(iosevkaHeavy);
        refreshButton.setFont(iosevkaHeavy);

        pathField.setText(cache.getDirectory());

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
        pathLabel.setFont(iosevkaHeavy);
        pathPanel.add(pathLabel, BorderLayout.WEST);
        pathPanel.add(pathField, BorderLayout.CENTER);
        add(pathPanel,
                new GBC(0, 0, 3, 1)
                        .setFill(GridBagConstraints.HORIZONTAL)
                        .setWeight(100, 0)
                        .setInsets(10, 10, 5, 10));
        var pathEditPanel = new JPanel();
        pathEditPanel.add(browseButton);
        pathEditPanel.add(refreshButton);
        add(pathEditPanel,
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
        var titledBorder = BorderFactory.createTitledBorder(etched, "Input");
        titledBorder.setTitleFont(iosevkaHeavy);
        inputScrollPane.setBorder(titledBorder);
        inputPanel.add(inputScrollPane);
        etched = BorderFactory.createEtchedBorder();
        titledBorder = BorderFactory.createTitledBorder(etched, "Expected Output");
        titledBorder.setTitleFont(iosevkaHeavy);
        expectedScrollPane.setBorder(titledBorder);
        inputPanel.add(expectedScrollPane);
        add(inputPanel, new GBC(0, 2, 4, 1)
                .setFill(GridBagConstraints.BOTH)
                .setWeight(100, 100)
                .setInsets(10, 10, 5, 10));

        etched = BorderFactory.createEtchedBorder();
        titledBorder = BorderFactory.createTitledBorder(etched, "Output");
        titledBorder.setTitleFont(iosevkaHeavy);
        outputScrollPane.setBorder(titledBorder);
        add(outputScrollPane,
                new GBC(0, 3, 4, 1)
                        .setFill(GridBagConstraints.BOTH)
                        .setWeight(100, 100)
                        .setInsets(5, 10, 5, 10));

        var clearButtonsPanel = new JPanel();
        var clearButtonsGridLayout = new GridLayout(1, 3, 10, 0);
        clearButtonsPanel.setLayout(clearButtonsGridLayout);
        clearButtonsPanel.add(clearInputButton);
        clearButtonsPanel.add(clearExpectedButton);
        clearButtonsPanel.add(clearOutputButton);
        add(clearButtonsPanel, new GBC(0, 4, 4, 1)
                .setFill(GridBagConstraints.NONE)
                .setWeight(100, 0)
                .setInsets(10, 10, 10, 10));

        var fileButtonsPanel = new JPanel();
        var fileButtonsFlowLayout = new FlowLayout(FlowLayout.CENTER, 10, 5);
        fileButtonsPanel.setLayout(fileButtonsFlowLayout);
        fileButtonsPanel.add(formatFileButton);
        fileButtonsPanel.add(compileFileButton);
        fileButtonsPanel.add(debugCompileButton);
        fileButtonsPanel.add(runFileButton);
        fileButtonsPanel.add(runAndDiffButton);
        add(fileButtonsPanel, new GBC(0, 5, 4, 1)
                .setFill(GridBagConstraints.NONE)
                .setWeight(100, 0)
                .setInsets(5, 10, 10, 10));

        // Setup listeners
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                var cacheContent = gson.toJson(cache);
                try {
                    var writer = new BufferedWriter(new FileWriter(cacheFile));
                    writer.write(cacheContent);
                    writer.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                super.windowClosing(e);
            }
        });

        pathField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                var dir = pathField.getText().trim();
                interfaceModel.setMainDirectory(dir);
                cache.setDirectory(dir);
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
                cache.setDirectory(dir);
                fileTree.updateUI();
            }
        });

        refreshButton.addActionListener(e -> fileTree.updateUI());

        disableFileButtons();
        fileTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        fileTree.addTreeSelectionListener(e -> {
            var node = (FileTreeNode) fileTree.getLastSelectedPathComponent();
            disableFileButtons();
            if (node == null) {
                interfaceModel.setSelectedFile(null);
                interfaceModel.setSelectedFileLanguage(null);
            } else if (node.isLeaf()) {
                var file = node.getFile();
                interfaceModel.setSelectedFile(file);
                interfaceModel.setSelectedFileLanguage(null);
                for (var lang : LANGUAGES) {
                    if (lang.hasFile(file.getName())) {
                        interfaceModel.setSelectedFileLanguage(lang);
                        if (lang.hasCompiler()) enableFileButtons();
                        else enableNonCompileFileButtons();
                        inputArea.setText("");
                        expectedArea.setText("");
                        outputArea.setText("");
                        break;
                    }
                }
                var entry = cache.getEntry(file);
                if (entry != null) {
                    inputArea.setText(entry.getInput());
                    expectedArea.setText(entry.getExpected());
                }
            }
        });

        formatFileButton.addActionListener(e -> {
            var file = interfaceModel.getSelectedFile();
            var lang = interfaceModel.getSelectedFileLanguage();
            assert (lang != null);
            try {
                outputArea.setText("");
                var result = lang.format(file);
                if (result != null && result.getExitCode() != 0) {
                    outputArea.setText(result.getStderrValue());
                }
            } catch (IOException | InterruptedException ex) {
                ex.printStackTrace();
            }
        });

        compileFileButton.addActionListener(e -> compileFile(false));
        debugCompileButton.addActionListener(e -> compileFile(true));

        runFileButton.addActionListener(e -> runFile());
        runAndDiffButton.addActionListener(e -> {
            var result = runFile();
            if (result != null && result.getExitCode() == 0) {
                var expectedOutput = expectedArea.getText();
                var actualOutput = result.getStdoutValue();
                try {
                    diffOutputs(expectedOutput, actualOutput);
                } catch (IOException | FontFormatException ex) {
                    System.err.println("Failed to create diff UI: " + e);
                    System.exit(1);
                }
            }
        });

        clearInputButton.addActionListener(e -> inputArea.setText(""));
        clearOutputButton.addActionListener(e -> outputArea.setText(""));
        clearExpectedButton.addActionListener(e -> expectedArea.setText(""));

        pack();
    }

    private void diffOutputs(String expected, String actual) throws IOException, FontFormatException {
        var expectedLines = splitLines(expected);
        var actualLines = splitLines(actual);
        var diffRows = diffRows(expectedLines, actualLines);
        var diffStringsMatrix = diffRows
                .stream()
                .map(this::diffRowToStrings)
                .collect(Collectors.toList());
        var mustNotDiff = diffStringsMatrix.stream()
                .allMatch(list -> list.stream()
                        .allMatch(str -> str.getType() == DiffStringType.Same));
        if (!mustNotDiff) {
            var diff = new DiffPanel(diffStringsMatrix);
            JOptionPane.showMessageDialog(this, diff,
                    "Outputs differ", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Obtained output matches expected",
                    "Outputs same", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private List<DiffRow> diffRows(List<String> expected, List<String> actual) {
        var diffRowGenerator = DiffRowGenerator.create()
                .showInlineDiffs(true)
                .inlineDiffByWord(true)
                .mergeOriginalRevised(true)
                .columnWidth(200)
                .build();
        List<DiffRow> rows = new ArrayList<>();
        if (expected.size() == actual.size()) {
            for (int i = 0, size = expected.size(); i < size; i++) {
                var expectedList = List.of(expected.get(i));
                var actualList = List.of(actual.get(i));
                var rowsList = diffRowGenerator.generateDiffRows(expectedList, actualList);
                assert (rowsList.size() == 1);
                rows.add(rowsList.get(0));
            }
        } else {
            rows = diffRowGenerator.generateDiffRows(expected, actual);
        }
        return rows;
    }

    private List<String> splitLines(String content) {
        var lines = new ArrayList<>(Arrays.asList(content.split("\n")));
        if (lines.size() >= 1 && lines.get(lines.size() - 1).isEmpty()) {
            lines.remove(lines.size() - 1);
        }
        return lines;
    }

    private List<DiffString> diffRowToStrings(DiffRow row) {
        var doc = Jsoup.parseBodyFragment(row.getOldLine());
        var body = doc.body();
        return body.childNodes()
                .stream()
                .map(node -> {
                    if (node instanceof TextNode) {
                        var textNode = (TextNode) node;
                        return new DiffString(textNode.text(), DiffStringType.Same);
                    } else {
                        assert (node instanceof Element);
                        var el = (Element) node;
                        assert (el.tagName().equals("span"));
                        var classVal = el.attr("class");
                        if (classVal.equals("editOldInline")) {
                            return new DiffString(el.html(), DiffStringType.Old);
                        } else if (classVal.equals("editNewInline")) {
                            return new DiffString(el.html(), DiffStringType.New);
                        }
                        throw new IllegalArgumentException("Invalid class attribute of DiffRow span");
                    }
                })
                .collect(Collectors.toList());
    }

    private ProcessResult runFile() {
        var file = interfaceModel.getSelectedFile();
        var lang = interfaceModel.getSelectedFileLanguage();
        outputArea.setText("");
        var entry = new CacheEntry(inputArea.getText(), expectedArea.getText());
        cache.addEntry(file, entry);
        assert (lang != null);
        try {
            ProcessResult result;
            if (lang.hasCompiler()) {
                result = lang.compile(file, false);
                if (result.getExitCode() == 0) {
                    assert (result.getCreatedFile().isPresent());
                    var compiledFile = result.getCreatedFile().get();
                    result = lang.run(compiledFile, inputArea.getText());
                }
            } else {
                result = lang.run(file, inputArea.getText());
            }
            if (result.getExitCode() == 0) {
                outputArea.setText(result.getStdoutValue());
            } else {
                outputArea.setText(result.getStderrValue());
            }
            return result;
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private void compileFile(boolean debug) {
        var file = interfaceModel.getSelectedFile();
        var lang = interfaceModel.getSelectedFileLanguage();
        outputArea.setText("");
        assert (lang != null);
        try {
            var result = lang.compile(file, debug);
            if (result.getExitCode() != 0)
                outputArea.setText(result.getStderrValue());
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    private void disableFileButtons() {
        formatFileButton.setEnabled(false);
        compileFileButton.setEnabled(false);
        debugCompileButton.setEnabled(false);
        runFileButton.setEnabled(false);
        runAndDiffButton.setEnabled(false);
    }

    private void enableFileButtons() {
        formatFileButton.setEnabled(true);
        compileFileButton.setEnabled(true);
        debugCompileButton.setEnabled(true);
        runFileButton.setEnabled(true);
        runAndDiffButton.setEnabled(true);
    }

    private void enableNonCompileFileButtons() {
        formatFileButton.setEnabled(true);
        runFileButton.setEnabled(true);
        runAndDiffButton.setEnabled(true);
    }

    class FileTreeModel implements TreeModel {
        private final EventListenerList listenerList = new EventListenerList();

        @Override
        public Object getRoot() {
            var dir = interfaceModel.getMainDirectory();
            return interfaceModel.isMainDirectoryValid()
                    ? new FileTreeNode(dir, true, Arrays.asList(LANGUAGES))
                    : EmptyFileTreeNode.INSTANCE;
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
