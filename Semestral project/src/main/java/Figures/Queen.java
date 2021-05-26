package Figures;

import Client.Field;
import Client.Move;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Figure {

    public Queen(figureColor color, Cell position){
        if(color == figureColor.BLACK)
            super.image = "bq.png";
        else
            super.image = "wq.png";
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
        for (int d = 1; d <= 7; d++) {
            Cell c = Cell.fromInt(position.getX(), position.getY() + d);
            if (checkMove(field, result, c)) break;
        }
        for (int d = 1; d <= 7; d++) {
            Cell c = Cell.fromInt(position.getX(), position.getY() - d);
            if (checkMove(field, result, c)) break;
        }
        for (int d = 1; d <= 7; d++) {
            Cell c = Cell.fromInt(position.getX() - d, position.getY());
            if (checkMove(field, result, c)) break;
        }
        for (int d = 1; d <= 7; d++) {
            Cell c = Cell.fromInt(position.getX() + d, position.getY());
            if (checkMove(field, result, c)) break;
        }
        return result;
    }

}
