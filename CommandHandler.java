public abstract class CommandHandler {
    private final String name;
    private final String description;

    protected CommandHandler(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public final String getName() {
        return name;
    }

    public final String getDescription() {
        return description;
    }

    public abstract boolean canHandle(String input);

    public abstract String execute(String input);
}
