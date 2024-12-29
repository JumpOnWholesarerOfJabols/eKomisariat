package main.java.logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class Logger {
    private static Logger instance;
    private PrintWriter writer;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm-ss");

    private Logger() {
        Path logPath = Path.of("src/main/resources/logs/ekomisariat-%s.log".formatted(formatter.format(LocalDateTime.now())));
        try {
            if (!Files.exists(logPath.getParent())) Files.createDirectories(logPath.getParent());

            writer = new PrintWriter(logPath.toString());
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("File system inaccessible");
            System.exit(1);
        }
    }

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void log(String message) {
        writer.println(message);
        writer.flush();
    }

    public void log(LogEventType type, Object... args){
        writer.println(type.value().formatted(args));
        writer.flush();
    }
}
