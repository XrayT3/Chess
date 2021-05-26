package Figures;

import Client.Field;
import Client.Move;

import java.util.ArrayList;
import java.util.List;

public class King extends Figure {

    public King(figureColor color, Cell position){
        if(color == figureColor.BLACK)
            super.image = "bk.png";
        else
            super.image = "wk.png";
        startPosition = position;
        super.position = position;
        super.color = color;
    }

    private static final int[][] kingMoves = new int[][]{{0, -1}, {1,-1},{1, 0}, {1,1},{0, 1}, {-1,1},{-1, 0}, {-1,-1}};

    @Override
    public List<Move> getValidMoves(Field field) {
        if (slain)
            return new ArrayList<>();
        List<Move> result = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            Cell c = Cell.fromInt(position.getX() + kingMoves[i][0], position.getY() + kingMoves[i][1]);
            checkMove(field, result, c);
        }
        if (!moved) {
            for (Figure f : field.figures)
            {
                if (f.color == color && f instanceof Rook && !f.moved)
                {
                    Move m = generateRoqueMove(field, this, (Rook)f);
                    if (m != null)
                        result.add(m);
                }
            }
        }
        return result;
    }

}
