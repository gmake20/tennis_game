package tennis_game;

import java.io.*;
import java.util.*;

public class TennisManager implements FileStorable {
    private static final String RESULTS_FILE = "results.txt";

    private List<Player> playerRoster = new ArrayList<>();
    private List<Match> matchHistory = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);

    public TennisManager() {
        playerRoster.add(new Player("양인석"));
        playerRoster.add(new Player("조지훈"));
        playerRoster.add(new Player("이지훈"));
        playerRoster.add(new Player("안정빈"));
        playerRoster.add(new Player("장미성"));
        playerRoster.add(new Player("류호훈"));
        playerRoster.add(new Player("이시연"));
        playerRoster.add(new Player("신창만"));
        playerRoster.add(new Player("오수빈"));
        playerRoster.add(new Player("문규리"));
    }

    public void run() {
        while (true) {
            int choice = showMenu();
            if (choice == 1) {
                startNewMatch();
            } else if (choice == 2) {
                queryPlayerRecord();
            } else if (choice == 3) {
                System.out.println("프로그램을 종료합니다.");
                break;
            }
        }
    }

    private int showMenu() {
        System.out.println("\n========== 테니스 게임 ==========");
        System.out.println("1. 게임 플레이");
        System.out.println("2. 게임 기록 조회");
        System.out.println("3. 종료");
        System.out.print("선택: ");
        while (true) {
            try {
                int v = Integer.parseInt(scanner.nextLine().trim());
                if (v >= 1 && v <= 3) return v;
            } catch (NumberFormatException ignored) {}
            System.out.print("1~3 중에서 선택하세요: ");
        }
    }

    private void startNewMatch() {
        int totalSets = readTotalSets();
        String matchType = readMatchType();
        Team[] teams = selectTeams(matchType);

        Match match = new Match(totalSets, matchType, teams);
        System.out.println("\n경기를 시작합니다...");
        match.simulate();
        match.display();

        matchHistory.add(match);
        saveToFile();
    }

    private int readTotalSets() {
        System.out.print("경기 세트 수를 입력하세요 (3 또는 5): ");
        while (true) {
            String line = scanner.nextLine().trim();
            if (line.equals("3") || line.equals("5")) return Integer.parseInt(line);
            System.out.print("3 또는 5를 입력하세요: ");
        }
    }

    private String readMatchType() {
        System.out.print("경기 유형을 선택하세요 (1: 단식 / 2: 복식): ");
        while (true) {
            String line = scanner.nextLine().trim();
            if (line.equals("1")) return "단식";
            if (line.equals("2")) return "복식";
            System.out.print("1 또는 2를 입력하세요: ");
        }
    }

    private Team[] selectTeams(String matchType) {
        printRoster();
        Set<Integer> used = new HashSet<>();

        if (matchType.equals("단식")) {
            Player p1 = pickPlayer("선수 1 선택 (번호 입력): ", used);
            Player p2 = pickPlayer("선수 2 선택 (번호 입력): ", used);
            return new Team[]{
                new Team(new Player[]{p1}, matchType),
                new Team(new Player[]{p2}, matchType)
            };
        } else {
            System.out.println("[팀 A 선수 선택]");
            Player a1 = pickPlayer("팀 A 선수 1 선택 (번호 입력): ", used);
            Player a2 = pickPlayer("팀 A 선수 2 선택 (번호 입력): ", used);
            System.out.println("[팀 B 선수 선택]");
            Player b1 = pickPlayer("팀 B 선수 1 선택 (번호 입력): ", used);
            Player b2 = pickPlayer("팀 B 선수 2 선택 (번호 입력): ", used);
            return new Team[]{
                new Team(new Player[]{a1, a2}, matchType),
                new Team(new Player[]{b1, b2}, matchType)
            };
        }
    }

    private Player pickPlayer(String prompt, Set<Integer> used) {
        System.out.print(prompt);
        while (true) {
            try {
                int idx = Integer.parseInt(scanner.nextLine().trim());
                if (idx >= 1 && idx <= 10 && !used.contains(idx)) {
                    used.add(idx);
                    return playerRoster.get(idx - 1);
                }
            } catch (NumberFormatException ignored) {}
            System.out.print("유효하지 않거나 이미 선택된 번호입니다. 다시 입력하세요: ");
        }
    }

    private void printRoster() {
        System.out.println("\n[선수 목록]");
        for (int i = 0; i < playerRoster.size(); i++) {
            System.out.printf("%2d. %-22s", i + 1, playerRoster.get(i).getName());
            if ((i + 1) % 3 == 0) System.out.println();
        }
        System.out.println();
    }

    private void queryPlayerRecord() {
        System.out.print("조회할 선수 이름을 입력하세요: ");
        String name = scanner.nextLine().trim();

        List<String[]> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(RESULTS_FILE))) {
            String line;
            Map<String, String> block = new LinkedHashMap<>();
            while ((line = br.readLine()) != null) {
                if (line.equals("---")) {
                    String team1 = block.getOrDefault("TEAM1", "");
                    String team2 = block.getOrDefault("TEAM2", "");
                    if (team1.contains(name) || team2.contains(name)) {
                        String date = block.getOrDefault("DATE", "");
                        String type = block.getOrDefault("TYPE", "");
                        String sets = block.getOrDefault("SETS", "");
                        String score = block.getOrDefault("SCORE", "");
                        String wName = block.getOrDefault("WINNER", "");
                        String opponent = team1.contains(name) ? team2 : team1;
                        String result = wName.contains(name) ? "승리" : "패배";
                        records.add(new String[]{date, sets + "세트 " + type, opponent, score, result});
                    }
                    block.clear();
                } else if (line.contains("=")) {
                    String[] kv = line.split("=", 2);
                    block.put(kv[0], kv[1]);
                }
            }
        } catch (IOException e) {
            // 파일 없으면 기록 없음으로 처리
        }

        System.out.printf("%n[%s 게임 기록]%n", name);
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

    @Override
    public void saveToFile() {
        try (FileWriter fw = new FileWriter(RESULTS_FILE, true)) {
            Match last = matchHistory.get(matchHistory.size() - 1);
            fw.write(last.toRecord());
        } catch (IOException e) {
            System.out.println("파일 저장 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @Override
    public void loadFromFile() {
        // 기록 조회는 queryPlayerRecord()에서 직접 파일을 읽음
    }
}
