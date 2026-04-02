package com.inventory.main;

import com.inventory.ui.frames.LoginFrame;
import javax.swing.*;

/**
 * Entry point of the application.
 * EBT: SwingUtilities.invokeLater ensures all Swing code runs on the EDT
 *      (Event Dispatch Thread) — core EBT/thread-safety practice.
 */
public class MainApp {

    public static void main(String[] args) {

        // Set system look & feel (optional, makes it feel more native)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // EBT: All UI work runs on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}

