package ru.yandex.practicum;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import ru.yandex.practicum.exceptions.*;
import ru.yandex.practicum.utils.WordValidator;

/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */
public class WordleController {

    private final WordleGame wordleGame;
    private final Scanner scanner;

    public WordleController(String wordsFileName, int attempts, int lengthWord)
            throws WordsFileNotFoundException, WordsFileLoadException {
        WordleDictionary dictionary = WordleDictionaryLoader.loadWordsFromFile(wordsFileName, lengthWord);
        wordleGame = new WordleGame(dictionary, attempts, lengthWord);
        scanner = new Scanner(System.in, StandardCharsets.UTF_8);
    }

    public void start() {
        Logger.write("Начать игру");
        wordleGame.initAnswer();

        while (true) {
            System.out.println("Попытка " + wordleGame.getCurrentStep() + ". Введите слово, либо нажмите [ENTER] для подсказки:");
            String suggestion = WordValidator.formatWord(scanner.nextLine());
            if (!WordValidator.isOnlyCyrillicLetters(suggestion)) {
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
            } catch (InvalidWordLengthException e) {
                Logger.write(e.getMessage());
                System.out.println("Слово должно быть из 5 символов");

            }
        }

        Logger.write("Закончить игру");
    }

}
