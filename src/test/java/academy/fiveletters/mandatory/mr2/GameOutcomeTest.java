package academy.fiveletters.mandatory.mr2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import academy.fiveletters.Dictionary;
import academy.fiveletters.GameFactory;
import academy.fiveletters.GameRules;
import academy.fiveletters.GameSession;
import academy.fiveletters.GameStatus;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Обязательные тесты: завершение партии. */
@DisplayName("MR2. Победа и поражение")
class GameOutcomeTest {

    @Test
    @DisplayName("Угаданное слово переводит сессию в статус WIN")
    void correctGuessWinsTheGame() {
        Dictionary dict = new Dictionary(List.of("озеро", "арбуз"));
        long seed = TestSeeds.forAnswer(dict, "озеро");
        GameSession session = GameFactory.startGame(dict, 6, seed);

        GameRules.applyGuess(session, dict, "арбуз");
        GameRules.applyGuess(session, dict, "озеро");

        assertEquals(GameStatus.WIN, session.getStatus());
        assertTrue(session.isFinished());
        assertEquals(2, session.getAttemptsUsed());
    }

    @Test
    @DisplayName("После 6 неудачных попыток сессия переходит в статус LOSE")
    void sixFailedAttemptsLoseTheGame() {
        Dictionary dict = new Dictionary(List.of("озеро", "арбуз"));
        long seed = TestSeeds.forAnswer(dict, "озеро");
        GameSession session = GameFactory.startGame(dict, 6, seed);

        for (int i = 0; i < 6; i++) {
            assertEquals(GameStatus.IN_PROGRESS, session.getStatus(), "До последней попытки партия ещё идёт");
            GameRules.applyGuess(session, dict, "арбуз");
        }

        assertEquals(GameStatus.LOSE, session.getStatus());
        assertEquals(6, session.getAttemptsUsed());
    }

    @Test
    @DisplayName("При поражении показывается загаданное слово")
    void answerIsRevealedOnLoss() {
        Dictionary dict = new Dictionary(List.of("озеро", "арбуз"));
        long seed = TestSeeds.forAnswer(dict, "озеро");
        GameSession session = GameFactory.startGame(dict, 6, seed);
        String expected = session.getAnswer();

        for (int i = 0; i < 6; i++) {
            GameRules.applyGuess(session, dict, "арбуз");
        }

        assertEquals(GameStatus.LOSE, session.getStatus());
        assertEquals("озеро", session.getAnswer());
        assertEquals(expected, session.getAnswer());
    }

    @Test
    @DisplayName("Завершённая партия больше не принимает попытки")
    void finishedGameRejectsFurtherGuesses() {
        Dictionary dict = new Dictionary(List.of("озеро", "арбуз"));
        long seed = TestSeeds.forAnswer(dict, "озеро");
        GameSession session = GameFactory.startGame(dict, 6, seed);

        GameRules.applyGuess(session, dict, "озеро");
        assertEquals(GameStatus.WIN, session.getStatus());
        assertTrue(session.isFinished());

        assertThrows(IllegalStateException.class, () -> GameRules.applyGuess(session, dict, "арбуз"));
        assertEquals(1, session.getAttemptsUsed(), "История не должна пополниться");
    }

    @Test
    @DisplayName("LOSE наступает после maxAttempts попыток, а не после хардкода 6")
    void loseAfterCustomMaxAttempts() {
        Dictionary dict = new Dictionary(List.of("озеро", "арбуз"));
        GameSession session = GameFactory.startGame(dict, 3, TestSeeds.forAnswer(dict, "озеро"));

        for (int i = 0; i < 3; i++) {
            GameRules.applyGuess(session, dict, "арбуз");
        }

        assertEquals(GameStatus.LOSE, session.getStatus());
        assertEquals(3, session.getAttemptsUsed());
    }

    @Test
    @DisplayName("Угадывание на последней попытке даёт WIN, а не LOSE")
    void winOnLastAttempt() {
        Dictionary dict = new Dictionary(List.of("озеро", "арбуз"));
        GameSession session = GameFactory.startGame(dict, 3, TestSeeds.forAnswer(dict, "озеро"));

        GameRules.applyGuess(session, dict, "арбуз");
        GameRules.applyGuess(session, dict, "арбуз");
        GameRules.applyGuess(session, dict, "озеро");

        assertEquals(GameStatus.WIN, session.getStatus());
        assertTrue(session.isFinished());
    }

    @Test
    @DisplayName("maxAttempts = 1: одна неудачная попытка → LOSE")
    void singleAttemptLosesImmediately() {
        Dictionary dict = new Dictionary(List.of("озеро", "арбуз"));
        GameSession session = GameFactory.startGame(dict, 1, TestSeeds.forAnswer(dict, "озеро"));

        GameRules.applyGuess(session, dict, "арбуз");

        assertEquals(GameStatus.LOSE, session.getStatus());
        assertEquals(1, session.getAttemptsUsed());
    }
}
