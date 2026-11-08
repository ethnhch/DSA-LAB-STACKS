package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;

/** Modern dynamic feedback card: cyan/emerald for results, rose for errors. */
public class Status extends JLabel {
    private Color borderCol = Theme.LINE;
    private Color bgCol = Theme.PANEL;

    public Status() {
        super(" ", SwingConstants.CENTER);
        setFont(Theme.BOLD.deriveFont(12.5f));
        setForeground(Theme.TEXT_SUB);
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
    }

    public void ok(String message) {
        setForeground(new Color(0xE0E7FF));
        borderCol = new Color(0x3B3D72);
        bgCol = new Color(0x141829);
        setText(message);
        repaint();
    }

    public void error(String message) {
        setForeground(Theme.ERROR_BRIGHT);
        borderCol = new Color(0x5E1F2F);
        bgCol = new Color(0x22111A);
        setText("! " + message);
        repaint();
    }

    @Override protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(bgCol);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
        g2.setColor(borderCol);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
        g2.dispose();
        super.paintComponent(g);
    }
}


