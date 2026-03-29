package com.inventory.main;

import javax.swing.UIManager;
import javax.swing.SwingUtilities;

import com.inventory.ui.LoginFrame;

public class MainApp {

    public static void main(String[] args) {

        // Set system look and feel (modern UI)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Look and Feel not applied");
        }

        // Run UI in Event Dispatch Thread (best practice)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginFrame().setVisible(true);
            }
        });
    }
}
