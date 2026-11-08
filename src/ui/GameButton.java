package ui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;

/** Flat rounded button with hover highlight, press reaction, and disabled styling. */
public class GameButton extends JButton {
    private Color base;
    private boolean hover;
    private boolean pressed;

    public GameButton(String text, Runnable action) {
        this(text, action, Theme.ACCENT);
    }

    public GameButton(String text, Runnable action, Color base) {
        super(text);
        this.base = base;
        setFont(Theme.BOLD);
        setForeground(Theme.TEXT);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 16, 8, 16));
        addActionListener(e -> {
            if (isEnabled() && action != null) action.run();
        });
        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { if (isEnabled()) { hover = true; repaint(); } }
            @Override public void mouseExited(MouseEvent e) { hover = false; pressed = false; repaint(); }
            @Override public void mousePressed(MouseEvent e) { if (isEnabled()) { pressed = true; repaint(); } }
            @Override public void mouseReleased(MouseEvent e) { pressed = false; repaint(); }
        });
    }

    public void setBase(Color color) {
        this.base = color;
        repaint();
    }

    @Override public void setEnabled(boolean b) {
        super.setEnabled(b);
        if (!b) { hover = false; pressed = false; }
        setCursor(b ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) : Cursor.getDefaultCursor());
        repaint();
    }

    @Override protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        if (!isEnabled()) {
            g2.setColor(new Color(0x131722));
            g2.fillRoundRect(0, 0, w, h, 8, 8);
            g2.setColor(new Color(0x1F2536));
            g2.drawRoundRect(0, 0, w - 1, h - 1, 8, 8);
            setForeground(Theme.MUTED_DARK);
        } else {
            Color fill;
            Color border;
            if (base == Theme.CELL) {
                fill = pressed ? new Color(0x161C2C) : (hover ? Theme.CELL_HOVER : Theme.CELL);
                border = hover ? Theme.LINE_FOCUS : Theme.LINE;
                setForeground(hover ? Color.WHITE : Theme.TEXT_SUB);
            } else if (base == Theme.ACCENT) {
                fill = pressed ? Theme.ACCENT_DARK : (hover ? new Color(0x7376F8) : Theme.ACCENT);
                border = hover ? Theme.ACCENT_BRIGHT : Theme.ACCENT_DARK;
                setForeground(Color.WHITE);
            } else {
                fill = pressed ? base.darker() : (hover ? base.brighter() : base);
                border = hover ? fill.brighter() : fill.darker();
                setForeground(Theme.TEXT);
            }

            g2.setColor(fill);
            g2.fillRoundRect(0, 0, w, h, 8, 8);

            g2.setColor(border);
            g2.setStroke(new BasicStroke(hover ? 1.4f : 1f));
            g2.drawRoundRect(0, 0, w - 1, h - 1, 8, 8);
        }

        g2.dispose();
        super.paintComponent(g);
    }
}



