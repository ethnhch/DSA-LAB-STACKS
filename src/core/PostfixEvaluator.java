package core;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Evaluates a postfix (RPN) mathematical expression using a stack.
 * Supports multi-digit operands (e.g. 10 is recognized as one number 10),
 * operators (+, -, *, /, $, ^), comma and space delimiters,
 * and step-by-step trace generation.
 */
public final class PostfixEvaluator {
    public record Step(int stepNumber, String symbol, String type, String action, String stack, Object[] cells) {}

    public record Result(double value, List<Step> steps) {}

    private static final String OPERATORS = "+-*/$^";   // $ and ^ = exponent

    private PostfixEvaluator() {}

    /** Tokenizes input supporting multi-digit numbers, operators, commas, and spaces. */
    public static List<String> tokenize(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            throw new IllegalArgumentException("Expression is empty");
        }
        List<String> tokens = new ArrayList<>();
        // Matches multi-digit numbers (\d+(\.\d+)?), operators ([+\-*/$^]), or other non-space/comma strings
        Matcher m = Pattern.compile("\\d+(?:\\.\\d+)?|[+\\-*/$^]|[^\\s,]+").matcher(expression);
        while (m.find()) {
            tokens.add(m.group());
        }
        if (tokens.isEmpty()) {
            throw new IllegalArgumentException("Expression has no valid tokens");
        }
        return tokens;
    }

    public static Result evaluate(String expression) {
        List<String> tokens = tokenize(expression);

        ArrayStack<Double> stack = new ArrayStack<>(tokens.size() + 4);
        List<Step> steps = new ArrayList<>();
        int stepCount = 0;

        for (String token : tokens) {
            stepCount++;
            String action;
            String type;

            if (isOperator(token)) {
                char op = token.charAt(0);
                if (stack.size() < 2) {
                    throw new IllegalArgumentException("Malformed expression: operator '" + op + "' requires two operands");
                }
                double b = stack.pop(); // right operand
                double a = stack.pop(); // left operand
                double r = apply(op, a, b);

                if (Double.isNaN(r) || Double.isInfinite(r)) {
                    throw new IllegalArgumentException("Math error: result of " + format(a) + " " + op + " " + format(b) + " is invalid or overflows");
                }

                stack.push(r);
                type = (op == '$' || op == '^') ? "Operator (Exponent " + op + ")" : "Operator (" + op + ")";
                action = "Pop " + format(b) + " and " + format(a) + "; calculate " + format(a) + " " + op + " " + format(b) + " = " + format(r) + "; push " + format(r);
            } else if (isNumber(token)) {
                double val = Double.parseDouble(token);
                stack.push(val);
                type = "Operand (" + token + ")";
                action = "Push " + format(val) + " onto stack";
            } else {
                throw new IllegalArgumentException("Invalid symbol: '" + token + "'");
            }

            Object[] cells = stack.toList().stream().map(PostfixEvaluator::format).toArray(Object[]::new);
            String stackStr = stack.toList().stream().map(PostfixEvaluator::format).collect(Collectors.joining(", "));
            steps.add(new Step(stepCount, token, type, action, stackStr.isEmpty() ? "Empty" : stackStr, cells));
        }

        if (stack.size() != 1) {
            throw new IllegalArgumentException("Malformed expression: " + stack.size() + " operands remaining on stack (too many operands)");
        }

        return new Result(stack.peek(), steps);
    }

    private static boolean isOperator(String token) {
        return token.length() == 1 && OPERATORS.indexOf(token.charAt(0)) >= 0;
    }

    private static boolean isNumber(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String format(double v) {
        if (Double.isNaN(v)) return "NaN";
        if (Double.isInfinite(v)) return v > 0 ? "Infinity" : "-Infinity";
        return v == Math.rint(v) && Math.abs(v) < 1e15 ? String.valueOf((long) v) : String.valueOf(v);
    }

    private static double apply(char op, double a, double b) {
        return switch (op) {
            case '+' -> a + b;
            case '-' -> a - b;
            case '*' -> a * b;
            case '/' -> {
                if (b == 0) throw new IllegalArgumentException("Division by zero");
                yield a / b;
            }
            case '$', '^' -> Math.pow(a, b);
            default -> throw new IllegalArgumentException("Unsupported operator: '" + op + "'");
        };
    }
}

