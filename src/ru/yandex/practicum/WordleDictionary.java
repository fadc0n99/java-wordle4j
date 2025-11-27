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

    public String getSuitableWord(List<Character> notPresentSymbols,
                                  Map<Character, Set<Integer>> wrongPositionSymbols,
                                  Map<Character, Set<Integer>> correctPositionSymbols)
            throws NotFoundSuitableWordException {

        List<String> filteredWords = getWordsWithoutSymbols(notPresentSymbols);

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

    private List<String> getWordsWithoutSymbols(List<Character> notPresentSymbols) {
        return words.stream()
                .filter(word -> notPresentSymbols.stream().noneMatch(l -> word.contains(l.toString())))
                .collect(Collectors.toList());
    }

    private boolean containsSymbolsAnotherPosition(String word, Map<Character, Set<Integer>> wrongPositionSymbols) {
        if (wrongPositionSymbols.isEmpty()) return true;

        for (Map.Entry<Character, Set<Integer>> symbolEntry : wrongPositionSymbols.entrySet()) {
            boolean symbolContainsAllowedPosition = false;

            for (int i = 0; i < word.length(); i++) {
                if (word.charAt(i) == symbolEntry.getKey() && !symbolEntry.getValue().contains(i)) {
                    symbolContainsAllowedPosition = true;
                    break;
                }
            }

            if (!symbolContainsAllowedPosition) {
                return false;
            }
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
