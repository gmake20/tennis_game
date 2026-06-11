package tennis_game;

public class Team {

    public static final int TEAM_A = 0;
    public static final int TEAM_B = 1;

    private Player[] players;
    private String matchType;

    public Team(Player[] players, String matchType) {
        this.players = players;
        this.matchType = matchType;
    }

    public String getDisplayName() {
        if (matchType.equals("복식")) {
            return players[TEAM_A].getName() + " / " + players[TEAM_B].getName();
        }
        return players[TEAM_A].getName();
    }

    public Player[] getPlayers() {
        return players;
    }
}
