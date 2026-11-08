package ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * Group Exercise - Basic Operations of Stack & Queue over a Shared Array Size.
 * Lab Sheet Flow:
 * 1. The program should input first the size of an array.
 * 2. Menu options to choose either: STACK, QUEUE, EXIT.
 * 3. Sub-menu options for stack/queue with corresponding methods.
 * 4. View array elements based on the selected method.
 * 5. Program will not close unless the user chooses EXIT from the menu options.
 */
public class GroupPanel extends JPanel {
    private final CardLayout cards = new CardLayout();

    public GroupPanel(Runnable onExit) {
        setLayout(cards);
        setOpaque(false);

        add(new SizePanel("Enter Shared Array Size", size -> {
            add(withHeader("Array Stack (LIFO)", new StackPanel(size)), "STACK");
            add(withHeader("Circular Array Queue (FIFO)", new QueuePanel(size)), "QUEUE");
            cards.show(this, "MENU");
        }), "SIZE");

        JPanel menu = new JPanel(new java.awt.GridBagLayout());
        menu.setOpaque(false);

        GameButton btnStack = choice("1. STACK Operations (LIFO)", "STACK", Theme.ACCENT);
        GameButton btnQueue = choice("2. QUEUE Operations (FIFO)", "QUEUE", new Color(0x0E6D8A));
        GameButton btnResize = new GameButton("Change Array Size", () -> cards.show(this, "SIZE"), Theme.CELL);
        btnResize.setPreferredSize(new java.awt.Dimension(280, 42));
        GameButton btnExit = new GameButton("EXIT", onExit, new java.awt.Color(0x3B1824));
        btnExit.setPreferredSize(new java.awt.Dimension(280, 42));

        JPanel menuCard = Theme.column(12,
                Theme.label("GROUP EXERCISE MENU", Theme.TITLE, Theme.TEXT),
                Theme.label("Choose a data structure sub-menu or EXIT:", Theme.BODY, Theme.MUTED),
                Box.createVerticalStrut(4),
                btnStack,
                btnQueue,
                btnResize,
                btnExit
        );
        menu.add(Theme.card(menuCard, 24));
        add(menu, "MENU");
    }

    private GameButton choice(String label, String cardName, java.awt.Color base) {
        GameButton b = new GameButton(label, () -> cards.show(this, cardName), base);
        b.setPreferredSize(new java.awt.Dimension(280, 44));
        return b;
    }

    private JPanel withHeader(String title, JComponent structure) {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setOpaque(false);

        JPanel topBar = new JPanel(new BorderLayout(12, 0));
        topBar.setOpaque(false);
        topBar.add(new GameButton("< Menu Options", () -> cards.show(this, "MENU"), Theme.CELL), BorderLayout.WEST);
        topBar.add(Theme.label("Sub-Menu: " + title, Theme.BOLD.deriveFont(15f), Theme.ACCENT2_BRIGHT), BorderLayout.CENTER);

        p.add(topBar, BorderLayout.NORTH);
        p.add(structure, BorderLayout.CENTER);
        return p;
    }
}

