package ru.yandex.practicum;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

public class Logger {

    private static final String LOG_FILENAME = "wordle.log";

    private Logger() {

    }

    public static void write(String message) {
        createFileIfNotExist(Paths.get(LOG_FILENAME));

        try (PrintWriter printWriter = new PrintWriter(
                new FileWriter(LOG_FILENAME, StandardCharsets.UTF_8, true))) {
            printWriter.append(LocalDateTime.now().toString()).append(" ").append(message).append("\n");
        } catch (IOException e) {
            System.out.printf("Произошла ошибка при чтении файла %s: %s%n", LOG_FILENAME, e.getMessage());
        }
    }

    private static void createFileIfNotExist(Path path) {
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                System.out.printf("Не удалось создать файл %s: %s%n", LOG_FILENAME, e.getMessage());
            }
        }
    }
}
