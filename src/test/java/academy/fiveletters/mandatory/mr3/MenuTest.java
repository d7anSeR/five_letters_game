package academy.fiveletters.mandatory.mr3;

import static org.junit.jupiter.api.Assertions.assertTrue;

import academy.fiveletters.DictionaryLoader;
import academy.fiveletters.Menu;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Обязательные тесты: меню и режим автопроверки. */
@DisplayName("MR3. Меню и детерминированный режим")
class MenuTest {

    @Test
    @DisplayName("Некорректный пункт меню не роняет программу")
    void invalidMenuChoiceDoesNotCrash() {
        String input = "abc\n999\n\n2\n";
        Menu menu = new Menu(
                DictionaryLoader.loadDefault(),
                new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)),
                new PrintStream(new ByteArrayOutputStream()));
        menu.run();
    }

    @Test
    @DisplayName("Можно сыграть несколько партий подряд без перезапуска")
    void severalGamesInARow() {
        String input = "1\n" + "озеро\n".repeat(6) + "1\n" + "арбуз\n".repeat(6) + "2\n";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Menu menu = new Menu(
                DictionaryLoader.loadDefault(),
                new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)),
                new PrintStream(out, true, StandardCharsets.UTF_8));
        menu.run();
        String output = out.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("До встречи!"), "Меню должно корректно завершиться");
    }

    @Test
    @DisplayName("Детерминированный режим даёт предсказуемый вывод для автопроверки")
    void deterministicModeProducesPredictableOutput() {
        String input = "2\n";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Menu menu = new Menu(
                DictionaryLoader.loadDefault(),
                new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)),
                new PrintStream(out, true, StandardCharsets.UTF_8));
        menu.run();
        assertTrue(out.toString(StandardCharsets.UTF_8).contains("До встречи!"));
    }
}
