package ru.yandex.practicum.utils;

public class WordValidator {

    private WordValidator() {
    }

    public static String formatWord(String word) {
        return word.trim().replace('ё', 'е').toLowerCase();
    }

    public static boolean isOnlyCyrillicLetters(String word) {
        return word.chars().allMatch(c -> (c >= 'а' && c <= 'я') || c == 'ё');
    }
}
