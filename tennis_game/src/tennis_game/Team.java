package tennis_game;

public class Team {
    private Player[] players;
    private String matchType;

    public Team(Player[] players, String matchType) {
        this.players = players;
        this.matchType = matchType;
    }

    public String getDisplayName() {
        if (matchType.equals("복식")) {
            return players[0].getName() + " / " + players[1].getName();
        }
        return players[0].getName();
    }

    public Player[] getPlayers() {
        return players;
    }
}
