package academy.fiveletters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Загрузка словаря из ресурсов. */
public final class DictionaryLoader {

    public static final String DEFAULT_RESOURCE = "/dictionary.txt";
    public static final String HINTS_RESOURCE = "/dictionary.hints.txt";
    public static final String ANIMAL_RESOURCE = "/dictionary.animals.txt";
    public static final String FOOD_RESOURCE = "/dictionary.food.txt";
    public static final String NATURE_RESOURCE = "/dictionary.nature.txt";

    private DictionaryLoader() {}

    public static Dictionary loadDefault() {
        List<String> words = readWords(DEFAULT_RESOURCE);
        Map<String, String> hints = readHints(HINTS_RESOURCE);
        return new Dictionary(words, hints);
    }

    public static Map<String, Dictionary> loadAll() {
        return Map.of(
                "животные", load(ANIMAL_RESOURCE),
                "еда", load(FOOD_RESOURCE),
                "природа", load(NATURE_RESOURCE));
    }

    /** Загрузка словаря без подсказок — используется для категорий. */
    public static Dictionary load(String resourcePath) {
        List<String> words = readWords(resourcePath);
        Map<String, String> hints = readHints(resourcePath);
        return new Dictionary(words, hints);
    }

    /** Читает файл слов: одна строка — одно слово, пустые строки и # пропускаются. */
    private static List<String> readWords(String resourcePath) {
        List<String> words = new ArrayList<>();
        for (String line : readLines(resourcePath)) {
            String trimmed = line.trim();
            if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                continue;
            }
            int bar = trimmed.indexOf('|');
            words.add(bar < 0 ? trimmed : trimmed.substring(0, bar).trim());
        }
        return words;
    }

    /** Читает файл подсказок формата "слово|подсказка". */
    private static Map<String, String> readHints(String resourcePath) {
        InputStream in = DictionaryLoader.class.getResourceAsStream(resourcePath);
        if (in == null) {
            return Map.of();
        }
        Map<String, String> hints = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                    continue;
                }
                int bar = trimmed.indexOf('|');
                if (bar < 0) {
                    continue;
                }
                String word = trimmed.substring(0, bar).trim().toLowerCase();
                String hint = trimmed.substring(bar + 1).trim();
                if (!word.isEmpty() && !hint.isEmpty()) {
                    hints.put(word, hint);
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return hints;
    }

    private static List<String> readLines(String resourcePath) {
        InputStream in = DictionaryLoader.class.getResourceAsStream(resourcePath);
        if (in == null) {
            throw new IllegalStateException("Dictionary resource not found: " + resourcePath);
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            return reader.lines().toList();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
