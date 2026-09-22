package academy.fiveletters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/** Загрузка словаря из ресурсов */
public final class DictionaryLoader {

    public static final String DEFAULT_RESOURCE = "/dictionary.txt";

    private DictionaryLoader() {}

    public static Dictionary loadDefault() {
        return load(DEFAULT_RESOURCE);
    }

    public static Dictionary load(String resourcePath) {
        try (InputStream in = DictionaryLoader.class.getResourceAsStream(resourcePath)) {
            if (in == null) {
                throw new IllegalStateException("Dictionary resource not found: " + resourcePath);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                List<String> words = reader.lines()
                        .map(String::trim)
                        .filter(line -> !line.isEmpty())
                        .filter(line -> !line.startsWith("#"))
                        .toList();
                return new Dictionary(words);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
