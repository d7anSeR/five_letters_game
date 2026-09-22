package academy.fiveletters.mandatory.mr2;

import academy.fiveletters.Dictionary;
import academy.fiveletters.GameFactory;

final class TestSeeds {
    private TestSeeds() {}

    static long forAnswer(Dictionary dict, String target) {
        for (long seed = 0; seed < 100_000; seed++) {
            if (GameFactory.startGame(dict, 6, seed).getAnswer().equals(target)) {
                return seed;
            }
        }
        throw new IllegalStateException("Не нашли seed для \"" + target + "\" в словаре: " + dict.getWords());
    }
}
