package tennis_game;

public class TennisApp {
    public static void main(String[] args) {
        GameInput input = new ConsoleInput();
        GameOutput output = new ConsoleOutput();
        TennisManager manager = new TennisManager(input, output);
        manager.run();
    }
}
