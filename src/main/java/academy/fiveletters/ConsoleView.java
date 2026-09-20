package academy.fiveletters;

/** Форматирование результатов игры в строки. Отделено от ввода-вывода: */
public final class ConsoleView {

    private static final String[] SYMBOLS = {"❌", "🟡", "✅"};

    private ConsoleView() {}

    public static String formatGuess(GuessResult result) {
        StringBuilder sb = new StringBuilder();
        for (LetterResult mark : result.marks()) {
            sb.append(symbol(mark));
        }
        sb.append(' ').append(result.guess());
        return sb.toString();
    }

    public static String formatAttemptsLeft(GameSession session) {
        int left = session.getMaxAttempts() - session.getAttemptsUsed();
        return "Осталось попыток: " + left;
    }

    public static String formatWin(GameSession session) {
        return "Победа! Слово угадано за " + session.getAttemptsUsed() + " " + attemptsWord(session.getAttemptsUsed());
    }

    public static String formatLose(GameSession session) {
        return "Неудача. Загаданное слово: " + session.getAnswer();
    }

    private static String symbol(LetterResult mark) {
        return switch (mark) {
            case CORRECT -> SYMBOLS[2];
            case PRESENT -> SYMBOLS[1];
            case ABSENT -> SYMBOLS[0];
        };
    }

    private static String attemptsWord(int n) {
        int mod10 = n % 10;
        int mod100 = n % 100;
        if (mod100 >= 11 && mod100 <= 14) return "попыток";
        if (mod10 == 1) return "попытку";
        if (mod10 >= 2 && mod10 <= 4) return "попытки";
        return "попыток";
    }
}
