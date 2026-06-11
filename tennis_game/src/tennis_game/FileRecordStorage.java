package tennis_game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FileRecordStorage implements RecordStorage {
    private static final String RESULTS_FILE = "results.txt";

    public void saveMatch(Match match) {
        try (FileWriter fw = new FileWriter(RESULTS_FILE, true)) {
            fw.write(match.toRecord());
        } catch (IOException e) {
            System.out.println("파일 저장 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // 선수 이름이 포함된 경기 기록을 파일에서 읽어 반환
    // 반환값: [날짜, 방식, 상대팀, 스코어, 결과(승리/패배)]
    public List<String[]> findByPlayer(String name) {
        List<String[]> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(RESULTS_FILE))) {
            String line;
            Map<String, String> block = new LinkedHashMap<>();
            while ((line = br.readLine()) != null) {
                if (line.equals("---")) {
                    String team1 = block.getOrDefault("TEAM1", "");
                    String team2 = block.getOrDefault("TEAM2", "");
                    if (team1.contains(name) || team2.contains(name)) {
                        String date     = block.getOrDefault("DATE", "");
                        String type     = block.getOrDefault("TYPE", "");
                        String sets     = block.getOrDefault("SETS", "");
                        String score    = block.getOrDefault("SCORE", "");
                        String winner   = block.getOrDefault("WINNER", "");
                        String opponent = team1.contains(name) ? team2 : team1;
                        String result   = winner.contains(name) ? "승리" : "패배";
                        records.add(new String[]{date, sets + "세트 " + type, opponent, score, result});
                    }
                    block.clear();
                } else if (line.contains("=")) {
                    String[] kv = line.split("=", 2);
                    block.put(kv[0], kv[1]);
                }
            }
        } catch (IOException ignored) {
            // 파일 없으면 빈 목록 반환
        }
        return records;
    }
    
    //경기에 참여했던 선수들만 반환.
    public List<String> recordedPlayers() {
    	List<String> playerList = new ArrayList<String>();
    	 try (BufferedReader br = new BufferedReader(new FileReader(RESULTS_FILE))) {
             String line;
             Map<String, String> block = new LinkedHashMap<>();
             while ((line = br.readLine()) != null) {
                 if (line.equals("---")) {
                     String[] team1 = block.getOrDefault("TEAM1", "").split("\s/\s");
                     String[] team2 = block.getOrDefault("TEAM2", "").split("\s/\s");
                     int index =0;
                     while(index<team1.length) {
                    	  playerList.add(team1[index]);
                    	  playerList.add(team2[index]);
                    	  index++;
                     }
                     block.clear();
                 } else if (line.contains("=")) {
                     String[] kv = line.split("=", 2);
                     block.put(kv[0], kv[1]);
                 }
             }
             playerList = playerList.stream().distinct().toList();
         } catch (IOException ignored) {
             // 파일 없으면 빈 목록 반환
         }
    	return playerList;
    }
}
