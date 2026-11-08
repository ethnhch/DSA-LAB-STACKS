package ui;

import core.PostfixEvaluator;
import java.util.List;

/**
 * Exercise 2:
 * Evaluate Postfix Expression Using Stack (Primary lab objective from Lab Sheet).
 */
public class Ex2Panel extends TracePanel {
    public Ex2Panel() {
        super(
                "Postfix Expression (use commas or spaces):",
                new String[]{"Step", "Symbol", "Symbol Type", "Operation / Action", "Stack (bottom -> top)"},
                new String[]{"10, 5, +", "10, 2, $", "2, 10, $", "100, 20, /, 3, +", "5, 3, +, 8, 2, -, *", "10, 5, +, 2, $", "5, 0, /"},
                Ex2Panel::evaluate
        );
    }

    private static TracePlan evaluate(String text) {
        PostfixEvaluator.Result r = PostfixEvaluator.evaluate(text);
        List<StepSnapshot> snapshots = r.steps().stream().map(s -> new StepSnapshot(
                s.stepNumber(),
                new String[]{String.valueOf(s.stepNumber()), s.symbol(), s.type(), s.action(), s.stack()},
                s.cells(),
                s.action(),
                "Evaluation"
        )).toList();
        return new TracePlan("Final Evaluated Result = " + PostfixEvaluator.format(r.value()), snapshots);
    }
}


