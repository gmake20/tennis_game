package tennis_game;

/*
Match.pointWinner(p)
    └─ SetScore.pointWinner(p)       ← 같은 메서드명으로 위임
            └─ GameScore.pointWinner(p)   ← 같은 메서드명으로 위임
*/

// 점수 계산 대상이 되는 모든 객체(게임/세트/매치)가 구현해야 할 공통 인터페이스
public interface Scorable {
    // 지정된 팀(Team.TEAM_A 또는 Team.TEAM_B)의 포인트 획득 처리
    void pointWinner(int team);

    // 해당 게임이 종료되었는지 여부 반환
    boolean isOver();

    // 승자 팀(1 또는 2) 반환 (게임이 종료되지 않았으면 0 반환)
    int getWinner();
}
