package org.deep.caffeine.ui;

import org.deep.caffeine.model.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DiffPanel extends JPanel {
    private static final Color REMOVE_COLOR = new Color(251, 199, 199);
    private static final Color ADD_COLOR = new Color(192, 242, 192);
    private final List<List<DiffString>> diffStringMatrix;

    public DiffPanel(List<List<DiffString>> diffStringMatrix) {
        this.diffStringMatrix = diffStringMatrix;
        initLayout();
    }

    private void initLayout() {
        setLayout(new GridLayout(diffStringMatrix.size(), 1));
        for (var rowStrings : diffStringMatrix) {
            var row = generateRow(rowStrings);
            add(row);
        }
    }

    private JPanel generateRow(List<DiffString> rowStrings) {
        var row = new JPanel();
        row.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        for (var rowString : rowStrings) {
            var label = new JLabel(rowString.getText());
            label.setOpaque(true);
            switch (rowString.getType()) {
                case New:
                    label.setBackground(REMOVE_COLOR);
                    break;
                case Old:
                    label.setBackground(ADD_COLOR);
                    break;
                case Same:
                    break;
            }
            row.add(label);
        }
        return row;
    }
}
