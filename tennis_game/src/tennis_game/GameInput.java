package tennis_game;

import java.util.Set;

public interface GameInput {
    int readMenuChoice();
    int readTotalSets();
    String readMatchType();
    int readPlayerIndex(String prompt, Set<Integer> used);
    String readPlayerName();
}
