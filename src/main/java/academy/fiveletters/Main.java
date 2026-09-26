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
    private static final int DEFAULT_MAX_ATTEMPTS = 6;

    private Main() {}

    public static void main(String[] args) {
        LOG.debug("Аргументы запуска: {}", String.join(" ", args));

        try {
            if (args.length > 0 && "check".equals(args[0])) {
                runCheck(args, System.out);
            } else if (args.length > 0 && "--seed".equals(args[0])) {
                runSingleGame(Long.parseLong(args[1]), System.out);
            } else if (args.length > 0 && "replay".equals(args[0])) {
                runReplay(args, System.out);
            } else {
                runInteractive(System.in, System.out);
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            LOG.error("Не удалось запустить игру", e);
            System.err.println("Ошибка: " + e.getMessage());
        }
    }

    /** replay --seed 42 озеро арбуз сорок */
    public static void runReplay(String[] args, PrintStream out) {
        long seed = -1;
        List<String> guesses = new ArrayList<>();

        int i = 1;
        while (i < args.length) {
            if ("--seed".equals(args[i])) {
                if (i + 1 >= args.length) {
                    throw new IllegalArgumentException("replay: --seed требует значение");
                }
                seed = Long.parseLong(args[i + 1]);
                i += 2;
            } else {
                guesses.add(args[i]);
                i++;
            }
        }

        if (seed < 0) {
            throw new IllegalArgumentException("replay требует --seed");
        }
        if (guesses.isEmpty()) {
            throw new IllegalArgumentException("replay требует хотя бы одну попытку");
        }

        Dictionary dictionary = DictionaryLoader.loadDefault();
        GameSession session = GameFactory.startGame(dictionary, DEFAULT_MAX_ATTEMPTS, seed);

        out.println("Replay: seed=" + seed + ", ответ=" + session.getAnswer());

        for (String guess : guesses) {
            GuessResult result;
            try {
                result = GameRules.applyGuess(session, dictionary, guess);
            } catch (InvalidGuessException e) {
                out.println("Пропуск \"" + guess + "\": " + e.getMessage());
                continue;
            }
            out.println(ConsoleView.formatGuess(result));
            if (result.status() == GameStatus.WIN) {
                out.println(ConsoleView.formatWin(session));
                return;
            }
            if (result.status() == GameStatus.LOSE) {
                out.println(ConsoleView.formatLose(session));
                return;
            }
            out.println(ConsoleView.formatAttemptsLeft(session));
        }
    }

    /** Интерактивное меню. */
    public static void runInteractive(InputStream in, PrintStream out) {
        Dictionary dictionary = DictionaryLoader.loadDefault();
        new Menu(dictionary, in, out).run();
    }

    /** Одна игра с фиксированным seed — для отладки и тестов. */
    public static void runSingleGame(long seed, PrintStream out) {
        Dictionary dictionary = DictionaryLoader.loadDefault();
        GameSession session = GameFactory.startGame(dictionary, DEFAULT_MAX_ATTEMPTS, seed);
        LOG.debug("Игра с seed={}", seed);
        out.println("Загадано: " + session.getAnswer());
        out.println("Попыток: " + session.getMaxAttempts());
    }

    /** check --answer озеро --guess арбуз [--guess сорок]... */
    public static void runCheck(String[] args, PrintStream out) {
        String answer = null;
        List<String> guesses = new ArrayList<>();

        int i = 1;
        while (i < args.length) {
            switch (args[i]) {
                case "--answer" -> {
                    if (i + 1 >= args.length) {
                        throw new IllegalArgumentException("--answer требует значение");
                    }
                    answer = args[i + 1];
                    i += 2;
                }
                case "--guess" -> {
                    if (i + 1 >= args.length) {
                        throw new IllegalArgumentException("--guess требует значение");
                    }
                    guesses.add(args[i + 1]);
                    i += 2;
                }
                default -> throw new IllegalArgumentException("Неизвестный флаг: " + args[i]);
            }
        }

        if (answer == null) {
            throw new IllegalArgumentException("check требует --answer");
        }

        Dictionary dictionary = DictionaryLoader.loadDefault();
        GameSession session = GameFactory.startGameWithAnswer(answer, DEFAULT_MAX_ATTEMPTS);

        for (String guess : guesses) {
            GuessResult result = GameRules.applyGuess(session, dictionary, guess);
            out.println(ConsoleView.formatGuess(result));
            if (result.status() == GameStatus.WIN) {
                out.println(ConsoleView.formatWin(session));
                return;
            }
            if (result.status() == GameStatus.LOSE) {
                out.println(ConsoleView.formatLose(session));
                return;
            }
            out.println(ConsoleView.formatAttemptsLeft(session));
        }
    }
}
