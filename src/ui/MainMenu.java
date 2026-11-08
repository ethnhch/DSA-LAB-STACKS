package ui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;

/** Modern landing screen with interactive exercise cards. */
public class MainMenu extends JPanel {
    public record Item(String tag, String title, String blurb, Runnable open) {}

    public MainMenu(List<Item> items, Runnable onQuit) {
        super(new GridBagLayout());
        setBackground(Theme.BG);

        JPanel grid = new JPanel(new GridLayout(2, 2, 16, 16));
        grid.setOpaque(false);
        items.forEach(item -> grid.add(createCard(item)));

        JPanel header = Theme.column(8,
                Theme.label("STACKS & QUEUES LAB", Theme.HERO, Theme.TEXT),
                Theme.label("Data Structures and Algorithms - Lesson 12 - Prof. Lester G. Diampoc", Theme.BODY, Theme.MUTED)
        );

        GameButton quitBtn = new GameButton("Quit Application", onQuit, new Color(0x231820));
        quitBtn.setPreferredSize(new Dimension(180, 38));

        add(Theme.column(26, header, grid, quitBtn));
    }

    private static JComponent createCard(Item item) {
        JPanel card = new JPanel(new BorderLayout(12, 8)) {
            private boolean hover = false;
            {
                setBackground(Theme.PANEL);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
                setPreferredSize(new Dimension(330, 120));

                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                    @Override public void mouseExited(MouseEvent e) { hover = false; repaint(); }
                    @Override public void mouseClicked(MouseEvent e) { item.open().run(); }
                });
            }

            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hover ? Theme.PANEL_HOVER : Theme.PANEL);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(hover ? Theme.LINE_FOCUS : Theme.LINE);
                g2.setStroke(new BasicStroke(hover ? 1.6f : 1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };

        // Tag pill
        JLabel lblTag = Theme.badge(item.tag().toUpperCase(), new Color(0x1F243B), Theme.ACCENT_BRIGHT, new Color(0x32395C));

        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
        topRow.add(lblTag, BorderLayout.WEST);

        JLabel lblArrow = Theme.label("->", Theme.BOLD.deriveFont(13f), Theme.MUTED);
        topRow.add(lblArrow, BorderLayout.EAST);

        JLabel lblTitle = Theme.label(item.title(), Theme.TITLE.deriveFont(16f), Theme.TEXT);
        JLabel lblBlurb = Theme.label(item.blurb(), Theme.BODY.deriveFont(12f), Theme.MUTED);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.add(lblTitle);
        content.add(Box.createVerticalStrut(4));
        content.add(lblBlurb);

        card.add(topRow, BorderLayout.NORTH);
        card.add(content, BorderLayout.CENTER);
        return card;
    }
}


