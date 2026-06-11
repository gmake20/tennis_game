package tennis_game;

public class SetScore implements Scorable {
    private int[] games = {0, 0};
    private GameScore currentGame;
    private boolean setOver;
    private int setWinner;
    private int tiebreakWinnerScore;
    private boolean wasTiebreak;

    public SetScore() {
        currentGame = new GameScore(false);
    }

    @Override
    public void pointWinner(int p) {
        if (setOver) return;
        currentGame.pointWinner(p);
        if (currentGame.isOver()) {
            games[currentGame.getWinner() - 1]++;
            checkSetWin();
            if (!setOver) {
                nextGame();
            }
        }
    }

    private void checkSetWin() {
        int g0 = games[0], g1 = games[1];
        if ((g0 >= 6 || g1 >= 6) && Math.abs(g0 - g1) >= 2) {
            setOver = true;
            setWinner = g0 > g1 ? 1 : 2;
        } else if (g0 == 7 && g1 == 6) {
            setOver = true;
            setWinner = 1;
        } else if (g1 == 7 && g0 == 6) {
            setOver = true;
            setWinner = 2;
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

    public void setTiebreakWinnerScore(int score) {
        tiebreakWinnerScore = score;
    }

    public int getTiebreakWinnerScore() {
        return tiebreakWinnerScore;
    }
}
