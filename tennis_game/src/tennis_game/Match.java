package tennis_game;

import java.time.LocalDate;

public class Match implements Scorable, Displayable {
    private int totalSets;
    private int setsToWin;
    private String matchType;
    private Team[] teams;
    private int[] setsWon = {0, 0};
    private SetScore[] setHistory;
    private SetScore currentSet;
    private boolean matchOver;
    private int winner;
    private String date;
    private int completedSets;

    public Match(int totalSets, String matchType, Team[] teams) {
        this.totalSets = totalSets;
        this.setsToWin = totalSets / 2 + 1;
        this.matchType = matchType;
        this.teams = teams;
        this.date = LocalDate.now().toString();
        this.setHistory = new SetScore[totalSets];
        this.currentSet = new SetScore();
    }

    @Override
    public void pointWinner(int p) {
        if (matchOver) return;
        currentSet.pointWinner(p);
        if (currentSet.isOver()) {
            // capture tiebreak loser score before saving
            if (currentSet.wasTiebreak()) {
                currentSet.setTiebreakWinnerScore(currentSet.getCurrentGame().getRawPoint(currentSet.getWinner()));
            }
            setHistory[completedSets++] = currentSet;
            setsWon[currentSet.getWinner() - 1]++;
            if (setsWon[0] >= setsToWin || setsWon[1] >= setsToWin) {
                matchOver = true;
                winner = setsWon[0] >= setsToWin ? 1 : 2;
            } else {
                currentSet = new SetScore();
            }
        }
    }

    public void simulate() {
        while (!matchOver) {
            int p = Math.random() < 0.5 ? 1 : 2;
            pointWinner(p);
        }
    }

    @Override
    public boolean isOver() {
        return matchOver;
    }

    @Override
    public int getWinner() {
        return winner;
    }

    @Override
    public void display() {
        System.out.println("================================");
        System.out.println("경기 종료");
        System.out.printf("경기 방식: %d세트 %s%n%n", totalSets, matchType);

        // header
        String label = matchType.equals("복식") ? "팀" : "선수";
        StringBuilder header = new StringBuilder();
        header.append(String.format("%-28s  세트", label));
        for (int i = 0; i < completedSets; i++) {
            header.append(String.format("   %d세트", i + 1));
        }
        System.out.println(header);

        for (int t = 0; t < 2; t++) {
            StringBuilder row = new StringBuilder();
            row.append(String.format("%-28s  %2d ", teams[t].getDisplayName(), setsWon[t]));
            for (int s = 0; s < completedSets; s++) {
                int[] g = setHistory[s].getGames();
                String cell;
                if (setHistory[s].wasTiebreak()) {
                    int tbScore = setHistory[s].getTiebreakWinnerScore();
                    if (t == setHistory[s].getWinner() - 1) {
                        cell = String.format("  7   ");
                    } else {
                        cell = String.format("  6(%d)", tbScore);
                    }
                } else {
                    cell = String.format("  %2d  ", g[t]);
                }
                row.append(cell);
            }
            System.out.println(row);
        }

        System.out.printf("%n최종 승자: %s (%d - %d)%n",
                teams[winner - 1].getDisplayName(), setsWon[winner - 1], setsWon[2 - winner]);
        System.out.println("================================");
    }

    public String toRecord() {
        StringBuilder sb = new StringBuilder();
        sb.append("DATE=").append(date).append("\n");
        sb.append("TYPE=").append(matchType).append("\n");
        sb.append("SETS=").append(totalSets).append("\n");
        sb.append("TEAM1=").append(teams[0].getDisplayName()).append("\n");
        sb.append("TEAM2=").append(teams[1].getDisplayName()).append("\n");
        sb.append("SCORE=").append(buildScoreString()).append("\n");
        sb.append("WINNER=").append(teams[winner - 1].getDisplayName()).append("\n");
        sb.append("---").append("\n");
        return sb.toString();
    }

    private String buildScoreString() {
        StringBuilder sb = new StringBuilder();
        for (int s = 0; s < completedSets; s++) {
            if (s > 0) sb.append(",");
            int[] g = setHistory[s].getGames();
            if (setHistory[s].wasTiebreak()) {
                int loserTb = setHistory[s].getWinner() == 1 ?
                        setHistory[s].getCurrentGame().getRawPoint(2) :
                        setHistory[s].getCurrentGame().getRawPoint(1);
                sb.append("7-6(").append(loserTb).append(")");
            } else {
                sb.append(g[0]).append("-").append(g[1]);
            }
        }
        return sb.toString();
    }

    public String getDate() { return date; }
    public String getMatchType() { return matchType; }
    public Team[] getTeams() { return teams; }
    public int[] getSetsWon() { return setsWon; }
}
