package academy.fiveletters.mandatory.mr2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import academy.fiveletters.Dictionary;
import academy.fiveletters.GameFactory;
import academy.fiveletters.GameRules;
import academy.fiveletters.GameSession;
import academy.fiveletters.GuessResult;
import academy.fiveletters.LetterResult;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Обязательные тесты: раскраска букв. */
@DisplayName("MR2. Проверка букв")
class LetterMatchingTest {

    @Test
    @DisplayName("Базовый случай: загадано \"озеро\", ввод \"арбуз\" -> ❌🟡❌❌🟡")
    void basicCase() {
        Dictionary dict = new Dictionary(List.of("озеро", "арбуз"));
        GameSession session = GameFactory.startGame(dict, 6, TestSeeds.forAnswer(dict, "озеро"));

        GuessResult result = GameRules.applyGuess(session, dict, "арбуз");
        assertEquals(
                List.of(
                        LetterResult.ABSENT,
                        LetterResult.PRESENT,
                        LetterResult.ABSENT,
                        LetterResult.ABSENT,
                        LetterResult.PRESENT),
                result.marks());
    }

    @Test
    @DisplayName("Полное совпадение: загадано \"озеро\", ввод \"озеро\" -> ✅✅✅✅✅")
    void exactMatch() {
        Dictionary dict = new Dictionary(List.of("озеро", "арбуз"));
        GameSession session = GameFactory.startGame(dict, 6, TestSeeds.forAnswer(dict, "озеро"));

        GuessResult result = GameRules.applyGuess(session, dict, "озеро");

        assertEquals(
                List.of(
                        LetterResult.CORRECT,
                        LetterResult.CORRECT,
                        LetterResult.CORRECT,
                        LetterResult.CORRECT,
                        LetterResult.CORRECT),
                result.marks());
    }

    @Test
    @DisplayName("Повторяющиеся буквы: загадано \"сорок\", ввод \"оооом\" -> ❌✅❌✅❌")
    void repeatedLettersAreNotDoubleCounted() {
        Dictionary dict = new Dictionary(List.of("сорок", "оооом"));
        GameSession session = GameFactory.startGame(dict, 6, TestSeeds.forAnswer(dict, "сорок"));

        GuessResult result = GameRules.applyGuess(session, dict, "оооом");

        assertEquals(
                List.of(
                        LetterResult.ABSENT,
                        LetterResult.CORRECT,
                        LetterResult.ABSENT,
                        LetterResult.CORRECT,
                        LetterResult.ABSENT),
                result.marks(),
                "Каждое вхождение буквы ответа расходуется один раз");
    }

    @Test
    @DisplayName("Ни одна буква не подошла: все позиции ❌")
    void noMatchingLetters() {
        Dictionary dict = new Dictionary(List.of("озеро", "филин"));
        GameSession session = GameFactory.startGame(dict, 6, TestSeeds.forAnswer(dict, "озеро"));

        GuessResult result = GameRules.applyGuess(session, dict, "филин");

        assertEquals(
                List.of(
                        LetterResult.ABSENT,
                        LetterResult.ABSENT,
                        LetterResult.ABSENT,
                        LetterResult.ABSENT,
                        LetterResult.ABSENT),
                result.marks());
    }
}
