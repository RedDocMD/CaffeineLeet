package org.deep.caffeine;

import com.formdev.flatlaf.FlatLightLaf;
import org.deep.caffeine.ui.MainUI;

import javax.swing.*;
import java.awt.*;

public class Caffeine {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                FlatLightLaf.setup();
                var ui = new MainUI();
                ui.setVisible(true);
                ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
    }
}
