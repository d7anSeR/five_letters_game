package academy.fiveletters;

import java.util.List;
import java.util.Objects;

/** Результат одной попытки: слово, метки по буквам и статус партии после хода. */
public final class GuessResult {

    private final String guess;
    private final List<LetterResult> marks;
    private final GameStatus status;

    public GuessResult(String guess, List<LetterResult> marks, GameStatus status) {
        this.guess = Objects.requireNonNull(guess, "guess must not be null");
        this.marks = List.copyOf(marks);
        this.status = Objects.requireNonNull(status, "status must not be null");
    }

    public String guess() {
        return guess;
    }

    public List<LetterResult> marks() {
        return marks;
    }

    public GameStatus status() {
        return status;
    }

    public boolean isWin() {
        return status == GameStatus.WIN;
    }
}
