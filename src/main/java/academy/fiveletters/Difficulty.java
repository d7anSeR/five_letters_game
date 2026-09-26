package academy.fiveletters;

/** Уровень сложности: влияет на число попыток. */
public enum Difficulty {
    EASY(8),
    NORMAL(6),
    HARD(4);

    private final int maxAttempts;

    Difficulty(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public int maxAttempts() {
        return maxAttempts;
    }
}
