package academy.fiveletters.mandatory.mr1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import academy.fiveletters.Dictionary;
import academy.fiveletters.DictionaryLoader;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Обязательные тесты: словарь. */
@DisplayName("MR1. Словарь")
class DictionaryTest {

    @Test
    @DisplayName("Словарь содержит не меньше 50 слов")
    void dictionaryContainsAtLeastFiftyWords() {
        Dictionary dictionary = DictionaryLoader.loadDefault();
        assertTrue(dictionary.getWordLength() >= 50, "Ожидалось ≥50 слов, получено: " + dictionary.getWordLength());
    }

    @Test
    @DisplayName("Все слова словаря состоят ровно из 5 букв")
    void allWordsAreExactlyFiveLettersLong() {
        Dictionary dictionary = DictionaryLoader.loadDefault();
        for (String word : dictionary.getWords()) {
            assertEquals(5, word.length(), "Слово не из 5 букв: " + word);
        }
    }

    @Test
    @DisplayName("Пустой словарь приводит к ошибке, а не к запуску игры без слова")
    void emptyDictionaryIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new Dictionary(List.of()));
    }

    @Test
    @DisplayName("contains не зависит от регистра")
    void containsIsCaseInsensitive() {
        Dictionary dictionary = new Dictionary(List.of("озеро"));
        assertTrue(dictionary.contains("озеро"));
        assertTrue(dictionary.contains("ОЗЕРО"));
        assertFalse(dictionary.contains("арбуз"));
    }
}
