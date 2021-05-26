package Figures;

import Client.Field;
import Client.Move;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Figure {

    public Rook(figureColor color, Cell position){
        if(color == figureColor.BLACK)
            super.image = "br.png";
        else
            super.image = "wr.png";
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
        if (!moved) {
            for (Figure f : field.figures)
            {
                if (f.color == color && f instanceof King && !f.moved)
                {
                    Move m = generateRoqueMove(field, (King)f, this);
                    if (m != null)
                        result.add(m);
                    break;
                }
            }
        }
        return result;
    }
}
