package ui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import javax.swing.*;

/**
 * Interactive step-by-step processor.
 * Presets and buttons will NEVER run all steps automatically without user intent.
 * Supports Step, Prev, Run All, Reset, live ArrayView stack visualization at each step,
 * and progressive TraceTable population.
 */
public class TracePanel extends JPanel {
    public record StepSnapshot(
            int stepNumber,
            String[] tableRow,
            Object[] cells,
            String actionMessage,
            String phase
    ) {}

    public record TracePlan(String finalSummary, List<StepSnapshot> steps) {}

    private final JTextField input = Theme.field(24);
    private final Status status = new Status();
    private final ArrayView view = new ArrayView();
    private final JScrollPane viewScroll = Theme.scroll(view);
    private final TraceTable table;
    private final Function<String, TracePlan> solver;

    private final GameButton btnStep;
    private final GameButton btnPrev;
    private final GameButton btnRunAll;
    private final GameButton btnReset;
    private final JLabel lblStepBadge = Theme.badge("Ready", new Color(0x1A2234), Theme.ACCENT_BRIGHT, Theme.LINE);
    private final JLabel lblPhaseBadge = Theme.badge("", new Color(0x132130), Theme.ACCENT2_BRIGHT, new Color(0x1E3A52));

    private TracePlan currentPlan = null;
    private int stepIndex = -1; // -1 = ready/not started, 0 = after step 1, etc.

    public TracePanel(String prompt, String[] columns, String[] samples, Function<String, TracePlan> solver) {
        super(new BorderLayout(0, 10));
        setOpaque(false);
        this.solver = solver;
        this.table = new TraceTable(columns);

        btnStep = new GameButton("Step >", this::stepForward, Theme.ACCENT);
        btnPrev = new GameButton("< Prev", this::stepBackward, Theme.CELL);
        btnRunAll = new GameButton("Run All >>", this::runAllRemaining, new Color(0x3B38A8));
        btnReset = new GameButton("Reset", this::resetStepper, Theme.CELL);

        btnStep.setEnabled(true);
        btnPrev.setEnabled(false);
        btnRunAll.setEnabled(true);
        btnReset.setEnabled(false);

        // Presets: clicking only loads the input into the field and prepares stepper. NEVER AUTO-RUNS!
        JPanel chips = Theme.row(FlowLayout.CENTER, 6, 2);
        chips.add(Theme.label("Try Presets:", Theme.BOLD.deriveFont(11.5f), Theme.MUTED));
        for (String sample : samples) {
            GameButton chip = new GameButton(sample, () -> {
                input.setText(sample);
                loadPlan(false);
            }, Theme.CELL);
            chip.setFont(Theme.MONO_XS);
            chip.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            chips.add(chip);
        }

        input.addActionListener(e -> loadPlan(false));

        // Top input row + presets
        JPanel inputRow = Theme.row(
                Theme.label(prompt, Theme.BOLD, Theme.TEXT),
                input,
                new GameButton("Load Input", () -> loadPlan(false), Theme.CELL)
        );

        // Step Control Toolbar
        JPanel controlBar = Theme.row(
                btnStep,
                btnPrev,
                btnRunAll,
                btnReset,
                Box.createHorizontalStrut(14),
                lblStepBadge,
                Box.createHorizontalStrut(6),
                lblPhaseBadge
        );

        JPanel topHeader = Theme.card(Theme.column(8, inputRow, chips, controlBar), 12);

        // Middle Section: Visual Stack on top, Trace Table below
        viewScroll.setPreferredSize(new Dimension(0, 136));
        view.show(new Object[0], new String[0]);

        JPanel center = new JPanel(new BorderLayout(0, 8));
        center.setOpaque(false);

        JPanel stackCard = new JPanel(new BorderLayout(0, 4));
        stackCard.setOpaque(false);
        stackCard.add(Theme.label("LIVE MEMORY STACK STATE (LIFO)", Theme.BADGE, Theme.MUTED), BorderLayout.NORTH);
        stackCard.add(viewScroll, BorderLayout.CENTER);

        JPanel tableCard = new JPanel(new BorderLayout(0, 4));
        tableCard.setOpaque(false);
        tableCard.add(Theme.label("STEP-BY-STEP TRACE EXECUTION LOG", Theme.BADGE, Theme.MUTED), BorderLayout.NORTH);
        tableCard.add(table, BorderLayout.CENTER);

        center.add(stackCard, BorderLayout.NORTH);
        center.add(tableCard, BorderLayout.CENTER);

        add(topHeader, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);

        status.ok("Ready. Enter an expression or choose a preset, then click \"Step >\" to begin.");
    }


