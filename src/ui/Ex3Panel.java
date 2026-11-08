package ui;

import core.DecimalToBinary;
import java.util.List;

/**
 * Exercise 3: Base Number Conversions using Stacks.
 * Demonstrates:
 * 1. Decimal to Binary: Division-by-2 into stack, then pop to reverse bits into
 * MSB order.
 * 2. Binary to Octal: Grouping 3 bits from bit stack (LSB at top) and pushing
 * to octal stack.
 * 3. Octal to Hexadecimal: Expanding octal digits to 3-bit binary, grouping 4
 * bits into hex stack.
 */
public class Ex3Panel extends TracePanel {
    public Ex3Panel() {
        super(
                "Decimal Number:",
                new String[] { "Step", "Logic / Conversion Phase", "Operation / Arithmetic", "Extracted / Digit",
                        "Progressive Output", "Stack (bottom -> top)" },
                new String[] { "13", "25", "255", "1024", "7", "0" },
                Ex3Panel::convert);
    }

    private static TracePlan convert(String text) {
        int number;
        try {
            number = Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Enter a whole number from 0 to " + Integer.MAX_VALUE);
        }
        if (number < 0) {
            throw new IllegalArgumentException("Negative numbers are not supported. Enter a positive number or 0.");
        }

        DecimalToBinary.Result r = DecimalToBinary.convert(number);
        List<StepSnapshot> snapshots = r.steps().stream().map(s -> new StepSnapshot(
                s.stepNumber(),
                new String[] {
                        String.valueOf(s.stepNumber()),
                        s.logicPhase(),
                        s.operation(),
                        s.extractedValue(),
                        s.progressiveOutput().isEmpty() ? "-" : s.progressiveOutput(),
                        s.stackString()
                },
                s.cells(),
                s.operation(),
                s.logicPhase())).toList();

        String summary = String.format("Decimal: %d  ➔  Binary: %s  ➔  Octal: %s  ➔  Hexadecimal: %s (0x%s)",
                r.decimal(), r.binary(), r.octal(), r.hex(), r.hex());

        return new TracePlan(summary, snapshots);
    }
}
