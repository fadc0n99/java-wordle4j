package ru.yandex.practicum;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {

    private WordleDictionaryLoader() {}

    public static WordleDictionary loadWordsFromFile(String fromFile) {
        List<String> words = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fromFile, StandardCharsets.UTF_8))) {
            while (bufferedReader.ready()) {
                String word = bufferedReader.readLine();
                if (word.length() == 5 && !word.contains("-")) {
                    String formattedWord = formatWord(word);
                    words.add(formattedWord);
                }
            }
        } catch (IOException e) {
            Logger.write(String.format("Произошла ошибка при чтении файла %s: %s%n", fromFile, e.getMessage()));
        }

        Logger.write(String.format("Загружено слов: %d", words.size()));
        return new WordleDictionary(words);
    }

    private static String formatWord(String word) {
        return word.toLowerCase().replace("ё", "е");
    }
}
