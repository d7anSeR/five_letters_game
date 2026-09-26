package academy.fiveletters;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

/** Консольное меню. Вся игровая логика делегируется в GameRules. */
public final class Menu {

    private Dictionary dictionary;
    private String category = "общий";
    private final Scanner in;
    private final PrintStream out;
    private Difficulty difficulty = Difficulty.NORMAL;
    private final SessionStats stats = new SessionStats();

    public Menu(Dictionary dictionary, InputStream in, PrintStream out) {
        this.dictionary = dictionary;
        this.in = new Scanner(in, java.nio.charset.StandardCharsets.UTF_8);
        this.out = out;
    }

    public void run() {
        ConsoleView.autoDetectColor();
        while (true) {
            printMenu();
            String choice;
            try {
                choice = in.nextLine().trim();
            } catch (NoSuchElementException e) {
                out.println("До встречи!");
                return;
            }
            switch (choice) {
                case "1" -> playOneGame();
                case "2" -> {
                    out.println("До встречи!");
                    return;
                }
                case "3" -> chooseDifficulty();
                case "4" -> printStats();
                case "5" -> chooseCategory();
                default -> out.println("Не понял. Введите 1 или 2.");
            }
        }
    }

    private void chooseCategory() {
        Map<String, Dictionary> all = DictionaryLoader.loadAll();
        out.println("0 — общий словарь");
        int i = 1;
        List<String> names = new ArrayList<>(all.keySet());
        for (String name : names) {
            out.println(i + " — " + name);
            i++;
        }
        out.print("> ");
        String choice = in.nextLine().trim();
        if ("0".equals(choice)) {
            dictionary = DictionaryLoader.loadDefault();
            category = "общий";
            return;
        }
        try {
            int idx = Integer.parseInt(choice) - 1;
            if (idx >= 0 && idx < names.size()) {
                String name = names.get(idx);
                Dictionary loaded = all.get(name);
                if (loaded != null) {
                    dictionary = loaded;
                } else {
                    out.println("Категория не найдена: " + name);
                }
                category = name;
            } else {
                out.println("Не понял, оставляю " + category);
            }
        } catch (NumberFormatException e) {
            out.println("Не понял, оставляю " + category);
        }
    }

    private void chooseDifficulty() {
        out.println("Выберите сложность:");
        out.println("1 — EASY (8 попыток)");
        out.println("2 — NORMAL (6 попыток)");
        out.println("3 — HARD (4 попытки)");
        out.print("> ");
        String choice = in.nextLine().trim();
        switch (choice) {
            case "1" -> difficulty = Difficulty.EASY;
            case "2" -> difficulty = Difficulty.NORMAL;
            case "3" -> difficulty = Difficulty.HARD;
            default -> out.println("Не понял, оставляю " + difficulty);
        }
        out.println("Сложность: " + difficulty);
    }

    private void printStats() {
        out.println("Игр: " + stats.gamesPlayed() + ", побед: " + stats.wins() + ", поражений: " + stats.losses());
        out.printf("Среднее число попыток: %.1f%n", stats.averageAttempts());
        out.println("Лучшая игра: " + stats.bestAttempts() + " попыток");
    }

    private void printMenu() {
        out.println();
        out.println("1 — новая игра");
        out.println("2 — выход");
        out.println("3 — сложность (сейчас: " + difficulty + ")");
        out.println("4 — статистика");
        out.println("5 — категория (сейчас: " + category + ")");
        out.print("> ");
    }

    private void playOneGame() {
        long seed = System.nanoTime();
        GameSession session = GameFactory.startGame(dictionary, difficulty, seed);
        out.println("Загадано слово из 5 букв. Попыток: " + session.getMaxAttempts());
        if (dictionary.hasHint(session.getAnswer())) {
            out.println("Можно ввести '?' для подсказки.");
        }
        while (!session.isFinished()) {
            out.print("Ввод: ");
            String line = in.nextLine();
            if ("?".equals(line.trim())) {
                String hint = dictionary.hint(session.getAnswer());
                out.println(hint == null ? "Подсказки нет." : "Подсказка: " + hint);
                continue;
            }
            try {
                GuessResult result = GameRules.applyGuess(session, dictionary, line);
                out.println(ConsoleView.formatGuess(result));
                if (result.status() == GameStatus.WIN) {
                    out.println(ConsoleView.formatWin(session));
                } else if (result.status() == GameStatus.LOSE) {
                    out.println(ConsoleView.formatLose(session));
                } else {
                    out.println(ConsoleView.formatAttemptsLeft(session));
                }
            } catch (InvalidGuessException e) {
                out.println("Ошибка: " + e.getMessage());
            } catch (IllegalStateException e) {
                out.println("Игра уже завершена: " + e.getMessage());
            }
        }
        stats.record(session);
    }
}
