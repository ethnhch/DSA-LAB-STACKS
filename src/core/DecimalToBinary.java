package core;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Exercise 3: Multi-Base Conversion Pipeline using Stacks.
 * 1. Decimal to Binary: Division-by-2 into stack, then pop to reverse.
 * 2. Binary to Octal: Load bits into stack (LSB at top), group 3 bits, push octal digits, pop.
 * 3. Octal to Hexadecimal: Expand octal digits to 3-bit binary, load into stack, group 4 bits (nibbles), push hex digits, pop.
 */
public final class DecimalToBinary {
    public record Step(
            int stepNumber,
            String logicPhase,
            String operation,
            String extractedValue,
            String progressiveOutput,
            String stackString,
            Object[] cells
    ) {}

    public record Result(
            int decimal,
            String binary,
            String octal,
            String hex,
            List<Step> steps
    ) {}

    private DecimalToBinary() {}

    public static Result convert(int number) {
        if (number < 0) {
            throw new IllegalArgumentException("Enter a non-negative whole number (0 or greater)");
        }

        List<Step> steps = new ArrayList<>();
        int stepNum = 0;

        // ==========================================
        // LOGIC 1: Decimal to Binary (Stack)
        // ==========================================
        StringBuilder binary = new StringBuilder();
        if (number == 0) {
            ArrayStack<Integer> binStack = new ArrayStack<>(10);
            stepNum++;
            binStack.push(0);
            steps.add(new Step(
                    stepNum,
                    "1. Dec -> Bin (Division)",
                    "0 is base case -> Push 0 onto Stack",
                    "0",
                    "-",
                    "0",
                    binStack.toList().toArray()
            ));

            stepNum++;
            int bit = binStack.pop();
            binary.append(bit);
            steps.add(new Step(
                    stepNum,
                    "1. Dec -> Bin (Pop)",
                    "Pop 0 from Stack -> Binary is \"0\"",
                    String.valueOf(bit),
                    "Bin: 0",
                    "Empty",
                    binStack.toList().toArray()
            ));
        } else {
            ArrayStack<Integer> binStack = new ArrayStack<>(64);
            int temp = number;

            // Phase 1A: Repeated division by 2
            while (temp > 0) {
                stepNum++;
                int rem = temp % 2;
                int quot = temp / 2;
                binStack.push(rem);
                String stackStr = binStack.toList().stream().map(String::valueOf).collect(Collectors.joining(" "));
                steps.add(new Step(
                        stepNum,
                        "1. Dec -> Bin (Division)",
                        temp + " / 2 = " + quot + " (rem " + rem + ") -> Push " + rem + " to Stack",
                        "rem " + rem,
                        "-",
                        stackStr,
                        binStack.toList().toArray()
                ));
                temp = quot;
            }

            // Phase 1B: Pop to construct binary string (MSB first)
            while (!binStack.isEmpty()) {
                stepNum++;
                int bit = binStack.pop();
                binary.append(bit);
                String stackStr = binStack.isEmpty() ? "Empty" : binStack.toList().stream().map(String::valueOf).collect(Collectors.joining(" "));
                steps.add(new Step(
                        stepNum,
                        "1. Dec -> Bin (Pop)",
                        "Pop bit " + bit + " from Stack -> Binary is now \"" + binary + "\"",
                        "bit " + bit,
                        "Bin: " + binary,
                        stackStr,
                        binStack.toList().toArray()
                ));
            }
        }
        String binaryStr = binary.toString();

        // ==========================================
        // LOGIC 2: Binary to Octal (Stack Grouping)
        // ==========================================
        StringBuilder octal = new StringBuilder();
        if (binaryStr.equals("0")) {
            ArrayStack<Integer> octalStack = new ArrayStack<>(10);
            stepNum++;
            octalStack.push(0);
            steps.add(new Step(
                    stepNum,
                    "2. Bin -> Oct (3-Bit Group)",
                    "Binary \"0\" -> 3-bit group [000] = 0 -> Push 0 to Octal Stack",
                    "0",
                    "Bin: 0",
                    "0",
                    octalStack.toList().toArray()
            ));

            stepNum++;
            int digit = octalStack.pop();
            octal.append(digit);
            steps.add(new Step(
                    stepNum,
                    "2. Bin -> Oct (Pop)",
                    "Pop 0 from Octal Stack -> Octal is \"0\"",
                    String.valueOf(digit),
                    "Oct: 0",
                    "Empty",
                    octalStack.toList().toArray()
            ));
        } else {
            ArrayStack<Integer> bitStack = new ArrayStack<>(binaryStr.length() + 8);
            for (char c : binaryStr.toCharArray()) {
                bitStack.push(c - '0');
            }

            stepNum++;
            String initBitStr = bitStack.toList().stream().map(String::valueOf).collect(Collectors.joining(" "));
            steps.add(new Step(
                    stepNum,
                    "2. Bin -> Oct (Bit Stack Init)",
                    "Push binary bits \"" + binaryStr + "\" into Stack (top is LSB for 3-bit grouping)",
                    binaryStr.length() + " bits",
                    "Bin: " + binaryStr,
                    initBitStr,
                    bitStack.toList().toArray()
            ));

            ArrayStack<Integer> octalStack = new ArrayStack<>(32);
            int groupIdx = 1;
            while (!bitStack.isEmpty()) {
                stepNum++;
                int b0 = bitStack.pop();
                int b1 = bitStack.isEmpty() ? 0 : bitStack.pop();
                int b2 = bitStack.isEmpty() ? 0 : bitStack.pop();
                int octalDigit = b2 * 4 + b1 * 2 + b0 * 1;
                octalStack.push(octalDigit);

                String stackStr = octalStack.toList().stream().map(String::valueOf).collect(Collectors.joining(" "));
                steps.add(new Step(
                        stepNum,
                        "2. Bin -> Oct (Group 3 Bits)",
                        "Group " + groupIdx + " [" + b2 + b1 + b0 + "] -> (" + b2 + "*4 + " + b1 + "*2 + " + b0 + "*1) = " + octalDigit + " -> Push " + octalDigit + " to Octal Stack",
                        "octal " + octalDigit,
                        "Bin: " + binaryStr,
                        stackStr,
                        octalStack.toList().toArray()
                ));
                groupIdx++;
            }

            // Pop octal stack to reverse into MSB order
            while (!octalStack.isEmpty()) {
                stepNum++;
                int digit = octalStack.pop();
                octal.append(digit);
                String stackStr = octalStack.isEmpty() ? "Empty" : octalStack.toList().stream().map(String::valueOf).collect(Collectors.joining(" "));
                steps.add(new Step(
                        stepNum,
                        "2. Bin -> Oct (Pop)",
                        "Pop " + digit + " from Octal Stack -> Octal is now \"" + octal + "\"",
                        "digit " + digit,
                        "Oct: " + octal,
                        stackStr,
                        octalStack.toList().toArray()
                ));
            }
        }
        String octalStr = octal.toString();

        // ==========================================
        // LOGIC 3: Octal to Hexadecimal (Stack Grouping)
        // ==========================================
        StringBuilder hex = new StringBuilder();
        if (octalStr.equals("0")) {
            ArrayStack<String> hexStack = new ArrayStack<>(10);
            stepNum++;
            hexStack.push("0");
            steps.add(new Step(
                    stepNum,
                    "3. Oct -> Hex (Group 4 Bits)",
                    "Octal \"0\" -> 4-bit nibble [0000] = 0 -> Push '0' to Hex Stack",
                    "0",
                    "Oct: 0",
                    "0",
                    hexStack.toList().toArray()
            ));

            stepNum++;
            String h = hexStack.pop();
            hex.append(h);
            steps.add(new Step(
                    stepNum,
                    "3. Oct -> Hex (Pop)",
                    "Pop '0' from Hex Stack -> Hexadecimal is \"0\"",
                    h,
                    "Hex: 0",
                    "Empty",
                    hexStack.toList().toArray()
            ));
        } else {
            // Step 3A: Expand each octal digit into 3 binary bits
            StringBuilder expandedBits = new StringBuilder();
            for (char c : octalStr.toCharArray()) {
                int d = c - '0';
                String b3 = String.format("%3s", Integer.toBinaryString(d)).replace(' ', '0');
                expandedBits.append(b3);
            }
            // Strip leading zero padding while keeping at least 1 bit
            String cleanBits = expandedBits.toString().replaceFirst("^0+(?!$)", "");

            ArrayStack<Integer> nibbleBitStack = new ArrayStack<>(cleanBits.length() + 8);
            for (char c : cleanBits.toCharArray()) {
                nibbleBitStack.push(c - '0');
            }

            stepNum++;
            String initNibbleStr = nibbleBitStack.toList().stream().map(String::valueOf).collect(Collectors.joining(" "));
            steps.add(new Step(
                    stepNum,
                    "3. Oct -> Hex (3-Bit Expand)",
                    "Expand octal \"" + octalStr + "\" digits to 3-bit groups -> \"" + cleanBits + "\" (Stack top is LSB)",
                    cleanBits.length() + " bits",
                    "Oct: " + octalStr,
                    initNibbleStr,
                    nibbleBitStack.toList().toArray()
            ));

            ArrayStack<Character> hexStack = new ArrayStack<>(32);
            char[] hexChars = "0123456789ABCDEF".toCharArray();
            int nibbleIdx = 1;

            while (!nibbleBitStack.isEmpty()) {
                stepNum++;
                int b0 = nibbleBitStack.pop();
                int b1 = nibbleBitStack.isEmpty() ? 0 : nibbleBitStack.pop();
                int b2 = nibbleBitStack.isEmpty() ? 0 : nibbleBitStack.pop();
                int b3 = nibbleBitStack.isEmpty() ? 0 : nibbleBitStack.pop();
                int hexVal = b3 * 8 + b2 * 4 + b1 * 2 + b0 * 1;
                char h = hexChars[hexVal];
                hexStack.push(h);

                String stackStr = hexStack.toList().stream().map(String::valueOf).collect(Collectors.joining(" "));
                steps.add(new Step(
                        stepNum,
                        "3. Oct -> Hex (Group 4 Bits)",
                        "Nibble " + nibbleIdx + " [" + b3 + b2 + b1 + b0 + "] -> (" + b3 + "*8 + " + b2 + "*4 + " + b1 + "*2 + " + b0 + "*1) = " + hexVal + " ('" + h + "') -> Push '" + h + "' to Hex Stack",
                        "hex " + h,
                        "Oct: " + octalStr,
                        stackStr,
                        hexStack.toList().toArray()
                ));
                nibbleIdx++;
            }

            // Pop hex stack to reverse into MSB order
            while (!hexStack.isEmpty()) {
                stepNum++;
                char h = hexStack.pop();
                hex.append(h);
                String stackStr = hexStack.isEmpty() ? "Empty" : hexStack.toList().stream().map(String::valueOf).collect(Collectors.joining(" "));
                steps.add(new Step(
                        stepNum,
                        "3. Oct -> Hex (Pop)",
                        "Pop '" + h + "' from Hex Stack -> Hexadecimal is now \"" + hex + "\"",
                        "digit " + h,
                        "Hex: " + hex,
                        stackStr,
                        hexStack.toList().toArray()
                ));
            }
        }
        String hexStr = hex.toString();

        return new Result(number, binaryStr, octalStr, hexStr, steps);
    }
}
