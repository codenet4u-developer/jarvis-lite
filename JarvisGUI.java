import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class JarvisGUI extends Application {
    private final CommandProcessor commandProcessor = new CommandProcessor();
    private final VoiceAssistant voiceAssistant = new VoiceAssistant();
    private TextArea conversation;
    private TextField input;
    private Label status;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Jarvis Lite");

        conversation = new TextArea();
        conversation.setEditable(false);
        conversation.setWrapText(true);
        conversation.setPromptText("Your conversation with Jarvis will appear here.");

        input = new TextField();
        input.setPromptText("Type a command, for example: 5 + 3");
        input.setOnAction(event -> submitCommand());

        Button sendButton = new Button("Send");
        sendButton.setOnAction(event -> submitCommand());

        HBox commandRow = new HBox(8, input, sendButton);
        HBox.setHgrow(input, Priority.ALWAYS);

        status = new Label("Ready. Type a command to get started.");
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(12));
        layout.setCenter(conversation);
        VBox bottom = new VBox(8, commandRow, status);
        bottom.setPadding(new Insets(12, 0, 0, 0));
        layout.setBottom(bottom);

        stage.setScene(new Scene(layout, 640, 420));
        stage.show();
        input.requestFocus();
    }

    private void submitCommand() {
        String command = input.getText().trim();
        if (command.isEmpty()) {
            status.setText("Enter a command first.");
            return;
        }

        appendConversation("You: " + command);
        input.clear();

        String response = commandProcessor.process(command);
        appendConversation("Jarvis: " + response);
        status.setText("Ready.");
        voiceAssistant.speak(response, error ->
                Platform.runLater(() -> status.setText(error)));
    }

    private void appendConversation(String message) {
        conversation.appendText(message + System.lineSeparator());
    }

}
