package Figures;

import Client.Field;
import Client.Move;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Figure {

    public Knight(figureColor color, Cell position){
        if(color == figureColor.BLACK)
            super.image = "bn.png";
        else
            super.image = "wn.png";
        startPosition = position;
        super.position = position;
        super.color = color;
    }

    public static final int[][] knightMoves = new int[][]{{-2, -1}, {-1,-2},{2, 1}, {1,2},{-2, 1}, {1,-2},{2, -1}, {-1,2}};

    @Override
    public List<Move> getValidMoves(Field field) {
        if (slain)
            return new ArrayList<>();
        List<Move> result = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            Cell c = Cell.fromInt(position.getX() + knightMoves[i][0], position.getY() + knightMoves[i][1]);
            checkMove(field, result, c);
        }
        return result;
    }

}
