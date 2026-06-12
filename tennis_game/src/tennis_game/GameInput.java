package tennis_game;

import java.util.Set;

// 입력 추상화 인터페이스
public interface GameInput {
    int readMenuChoice();

    int readTotalSets();

    MatchType readMatchType();

    int readPlayerIndex(String prompt, Set<Integer> used);

    String readPlayerName();

    boolean waitForContinue();
}
