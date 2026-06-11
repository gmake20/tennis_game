package tennis_game;

public class Team {

    public static final int TEAM_A = 0;
    public static final int TEAM_B = 1;

    private Player[] players;
    private MatchType matchType;

    public Team(Player[] players, MatchType matchType) {
        this.players = players;
        this.matchType = matchType;
    }

    public String getDisplayName() {
        if (matchType == MatchType.DOUBLES) {
            return players[TEAM_A].getName() + " / " + players[TEAM_B].getName();
        }
        return players[TEAM_A].getName();
    }

    public Player[] getPlayers() {
        return players;
    }
}
