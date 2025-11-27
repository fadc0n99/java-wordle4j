package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.WordsFileLoadException;
import ru.yandex.practicum.exceptions.WordsFileNotFoundException;

public class Main {
    public static final String DICTIONARY_FILENAME = "words_ru.txt";
    public static final int MAX_ATTEMPTS = 6;
    public static final int LENGTH_WORD = 5;

    public static void main(String[] args) {
        try {
            WordleController wordleController = new WordleController(DICTIONARY_FILENAME, MAX_ATTEMPTS, LENGTH_WORD);
            wordleController.start();
        } catch (WordsFileNotFoundException | WordsFileLoadException e) {
            System.out.println(e.getMessage());
        }
    }
}
