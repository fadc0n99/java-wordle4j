package ru.yandex.practicum;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

class WordleDictionaryTest {

    @Test
    void testGetSuitableWord_filtersByNotPresentSymbols() throws Exception {
        List<String> words = List.of("apple", "bread", "crane", "word");

        WordleDictionary dictionary = new WordleDictionary(words);

        List<String> inputWords = List.of("apple");
        List<Character> notPresentSymbols = List.of('e', 'a');
        Map<Character, Set<Integer>> wrongPositionSymbols = new HashMap<>();
        Map<Character, Set<Integer>> correctPositionSymbols = new HashMap<>();
        String suitable =
                dictionary.getSuitableWord(inputWords, notPresentSymbols, wrongPositionSymbols, correctPositionSymbols);

        assertNotEquals("apple", suitable);
        assertEquals("word", suitable);
    }

    @Test
    void testGetSuitableWord_filtersByWrongPositionSymbols() throws Exception {
        List<String> words = List.of("apple", "bread", "crane", "word", "place");

        WordleDictionary dictionary = new WordleDictionary(words);

        List<String> inputWords = List.of("apple");
        List<Character> notPresentSymbols = List.of('n', 'd');
        Map<Character, Set<Integer>> wrongPositionSymbols = new HashMap<>();
        Map<Character, Set<Integer>> correctPositionSymbols = new HashMap<>();

        wrongPositionSymbols.put('p', Set.of(1,2));
        wrongPositionSymbols.put('l', Set.of(3));

        String suitable =
                dictionary.getSuitableWord(inputWords, notPresentSymbols, wrongPositionSymbols, correctPositionSymbols);

        assertNotEquals("crane", suitable);
        assertEquals("place", suitable);
    }

    @Test
    void testGetSuitableWord_filtersByCorrectPositionSymbols() throws Exception {
        List<String> words = List.of("apple", "bread", "crane", "word", "place", "apace", "agpie");

        WordleDictionary dictionary = new WordleDictionary(words);

        List<String> inputWords = List.of("apple");
        List<Character> notPresentSymbols = List.of('n', 'd');
        Map<Character, Set<Integer>> wrongPositionSymbols = new HashMap<>();
        Map<Character, Set<Integer>> correctPositionSymbols = new HashMap<>();

        correctPositionSymbols.put('a', Set.of(0));
        correctPositionSymbols.put('p', Set.of(1));
        correctPositionSymbols.put('e', Set.of(4));

        wrongPositionSymbols.put('p', Set.of(2));

        String suitable =
                dictionary.getSuitableWord(inputWords, notPresentSymbols, wrongPositionSymbols, correctPositionSymbols);

        assertNotEquals("agpie", suitable);
        assertEquals("apace", suitable);
    }


}
