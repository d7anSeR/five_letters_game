package academy.fiveletters.bonus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import academy.fiveletters.Dictionary;
import academy.fiveletters.DictionaryLoader;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Бонус: категории слов")
class CategoriesTest {

    @Test
    @DisplayName("Есть минимум три категории, каждая ≥10 слов по 5 букв")
    void categoriesAreWellFormed() {
        Map<String, Dictionary> all = DictionaryLoader.loadAll();
        assertFalse(all.isEmpty());
        assertTrue(all.size() >= 3, "Ожидалось ≥3 категорий");

        all.forEach((name, dict) -> {
            assertTrue(dict.getWordLength() >= 10, "Категория " + name + " слишком маленькая: " + dict.getWordLength());
            dict.getWords().forEach(w -> assertEquals(5, w.length(), "Слово не из 5 букв в " + name + ": " + w));
        });
    }

    @Test
    @DisplayName("Категории не пустые и не совпадают полностью")
    void categoriesDiffer() {
        Map<String, Dictionary> all = DictionaryLoader.loadAll();
        var sets = all.values().stream()
                .map(d -> new java.util.HashSet<>(d.getWords()))
                .toList();

        boolean anyDiffers = false;
        for (int i = 0; i < sets.size() && !anyDiffers; i++) {
            for (int j = i + 1; j < sets.size(); j++) {
                if (!sets.get(i).equals(sets.get(j))) {
                    anyDiffers = true;
                    break;
                }
            }
        }
        assertTrue(anyDiffers, "Все категории — один и тот же набор слов");
    }
}
