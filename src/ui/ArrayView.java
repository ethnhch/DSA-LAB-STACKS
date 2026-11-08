package ui;

import java.awt.*;
import javax.swing.JPanel;

/** Draws an array as a row of sleek indexed cells with interactive tags (TOP, FRONT, REAR). */
public class ArrayView extends JPanel {
    private static final int W = 64, GAP = 10, TAG_H = 28, INDEX_H = 22;
    private Object[] cells = new Object[0];
    private String[] tags = new String[0];

    public ArrayView() {
        setOpaque(false);
    }

    public void show(Object[] cells, String[] tags) {
        this.cells = cells != null ? cells : new Object[0];
        this.tags = tags != null ? tags : new String[this.cells.length];
        int w = this.cells.length == 0 ? 360 : 2 * GAP + rowWidth();
        setPreferredSize(new Dimension(w, TAG_H + W + INDEX_H + 12));
        revalidate();
        repaint();
    }

    private int rowWidth() {
        return cells.length * (W + GAP) - GAP;
    }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        if (cells == null || cells.length == 0) {
            g2.setColor(Theme.MUTED_DARK);
            g2.setFont(Theme.BODY);
            String emptyMsg = "- Memory Buffer is Empty (0 elements allocated) -";
            FontMetrics fm = g2.getFontMetrics();
            int sw = fm.stringWidth(emptyMsg);
            g2.drawString(emptyMsg, (getWidth() - sw) / 2, getHeight() / 2 + 5);
            g2.dispose();
            return;
        }

        int x0 = Math.max(GAP, (getWidth() - rowWidth()) / 2);
        int y0 = Math.max(0, (getHeight() - TAG_H - W - INDEX_H) / 2) + TAG_H;

        for (int i = 0; i < cells.length; i++) {
            int x = x0 + i * (W + GAP);
            boolean filled = cells[i] != null;
            String tag = (tags != null && i < tags.length) ? tags[i] : null;

            // Draw Cell Container Card
            if (filled) {
                // Active / Filled memory slot
                g2.setColor(new Color(0x1B1F33));
                g2.fillRoundRect(x, y0, W, W, 10, 10);
                g2.setColor(tag != null ? Theme.ACCENT : new Color(0x32395C));
                g2.setStroke(new BasicStroke(tag != null ? 1.8f : 1f));
                g2.drawRoundRect(x, y0, W - 1, W - 1, 10, 10);
            } else {
                // Empty memory slot
                g2.setColor(new Color(0x0E121B));
                g2.fillRoundRect(x, y0, W, W, 10, 10);
                g2.setColor(new Color(0x1E2536));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(x, y0, W - 1, W - 1, 10, 10);
            }

            // Draw Pointer Tag Badge above cell if present
            if (tag != null) {
                Color tagBg;
                Color tagBorder;
                Color tagText;

                if (tag.contains("FRONT") && tag.contains("REAR")) {
                    tagBg = new Color(0x33220E);
                    tagBorder = new Color(0xD97706);
                    tagText = new Color(0xFDE68A);
                } else if (tag.contains("FRONT")) {
                    tagBg = new Color(0x0C2E42);
                    tagBorder = new Color(0x0284C7);
                    tagText = new Color(0xBAE6FD);
                } else if (tag.contains("REAR")) {
                    tagBg = new Color(0x0E3224);
                    tagBorder = new Color(0x059669);
                    tagText = new Color(0xA7F3D0);
                } else {
                    tagBg = new Color(0x2A2357);
                    tagBorder = new Color(0x6366F1);
                    tagText = new Color(0xE0E7FF);
                }

                String badgeLabel = "v " + tag;
                g2.setFont(Theme.BADGE);
                FontMetrics tfm = g2.getFontMetrics();
                int pillW = tfm.stringWidth(badgeLabel) + 12;
                int pillH = 20;
                int pillX = x + (W - pillW) / 2;
                int pillY = y0 - pillH - 4;

                g2.setColor(tagBg);
                g2.fillRoundRect(pillX, pillY, pillW, pillH, 8, 8);
                g2.setColor(tagBorder);
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(pillX, pillY, pillW, pillH, 8, 8);

                g2.setColor(tagText);
                g2.drawString(badgeLabel, pillX + 6, pillY + tfm.getAscent() + 2);
            }

            // Draw Value
            if (filled) {
                text(g2, String.valueOf(cells[i]), Theme.MONO.deriveFont(16f), Theme.TEXT, x + W / 2, y0 + W / 2);
            } else {
                text(g2, "-", Theme.MONO_SM, Theme.MUTED_DARK, x + W / 2, y0 + W / 2);
            }

            // Draw Index below cell
            text(g2, "[" + i + "]", Theme.MONO_XS, Theme.MUTED, x + W / 2, y0 + W + INDEX_H / 2 + 2);
        }
        g2.dispose();
    }

    /** Draws s centered on (cx, cy), shrinking the font if it would overflow a cell. */
    private static void text(Graphics2D g2, String s, Font font, Color color, int cx, int cy) {
        g2.setFont(font);
        FontMetrics fm = g2.getFontMetrics();
        int width = fm.stringWidth(s);
        if (width > W - 8) {
            g2.setFont(font.deriveFont(font.getSize2D() * (W - 8) / width));
            fm = g2.getFontMetrics();
            width = fm.stringWidth(s);
        }
        g2.setColor(color);
        g2.drawString(s, cx - width / 2, cy + (fm.getAscent() - fm.getDescent()) / 2);
    }
}



