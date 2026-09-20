package academy.fiveletters.mandatory.mr3;

import static org.junit.jupiter.api.Assertions.assertEquals;

import academy.fiveletters.DictionaryLoader;
import academy.fiveletters.GameFactory;
import academy.fiveletters.GameRules;
import academy.fiveletters.GameSession;
import academy.fiveletters.GuessResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Обязательные тесты MR3: детерминированный режим. */
@DisplayName("MR3. Детерминированный режим")
class DeterministicModeTest {

    @Test
    @DisplayName("check --answer озеро --guess арбуз даёт предсказуемый вывод")
    void checkModeIsPredictable() {
        var dict = DictionaryLoader.loadDefault();
        GameSession session = GameFactory.startGameWithAnswer("озеро", 6);

        GuessResult result = GameRules.applyGuess(session, dict, "арбуз");

        assertEquals("❌🟡❌❌🟡 арбуз", academy.fiveletters.ConsoleView.formatGuess(result));
    }

    @Test
    @DisplayName("Повторный запуск с тем же seed даёт тот же ответ")
    void seedIsReproducible() {
        var dict = DictionaryLoader.loadDefault();
        GameSession first = GameFactory.startGame(dict, 6, 42L);
        GameSession second = GameFactory.startGame(dict, 6, 42L);
        assertEquals(first.getAnswer(), second.getAnswer());
    }
}
