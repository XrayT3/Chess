package Figures;

import Client.Field;
import Client.Move;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Figure {

    public Bishop(figureColor color, Cell position) {
        if (color == figureColor.BLACK)
            super.image = "bb.png";
        else
            super.image = "wb.png";
        startPosition = position;
        super.position = position;
        super.color = color;
    }

    @Override
    public List<Move> getValidMoves(Field field) {
        if (slain)
            return new ArrayList<>();
        List<Move> result = new ArrayList<>();
        for (int d = 1; d <= 7; d++) {
            Cell c = Cell.fromInt(position.getX() + d, position.getY() + d);
            if (checkMove(field, result, c)) break;
        }
        for (int d = 1; d <= 7; d++) {
            Cell c = Cell.fromInt(position.getX() - d, position.getY() + d);
            if (checkMove(field, result, c)) break;
        }
        for (int d = 1; d <= 7; d++) {
            Cell c = Cell.fromInt(position.getX() - d, position.getY() - d);
            if (checkMove(field, result, c)) break;
        }
        for (int d = 1; d <= 7; d++) {
            Cell c = Cell.fromInt(position.getX() + d, position.getY() - d);
            if (checkMove(field, result, c)) break;
        }
        return result;
    }
}