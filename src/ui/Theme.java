package ui;

import java.awt.*;
import javax.swing.*;

/** Modern, minimal, dynamic dark design system tokens and component helpers. */
public final class Theme {
    // Obsidian & Slate Deep Space Palette
    public static final Color BG = new Color(0x0A0D14);         // Deep space obsidian background
    public static final Color PANEL = new Color(0x111622);      // Card and container background
    public static final Color PANEL_ALT = new Color(0x161C2C);  // Alternating card background
    public static final Color PANEL_HOVER = new Color(0x1C2438);// Hover state for cards
    public static final Color CELL = new Color(0x1E273A);       // Interactive element & button base
    public static final Color CELL_HOVER = new Color(0x2B3750); // Button hover
    public static final Color LINE = new Color(0x242D42);       // Hairline card borders
    public static final Color LINE_MUTED = new Color(0x1B2232); // Subtle hairline
    public static final Color LINE_FOCUS = new Color(0x6366F1); // Glowing border on active

    // Modern Accents
    public static final Color ACCENT = new Color(0x6366F1);     // Modern Indigo
    public static final Color ACCENT_BRIGHT = new Color(0x818CF8); // Indigo Bright
    public static final Color ACCENT_DARK = new Color(0x4F46E5);   // Indigo Dark
    public static final Color ACCENT2 = new Color(0x06B6D4);    // Cyan
    public static final Color ACCENT2_BRIGHT = new Color(0x38BDF8);
    public static final Color SUCCESS = new Color(0x10B981);    // Emerald
    public static final Color SUCCESS_BRIGHT = new Color(0x34D399);
    public static final Color WARNING = new Color(0xF59E0B);    // Amber
    public static final Color WARNING_BRIGHT = new Color(0xFBBF24);
    public static final Color ERROR = new Color(0xF43F5E);      // Rose Red
    public static final Color ERROR_BRIGHT = new Color(0xFB7185);

    // High Readability Text Hierarchy
    public static final Color TEXT = new Color(0xF8FAFC);       // Pure readable white
    public static final Color TEXT_SUB = new Color(0xCBD5E1);   // Light slate
    public static final Color MUTED = new Color(0x8492A6);      // Neutral slate gray
    public static final Color MUTED_DARK = new Color(0x566378); // Dim slate

    // Dynamic Fonts: prefer Segoe UI on Windows for native crispness
    private static Font getFont(String name, int style, int size) {
        Font f = new Font(name, style, size);
        return f.getFamily().equalsIgnoreCase(name) ? f : new Font(Font.SANS_SERIF, style, size);
    }

    public static final Font HERO = getFont("Segoe UI", Font.BOLD, 28);
    public static final Font TITLE = getFont("Segoe UI", Font.BOLD, 19);
    public static final Font SUBTITLE = getFont("Segoe UI", Font.BOLD, 14);
    public static final Font BODY = getFont("Segoe UI", Font.PLAIN, 13);
    public static final Font BOLD = getFont("Segoe UI", Font.BOLD, 13);
    public static final Font BADGE = getFont("Segoe UI", Font.BOLD, 11);
    public static final Font MONO = new Font("Consolas", Font.BOLD, 14);
    public static final Font MONO_SM = new Font("Consolas", Font.PLAIN, 12);
    public static final Font MONO_XS = new Font("Consolas", Font.PLAIN, 11);

    private Theme() {}

    public static JLabel label(String text, Font font, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(font);
        l.setForeground(color);
        return l;
    }

    public static JTextField field(int columns) {
        JTextField f = new JTextField(columns);
        f.setFont(MONO);
        f.setBackground(PANEL_ALT);
        f.setForeground(TEXT);
        f.setCaretColor(ACCENT2_BRIGHT);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LINE, 1),
                BorderFactory.createEmptyBorder(7, 12, 7, 12)));
        return f;
    }

    /** Transparent horizontal row, centered. */
    public static JPanel row(Component... items) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));
        p.setOpaque(false);
        for (Component c : items) p.add(c);
        return p;
    }

    /** Transparent horizontal row with specified alignment and gaps. */
    public static JPanel row(int align, int hgap, int vgap, Component... items) {
        JPanel p = new JPanel(new FlowLayout(align, hgap, vgap));
        p.setOpaque(false);
        for (Component c : items) p.add(c);
        return p;
    }

    /** Transparent vertical column, centered. */
    public static JPanel column(int gap, Component... items) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.insets = new Insets(gap / 2, 0, gap / 2, 0);
        for (Component c : items) p.add(c, g);
        return p;
    }

    /** Modern rounded card container with hairline border. */
    public static JPanel card(Component comp, int pad) {
        JPanel p = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(PANEL);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(LINE);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        p.setOpaque(false);
        p.setBorder(BorderFactory.createEmptyBorder(pad, pad, pad, pad));
        p.add(comp, BorderLayout.CENTER);
        return p;
    }

    /** Minimal badge pill. */
    public static JLabel badge(String text, Color bg, Color fg, Color border) {
        JLabel l = new JLabel(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                if (border != null) {
                    g2.setColor(border);
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        l.setFont(BADGE);
        l.setForeground(fg);
        l.setOpaque(false);
        l.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        return l;
    }

    public static JScrollPane scroll(Component view) {
        JScrollPane s = new JScrollPane(view);
        s.setBorder(BorderFactory.createLineBorder(LINE, 1));
        s.getViewport().setBackground(PANEL);
        s.getVerticalScrollBar().setUnitIncrement(16);
        return s;
    }
}

