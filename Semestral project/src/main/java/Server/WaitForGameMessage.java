package Server;

public class WaitForGameMessage extends Message {
    public String playerName;

    public WaitForGameMessage() {
    }

    public WaitForGameMessage(String s) {
        playerName = s.substring(1);
    }

    @Override
    public String msgToBuffer() {
        return "w" + playerName;
    }
}
