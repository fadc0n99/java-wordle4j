package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.NotFoundSuitableWordException;

import java.util.*;
import java.util.stream.Collectors;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */
public class WordleDictionary {

    private final List<String> words;
    private final Random random = new Random();

    public WordleDictionary(List<String> words) {
        this.words = new ArrayList<>(words);
    }

    public String getRandomWord() {
        return words.get(random.nextInt(words.size()));
    }

    public boolean isExistWord(String word) {
        return words.contains(word);
    }

    public String getSuitableWord(
            List<String> inputWords, List<Character> notPresentSymbols,
            Map<Character, Set<Integer>> wrongPositionSymbols, Map<Character, Set<Integer>> correctPositionSymbols) throws NotFoundSuitableWordException {

        List<String> filteredWords = filterWords(inputWords, notPresentSymbols);

        Collections.shuffle(filteredWords);

        for (String word : filteredWords) {
            if (allPositionsSymbolsCorrect(word, correctPositionSymbols)
                    && containsSymbolsAnotherPosition(word, wrongPositionSymbols)) {
                return word;
            }
        }

        if (!filteredWords.isEmpty()) {
            return filteredWords.getFirst();
        } else {
            throw new NotFoundSuitableWordException("В словаре нет подходящих слов");
        }
    }

    private List<String> filterWords(List<String> inputWords, List<Character> notPresentSymbols) {
        return words.stream()
                .filter(word -> !inputWords.contains(word))
                .filter(word -> notPresentSymbols.stream().noneMatch(l -> word.contains(l.toString())))
                .collect(Collectors.toList());
    }

    private boolean containsSymbolsAnotherPosition(
            String word, Map<Character, Set<Integer>> wrongPositionSymbols) {
        boolean wordContainsAllWrongPositionSymbols =
                wrongPositionSymbols.keySet().stream().allMatch(symbol -> word.contains(symbol.toString()));

        if (wordContainsAllWrongPositionSymbols) {
            char[] symbols = word.toCharArray();

            for (Map.Entry<Character, Set<Integer>> symbolEntry : wrongPositionSymbols.entrySet()) {
                for (int i = 0; i < symbols.length; i++) {
                    if (symbols[i] == symbolEntry.getKey() && symbolEntry.getValue().contains(i)) {
                        return false;
                    }
                }
            }
        } else {
            return false;
        }

        return true;
    }

    private boolean allPositionsSymbolsCorrect(String checkedWord, Map<Character, Set<Integer>> correctPositionSymbols) {
        return correctPositionSymbols.entrySet()
                .stream()
                .allMatch(l ->
                        l.getValue().stream().allMatch(k -> checkedWord.charAt(k) == l.getKey())
                );
    }
}
