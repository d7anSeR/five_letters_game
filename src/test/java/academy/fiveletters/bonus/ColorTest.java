package academy.fiveletters.bonus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import academy.fiveletters.ConsoleView;
import academy.fiveletters.GameStatus;
import academy.fiveletters.GuessResult;
import academy.fiveletters.LetterResult;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Бонус: цветной вывод")
class ColorTest {

    @AfterEach
    void resetColor() {
        ConsoleView.setColorEnabled(false);
    }

    @Test
    @DisplayName("Цвет выключен — вывод без ANSI")
    void colorDisabledPlain() {
        ConsoleView.setColorEnabled(false);
        GuessResult r = new GuessResult(
                "озеро",
                List.of(
                        LetterResult.CORRECT,
                        LetterResult.PRESENT,
                        LetterResult.ABSENT,
                        LetterResult.CORRECT,
                        LetterResult.PRESENT),
                GameStatus.IN_PROGRESS);

        String out = ConsoleView.formatGuess(r);
        assertEquals("✅🟡❌✅🟡 озеро", out);
    }

    @Test
    @DisplayName("Цвет включён — в выводе есть ANSI-коды")
    void colorEnabledAnsi() {
        ConsoleView.setColorEnabled(true);
        GuessResult r = new GuessResult(
                "озеро",
                List.of(
                        LetterResult.CORRECT,
                        LetterResult.CORRECT,
                        LetterResult.CORRECT,
                        LetterResult.CORRECT,
                        LetterResult.CORRECT),
                GameStatus.WIN);

        String out = ConsoleView.formatGuess(r);
        assertTrue(out.contains("\u001B[32m"), "Должен быть зелёный код");
        assertTrue(out.contains("\u001B[0m"), "Должен быть reset");
    }
}
