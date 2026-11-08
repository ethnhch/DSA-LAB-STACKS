package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.function.Supplier;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Shared layout for the stack and queue screens:
 * value box, operation buttons, dynamic HUD capacity status, live array view, feedback.
 */
public abstract class StructurePanel extends JPanel {
    private final JTextField value = Theme.field(6);
    private final JPanel bar = Theme.row(FlowLayout.CENTER, 8, 4);
    private final ArrayView view = new ArrayView();
    private final Status status = new Status();
    private final JLabel lblCount = Theme.badge("Items: 0", new Color(0x161C2C), Theme.ACCENT2_BRIGHT, Theme.LINE);

    protected StructurePanel(Runnable onChangeSize) {
        super(new BorderLayout(0, 10));
        setOpaque(false);

        value.setPreferredSize(new Dimension(80, 36));
        bar.add(Theme.label("Value:", Theme.BOLD, Theme.TEXT));
        bar.add(value);

        if (onChangeSize != null) {
            bar.add(new GameButton("Change Size", onChangeSize, Theme.CELL));
        }

        JPanel topCard = Theme.card(Theme.column(8, bar, Theme.row(lblCount)), 12);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 6));
        centerPanel.setOpaque(false);
        centerPanel.add(Theme.label("LIVE ARRAY MEMORY SLOTS", Theme.BADGE, Theme.MUTED), BorderLayout.NORTH);
        centerPanel.add(Theme.scroll(view), BorderLayout.CENTER);

        add(topCard, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);
    }

    /** Adds an operation button with default accent. */
    protected GameButton op(String label, Supplier<String> action) {
        return op(label, action, Theme.CELL);
    }

    /** Adds an operation button with custom base color. */
    protected GameButton op(String label, Supplier<String> action, Color base) {
        GameButton button = new GameButton(label, () -> {
            try {
                status.ok(action.get());
            } catch (IllegalStateException | IllegalArgumentException e) {
                status.error(e.getMessage());
            }
            refresh();
        }, base);
        bar.add(button);
        return button;
    }

    /** Integer typed in the Value box. */
    protected int input() {
        try {
            return Integer.parseInt(value.getText().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Type a whole number in the Value box first");
        }
    }

    protected void triggerOnEnter(GameButton button) {
        value.addActionListener(e -> button.doClick());
    }

    protected void setCountText(String text) {
        lblCount.setText(text);
    }

    protected void refresh() {
        Object[] cells = cells();
        String[] tags = new String[cells.length];
        mark(tags);
        view.show(cells, tags);
        updateCount();
        value.requestFocusInWindow();
        value.selectAll();
    }

    protected abstract Object[] cells();

    /** Fill tags[i] for every cell that needs a label such as TOP, FRONT or REAR. */
    protected abstract void mark(String[] tags);

    protected abstract void updateCount();
}


