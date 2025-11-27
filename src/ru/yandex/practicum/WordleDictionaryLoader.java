package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.WordsFileLoadException;
import ru.yandex.practicum.exceptions.WordsFileNotFoundException;
import ru.yandex.practicum.utils.WordValidator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {

    private WordleDictionaryLoader() {

    }

    public static WordleDictionary loadWordsFromFile(String fromFile, int requiredWordLength)
            throws WordsFileNotFoundException, WordsFileLoadException {
        List<String> words = new ArrayList<>();

        if (!Files.exists(Paths.get(fromFile))) {
            throw new WordsFileNotFoundException(String.format("Файл %s не найден.", fromFile));
        }

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fromFile, StandardCharsets.UTF_8))) {
            while (bufferedReader.ready()) {
                String word = bufferedReader.readLine();
                if (word.length() == requiredWordLength && !word.contains("-")) {
                    String formattedWord = WordValidator.formatWord(word);
                    words.add(formattedWord);
                }
            }
        } catch (IOException e) {
            String errorMessage =
                    String.format("Произошла ошибка при чтении файла %s: %s%n", fromFile, e.getMessage());
            Logger.write(errorMessage);
            throw new WordsFileLoadException(errorMessage);
        }

        if (words.isEmpty()) {
            throw new WordsFileLoadException(String.format("В файле %s отсутствуют слова", fromFile));
        }

        Logger.write(String.format("Загружено слов: %d", words.size()));
        return new WordleDictionary(words);
    }
}
