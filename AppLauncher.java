import java.io.IOException;
import java.util.Locale;
import java.util.Map;

public class AppLauncher extends CommandHandler {
    private static final Map<String, String> APPLICATIONS = Map.of(
            "notepad", "notepad.exe",
            "calculator", "calc.exe");

    public AppLauncher() {
        super("Application launcher", "Launches a small whitelist of Windows apps.");
    }

    @Override
    public boolean canHandle(String input) {
        if (input == null) {
            return false;
        }
        String command = input.trim().toLowerCase(Locale.ROOT);
        return command.startsWith("launch ")
                || command.startsWith("start ")
                || command.startsWith("open notepad")
                || command.startsWith("open calculator");
    }

    @Override
    public String execute(String input) {
        String command = input.trim().toLowerCase(Locale.ROOT);
        String applicationName;
        if (command.startsWith("launch ")) {
            applicationName = command.substring("launch ".length()).trim();
        } else if (command.startsWith("start ")) {
            applicationName = command.substring("start ".length()).trim();
        } else if (command.startsWith("open ")) {
            applicationName = command.substring("open ".length()).trim();
        } else {
            return "Use 'launch Notepad' or 'launch Calculator'.";
        }

        String executable = APPLICATIONS.get(applicationName);
        if (executable == null) {
            return "That application is not on my safe launch list. "
                    + "I can launch Notepad or Calculator.";
        }
        if (!System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("windows")) {
            return "Launching Windows applications is only available on Windows.";
        }

        try {
            new ProcessBuilder(executable).start();
            return "Launching " + capitalize(applicationName) + ".";
        } catch (IOException exception) {
            return capitalize(applicationName)
                    + " is not available. Check that it is installed and accessible.";
        }
    }

    private String capitalize(String value) {
        return value.substring(0, 1).toUpperCase(Locale.ROOT) + value.substring(1);
    }
}
