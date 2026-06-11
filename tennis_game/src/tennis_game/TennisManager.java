package tennis_game;

import java.util.*;
import java.util.stream.Collectors;

public class TennisManager {
	private final RecordStorage storage;
	private final GameInput input;
	private final GameOutput output;

	private List<Player> playerRoster = new ArrayList<>();
	private List<Match> matchHistory = new ArrayList<>();

	public TennisManager(GameInput input, GameOutput output) {
		storage = new FileRecordStorage();

		this.input = input;
		this.output = output;
		playerRoster.add(new Player("양인석"));
		playerRoster.add(new Player("조지훈"));
		playerRoster.add(new Player("이지훈"));
		playerRoster.add(new Player("안정빈"));
		playerRoster.add(new Player("장미성"));
		playerRoster.add(new Player("류호훈"));
		playerRoster.add(new Player("이시연"));
		playerRoster.add(new Player("신창만"));
		playerRoster.add(new Player("오수빈"));
		playerRoster.add(new Player("문규리"));
	}

	public void run() {
		while (true) {
			output.showMenu();
			int choice = input.readMenuChoice();
			if (choice == 1) {
				startNewMatch();
			} else if (choice == 2) {
				queryPlayerRecord();
			} else if (choice == 3) {
				output.showMessage("프로그램을 종료합니다.");
				break;
			}
		}
	}

	private void startNewMatch() {
		int totalSets = input.readTotalSets();
		String matchType = input.readMatchType();
		Team[] teams = selectTeams(matchType);

		Match match = new Match(totalSets, matchType, teams);
		output.showMessage("\n경기를 시작합니다...");
		match.simulate(input, output);
		output.showScoreBoard(match.dispScoreBoard());

		matchHistory.add(match);
		storage.saveMatch(match);
	}

	private Team[] selectTeams(String matchType) {
		List<String> names = playerRoster.stream()
				.map(Player::getName)
				.collect(Collectors.toList());
		output.showRoster(names);
		Set<Integer> used = new HashSet<>();

		if (matchType.equals("단식")) {
			Player p1 = pickPlayer("선수 1 선택 (번호 입력): ", used);
			Player p2 = pickPlayer("선수 2 선택 (번호 입력): ", used);
			return new Team[]{
					new Team(new Player[]{p1}, matchType),
					new Team(new Player[]{p2}, matchType)
			};
		} else {
			output.showMessage("[팀 A 선수 선택]");
			Player a1 = pickPlayer("팀 A 선수 1 선택 (번호 입력): ", used);
			Player a2 = pickPlayer("팀 A 선수 2 선택 (번호 입력): ", used);
			output.showMessage("[팀 B 선수 선택]");
			Player b1 = pickPlayer("팀 B 선수 1 선택 (번호 입력): ", used);
			Player b2 = pickPlayer("팀 B 선수 2 선택 (번호 입력): ", used);
			return new Team[]{
					new Team(new Player[]{a1, a2}, matchType),
					new Team(new Player[]{b1, b2}, matchType)
			};
		}
	}

	private Player pickPlayer(String prompt, Set<Integer> used) {
		int idx = input.readPlayerIndex(prompt, used);
		used.add(idx);
		return playerRoster.get(idx - 1);
	}

	private void queryPlayerRecord() {
		// TODO : 선수 이름 목록 보여주기
		RecordStorage str = storage;
		List<String> recordedPlayersList = str.recordedPlayers();
		Iterator<String> isPL = recordedPlayersList.iterator();
		int n = 1;
		while (isPL.hasNext()) {
			System.out.print(n + ".\s" + isPL.next() + "\t\t\t");
			if(n%3==0) System.out.println();
			n++;
		}
		System.out.println();

		String name = input.readPlayerName();
		int i = -1;
		try {
			i = Integer.parseInt(name);
		} catch (NumberFormatException e) {

		}
		name = (i != -1 && i<=recordedPlayersList.size()) ? recordedPlayersList.get(i-1) : name;
		List<String[]> records = storage.findByPlayer(name);
		output.showPlayerRecords(name, records);
	}
}
