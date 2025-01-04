package main.java.view.AuthenticationPage;

import main.java.view.AbstractPage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AuthenticationPage extends AbstractPage {
    protected static final Dimension BTN_DIMENSION = new Dimension(200, 50);
    private JPanel centerPanel;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public JPanel generatePage(CardLayout cardLayout, JPanel mainPanel) {
        initializeRootPanel();

        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;

        addCenterPanel();
        rootPanel.add(createLogoLabel(), BorderLayout.NORTH);
        addButtonPanel();

        return rootPanel;
    }

    private JLabel createLogoLabel() {
        ImageIcon logo = new ImageIcon("src/main/resources/p.png");
        JLabel logoLabel = new JLabel(logo);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 30));
        return logoLabel;
    }

    private void initializeRootPanel() {
        rootPanel.setLayout(new BorderLayout());
        rootPanel.setBackground(new Color(23, 29, 50));
    }

    private void addCenterPanel() {
        centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(40, 50, 80));

        centerPanel.add(new LoginPanel(cardLayout,mainPanel), BorderLayout.CENTER);

        rootPanel.add(centerPanel, BorderLayout.CENTER);
    }

    private void addButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setBackground(new Color(24, 35, 53));

        buttonPanel.add(createButton1());
        buttonPanel.add(createButton2());

        rootPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    protected JButton createButton1() {
        JButton button = createHoverButton(
                "Logowanie",
                "Logowanie",
                new Color(40, 50, 90)
        );
        button.setBackground(new Color(30, 40, 90));
        button.setForeground(Color.WHITE);

        button.addActionListener(e -> {
            switchToPanel(new LoginPanel(cardLayout, mainPanel));
        });

        return button;
    }

    protected JButton createButton2() {
        JButton button = createHoverButton(
                "Rejestracja",
                "Rejestracja",
                new Color(40, 50, 100)
        );
        button.setBackground(new Color(30, 40, 100));
        button.setForeground(Color.WHITE);

        button.addActionListener(e -> {
            switchToPanel(new RegisterPanel(cardLayout, mainPanel));
        });

        return button;
    }

    private void switchToPanel(JPanel newPanel) {
        centerPanel.removeAll();
        centerPanel.add(newPanel, BorderLayout.CENTER);
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    protected JButton createHoverButton(String text, String hoverText, Color hoverColor) {
        JButton button = new JButton(text);
        button.setPreferredSize(BTN_DIMENSION);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setBackground(new Color(50, 60, 110));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
                button.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(30, 40, 90));
                button.setForeground(Color.WHITE);
            }
        });

        return button;
    }
}
