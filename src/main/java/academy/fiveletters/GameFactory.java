package academy.fiveletters;

import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Точка входа в новую партию. Выбор слова детерминирован относительно seed: одинаковый seed и словарь = = одинаковое
 * загаданное слово.
 */
public final class GameFactory {

    private GameFactory() {}

    public static GameSession startGame(Dictionary dictionary, int maxAttempts, long seed) {
        Objects.requireNonNull(dictionary, "dictionary must not be null");
        if (maxAttempts <= 0) {
            throw new IllegalArgumentException("maxAttempts must be positive");
        }

        List<String> words = dictionary.getWords();
        Random random = new Random(seed);
        String answer = words.get(random.nextInt(words.size()));
        return new GameSession(answer, maxAttempts);
    }

    public static GameSession startGameWithAnswer(String answer, int maxAttempts) {
        return new GameSession(answer, maxAttempts);
    }
}
