package academy.fiveletters;

/**
 * Некорректная попытка: неверная длина, не буквы, слово вне словаря. Бросается до изменения состояния сессии, поэтому
 * попытка не тратится.
 */
public class InvalidGuessException extends RuntimeException {

    public InvalidGuessException(String message) {
        super(message);
    }
}
