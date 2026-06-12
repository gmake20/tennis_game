package tennis_game;

// 하나의 세트(게임 단위)를 관리. 게임 수를 누적하고 6-6 시 타이브레이크 진입 결정.
public class SetScore implements Scorable, Displayable {
    private int[] games = { 0, 0 }; // 각 팀의 게임 수 [TEAM_A, TEAM_B]
    private GameScore currentGame; // 현재 진행 중인 게임
    private boolean setOver; // 세트 종료 여부
    private int setWinner; // 세트 승자 (Team.TEAM_A 또는 Team.TEAM_B)
    private boolean wasTiebreak; // 이 세트가 타이브레이크로 끝났는지 여부

    public SetScore() {
        currentGame = new GameScore(false);
    }

    @Override
    public void pointWinner(int team) {

        if (setOver)
            return;
        currentGame.pointWinner(team);
        if (currentGame.isOver()) {
            games[currentGame.getWinner()]++;
            checkSetWin();
            if (!setOver) {
                nextGame();
            }
        }
    }

    private void checkSetWin() {
        int g0 = games[Team.TEAM_A], g1 = games[Team.TEAM_B];
        if ((g0 >= 6 || g1 >= 6) && Math.abs(g0 - g1) >= 2) {
            setOver = true;
            setWinner = g0 > g1 ? Team.TEAM_A : Team.TEAM_B;
        } else if (g0 == 7 && g1 == 6) {
            setOver = true;
            setWinner = Team.TEAM_A;
        } else if (g1 == 7 && g0 == 6) {
            setOver = true;
            setWinner = Team.TEAM_B;
        }
    }

    private void nextGame() {
        checkTiebreak();
    }

    private void checkTiebreak() {
        if (games[0] == 6 && games[1] == 6) {
            wasTiebreak = true;
            currentGame = new GameScore(true);
        } else {
            currentGame = new GameScore(false);
        }
    }

    @Override
    public boolean isOver() {
        return setOver;
    }

    @Override
    public int getWinner() {
        return setWinner;
    }

    public int[] getGames() {
        return games;
    }

    public GameScore getCurrentGame() {
        return currentGame;
    }

    public boolean wasTiebreak() {
        return wasTiebreak;
    }

    @Override
    public String dispScoreBoard() {
        return String.format("[세트 스코어]  %d - %d%n", games[Team.TEAM_A], games[Team.TEAM_B])
                + currentGame.dispScoreBoard();
    }

    @Override
    public String display() {
        return dispScoreBoard();
    }
}
