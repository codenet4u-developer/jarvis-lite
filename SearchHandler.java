import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;

public class SearchHandler extends CommandHandler {
    private static final Map<String, URI> WEBSITES = Map.of(
            "google", URI.create("https://www.google.com"),
            "youtube", URI.create("https://www.youtube.com"),
            "wikipedia", URI.create("https://www.wikipedia.org"),
            "github", URI.create("https://github.com"),
            "gmail", URI.create("https://mail.google.com"));

    public SearchHandler() {
        super("Search", "Opens Google searches and approved websites.");
    }

    @Override
    public boolean canHandle(String input) {
        if (input == null) {
            return false;
        }
        String command = input.trim().toLowerCase(Locale.ROOT);
        return command.startsWith("search ")
                || command.startsWith("google ")
                || command.startsWith("open ");
    }

    @Override
    public String execute(String input) {
        String command = input.trim();
        String lowerCommand = command.toLowerCase(Locale.ROOT);
        if (lowerCommand.startsWith("search ") || lowerCommand.startsWith("google ")) {
            String query = command.substring(command.indexOf(' ') + 1).trim();
            if (query.isEmpty()) {
                return "Tell me what you want to search for.";
            }
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
            return openUri(URI.create("https://www.google.com/search?q=" + encodedQuery),
                    "Opening a Google search for: " + query);
        }

        String website = command.substring("open ".length()).trim()
                .toLowerCase(Locale.ROOT);
        URI address = WEBSITES.get(website);
        if (address == null) {
            return "I can open Google, YouTube, Wikipedia, GitHub, or Gmail. "
                    + "For a web search, try 'search your topic'.";
        }
        return openUri(address, "Opening " + capitalize(website) + ".");
    }

    private String openUri(URI address, String successMessage) {
        if (!"https".equalsIgnoreCase(address.getScheme())
                || address.getHost() == null
                || !Desktop.isDesktopSupported()
                || !Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            return "I can't open a browser on this computer.";
        }

        try {
            Desktop.getDesktop().browse(address);
            return successMessage;
        } catch (IOException | UnsupportedOperationException exception) {
            return "I couldn't open that page in your default browser.";
        }
    }

    private String capitalize(String value) {
        return value.substring(0, 1).toUpperCase(Locale.ROOT) + value.substring(1);
    }
}
