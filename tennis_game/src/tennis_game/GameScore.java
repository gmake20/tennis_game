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
        points[p - 1]++;
        checkWin();
    }

    private void checkWin() {
        if (isTiebreak) {
            if ((points[0] >= 7 || points[1] >= 7) && Math.abs(points[0] - points[1]) >= 2) {
                gameOver = true;
                gameWinner = points[0] > points[1] ? 1 : 2;
            }
        } else {
            if ((points[0] >= 4 || points[1] >= 4) && Math.abs(points[0] - points[1]) >= 2) {
                gameOver = true;
                gameWinner = points[0] > points[1] ? 1 : 2;
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
        return !isTiebreak && points[0] >= 3 && points[1] >= 3 && points[0] == points[1];
    }

    public int getAdvantageTeam() {
        if (!isTiebreak && points[0] >= 3 && points[1] >= 3 && Math.abs(points[0] - points[1]) == 1) {
            return points[0] > points[1] ? 1 : 2;
        }
        return 0;
    }

    public String getPointDisplay(int team) {
        int idx = team - 1;
        if (isTiebreak) {
            return String.valueOf(points[idx]);
        }
        if (isDeuceState()) {
            return "40";
        }
        int adv = getAdvantageTeam();
        if (adv != 0) {
            return adv == team ? "Adv" : "40";
        }
        switch (points[idx]) {
            case 0: return "0";
            case 1: return "15";
            case 2: return "30";
            case 3: return "40";
            default: return String.valueOf(points[idx]);
        }
    }

    public int getRawPoint(int team) {
        return points[team - 1];
    }

    @Override
    public String dispScoreBoard() {
        StringBuilder sb = new StringBuilder("[현재 게임 포인트]  ");
        if (isTiebreak) {
            sb.append(String.format("%d - %d  (Tie-break)%n", points[0], points[1]));
        } else if (isDeuceState()) {
            sb.append("Deuce\n");
        } else if (getAdvantageTeam() != 0) {
            sb.append(getAdvantageTeam() == 1 ? "Adv - 40\n" : "40 - Adv\n");
        } else {
            sb.append(String.format("%s - %s%n", getPointDisplay(1), getPointDisplay(2)));
        }
        return sb.toString();
    }

    @Override
    public String display() {
        return dispScoreBoard();
    }
}
