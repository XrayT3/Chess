package Server;

import Client.Move;
import Figures.Cell;

public class MoveMessage extends Message{
//    Move move;
    public Cell begin;
    public Cell end;
    public int player;
    public int gameId;

    public MoveMessage(){}

    public MoveMessage(String s) {

        gameId = Integer.parseInt(s.substring(1,11));
        player = Integer.parseInt(s.substring(11,21));
        begin = Cell.fromInt(Integer.parseInt(s.substring(21,22)), Integer.parseInt(s.substring(22,23)));
        end = Cell.fromInt(Integer.parseInt(s.substring(23,24)), Integer.parseInt(s.substring(24,25)));
//        move = new Move(Cell.fromInt(Integer.parseInt(s.substring(21,22)), Integer.parseInt(s.substring(22,23))),
//                Cell.fromInt(Integer.parseInt(s.substring(23,24)), Integer.parseInt(s.substring(24,25))));
    }

    @Override
    public String msgToBuffer() {
        return "m" + String.format("%010d", gameId) + String.format("%010d", player) +
                String.valueOf(begin.getX())  + String.valueOf(begin.getY()) +
                String.valueOf(end.getX()) + String.valueOf(end.getY());
    }
}
