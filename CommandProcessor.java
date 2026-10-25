import java.util.List;

public class CommandProcessor {
    private final List<CommandHandler> handlers;

    public CommandProcessor() {
        handlers = List.of(
                new Calculator(),
                new AppLauncher(),
                new SearchHandler());
    }

    public String process(String input) {
        if (input == null || input.isBlank()) {
            return "Please enter a command.";
        }

        for (CommandHandler handler : handlers) {
            if (handler.canHandle(input)) {
                return handler.execute(input);
            }
        }

        return "I don't recognize that command. Try a calculation, "
                + "'search cats', 'open YouTube', or 'launch Notepad'.";
    }
}
