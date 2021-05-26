package Figures;

import Client.Field;
import Client.Move;
import Client.MoveType;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class Figure {

    Cell position;
    public Cell startPosition = null;
    String image;
    public boolean moved = false;
    public figureColor color;
    public Rectangle img;
    public boolean slain = false;

    public String getImage(){ return image; }

    public Cell getPosition(){ return position; }

    public void setPosition(Cell cell, Field field) {
        if (field.cellsToFigures.get(position) == this)
            field.cellsToFigures.put(position, null);
        if (cell != null) {
            slain = false;
            field.cellsToFigures.put(cell, this);
        }
        else {
            slain = true;
        }
        position = cell;

    }

    public List<Move> getValidMoves(Field field) {
        return new ArrayList<>();
    }

    boolean checkMove(Field field, List<Move> result, Cell c) {
        if (c != null) {
//            if (!field.isGameStarted)
//            {
//                if (field.cellsToFigures.get(c) == null) {
//                    Move m = new Move(position, c, MoveType.MOVEMENT, this, null);
//                    result.add(m);
//                } else {
//                    Move m = new Move(position, c, MoveType.ATTACK, this, field.cellsToFigures.get(c));
//                    result.add(m);
//                    return true;
//                }
//            }
            if (field.cellsToFigures.get(c) == null) {
                Move m = new Move(position, c, MoveType.MOVEMENT, this, null);
                result.add(m);
            } else if (field.cellsToFigures.get(c).color == this.color)
                return true;
            else {
                Move m = new Move(position, c, MoveType.ATTACK, this, field.cellsToFigures.get(c));
                result.add(m);
                return true;
            }
        } else
            return true;
        return false;
    }

    Move generateRoqueMove(Field field, King king, Rook rook) {
        if (king.position == null || rook.position == null)
            return null;
        int from, to, y = rook.position.getY(), dir;
        if (king.position.getY() - rook.position.getY() != 0)
            return null;
        if (king.position.getX() < rook.position.getX()) {
            from = king.position.getX();
            to = rook.position.getX();
            dir = 1;
        } else {
            from = rook.position.getX();
            to = king.position.getX();
            dir = -1;
        }

        for (int x = from + 1; x < to; x++) {
            Cell c = Cell.fromInt(x, y);
            Figure tmp = field.cellsToFigures.get(c);
            if (tmp != null)
                return null;
        }
        return new Move(Cell.fromInt(king.position.getX() + 2*dir, y), Cell.fromInt(king.position.getX() + dir, y), MoveType.ROQUE, king, rook);
    }
}
