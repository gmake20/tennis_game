package tennis_game;

import java.util.List;

public class ConsoleOutput implements GameOutput {

    @Override
    public void showMenu() {
        System.out.println("\n========== 테니스 게임 ==========");
        System.out.println("1. 게임 플레이");
        System.out.println("2. 게임 기록 조회");
        System.out.println("3. 종료");
    }

    @Override
    public void showMessage(String msg) {
        System.out.println(msg);
    }

    @Override
    public void showRoster(List<String> playerNames) {
        System.out.println("\n[선수 목록]");
        for (int i = 0; i < playerNames.size(); i++) {
            System.out.printf("%2d. %-22s", i + 1, playerNames.get(i));
            if ((i + 1) % 3 == 0) System.out.println();
        }
        System.out.println();
    }

    @Override
    public void showScoreBoard(String text) {
        System.out.print(text);
    }

    @Override
    public void showPlayerRecords(String playerName, List<String[]> records) {
        System.out.printf("%n[%s 게임 기록]%n", playerName);
        if (records.isEmpty()) {
            System.out.println("기록된 경기가 없습니다.");
            return;
        }
        System.out.println("--------------------------------------------------");
        System.out.printf("%-12s %-12s %-24s %-10s %s%n", "날짜", "방식", "상대", "스코어", "결과");
        for (String[] r : records) {
            System.out.printf("%-12s %-12s %-24s %-10s %s%n", r[0], r[1], r[2], r[3], r[4]);
        }
        System.out.println("--------------------------------------------------");
        long wins = records.stream().filter(r -> r[4].equals("승리")).count();
        System.out.printf("총 %d경기 | %d승 %d패%n", records.size(), wins, records.size() - wins);
    }
}
