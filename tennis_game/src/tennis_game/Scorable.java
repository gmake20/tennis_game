package tennis_game;

public interface Scorable {
    void pointWinner(int p);
    boolean isOver();
    int getWinner();
}
