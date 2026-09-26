package academy.fiveletters.bonus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import academy.fiveletters.Main;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Бонус: replay")
class ReplayTest {

    @Test
    @DisplayName("Replay воспроизводит партию детерминированно")
    void replayIsDeterministic() {
        String[] args = {"replay", "--seed", "42", "озеро", "арбуз", "сорок"};
        ByteArrayOutputStream a = new ByteArrayOutputStream();
        ByteArrayOutputStream b = new ByteArrayOutputStream();

        Main.runReplay(args, new PrintStream(a, true, StandardCharsets.UTF_8));
        Main.runReplay(args, new PrintStream(b, true, StandardCharsets.UTF_8));

        assertEquals(a.toString(StandardCharsets.UTF_8), b.toString(StandardCharsets.UTF_8));
    }

    @Test
    @DisplayName("Replay печатает ответ и первую попытку")
    void replayPrintsHeaderAndGuess() {
        String[] args = {"replay", "--seed", "42", "арбуз"};
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Main.runReplay(args, new PrintStream(out, true, StandardCharsets.UTF_8));

        String s = out.toString(StandardCharsets.UTF_8);
        assertTrue(s.contains("Replay: seed=42"), "Заголовок с seed");
        assertTrue(s.contains(" арбуз"), "Строка с попыткой");
    }
}
