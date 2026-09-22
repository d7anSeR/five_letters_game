package academy.fiveletters;

import java.util.List;

/**
 * Правила игры: подсчёт подсказок и переходы статусов.
 *
 * <p>Алгоритм двухпроходный:
 *
 * <ol>
 *   <li>сначала отмечаем все ✅ и «расходуем» соответствующие буквы ответа;
 *   <li>затем по оставшимся буквам ответа закрываем 🟡;
 *   <li>остальное — ❌.
 * </ol>
 *
 * Каждое вхождение буквы ответа используется не более одного раза.
 */
public final class GameRules {

    private GameRules() {}

    public static GuessResult applyGuess(GameSession session, Dictionary dictionary, String rawGuess) {
        if (session.isFinished()) {
            throw new IllegalStateException("Партия завершена, новые попытки не принимаются");
        }

        String guess = GuessValidator.validate(rawGuess, dictionary);
        String answer = session.getAnswer();

        List<LetterResult> marks = markLetters(guess, answer);

        session.registerAttempt(guess);

        GameStatus status = resolveStatus(session, guess);
        if (status != GameStatus.IN_PROGRESS) {
            session.finish(status);
        }

        return new GuessResult(guess, marks, status);
    }

    private static List<LetterResult> markLetters(String guess, String answer) {
        int n = Dictionary.WORD_LENGTH;
        LetterResult[] marks = new LetterResult[n];
        boolean[] answerUsed = new boolean[n];

        for (int i = 0; i < n; i++) {
            if (guess.charAt(i) == answer.charAt(i)) {
                marks[i] = LetterResult.CORRECT;
                answerUsed[i] = true;
            }
        }

        for (int i = 0; i < n; i++) {
            if (marks[i] == null) {
                marks[i] = matchPresent(guess.charAt(i), answer, answerUsed);
            }
        }
        return List.of(marks);
    }

    private static LetterResult matchPresent(char c, String answer, boolean[] answerUsed) {
        for (int j = 0; j < answer.length(); j++) {
            if (!answerUsed[j] && answer.charAt(j) == c) {
                answerUsed[j] = true;
                return LetterResult.PRESENT;
            }
        }
        return LetterResult.ABSENT;
    }

    private static GameStatus resolveStatus(GameSession session, String guess) {
        if (guess.equals(session.getAnswer())) {
            return GameStatus.WIN;
        }
        if (session.getAttemptsUsed() >= session.getMaxAttempts()) {
            return GameStatus.LOSE;
        }
        return GameStatus.IN_PROGRESS;
    }
}
