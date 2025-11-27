package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

class FakeDictionary extends WordleDictionary {

    private final List<String> words;
    private final String fixedRandomWord;
    private final String suitableWord;

    public FakeDictionary(List<String> words, String fixedRandomWord, String suitableWord) {
        super(words);
        this.words = words;
        this.fixedRandomWord = fixedRandomWord;
        this.suitableWord = suitableWord;
    }

    @Override
    public String getRandomWord() {
        return fixedRandomWord;
    }

    @Override
    public boolean isExistWord(String word) {
        return words.contains(word.toLowerCase());
    }

    @Override
    public String getSuitableWord(List<Character> notPresentSymbols,
                                  Map<Character, Set<Integer>> wrongPositionSymbols,
                                  Map<Character, Set<Integer>> correctPositionSymbols) {
        // Для простоты возвращаем фиксированное слово
        return suitableWord;
    }
}

class WordleControllerTest {

    public static List<String> words;
    public static WordleGame game;
    public static FakeDictionary dictionary;

    @BeforeEach
    void initGame() {
        words = List.of("apple");
        dictionary = new FakeDictionary(words, "apple", "orange");
        game = new WordleGame(dictionary, 6, 5);
        game.initAnswer();
    }

    @Test
    void testTakeStep_WhenAllSymbolsNotPresentShouldReturnsAllDashes() {
        String mask = game.takeStep("zzzzz");

        assertEquals("-----", mask);
        assertEquals(2, game.getCurrentStep());

        mask = game.takeStep("bbbbb");
        assertEquals("-----", mask);
        assertEquals(3, game.getCurrentStep());
    }

    @Test
    void testTakeStep_WhenAllSymbolsWrongPositionShouldReturnsAllCarets() {
        String mask = game.takeStep("pleap");

        assertEquals("^^^^^", mask);
        assertEquals(2, game.getCurrentStep());
    }

    @Test
    void testTakeStep_masksCorrectly() {
        // apple
        String mask = game.takeStep("alppb");

        assertEquals("+^+^-", mask);
        assertEquals(2, game.getCurrentStep());
    }

    @Test
    void testIsGameOver_whenCurrentStepEqualsMaxAttempts_returnsTrue() {
        for (int i = 1; i < 6; i++) {
            game.takeStep("apple");  // увеличиваем currentStep
        }
        assertTrue(game.isGameOver(), "Игра должна завершиться при currentStep >= maxAttempts");
        assertEquals(6, game.getCurrentStep());
    }

}
