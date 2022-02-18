package com.aktog.yusuf;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    public Main() {
        this.add(new GamePanel());
        loadPreferences();
    }

    final void loadPreferences() {

        this.setTitle("Wordly");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
