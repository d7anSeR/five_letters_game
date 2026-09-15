package academy.fiveletters.mandatory.mr1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import academy.fiveletters.Dictionary;
import academy.fiveletters.DictionaryLoader;
import academy.fiveletters.GameFactory;
import academy.fiveletters.GameSession;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Обязательные тесты: одинаковый seed обязан давать одинаковое загаданное слово. */
@DisplayName("MR1. Воспроизводимость по seed")
class SeedTest {

    @Test
    @DisplayName("Одинаковый seed даёт одинаковое загаданное слово")
    void sameSeedProducesSameAnswer() {
        Dictionary dictionary = DictionaryLoader.loadDefault();
        GameSession first = GameFactory.startGame(dictionary, 6, 12345L);
        GameSession second = GameFactory.startGame(dictionary, 6, 12345L);
        assertEquals(first.getAnswer(), second.getAnswer());
    }

    @Test
    @DisplayName("Разные seed'ы дают разные слова хотя бы иногда")
    void differentSeedsProduceDifferentAnswers() {
        Dictionary dictionary = DictionaryLoader.loadDefault();
        Set<String> answers = new HashSet<>();
        for (long seed = 0; seed < 20; seed++) {
            answers.add(GameFactory.startGame(dictionary, 6, seed).getAnswer());
        }
        assertTrue(answers.size() > 1, "20 разных seed'ов дали одно и то же слово: " + answers);
    }
}
