import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

public class VoiceAssistant {
    public void speak(String text, Consumer<String> errorHandler) {
        if (!System.getProperty("os.name").toLowerCase().contains("windows")) {
            return;
        }

        String script = "Add-Type -AssemblyName System.Speech; "
                + "$speech = New-Object System.Speech.Synthesis.SpeechSynthesizer; "
                + "$speech.Speak([Console]::In.ReadToEnd())";
        Process process;
        try {
            process = new ProcessBuilder(
                    "powershell.exe",
                    "-NoProfile",
                    "-NonInteractive",
                    "-Command",
                    script).redirectErrorStream(true).start();
        } catch (IOException exception) {
            errorHandler.accept("Speech output is unavailable; typed commands still work.");
            return;
        }

        Thread speechThread = new Thread(() -> writeSpeechInput(process, text, errorHandler),
                "jarvis-speech-output");
        speechThread.setDaemon(true);
        speechThread.start();
    }

    private void writeSpeechInput(
            Process process,
            String text,
            Consumer<String> errorHandler) {
        try (OutputStream input = process.getOutputStream()) {
            input.write(text.getBytes(StandardCharsets.UTF_8));
        } catch (IOException exception) {
            process.destroyForcibly();
            errorHandler.accept("Speech output could not read the response.");
            return;
        }

        try {
            if (process.waitFor() != 0) {
                errorHandler.accept("Speech output failed; typed commands still work.");
            }
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            process.destroyForcibly();
            errorHandler.accept("Speech output was interrupted.");
        }
    }
}
