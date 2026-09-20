package academy.fiveletters;

/** Форматирование результатов игры в строки. Отделено от ввода-вывода: */
public final class ConsoleView {

    private static final String[] SYMBOLS = {"❌", "🟡", "✅"};
    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";
    private static final int TEN = 10;
    private static final int HUNDRED = 100;
    private static final int ELEVEN = 11;
    private static final int FOURTEEN = 14;
    private static final int FOUR = 4;
    private static boolean colorEnabled = false;

    /** Включает/выключает ANSI-цвета. По умолчанию выключено. */
    public static void setColorEnabled(boolean enabled) {
        colorEnabled = enabled;
    }

    public static boolean isColorEnabled() {
        return colorEnabled;
    }

    /** Автоопределение: цвет только в настоящем терминале и без NO_COLOR. */
    public static void autoDetectColor() {
        colorEnabled = System.console() != null && System.getenv("NO_COLOR") == null;
    }

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
        String s =
                switch (mark) {
                    case CORRECT -> "✅";
                    case PRESENT -> "🟡";
                    case ABSENT -> "❌";
                };
        if (!colorEnabled) {
            return s;
        }
        return switch (mark) {
            case CORRECT -> GREEN + s + RESET;
            case PRESENT -> YELLOW + s + RESET;
            case ABSENT -> RED + s + RESET;
        };
    }

    private static String attemptsWord(int n) {
        int mod10 = n % TEN;
        int mod100 = n % HUNDRED;
        if (mod100 >= ELEVEN && mod100 <= FOURTEEN) {
            return "попыток";
        }
        if (mod10 == 1) {
            return "попытку";
        }
        if (mod10 >= 2 && mod10 <= FOUR) {
            return "попытки";
        }
        return "попыток";
    }
}