    /** Loads and prepares the execution plan without advancing automatically. */
    private boolean loadPlan(boolean autoAdvance) {
        String raw = input.getText().trim();
        if (raw.isEmpty()) {
            status.error("Please enter an input value first.");
            return false;
        }

        try {
            currentPlan = solver.apply(raw);
            stepIndex = -1;
            table.clear();
            view.show(new Object[0], new String[0]);

            lblStepBadge.setText("Step 0 / " + currentPlan.steps().size());
            lblPhaseBadge.setText(currentPlan.steps().isEmpty() ? "" : "[" + currentPlan.steps().get(0).phase() + "]");
            btnStep.setEnabled(!currentPlan.steps().isEmpty());
            btnPrev.setEnabled(false);
            btnRunAll.setEnabled(!currentPlan.steps().isEmpty());
            btnReset.setEnabled(true);

            status.ok("Loaded: \"" + raw + "\". Click \"Step >\" to advance symbol-by-symbol, or \"Run All >>\" to complete.");
            if (autoAdvance) {
                stepForward();
            }
            return true;
        } catch (IllegalArgumentException | IllegalStateException e) {
            currentPlan = null;
            stepIndex = -1;
            table.clear();
            view.show(new Object[0], new String[0]);
            lblStepBadge.setText("Error");
            lblPhaseBadge.setText("");
            btnStep.setEnabled(false);
            btnPrev.setEnabled(false);
            btnRunAll.setEnabled(false);
            btnReset.setEnabled(false);
            status.error(e.getMessage());
            return false;
        }
    }

    /** Advances one single step. */
    private void stepForward() {
        if (currentPlan == null) {
            if (!loadPlan(true)) return;
            return;
        }

        if (stepIndex + 1 >= currentPlan.steps().size()) {
            status.ok("Already completed. " + currentPlan.finalSummary());
            return;
        }

        stepIndex++;
        StepSnapshot step = currentPlan.steps().get(stepIndex);

        // Add row to table
        table.addRow(step.tableRow());

        // Update visual stack
        showCells(step.cells());

        // Update badges and buttons
        int total = currentPlan.steps().size();
        boolean isLast = (stepIndex == total - 1);

        lblStepBadge.setText(isLast ? "Completed (" + total + "/" + total + ")" : "Step " + (stepIndex + 1) + " / " + total);
        lblPhaseBadge.setText("[" + step.phase() + "]");

        btnPrev.setEnabled(stepIndex > 0);
        btnStep.setEnabled(!isLast);
        btnRunAll.setEnabled(!isLast);
        btnReset.setEnabled(true);

        if (isLast) {
            status.ok("Finished! " + currentPlan.finalSummary());
        } else {
            status.ok("Step " + (stepIndex + 1) + ": " + step.actionMessage());
        }
    }

    /** Steps backward one single step. */
    private void stepBackward() {
        if (currentPlan == null || stepIndex <= 0) return;

        stepIndex--;
        // Rebuild table rows up to stepIndex
        List<String[]> rows = new ArrayList<>();
        for (int i = 0; i <= stepIndex; i++) {
            rows.add(currentPlan.steps().get(i).tableRow());
        }
        table.setRows(rows);

        StepSnapshot step = currentPlan.steps().get(stepIndex);
        showCells(step.cells());

        int total = currentPlan.steps().size();
        lblStepBadge.setText("Step " + (stepIndex + 1) + " / " + total);
        lblPhaseBadge.setText("[" + step.phase() + "]");

        btnPrev.setEnabled(stepIndex > 0);
        btnStep.setEnabled(true);
        btnRunAll.setEnabled(true);
        btnReset.setEnabled(true);

        status.ok("Step " + (stepIndex + 1) + ": " + step.actionMessage());
    }

    /** Completes all remaining steps. */
    private void runAllRemaining() {
        if (currentPlan == null) {
            if (!loadPlan(false)) return;
        }
        if (currentPlan == null || currentPlan.steps().isEmpty()) return;

        List<String[]> allRows = currentPlan.steps().stream().map(StepSnapshot::tableRow).toList();
        table.setRows(allRows);

        stepIndex = currentPlan.steps().size() - 1;
        StepSnapshot lastStep = currentPlan.steps().get(stepIndex);
        showCells(lastStep.cells());

        int total = currentPlan.steps().size();
        lblStepBadge.setText("Completed (" + total + "/" + total + ")");
        lblPhaseBadge.setText("[" + lastStep.phase() + "]");

        btnStep.setEnabled(false);
        btnPrev.setEnabled(total > 1);
        btnRunAll.setEnabled(false);
        btnReset.setEnabled(true);

        status.ok("Finished! " + currentPlan.finalSummary());
    }

    /** Resets the stepper back to step 0. */
    private void resetStepper() {
        stepIndex = -1;
        table.clear();
        view.show(new Object[0], new String[0]);

        if (currentPlan != null) {
            lblStepBadge.setText("Step 0 / " + currentPlan.steps().size() + " (Ready)");
            lblPhaseBadge.setText(currentPlan.steps().isEmpty() ? "" : "[" + currentPlan.steps().get(0).phase() + "]");
            btnStep.setEnabled(!currentPlan.steps().isEmpty());
            btnRunAll.setEnabled(!currentPlan.steps().isEmpty());
        } else {
            lblStepBadge.setText("Ready");
            lblPhaseBadge.setText("");
            btnStep.setEnabled(true);
            btnRunAll.setEnabled(true);
        }
        btnPrev.setEnabled(false);
        status.ok("Reset to initial state. Click \"Step >\" to begin.");
    }

    private void showCells(Object[] cells) {
        if (cells == null || cells.length == 0) {
            view.show(new Object[0], new String[0]);
            return;
        }
        String[] tags = new String[cells.length];
        tags[cells.length - 1] = "TOP";
        view.show(cells, tags);
    }
}

