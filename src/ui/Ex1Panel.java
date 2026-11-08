package ui;

import java.awt.CardLayout;
import javax.swing.JPanel;

/**
 * Exercise 1: Implementation of Stack Operations.
 * Lab Sheet requirements:
 * Fields: _stackArray, _top, _maxSize.
 * Methods: Push, Pop, Peek, IsEmpty, IsFull, Display.
 */
public class Ex1Panel extends JPanel {
    public Ex1Panel() {
        super(new CardLayout());
        setOpaque(false);
        CardLayout cards = (CardLayout) getLayout();
        add(new SizePanel("Enter Stack Size (_maxSize)", size -> {
            add(new StackPanel(size, () -> cards.show(this, "size")), "stack");
            cards.show(this, "stack");
        }), "size");
    }
}

