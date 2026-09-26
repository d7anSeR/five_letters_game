package academy.fiveletters;

/** Статистика игрока за один запуск программы. */
public final class SessionStats {

    private int gamesPlayed;
    private int wins;
    private int losses;
    private int totalAttempts;
    private int bestAttempts = Integer.MAX_VALUE;

    /** Учесть завершённую партию. Незавершённые игнорируются. */
    public void record(GameSession session) {
        if (session.getStatus() == GameStatus.IN_PROGRESS) {
            return;
        }
        gamesPlayed++;
        totalAttempts += session.getAttemptsUsed();
        if (session.getStatus() == GameStatus.WIN) {
            wins++;
            bestAttempts = Math.min(bestAttempts, session.getAttemptsUsed());
        } else {
            losses++;
        }
    }

    public int gamesPlayed() {
        return gamesPlayed;
    }

    public int wins() {
        return wins;
    }

    public int losses() {
        return losses;
    }

    public int totalAttempts() {
        return totalAttempts;
    }

    public int bestAttempts() {
        return wins == 0 ? 0 : bestAttempts;
    }

    public double averageAttempts() {
        return gamesPlayed == 0 ? 0.0 : (double) totalAttempts / gamesPlayed;
    }
}
