package ui;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.util.List;
import java.util.function.Supplier;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

/** Main window: a menu card plus one freshly built exercise page at a time. */
public class AppFrame extends JFrame {
    private final CardLayout cards = new CardLayout();
    private final JPanel deck = new JPanel(cards);
    private Page current;

    public AppFrame() {
        super("DSA Finals: Stacks & Queues Lab");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        deck.setBackground(Theme.BG);

        deck.add(new MainMenu(List.of(
                new MainMenu.Item("Exercise 1", "Stack Operations", "LIFO Array - Push, Pop, Peek, IsEmpty, IsFull",
                        () -> open("Exercise 1 - Implementation of Stack Operations", Ex1Panel::new)),
                new MainMenu.Item("Exercise 2", "Postfix Evaluation", "Evaluate Postfix Mathematical Expressions Using Stack",
                        () -> open("Exercise 2 - Evaluate Postfix Expression", Ex2Panel::new)),
                new MainMenu.Item("Exercise 3", "Decimal to Binary", "Division & Popping Phases using Stack",
                        () -> open("Exercise 3", Ex3Panel::new)),
                new MainMenu.Item("Group Exercise", "Stack & Circular Queue", "Shared Array - Modulo Wrapping - Exit Compliance",
                        () -> open("Group Exercise - Array Stack & Circular Queue", () -> new GroupPanel(this::home)))),
                () -> System.exit(0)), "menu");

        add(deck);
        setMinimumSize(new Dimension(960, 660));
        setSize(1140, 740);
        setLocationRelativeTo(null);
    }

    private void open(String title, Supplier<JComponent> body) {
        if (current != null) deck.remove(current);
        current = new Page(title, body.get(), this::home);
        deck.add(current, "page");
        cards.show(deck, "page");
    }

    private void home() {
        cards.show(deck, "menu");
    }
}
