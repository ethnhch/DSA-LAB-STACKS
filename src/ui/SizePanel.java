package ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.function.IntConsumer;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/** Modern card asking for array capacity with quick size presets. */
public class SizePanel extends JPanel {
    private static final int MAX = 30;

    public SizePanel(String prompt, IntConsumer onCreate) {
        super(new java.awt.GridBagLayout());
        setOpaque(false);

        JTextField size = Theme.field(6);
        size.setHorizontalAlignment(SwingConstants.CENTER);
        size.setText("5");
        size.setPreferredSize(new Dimension(80, 36));

        Status status = new Status();

        Runnable create = () -> {
            try {
                int n = Integer.parseInt(size.getText().trim());
                if (n < 1 || n > MAX) throw new NumberFormatException();
                onCreate.accept(n);
            } catch (NumberFormatException e) {
                status.error("Enter a valid capacity between 1 and " + MAX);
            }
        };
        size.addActionListener(e -> create.run());

        // Quick preset chips
        JPanel presets = Theme.row(FlowLayout.CENTER, 6, 2);
        presets.add(Theme.label("Quick size:", Theme.BODY, Theme.MUTED));
        for (int p : new int[]{3, 5, 8, 10}) {
            GameButton chip = new GameButton(String.valueOf(p), () -> {
                size.setText(String.valueOf(p));
                create.run();
            }, Theme.CELL);
            chip.setFont(Theme.MONO_XS);
            chip.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 12, 4, 12));
            presets.add(chip);
        }

        JPanel formCard = Theme.column(14,
                Theme.label(prompt, Theme.TITLE, Theme.TEXT),
                Theme.label("Allocate a fixed contiguous array capacity (1 to " + MAX + ")", Theme.BODY, Theme.MUTED),
                Theme.row(size, new GameButton("Allocate Array ->", create, Theme.ACCENT)),
                presets,
                status
        );

        add(Theme.card(formCard, 28));
    }
}

