package org.deep.caffeine;

import org.deep.caffeine.ui.MainUI;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Caffeine {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainUI ui = null;
                try {
                    ui = new MainUI();
                } catch (IOException | FontFormatException e) {
                    System.err.println("Failed to init UI: " + e);
                    System.exit(1);
                }
                ui.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/caffeine.png")));
                ui.setVisible(true);
                ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
    }
}
