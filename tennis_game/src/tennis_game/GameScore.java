package tennis_game;

public class GameScore implements Scorable, Displayable {
    private int[] points = {0, 0};
    private boolean isTiebreak;
    private boolean gameOver;
    private int gameWinner;

    public GameScore(boolean isTiebreak) {
        this.isTiebreak = isTiebreak;
    }

    @Override
    public void pointWinner(int p) {
        if (gameOver) return;
        points[p]++;
        checkWin();
    }

    private void checkWin() {
        if (isTiebreak) {
            if ((points[Team.TEAM_A] >= 7 || points[Team.TEAM_B] >= 7) && Math.abs(points[Team.TEAM_A] - points[Team.TEAM_B]) >= 2) {
                gameOver = true;
                gameWinner = points[Team.TEAM_A] > points[Team.TEAM_B] ? Team.TEAM_A : Team.TEAM_B;
            }
        } else {
            if ((points[Team.TEAM_A] >= 4 || points[Team.TEAM_B] >= 4) && Math.abs(points[Team.TEAM_A] - points[Team.TEAM_B]) >= 2) {
                gameOver = true;
                gameWinner = points[Team.TEAM_A] > points[Team.TEAM_B] ? Team.TEAM_A : Team.TEAM_B;
            }
        }
    }

    @Override
    public boolean isOver() {
        return gameOver;
    }

    @Override
    public int getWinner() {
        return gameWinner;
    }

    public boolean isDeuceState() {
        return !isTiebreak && points[Team.TEAM_A] >= 3 && points[Team.TEAM_B] >= 3 && points[Team.TEAM_A] == points[Team.TEAM_B];
    }

    public int getAdvantageTeam() {
        if (!isTiebreak && points[Team.TEAM_A] >= 3 && points[Team.TEAM_B] >= 3 && Math.abs(points[Team.TEAM_A] - points[Team.TEAM_B]) == 1) {
            return points[Team.TEAM_A] > points[Team.TEAM_B] ? 1 : 2;
        }
        return 0;
    }   

    public String getPointDisplay(int team) {
        if (isTiebreak) {  
            return String.valueOf(points[team]);
        }
        if (isDeuceState()) {
            return "40";
        }
        int adv = getAdvantageTeam();
        if (adv != 0) {
            return adv == team ? "Adv" : "40";
        }
        switch (points[team]) {
            case 0: return "0";
            case 1: return "15";
            case 2: return "30";
            case 3: return "40";
            default: return String.valueOf(points[team]);
        }
    }

    public int getRawPoint(int team) {
        return points[team];
    }

    @Override
    public String dispScoreBoard() {
        StringBuilder sb = new StringBuilder("[현재 게임 포인트]  ");
        if (isTiebreak) {
            sb.append(String.format("%d - %d  (Tie-break)%n", points[Team.TEAM_A], points[Team.TEAM_B]));
        } else if (isDeuceState()) {
            sb.append("Deuce\n");
        } else if (getAdvantageTeam() != 0) {
            sb.append(getAdvantageTeam() == Team.TEAM_A ? "Adv - 40\n" : "40 - Adv\n");
        } else {
            sb.append(String.format("%s - %s%n", getPointDisplay(Team.TEAM_A), getPointDisplay(Team.TEAM_B)));
        }
        return sb.toString();
    }

    @Override
    public String display() {
        return dispScoreBoard();
    }
}
