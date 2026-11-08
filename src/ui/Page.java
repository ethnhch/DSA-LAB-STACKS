package ui;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

/** Standard exercise page: back button + title in header, exercise body below. */
public class Page extends JPanel {
    public Page(String title, JComponent body, Runnable onBack) {
        super(new BorderLayout(0, 14));
        setBackground(Theme.BG);
        setBorder(BorderFactory.createEmptyBorder(16, 24, 20, 24));

        JPanel header = new JPanel(new BorderLayout(16, 0));
        header.setOpaque(false);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.LINE),
                BorderFactory.createEmptyBorder(0, 0, 12, 0)
        ));

        GameButton btnBack = new GameButton("< Back to Menu", onBack, Theme.CELL);
        btnBack.setFont(Theme.BOLD.deriveFont(12.5f));

        header.add(btnBack, BorderLayout.WEST);
        header.add(Theme.label(title, Theme.TITLE, Theme.TEXT), BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);
        add(body, BorderLayout.CENTER);
    }
}

