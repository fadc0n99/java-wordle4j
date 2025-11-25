package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.*;

import java.util.*;


/*
в этом классе хранится словарь и состояние игры
    текущий шаг
    всё что пользователь вводил
    правильный ответ

в этом классе нужны методы, которые
    проанализируют совпадение слова с ответом

предложат слово-подсказку с учётом всего, что вводил пользователь ранее
не забудьте про специальные типы исключений для игровых и неигровых ошибок
 */
public class WordleGame {

    private static final char SYMBOL_CORRECT_POSITION = '+';
    private static final char SYMBOL_WRONG_POSITION = '^';
    private static final char SYMBOL_NOT_PRESENT = '-';

    private final List<String> inputWords = new ArrayList<>();
    private final List<Character> notPresentSymbols = new ArrayList<>();
    private final Map<Character, Set<Integer>> correctPositionSymbols = new LinkedHashMap<>();
    private final Map<Character, Set<Integer>> wrongPositionSymbols = new LinkedHashMap<>();

    private String answer;
    private int currentStep = 1;
    private int maxAttempts = 6;
    private int lengthWord = 5;
    private final WordleDictionary dictionary;

    public WordleGame(WordleDictionary dictionary) {
        this.dictionary = dictionary;
    }

    public WordleGame(WordleDictionary dictionary, int maxAttempts, int lengthWord) {
        this.dictionary = dictionary;
        this.maxAttempts = maxAttempts;
        this.lengthWord = lengthWord;
    }

    public String getAnswer() {
        return answer;
    }

    public int getCurrentStep() {
        return currentStep;
    }

    public void initAnswer() {
        answer = dictionary.getRandomWord();
        Logger.write(String.format("Загадываю слово \"%s\"", answer));
    }

    public boolean isGameOver() {
        return currentStep >= maxAttempts;
    }

    public boolean isValidAnswer(String userSuggestion)
            throws WordNotFoundInDictionaryException, WordLengthTooShortException, WordLengthTooLongException {

        if (userSuggestion.length() > lengthWord) {
            throw new WordLengthTooLongException(
                    String.format("В слове \"%s\" больше %d символов. Пропуск хода", userSuggestion, lengthWord));
        }
        if (userSuggestion.length() < lengthWord) {
            throw new WordLengthTooShortException(
                    String.format("В слове \"%s\" меньше %d символов. Пропуск хода", userSuggestion, lengthWord));
        }
        if (!dictionary.isExistWord(userSuggestion)) {
            throw new WordNotFoundInDictionaryException(
                    String.format("Не удалось найти слово \"%s\" в словаре. Пропуск хода", userSuggestion));
        }


        return answer.equalsIgnoreCase(userSuggestion);
    }

    public String takeStep(String suggestion) {
        String maskedAnswer = maskSymbols(suggestion);

        if (!inputWords.contains(suggestion)) {
            inputWords.add(suggestion);
        }
        currentStep++;

        return maskedAnswer;
    }

    private String maskSymbols(String word) {
        StringBuilder builder = new StringBuilder();
        char[] symbols = word.toCharArray();

        for (int i = 0; i < symbols.length; i++) {
            if (symbols[i] == answer.charAt(i)) {
                builder.append(SYMBOL_CORRECT_POSITION);
                correctPositionSymbols.computeIfAbsent(symbols[i], k -> new HashSet<>()).add(i);
            } else if (answer.indexOf(symbols[i]) != -1) {
                builder.append(SYMBOL_WRONG_POSITION);
                wrongPositionSymbols.computeIfAbsent(symbols[i], k -> new HashSet<>()).add(i);
            } else {
                builder.append(SYMBOL_NOT_PRESENT);
                notPresentSymbols.add(symbols[i]);
            }
        }

        return builder.toString();
    }

    public String searchHintWord() throws NotFoundSuitableWordException {
        return dictionary.getSuitableWord(
                inputWords, notPresentSymbols, wrongPositionSymbols, correctPositionSymbols);
    }

}
