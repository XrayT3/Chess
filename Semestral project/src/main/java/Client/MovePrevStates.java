package Client;

import Figures.Cell;

public class MovePrevStates {
    public Move move;
    public boolean fig1Moved, fig2Moved;
    public Cell fig1Pos, fig2Pos;
    public boolean fig1Promoted, fig2Promoted;

    public MovePrevStates(Move move, boolean fig1Moved, boolean fig2Moved, Cell fig1Pos, Cell fig2Pos, boolean fig1Promoted, boolean fig2Promoted) {
        this.move = move;
        this.fig1Moved = fig1Moved;
        this.fig2Moved = fig2Moved;
        this.fig1Pos = fig1Pos;
        this.fig2Pos = fig2Pos;
        this.fig1Promoted = fig1Promoted;
        this.fig2Promoted = fig2Promoted;
    }
}
