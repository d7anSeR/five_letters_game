package academy.fiveletters.mandatory.mr2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import academy.fiveletters.Dictionary;
import academy.fiveletters.DictionaryLoader;
import academy.fiveletters.GameFactory;
import academy.fiveletters.GameRules;
import academy.fiveletters.GameSession;
import academy.fiveletters.GameStatus;
import academy.fiveletters.InvalidGuessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/** Обязательные тесты: валидация ввода. */
@DisplayName("MR2. Валидация ввода")
class GuessValidationTest {

    private final Dictionary dictionary = DictionaryLoader.loadDefault();

    @ParameterizedTest
    @ValueSource(strings = {"дом", "домики", ""})
    @DisplayName("Слово не из 5 букв отклоняется: \"{0}\"")
    void wordOfWrongLengthIsRejected(String guess) {
        GameSession session = GameFactory.startGame(dictionary, 6, 1L);
        assertThrows(InvalidGuessException.class, () -> GameRules.applyGuess(session, dictionary, guess));
        assertEquals(0, session.getAttemptsUsed(), "Некорректный ввод не должен тратить попытку");
        assertEquals(GameStatus.IN_PROGRESS, session.getStatus());
    }

    @ParameterizedTest
    @ValueSource(strings = {"дом12", "дом!!", "до ма", " озер"})
    @DisplayName("Ввод с не-буквами отклоняется: \"{0}\"")
    void nonLetterInputIsRejected(String guess) {
        GameSession session = GameFactory.startGame(dictionary, 6, 1L);
        assertThrows(InvalidGuessException.class, () -> GameRules.applyGuess(session, dictionary, guess));
        assertEquals(0, session.getAttemptsUsed());
    }

    @Test
    @DisplayName("Слово, которого нет в словаре, отклоняется")
    void wordOutsideDictionaryIsRejected() {
        GameSession session = GameFactory.startGame(dictionary, 6, 1L);
        assertThrows(InvalidGuessException.class, () -> GameRules.applyGuess(session, dictionary, "ъъъъъ"));
        assertEquals(0, session.getAttemptsUsed());
    }

    @Test
    @DisplayName("Некорректный ввод не тратит попытку")
    void invalidInputDoesNotConsumeAttempt() {
        GameSession session = GameFactory.startGame(dictionary, 6, 1L);
        assertThrows(InvalidGuessException.class, () -> GameRules.applyGuess(session, dictionary, "кот"));
        assertThrows(InvalidGuessException.class, () -> GameRules.applyGuess(session, dictionary, "12345"));
        assertThrows(InvalidGuessException.class, () -> GameRules.applyGuess(session, dictionary, "ъъъъъ"));

        assertEquals(0, session.getAttemptsUsed());
        assertEquals(GameStatus.IN_PROGRESS, session.getStatus());
    }

    @Test
    @DisplayName("Ввод не зависит от регистра: \"ОЗЕРО\" и \"озеро\" обрабатываются одинаково")
    void inputIsCaseInsensitive() {
        Dictionary dict = new Dictionary(java.util.List.of("озеро", "арбуз"));
        long seed = TestSeeds.forAnswer(dict, "озеро");

        GameSession lower = GameFactory.startGame(dict, 6, seed);
        GameSession upper = GameFactory.startGame(dict, 6, seed);

        GameRules.applyGuess(lower, dict, "озеро");
        GameRules.applyGuess(upper, dict, "ОЗЕРО");

        assertEquals(GameStatus.WIN, lower.getStatus());
        assertEquals(GameStatus.WIN, upper.getStatus());
        assertEquals(lower.getAttemptsUsed(), upper.getAttemptsUsed());
    }
}
