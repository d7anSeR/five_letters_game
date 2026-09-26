package academy.fiveletters.bonus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import academy.fiveletters.Dictionary;
import academy.fiveletters.DictionaryLoader;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Бонус: подсказки")
class HintTest {

    @Test
    @DisplayName("hint для 'озеро' возвращает подсказку из ресурсов")
    void hintFromResourceIsLoaded() {
        Dictionary d = DictionaryLoader.loadDefault();
        String hint = d.hint("озеро");
        assertNotNull(hint, "Подсказка для 'озеро' не загрузилась из dictionary.hints.txt");
        assertTrue(hint.contains("водо"), "Подсказка не та: " + hint);
    }

    @Test
    @DisplayName("Для слова без подсказки — null")
    void hintForWordWithoutHint() {
        Dictionary d = DictionaryLoader.loadDefault();
        assertNull(d.hint("автор"), "'автор' не должен иметь подсказки");
        assertFalse(d.hasHint("автор"));
    }

    @Test
    @DisplayName("hint не зависит от регистра")
    void hintIsCaseInsensitive() {
        Dictionary d = new Dictionary(List.of("озеро"), Map.of("озеро", "водоём"));
        assertEquals("водоём", d.hint("ОЗЕРО"));
    }

    @Test
    @DisplayName("Хотя бы одно слово из ресурсов имеет подсказку")
    void atLeastOneHintLoaded() {
        Dictionary d = DictionaryLoader.loadDefault();
        assertTrue(
                d.getWords().stream().anyMatch(d::hasHint),
                "Ни одной подсказки не загружено — проверь dictionary.hints.txt");
    }

    @Test
    @DisplayName("Конкретное слово 'озеро' имеет подсказку из ресурсов")
    void lakeHasHint() {
        Dictionary d = DictionaryLoader.loadDefault();
        assertTrue(d.hasHint("озеро"));
        assertEquals("водоём с неподвижной водой", d.hint("озеро"));
    }

    @Test
    @DisplayName("load парсит подсказки из одного файла")
    void loadParsesInlineHints() {
        Map<String, Dictionary> all = DictionaryLoader.loadAll();
        Dictionary animals = all.get("животные");
        assertNotNull(animals, "Категория 'животные' должна быть");
        assertTrue(
                animals.hasHint("мышка"),
                "Для 'мышка' должна быть подсказка, если ты добавил '|' в dictionary.animals.txt");
        assertEquals("маленький серый грызун", animals.hint("мышка"));
    }

    @Test
    @DisplayName("Слово без подсказки в категории даёт hasHint == false")
    void wordWithoutHintInCategory() {
        Map<String, Dictionary> all = DictionaryLoader.loadAll();
        Dictionary animals = all.get("животные");
        assertFalse(animals.hasHint("филин"), "Для 'филин' подсказки не должно быть, если ты её не добавлял");
    }
}
