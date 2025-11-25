package ru.yandex.practicum.exceptions;

public class WordNotFoundInDictionaryException extends Exception {

    public WordNotFoundInDictionaryException(String message) {
        super(message);
    }
}
