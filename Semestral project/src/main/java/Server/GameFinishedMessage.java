package Server;

public class GameFinishedMessage extends Message {
    public int gameId;
    public int player;
    public boolean result;

    public GameFinishedMessage() { }

    GameFinishedMessage(String s) {

        gameId = Integer.parseInt(s.substring(1,11));
        player = Integer.parseInt(s.substring(11,21));
        result = s.charAt(21) == '1';
    }

    @Override
    public String msgToBuffer() {
        return "f" + String.format("%010d", gameId) + String.format("%010d", player) +
                String.valueOf(result ? 1 : 0);
    }
}
