package academy.fiveletters;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

/** Консольное меню. Вся игровая логика делегируется в GameRules. */
public final class Menu {

    private final Dictionary dictionary;
    private final Scanner in;
    private final PrintStream out;
    private Difficulty difficulty = Difficulty.NORMAL;

    public Menu(Dictionary dictionary, InputStream in, PrintStream out) {
        this.dictionary = dictionary;
        this.in = new Scanner(in, java.nio.charset.StandardCharsets.UTF_8);
        this.out = out;
    }

    public void run() {
        while (true) {
            printMenu();
            String choice = in.nextLine().trim();
            switch (choice) {
                case "1" -> playOneGame();
                case "2" -> {
                    out.println("До встречи!");
                    return;
                }
                case "3" -> chooseDifficulty();
                default -> out.println("Не понял. Введите 1 или 2.");
            }
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

    private void printMenu() {
        out.println();
        out.println("1 — новая игра");
        out.println("2 — выход");
        out.println("3 — сложность (сейчас: " + difficulty + ")");
        out.print("> ");
    }

    private void playOneGame() {
        long seed = System.nanoTime();
        GameSession session = GameFactory.startGame(dictionary, difficulty, seed);
        out.println("Загадано слово из 5 букв. Попыток: " + session.getMaxAttempts());

        while (!session.isFinished()) {
            out.print("Ввод: ");
            String line = in.nextLine();
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
    }
}
