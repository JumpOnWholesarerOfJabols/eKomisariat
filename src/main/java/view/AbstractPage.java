package main.java.view;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public abstract class AbstractPage {
    protected static final Color BG_COLOR = new Color(35,78,117);
    protected static final Border BORDER = BorderFactory.createEmptyBorder(60, 145, 60, 145);
    protected JPanel rootPanel;

    protected AbstractPage(){
        rootPanel = new JPanel();
        rootPanel.setBackground(BG_COLOR);
        rootPanel.setBorder(BORDER);
    }

    public abstract JPanel generatePage(CardLayout cardLayout, JPanel mainPanel);
}
