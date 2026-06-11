package tennis_game;

import java.util.*;

public class ConsoleInput implements GameInput {
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public int readMenuChoice() {
        System.out.print("선택: ");
        while (true) {
            try {
                int v = Integer.parseInt(scanner.nextLine().trim());
                if (v >= 1 && v <= 3) return v;
            } catch (NumberFormatException ignored) {}
            System.out.print("1~3 중에서 선택하세요: ");
        }
    }

    @Override
    public int readTotalSets() {
        System.out.print("경기 세트 수를 입력하세요 (3 또는 5): ");
        while (true) {
            String line = scanner.nextLine().trim();
            if (line.equals("3") || line.equals("5")) return Integer.parseInt(line);
            System.out.print("3 또는 5를 입력하세요: ");
        }
    }

    @Override
    public String readMatchType() {
        System.out.print("경기 유형을 선택하세요 (1: 단식 / 2: 복식): ");
        while (true) {
            String line = scanner.nextLine().trim();
            if (line.equals("1")) return "단식";
            if (line.equals("2")) return "복식";
            System.out.print("1 또는 2를 입력하세요: ");
        }
    }

    @Override
    public int readPlayerIndex(String prompt, Set<Integer> used) {
        System.out.print(prompt);
        while (true) {
            try {
                int idx = Integer.parseInt(scanner.nextLine().trim());
                if (idx >= 1 && idx <= 10 && !used.contains(idx)) return idx;
            } catch (NumberFormatException ignored) {}
            System.out.print("유효하지 않거나 이미 선택된 번호입니다. 다시 입력하세요: ");
        }
    }

    @Override
    public String readPlayerName() {
        System.out.print("조회할 선수 이름을 입력하세요: ");
        return scanner.nextLine().trim();
    }

    @Override
    public void waitForContinue() {
        System.out.println("Press Enter to continue...");
        try {
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
