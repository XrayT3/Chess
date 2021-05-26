package Server;

import Figures.figureColor;

public class StartGameMessage extends Message {
    public figureColor color;
    public int player;
    public int gameId;

    StartGameMessage() {
    }

    public StartGameMessage(String s) {

        gameId = Integer.parseInt(s.substring(1,11));
        player = Integer.parseInt(s.substring(11,21));
        color = s.charAt(21) == '1' ? figureColor.WHITE : figureColor.BLACK;
    }

    @Override
    public String msgToBuffer() {
        return "s" + String.format("%010d", gameId) + String.format("%010d", player) +
                (color == figureColor.WHITE ? 1 : 0);
    }
}
