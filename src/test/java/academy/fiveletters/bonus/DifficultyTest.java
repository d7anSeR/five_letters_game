package academy.fiveletters.bonus;

import static org.junit.jupiter.api.Assertions.assertEquals;

import academy.fiveletters.Dictionary;
import academy.fiveletters.DictionaryLoader;
import academy.fiveletters.Difficulty;
import academy.fiveletters.GameFactory;
import academy.fiveletters.GameRules;
import academy.fiveletters.GameSession;
import academy.fiveletters.GameStatus;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Бонус: уровни сложности")
class DifficultyTest {

    @Test
    @DisplayName("EASY даёт 8 попыток, NORMAL — 6, HARD — 4")
    void attemptsMatchDifficulty() {
        Dictionary dict = DictionaryLoader.loadDefault();
        assertEquals(8, GameFactory.startGame(dict, Difficulty.EASY, 1L).getMaxAttempts());
        assertEquals(6, GameFactory.startGame(dict, Difficulty.NORMAL, 1L).getMaxAttempts());
        assertEquals(4, GameFactory.startGame(dict, Difficulty.HARD, 1L).getMaxAttempts());
    }

    @Test
    @DisplayName("HARD: LOSE ровно после 4 попыток")
    void hardLosesAfterFourAttempts() {
        Dictionary dict = new Dictionary(List.of("озеро", "арбуз"));
        long seed = seedFor(dict, "озеро");
        GameSession session = GameFactory.startGame(dict, Difficulty.HARD, seed);

        for (int i = 0; i < 4; i++) {
            GameRules.applyGuess(session, dict, "арбуз");
        }

        assertEquals(GameStatus.LOSE, session.getStatus());
        assertEquals(4, session.getAttemptsUsed());
    }

    private static long seedFor(Dictionary dict, String target) {
        for (long seed = 0; seed < 100_000; seed++) {
            if (GameFactory.startGame(dict, 6, seed).getAnswer().equals(target)) {
                return seed;
            }
        }
        throw new IllegalStateException("Не нашли seed для " + target);
    }
}
