package ru.yandex.practicum;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import ru.yandex.practicum.exceptions.*;

/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */
public class Wordle {


    public static final String DICTIONARY_FILENAME = "words_ru.txt";
    public static final int MAX_ATTEMPTS = 6;
    public static final int LENGTH_WORD = 5;

    public static final WordleDictionary dictionary =
            WordleDictionaryLoader.loadWordsFromFile(DICTIONARY_FILENAME);
    public static final WordleGame wordleGame = new WordleGame(dictionary, MAX_ATTEMPTS, LENGTH_WORD);
    public static final Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);

    public static void main(String[] args) {
        Logger.write("Начать игру");
        startWordle();
        Logger.write("Закончить игру");
    }

    public static void startWordle() {
        wordleGame.initAnswer();

        while(true) {
            System.out.println("Попытка " + wordleGame.getCurrentStep() + ". Введите слово, либо нажмите [ENTER] для подсказки:");
            String suggestion = formatWord(scanner.nextLine());
            if (!isOnlyCyrillicLetters(suggestion)) {
                System.out.println("В введенном слове допускается только кириллица");
                continue;
            }

            if (suggestion.isBlank()) {
                try {
                    suggestion = wordleGame.searchHintWord();
                    System.out.printf("Слово-подсказка: %s%n", suggestion);
                } catch (NotFoundSuitableWordException e) {
                    System.out.println(e.getMessage());
                    continue;
                }
            }

            try {
                if (wordleGame.isValidAnswer(suggestion)) {
                    System.out.println("Верный ответ!");
                    break;
                }

                if (wordleGame.isGameOver()) {
                    System.out.println("Поражение! Загаданное слово: " + wordleGame.getAnswer());
                    break;
                }

                String maskedSuggestion = wordleGame.takeStep(suggestion);
                System.out.println(maskedSuggestion);
            } catch (WordNotFoundInDictionaryException e) {
                Logger.write(e.getMessage());
                System.out.println("Слово не найдено");
            } catch (WordLengthTooLongException | WordLengthTooShortException e) {
                Logger.write(e.getMessage());
                System.out.println("Слово должно быть из 5 символов");

            }
        }
    }

    private static String formatWord(String word) {
        return word.trim().replace('ё', 'е').toLowerCase();
    }

    private static boolean isOnlyCyrillicLetters(String word) {
        String cyrillic = "абвгдежзийклмнопрстуфхцчшщъыьэюя";

        for (String symbol : word.split("")) {
            if (!cyrillic.contains(symbol)) {
                return false;
            }
        }
        return true;
    }

}
