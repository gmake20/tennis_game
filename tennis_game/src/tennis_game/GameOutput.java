package tennis_game;

import java.util.List;

// 출력 추상화 인터페이스
public interface GameOutput {
    void showMenu();

    void showMessage(String msg);

    void showRoster(List<String> playerNames);

    void showScoreBoard(String text);

    void showPlayerRecords(String playerName, List<String[]> records);
}
