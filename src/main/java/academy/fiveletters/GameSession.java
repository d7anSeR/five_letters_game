package academy.fiveletters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Модель игровой партии. Хранит ответ, лимит попыток, историю и статус.
 *
 * <p>Инварианты:
 *
 * <ul>
 *   <li>ответ ровно из {@link Dictionary#WORD_LENGTH} букв;
 *   <li>maxAttempts > 0;
 *   <li>завершённая партия не принимает новые попытки и не меняет статус.
 * </ul>
 */
public final class GameSession {

    private final String answer;
    private final int maxAttempts;
    private final List<String> attemptsHistory = new ArrayList<>();
    private GameStatus status = GameStatus.IN_PROGRESS;

    GameSession(String answer, int maxAttempts) {
        if (answer == null || answer.length() != Dictionary.WORD_LENGTH) {
            throw new IllegalArgumentException("Answer must be exactly " + Dictionary.WORD_LENGTH + " letters");
        }
        if (maxAttempts <= 0) {
            throw new IllegalArgumentException("maxAttempts must be positive");
        }
        this.answer = answer.toLowerCase();
        this.maxAttempts = maxAttempts;
    }

    public String getAnswer() {
        return answer;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public int getAttemptsUsed() {
        return attemptsHistory.size();
    }

    public List<String> getAttemptsHistory() {
        return Collections.unmodifiableList(attemptsHistory);
    }

    public GameStatus getStatus() {
        return status;
    }

    public boolean isFinished() {
        return status != GameStatus.IN_PROGRESS;
    }

    /**
     * Фиксирует уже провалидированную попытку. Слою правил (MR2) запрещено вызывать этот метод для некорректного ввода
     * — опечатка не должна тратить попытку.
     *
     * @throws IllegalStateException если партия уже завершена
     */
    public void registerAttempt(String guess) {
        Objects.requireNonNull(guess, "guess must not be null");
        if (isFinished()) {
            throw new IllegalStateException("Finished game does not accept new attempts");
        }
        attemptsHistory.add(guess);
    }

    /**
     * Завершает партию. Допустимые финальные статусы — {@link GameStatus#WIN} и {@link GameStatus#LOSE}.
     *
     * @throws IllegalArgumentException если передан {@link GameStatus#IN_PROGRESS}
     * @throws IllegalStateException если партия уже завершена
     */
    public void finish(GameStatus finalStatus) {
        Objects.requireNonNull(finalStatus, "finalStatus must not be null");
        if (finalStatus == GameStatus.IN_PROGRESS) {
            throw new IllegalArgumentException("Cannot finish with IN_PROGRESS");
        }
        if (isFinished()) {
            throw new IllegalStateException("Game already finished: " + status);
        }
        this.status = finalStatus;
    }
}
