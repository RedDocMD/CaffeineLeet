package org.deep.caffeine;

import org.deep.caffeine.ui.MainUI;

import javax.swing.*;
import java.awt.*;

public class Caffeine {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                var ui = new MainUI();
                ui.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/caffeine.png")));
                ui.setVisible(true);
                ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
    }
}
