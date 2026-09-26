package academy.fiveletters.bonus;

import static org.junit.jupiter.api.Assertions.assertEquals;

import academy.fiveletters.Dictionary;
import academy.fiveletters.GameFactory;
import academy.fiveletters.GameRules;
import academy.fiveletters.GameSession;
import academy.fiveletters.SessionStats;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Бонус: статистика сессии")
class SessionStatsTest {

    private static long seedFor(Dictionary dict, String target) {
        for (long seed = 0; seed < 100_000; seed++) {
            if (GameFactory.startGame(dict, 6, seed).getAnswer().equals(target)) {
                return seed;
            }
        }
        throw new IllegalStateException("Не нашли seed для " + target);
    }

    @Test
    @DisplayName("Пустая статистика: 0 игр, среднее 0, лучшая 0")
    void emptyStats() {
        SessionStats s = new SessionStats();
        assertEquals(0, s.gamesPlayed());
        assertEquals(0, s.wins());
        assertEquals(0, s.losses());
        assertEquals(0.0, s.averageAttempts(), 1e-9);
        assertEquals(0, s.bestAttempts());
    }

    @Test
    @DisplayName("Две победы и одно поражение — счётчики корректны")
    void statsAccumulate() {
        Dictionary dict = new Dictionary(List.of("озеро", "арбуз"));
        long seed = seedFor(dict, "озеро");

        GameSession win1 = GameFactory.startGame(dict, 6, seed);
        GameRules.applyGuess(win1, dict, "арбуз");
        GameRules.applyGuess(win1, dict, "озеро");

        GameSession win2 = GameFactory.startGame(dict, 6, seed);
        GameRules.applyGuess(win2, dict, "озеро");

        GameSession lose = GameFactory.startGame(dict, 6, seed);
        for (int i = 0; i < 6; i++) {
            GameRules.applyGuess(lose, dict, "арбуз");
        }

        SessionStats s = new SessionStats();
        s.record(win1);
        s.record(win2);
        s.record(lose);

        assertEquals(3, s.gamesPlayed());
        assertEquals(2, s.wins());
        assertEquals(1, s.losses());
        assertEquals(1, s.bestAttempts());
    }

    @Test
    @DisplayName("Незавершённая партия не учитывается")
    void inProgressIgnored() {
        Dictionary dict = new Dictionary(List.of("озеро", "арбуз"));
        GameSession session = GameFactory.startGame(dict, 6, seedFor(dict, "озеро"));

        SessionStats s = new SessionStats();
        s.record(session);

        assertEquals(0, s.gamesPlayed());
    }
}
