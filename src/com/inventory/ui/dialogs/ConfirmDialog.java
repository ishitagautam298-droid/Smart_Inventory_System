package com.inventory.ui.dialogs;

import com.inventory.ui.theme.AppColors;
import com.inventory.ui.theme.AppFonts;
import com.inventory.ui.theme.UIFactory;

import javax.swing.*;
import java.awt.*;

/**
 * JFC: JDialog — reusable yes/no confirmation popup.
 * EBT: ActionListener on Yes/No buttons.
 * OOPs: Encapsulation of dialog state (confirmed).
 */
public class ConfirmDialog extends JDialog {

    private boolean confirmed = false;

    public ConfirmDialog(Window parent, String title, String message) {
        super(parent, title, ModalityType.APPLICATION_MODAL);
        setSize(400, 200);
        setResizable(false);
        setLocationRelativeTo(parent);
        buildUI(message);
    }

    private void buildUI(String message) {
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(AppColors.BG_CARD);

        // Icon + message
        JPanel body = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        body.setBackground(AppColors.BG_CARD);

        JLabel iconLbl = new JLabel("\u26A0\uFE0F");
        iconLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        JLabel msgLbl = new JLabel("<html><body style='width:240px'>" + message + "</body></html>");
        msgLbl.setFont(AppFonts.BODY);
        msgLbl.setForeground(AppColors.TEXT_PRIMARY);
        textPanel.add(msgLbl);

        body.add(iconLbl);
        body.add(textPanel);
        root.add(body, BorderLayout.CENTER);

        // Buttons
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        footer.setBackground(new Color(248, 250, 252));
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, AppColors.BORDER));

        JButton noBtn  = UIFactory.outlineButton("Cancel", AppColors.TEXT_SECONDARY);
        JButton yesBtn = UIFactory.dangerButton("Yes, confirm");

        // EBT: ActionListener
        noBtn.addActionListener(e -> dispose());
        yesBtn.addActionListener(e -> { confirmed = true; dispose(); });

        footer.add(noBtn);
        footer.add(yesBtn);
        root.add(footer, BorderLayout.SOUTH);

        setContentPane(root);
    }

    public boolean isConfirmed() { return confirmed; }
}
