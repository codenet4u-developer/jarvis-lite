import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator extends CommandHandler {
    private static final Pattern EXPRESSION = Pattern.compile(
            "^\\s*(-?(?:\\d+(?:\\.\\d*)?|\\.\\d+))\\s*([+*/-])\\s*"
                    + "(-?(?:\\d+(?:\\.\\d*)?|\\.\\d+))\\s*$");
    private static final Pattern CALCULATION_INTENT = Pattern.compile(
            "^\\s*(?:calculate\\s+)?[-+]?\\d.*$",
            Pattern.CASE_INSENSITIVE);

    public Calculator() {
        super("Calculator", "Performs basic arithmetic.");
    }

    @Override
    public boolean canHandle(String input) {
        return input != null && CALCULATION_INTENT.matcher(input).matches();
    }

    @Override
    public String execute(String input) {
        String expression = input.replaceFirst("(?i)^\\s*calculate\\s+", "");
        Matcher matcher = EXPRESSION.matcher(expression);
        if (!matcher.matches()) {
            return "I couldn't read that calculation. Use a format like 5 + 3.";
        }

        try {
            double first = Double.parseDouble(matcher.group(1));
            double second = Double.parseDouble(matcher.group(3));
            double result;
            switch (matcher.group(2)) {
                case "+" -> result = first + second;
                case "-" -> result = first - second;
                case "*" -> result = first * second;
                case "/" -> {
                    if (second == 0.0) {
                        return "Division by zero is not allowed.";
                    }
                    result = first / second;
                }
                default -> throw new IllegalStateException("Unexpected operator.");
            }

            if (!Double.isFinite(result)) {
                return "That calculation is outside the supported number range.";
            }
            return expression.trim() + " = "
                    + BigDecimal.valueOf(result).stripTrailingZeros().toPlainString();
        } catch (NumberFormatException exception) {
            return "That calculation contains a number I can't process.";
        }
    }
}
