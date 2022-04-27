package org.deep.caffeine.ui;

import org.deep.caffeine.model.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class DiffPanel extends JPanel {
    private static final Color REMOVE_COLOR = new Color(251, 199, 199);
    private static final Color ADD_COLOR = new Color(192, 242, 192);
    private final List<List<DiffString>> diffStringMatrix;
    private final Font iosevkaSemiBold;

    public DiffPanel(List<List<DiffString>> diffStringMatrix) throws IOException, FontFormatException {
        this.diffStringMatrix = diffStringMatrix;
        var baseIosevkaSemiBold = Font.createFont(Font.TRUETYPE_FONT,
                Objects.requireNonNull(getClass().getResourceAsStream("/iosevka-etoile-semibold.ttf")));
        iosevkaSemiBold = baseIosevkaSemiBold.deriveFont(13.0f);
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
            label.setFont(iosevkaSemiBold);
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
