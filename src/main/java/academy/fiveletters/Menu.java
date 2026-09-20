package academy.fiveletters;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

/** Консольное меню. Вся игровая логика делегируется в GameRules. */
public final class Menu {

    private final Dictionary dictionary;
    private final Scanner in;
    private final PrintStream out;

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
                default -> out.println("Не понял. Введите 1 или 2.");
            }
        }
    }

    private void printMenu() {
        out.println();
        out.println("1 — новая игра");
        out.println("2 — выход");
        out.print("> ");
    }

    private void playOneGame() {
        long seed = System.nanoTime();
        GameSession session = GameFactory.startGame(dictionary, 6, seed);
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
