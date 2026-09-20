package academy.fiveletters;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Точка входа в игру «5 букв».
 *
 * <p>Режимы запуска:
 *
 * <ul>
 *   <li>без аргументов — интерактивное меню;
 *   <li>{@code --seed N} — одна игра с фиксированным seed;
 *   <li>{@code check --answer озеро --guess арбуз [--guess ...]} — детерминированная проверка.
 * </ul>
 */
public final class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    private Main() {}

    public static void main(String[] args) {
        LOG.debug("Аргументы запуска: {}", String.join(" ", args));

        try {
            if (args.length > 0 && "check".equals(args[0])) {
                runCheck(args);
            } else if (args.length > 0 && "--seed".equals(args[0])) {
                runSingleGame(Long.parseLong(args[1]));
            } else {
                runInteractive(System.in, System.out);
            }
        } catch (RuntimeException e) {
            LOG.error("Не удалось запустить игру", e);
            System.err.println("Ошибка: " + e.getMessage());
        }
    }

    /** Интерактивное меню. */
    public static void runInteractive(InputStream in, PrintStream out) {
        Dictionary dictionary = DictionaryLoader.loadDefault();
        new Menu(dictionary, in, out).run();
    }

    /** Одна игра с фиксированным seed — для отладки и тестов. */
    public static void runSingleGame(long seed) {
        Dictionary dictionary = DictionaryLoader.loadDefault();
        GameSession session = GameFactory.startGame(dictionary, 6, seed);
        LOG.debug("Игра с seed={}", seed);
        System.out.print("Загадано: " + session.getAnswer());
        System.out.print("\nПопыток: " + session.getMaxAttempts());
    }

    public static void runCheck(String[] args) {
        String answer = null;
        List<String> guesses = new ArrayList<>();

        for (int i = 1; i < args.length - 1; i++) {
            switch (args[i]) {
                case "--answer" -> answer = args[++i];
                case "--guess" -> guesses.add(args[++i]);
                default -> throw new IllegalArgumentException("Неизвестный флаг: " + args[i]);
            }
        }
        if (answer == null) {
            throw new IllegalArgumentException("check требует --answer");
        }

        Dictionary dictionary = DictionaryLoader.loadDefault();
        GameSession session = GameFactory.startGameWithAnswer(answer, 6);

        for (String guess : guesses) {
            GuessResult result = GameRules.applyGuess(session, dictionary, guess);
            System.out.println(ConsoleView.formatGuess(result));
            if (result.status() == GameStatus.WIN) {
                System.out.println(ConsoleView.formatWin(session));
                return;
            }
            if (result.status() == GameStatus.LOSE) {
                System.out.println(ConsoleView.formatLose(session));
                return;
            }
            System.out.println(ConsoleView.formatAttemptsLeft(session));
        }
    }
}
