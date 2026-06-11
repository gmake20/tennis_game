package tennis_game;

import java.util.List;

public interface RecordStorage {
    
    public void saveMatch(Match match);
    public List<String[]> findByPlayer(String name);
    public List<String> recordedPlayers();
    
}
