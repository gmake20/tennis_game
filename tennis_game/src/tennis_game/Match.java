package tennis_game;

import java.time.LocalDate;

// 경기 전체(세트 단위)를 관리. 세트 수를 누적하고 승리 조건 도달 시 경기 종료 처리.
public class Match implements Scorable, Displayable {
    private int totalSets; // 경기 세트 수. 사용자가 선택한 3 또는 5
    private int setsToWin; // 승리에 필요한 세트 수. totalSets / 2 + 1 로 계산 (3세트면 2, 5세트면 3)
    private MatchType matchType; // 경기 방식. SINGLES 또는 DOUBLES
    private Team[] teams; // 참가 팀. 단식: Team[2], 복식: Team[2]
    private int[] setsWon = { 0, 0 }; // 각 팀의 세트 승리 횟수
    private SetScore[] setHistory; // 세트 결과 기록 배열
    private SetScore currentSet; // 현재 진행 중인 세트
    private boolean matchOver; // 경기 종료 여부
    private int winner; // 최종 승자 Team.TEAM_A 또는 Team.TEAM_B
    private String date; // 경기 날짜
    private int completedSets; // 완료된 세트 수

    public Match(int totalSets, MatchType matchType, Team[] teams) {
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
        if (matchOver)
            return;
        currentSet.pointWinner(p);
        if (currentSet.isOver()) {
            setHistory[completedSets++] = currentSet;
            setsWon[currentSet.getWinner()]++;
            if (setsWon[0] >= setsToWin || setsWon[1] >= setsToWin) {
                matchOver = true;
                winner = setsWon[0] >= setsToWin ? Team.TEAM_A : Team.TEAM_B;
            } else {
                currentSet = new SetScore();
            }
        }
    }

    public void simulate(GameInput input, GameOutput output) {
        boolean stepMode = true;
        while (!matchOver) {
            int p = Math.random() < 0.5 ? Team.TEAM_A : Team.TEAM_B;
            pointWinner(p);
            if (stepMode) {
                output.showScoreBoard(dispScoreBoard());
                if (!matchOver) {
                    stepMode = input.waitForContinue();
                }
            }
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
    public String dispScoreBoard() {
        if (matchOver) {
            return display();
        }

        StringBuilder sb = new StringBuilder();
        sb.append("================================\n");
        sb.append("현재 스코어보드\n");
        sb.append(String.format("경기 방식: %d세트 %s%n%n", totalSets, matchType));

        String label = matchType == MatchType.DOUBLES ? "팀" : "선수";
        GameScore game = currentSet.getCurrentGame();
        int[] setGames = currentSet.getGames();

        sb.append(String.format("%-28s  세트  게임  현재 게임 포인트%n", label));
        for (int t = 0; t < 2; t++) {
            int team = t;
            String pointStr = currentSet.wasTiebreak()
                    ? String.valueOf(game.getRawPoint(team))
                    : game.getPointDisplay(team);
            sb.append(String.format("%s %2d   %3d   %s%n",
                    teams[t].getDisplayName(), setsWon[t], setGames[t], pointStr));
        }

        sb.append("\n");
        if (currentSet.wasTiebreak()) {
            sb.append(String.format("현재 세트: Tie-break  |  %s %d - %s %d%n",
                    teams[0].getDisplayName(), game.getRawPoint(Team.TEAM_A),
                    teams[1].getDisplayName(), game.getRawPoint(Team.TEAM_B)));
        } else if (game.isDeuceState()) {
            sb.append("현재 게임 포인트: Deuce\n");
        } else if (game.getAdvantageTeam() != 0) {
            sb.append(String.format("현재 게임 포인트: Advantage %s%n",
                    teams[game.getAdvantageTeam() - 1].getDisplayName()));
        }

        sb.append("================================\n");
        return sb.toString();
    }

    @Override
    public String display() {
        StringBuilder sb = new StringBuilder();
        sb.append("================================\n");
        sb.append("경기 종료\n");
        sb.append(String.format("경기 방식: %d세트 %s%n%n", totalSets, matchType));

        String label = matchType == MatchType.DOUBLES ? "팀" : "선수";
        StringBuilder header = new StringBuilder();
        header.append(String.format("%s 1\t\t\t\t승수", label));
        for (int i = 0; i < completedSets; i++) {
            header.append(String.format("   %d세트", i + 1));
        }
        sb.append(header).append("\n");

        for (int t = 0; t < 2; t++) {
            StringBuilder row = new StringBuilder();
            row.append(String.format("%-28s  %2d ", teams[t].getDisplayName(), setsWon[t]));
            for (int s = 0; s < completedSets; s++) {
                int[] g = setHistory[s].getGames();
                String cell;
                if (setHistory[s].wasTiebreak()) {
                    int tbScore = setHistory[s].getCurrentGame().getRawPoint(t);
                    cell = (t == setHistory[s].getWinner())
                            ? String.format("  7(%d)", tbScore)
                            : String.format("  6(%d)", tbScore);
                } else {
                    cell = String.format("    %2d  ", g[t]);
                }
                row.append(cell);
            }
            sb.append(row).append("\n");
        }

        sb.append(String.format("%n최종 승자: %s (%d - %d)%n",
                teams[winner].getDisplayName(), setsWon[winner], setsWon[1 - winner]));

        sb.append("================================\n");
        return sb.toString();
    }

    public String toRecord() {
        StringBuilder sb = new StringBuilder();
        sb.append("DATE=").append(date).append("\n");
        sb.append("TYPE=").append(matchType).append("\n");
        sb.append("SETS=").append(totalSets).append("\n");
        sb.append("TEAM1=").append(teams[0].getDisplayName()).append("\n");
        sb.append("TEAM2=").append(teams[1].getDisplayName()).append("\n");
        sb.append("SCORE=").append(buildScoreString()).append("\n");
        sb.append("WINNER=").append(teams[winner].getDisplayName()).append("\n");
        sb.append("---").append("\n");
        return sb.toString();
    }

    private String buildScoreString() {
        StringBuilder sb = new StringBuilder();
        for (int s = 0; s < completedSets; s++) {
            if (s > 0)
                sb.append(",");
            int[] g = setHistory[s].getGames();
            if (setHistory[s].wasTiebreak()) {
                int loserTb = setHistory[s].getCurrentGame().getRawPoint(1 - setHistory[s].getWinner());
                sb.append("7-6(").append(loserTb).append(")");
            } else {
                sb.append(g[Team.TEAM_A]).append("-").append(g[Team.TEAM_B]);
            }
        }
        return sb.toString();
    }

    public String getDate() {
        return date;
    }

    public MatchType getMatchType() {
        return matchType;
    }

    public Team[] getTeams() {
        return teams;
    }

    public int[] getSetsWon() {
        return setsWon;
    }
}
