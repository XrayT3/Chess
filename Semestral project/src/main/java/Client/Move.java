package Client;

import Figures.Cell;
import Figures.Figure;

public class Move {
    public Cell begin;
    public Cell end;
    public MoveType type;
    public Figure figure1, figure2;

    public Move(Cell begin, Cell end) {
        this.begin = begin;
        this.end = end;
    }

    public Move(Cell begin, Cell end, MoveType type, Figure figure1, Figure figure2){
        this.begin = begin;
        this.end = end;
        this.type = type;
        this.figure1 = figure1;
        this.figure2 = figure2;
    }
}
