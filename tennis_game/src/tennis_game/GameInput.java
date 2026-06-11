package tennis_game;

import java.util.Set;

public interface GameInput {
    int readMenuChoice();
    int readTotalSets();
    MatchType readMatchType();
    int readPlayerIndex(String prompt, Set<Integer> used);
    String readPlayerName();
    boolean waitForContinue();
}
