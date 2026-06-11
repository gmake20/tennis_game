package tennis_game;

import java.util.List;

public interface GameOutput {
    void showMenu();
    void showMessage(String msg);
    void showRoster(List<String> playerNames);
    void showScoreBoard(String text);
    void showPlayerRecords(String playerName, List<String[]> records);
}
