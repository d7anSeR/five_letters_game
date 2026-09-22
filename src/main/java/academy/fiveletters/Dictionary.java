package academy.fiveletters;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Словарь допустимых слов. Ограничения:
 *
 * <ul>
 *   <li>непустой список;
 *   <li>каждое слово ровно из 5 русских букв;
 *   <li>все слова приведены к нижнему регистру, дубликаты удалены.
 * </ul>
 */
public final class Dictionary {

    public static final int WORD_LENGTH = 5;

    private final List<String> words;

    public Dictionary(List<String> words) {
        Objects.requireNonNull(words, "words must not be null");
        if (words.isEmpty()) {
            throw new IllegalArgumentException("Dictionary must not be empty");
        }

        Set<String> unique = new LinkedHashSet<>();
        for (String raw : words) {
            if (raw == null) {
                throw new IllegalArgumentException("Dictionary contains null word");
            }
            String word = raw.toLowerCase();
            if (word.length() != WORD_LENGTH) {
                throw new IllegalArgumentException("Word must be exactly " + WORD_LENGTH + " letters: " + raw);
            }
            for (int i = 0; i < word.length(); i++) {
                if (!isRussianLetter(word.charAt(i))) {
                    throw new IllegalArgumentException("Word must contain only Russian letters: " + raw);
                }
            }
            unique.add(word);
        }
        this.words = List.copyOf(unique);
    }

    public List<String> getWords() {
        return words;
    }

    public int getWordLength() {
        return words.size();
    }

    public boolean contains(String word) {
        return word != null && words.contains(word.toLowerCase());
    }

    private static boolean isRussianLetter(char c) {
        return Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CYRILLIC;
    }
}
