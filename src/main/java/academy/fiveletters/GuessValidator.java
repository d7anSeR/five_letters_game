package academy.fiveletters;

/** Валидация ввода игрока. */
public final class GuessValidator {

    private GuessValidator() {}

    /**
     * Приводит ввод к нижнему регистру и проверяет:
     *
     * <ul>
     *   <li>не null;
     *   <li>ровно {@link Dictionary#WORD_LENGTH} русских букв;
     *   <li>слово есть в словаре.
     * </ul>
     *
     * @return нормализованное слово (нижний регистр)
     * @throws InvalidGuessException при любом нарушении
     */
    public static String validate(String raw, Dictionary dictionary) {
        if (raw == null) {
            throw new InvalidGuessException("Ввод пуст");
        }
        String guess = raw.trim().toLowerCase();
        if (guess.length() != Dictionary.WORD_LENGTH) {
            throw new InvalidGuessException("Нужно слово ровно из " + Dictionary.WORD_LENGTH + " букв");
        }
        for (int i = 0; i < guess.length(); i++) {
            char c = guess.charAt(i);
            if (Character.UnicodeBlock.of(c) != Character.UnicodeBlock.CYRILLIC) {
                throw new InvalidGuessException("Только русские буквы: " + raw);
            }
        }
        if (!dictionary.contains(guess)) {
            throw new InvalidGuessException("Слова нет в словаре: " + raw);
        }
        return guess;
    }
}
