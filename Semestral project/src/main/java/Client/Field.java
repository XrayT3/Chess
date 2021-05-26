package Client;

import Figures.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Figures.Knight.knightMoves;
/**
 * Field...
 **/
public class Field {

    public Map<Cell, Figure> cellsToFigures = new HashMap<>();
    figureColor lastMove = figureColor.BLACK;
    private final List<MovePrevStates> movesHistory = new ArrayList<>();
//    public boolean isGameStarted = false;
    private final King whiteKing, blackKing;
    public List<Figure> figures = new ArrayList<>();

    public Move getLastMove() {
        if (movesHistory.size() == 0)
            return null;
        return movesHistory.get(movesHistory.size()-1).move;
    }

    //put the figures in their starting positions
    public Field(){
        //white
        Figure f = new Pawn(figureColor.WHITE, Cell.A2);
        figures.add(f);
        cellsToFigures.put(Cell.A2, f);
        f = new Pawn(figureColor.WHITE, Cell.B2);
        figures.add(f);
        cellsToFigures.put(Cell.B2, f);
        f = new Pawn(figureColor.WHITE, Cell.C2);
        figures.add(f);
        cellsToFigures.put(Cell.C2, f);
        f = new Pawn(figureColor.WHITE, Cell.D2);
        figures.add(f);
        cellsToFigures.put(Cell.D2, f);
        f = new Pawn(figureColor.WHITE, Cell.E2);
        figures.add(f);
        cellsToFigures.put(Cell.E2, f);
        f = new Pawn(figureColor.WHITE, Cell.F2);
        figures.add(f);
        cellsToFigures.put(Cell.F2, f);
        f = new Pawn(figureColor.WHITE, Cell.G2);
        figures.add(f);
        cellsToFigures.put(Cell.G2, f);
        f = new Pawn(figureColor.WHITE, Cell.H2);
        figures.add(f);
        cellsToFigures.put(Cell.H2, f);
        f = new Bishop(figureColor.WHITE, Cell.F1);
        figures.add(f);
        cellsToFigures.put(Cell.F1, f);
        f = new Bishop(figureColor.WHITE, Cell.C1);
        figures.add(f);
        cellsToFigures.put(Cell.C1, f);
        f = new Rook(figureColor.WHITE, Cell.A1);
        figures.add(f);
        cellsToFigures.put(Cell.A1, f);
        f = new Rook(figureColor.WHITE, Cell.H1);
        figures.add(f);
        cellsToFigures.put(Cell.H1, f);
        f = new Queen(figureColor.WHITE, Cell.D1);
        figures.add(f);
        cellsToFigures.put(Cell.D1, f);
        whiteKing = new King(figureColor.WHITE, Cell.E1);
        figures.add(whiteKing);
        cellsToFigures.put(Cell.E1, whiteKing);
        f = new Knight(figureColor.WHITE, Cell.B1);
        figures.add(f);
        cellsToFigures.put(Cell.B1, f);
        f = new Knight(figureColor.WHITE, Cell.G1);
        figures.add(f);
        cellsToFigures.put(Cell.G1, f);
        //black
        f = new Pawn(figureColor.BLACK, Cell.A7);
        figures.add(f);
        cellsToFigures.put(Cell.A7, f);
        f = new Pawn(figureColor.BLACK, Cell.B7);
        figures.add(f);
        cellsToFigures.put(Cell.B7, f);
        f = new Pawn(figureColor.BLACK, Cell.C7);
        figures.add(f);
        cellsToFigures.put(Cell.C7, f);
        f = new Pawn(figureColor.BLACK, Cell.D7);
        figures.add(f);
        cellsToFigures.put(Cell.D7, f);
        f = new Pawn(figureColor.BLACK, Cell.E7);
        figures.add(f);
        cellsToFigures.put(Cell.E7, f);
        f = new Pawn(figureColor.BLACK, Cell.F7);
        figures.add(f);
        cellsToFigures.put(Cell.F7, f);
        f = new Pawn(figureColor.BLACK, Cell.G7);
        figures.add(f);
        cellsToFigures.put(Cell.G7, f);
        f = new Pawn(figureColor.BLACK, Cell.H7);
        figures.add(f);
        cellsToFigures.put(Cell.H7, f);
        f = new Bishop(figureColor.BLACK, Cell.C8);
        figures.add(f);
        cellsToFigures.put(Cell.C8, f);
        f = new Bishop(figureColor.BLACK, Cell.F8);
        figures.add(f);
        cellsToFigures.put(Cell.F8, f);
        f = new Rook(figureColor.BLACK, Cell.A8);
        figures.add(f);
        cellsToFigures.put(Cell.A8, f);
        f = new Rook(figureColor.BLACK, Cell.H8);
        figures.add(f);
        cellsToFigures.put(Cell.H8, f);
        f = new Queen(figureColor.BLACK, Cell.D8);
        figures.add(f);
        cellsToFigures.put(Cell.D8, f);
        blackKing = new King(figureColor.BLACK, Cell.E8);
        figures.add(blackKing);
        cellsToFigures.put(Cell.E8,blackKing);
        f = new Knight(figureColor.BLACK, Cell.B8);
        figures.add(f);
        cellsToFigures.put(Cell.B8, f);
        f = new Knight(figureColor.BLACK, Cell.G8);
        figures.add(f);
        cellsToFigures.put(Cell.G8, f);
    }

    /**
     * Checks if this is the correct move
     * @param move we want to make
     * @return true of false
     **/
    public boolean isValidMove(Move move, boolean allowCheck){
        if (move == null)
            return false;

        if(move.figure1.color == lastMove)
            return false;

        if(move.type != MoveType.ROQUE && move.figure2 instanceof King)
            return false;

        if(!allowCheck && leadsToCheck(move))
            return false;

        return true;
    }

    /**
     * checks if there is a check after this move
     * @param move
     * @return true of false
     **/
    private boolean leadsToCheck(Move move){
        makeMove(move);
        boolean res = isCheck(lastMove);
        undoMove();
        return res;
    }

    /**
     * Makes a move
     * @param move we want to make
     **/
    public void makeMove(Move move){
        if (move == null)
            return;
//        if (!isGameStarted) {
//            if (move.figure1 != null)
//                move.figure1.setPosition(move.end, this);
//            if (move.figure2 != null)
//                move.figure2.setPosition(null, this);
//            return;
//        }
        MovePrevStates mp = new MovePrevStates(move,
                (move.figure1 != null && move.figure1.moved), (move.figure2 != null && move.figure2.moved),
                (move.figure1 != null ? move.figure1.getPosition() : null), (move.figure2 != null ? move.figure2.getPosition() : null),
                (move.figure1 instanceof Pawn && ((Pawn) move.figure1).promoted), (move.figure2 instanceof Pawn && ((Pawn) move.figure2).promoted));
        movesHistory.add(mp);
        move.figure1.moved = true;
        lastMove = move.figure1.color;
        if (move.type == MoveType.ROQUE)
        {
            move.figure1.setPosition(move.begin, this);
            move.figure2.setPosition(move.end, this);
            return;
        }

        move.figure1.setPosition(move.end, this);
        if (move.type == MoveType.MOVEMENT) {


        }
        else if (move.type == MoveType.ATTACK) {
            move.figure2.setPosition(null, this);
        }
        else if (move.type == MoveType.ENPASSANT) {
            move.figure2.setPosition(null, this);
        }
        else if (move.type == MoveType.PROMOTION) {
            if (move.figure2 != null)
                move.figure2.setPosition(null, this);
        }
    }

    public void undoMove() {
        if(movesHistory.size() == 0)
            return;
        MovePrevStates mp = movesHistory.get(movesHistory.size()-1);
        if (mp.move.figure1 != null) {
            mp.move.figure1.setPosition(mp.fig1Pos, this);
            mp.move.figure1.moved = mp.fig1Moved;
            if (mp.move.figure1 instanceof Pawn && ((Pawn) mp.move.figure1).promoted && !mp.fig1Promoted) {
                ((Pawn) mp.move.figure1).demote();
            }
        }
        if (mp.move.figure2 != null) {
            mp.move.figure2.setPosition(mp.fig2Pos, this);
            mp.move.figure2.moved = mp.fig1Moved;
            if (mp.move.figure2 instanceof Pawn && ((Pawn) mp.move.figure2).promoted && !mp.fig2Promoted) {
                ((Pawn) mp.move.figure2).demote();
            }
        }
        lastMove = lastMove.flip();
        movesHistory.remove(movesHistory.size()-1);
    }

    private boolean checkCellForCheck(Cell c, figureColor color, boolean diagonal) {
        if (cellsToFigures.get(c) == null)
            return false;
        if (cellsToFigures.get(c).color == color)
            return false;
        if (diagonal && (cellsToFigures.get(c) instanceof Bishop || (cellsToFigures.get(c) instanceof Pawn && ((Pawn) cellsToFigures.get(c)).promotedto instanceof Bishop)))
            return true;
        if (cellsToFigures.get(c) instanceof Queen || (cellsToFigures.get(c) instanceof Pawn && ((Pawn) cellsToFigures.get(c)).promotedto instanceof Queen))
            return true;
        if (!diagonal && (cellsToFigures.get(c) instanceof Rook || (cellsToFigures.get(c) instanceof Pawn && ((Pawn) cellsToFigures.get(c)).promotedto instanceof Rook)))
            return true;
        if (cellsToFigures.get(c) instanceof Pawn){
            for (Move m: cellsToFigures.get(c).getValidMoves(this)) {
                if (m.end == c)
                    return true;
            }
        }
        return false;
    }

    /**
     * Checks if there is a check
     * @param color of king
     * @return true of false
     **/
    public boolean isCheck(figureColor color){
        King king = (color == figureColor.BLACK) ? blackKing : whiteKing;
        Cell position = king.getPosition();
        if(position == null)
            return false;
        for (int d = 1; d <= 7; d++) {
            Cell c = Cell.fromInt(position.getX() + d, position.getY() + d);
            if (checkCellForCheck(c, color, true))
                return true;
            if (cellsToFigures.get(c) != null)
                break;
        }
        for (int d = 1; d <= 7; d++) {
            Cell c = Cell.fromInt(position.getX() - d, position.getY() + d);
            if (checkCellForCheck(c, color, true))
                return true;
            if (cellsToFigures.get(c) != null)
                break;
        }
        for (int d = 1; d <= 7; d++) {
            Cell c = Cell.fromInt(position.getX() - d, position.getY() - d);
            if (checkCellForCheck(c, color, true))
                return true;
            if (cellsToFigures.get(c) != null)
                break;
        }
        for (int d = 1; d <= 7; d++) {
            Cell c = Cell.fromInt(position.getX() + d, position.getY() - d);
            if (checkCellForCheck(c, color, true))
                return true;
            if (cellsToFigures.get(c) != null)
                break;
        }
        for (int d = 1; d <= 7; d++) {
            Cell c = Cell.fromInt(position.getX(), position.getY() + d);
            if (checkCellForCheck(c, color, false))
                return true;
            if (cellsToFigures.get(c) != null)
                break;
        }
        for (int d = 1; d <= 7; d++) {
            Cell c = Cell.fromInt(position.getX(), position.getY() - d);
            if (checkCellForCheck(c, color, false))
                return true;
            if (cellsToFigures.get(c) != null)
                break;
        }
        for (int d = 1; d <= 7; d++) {
            Cell c = Cell.fromInt(position.getX() - d, position.getY());
            if (checkCellForCheck(c, color, false))
                return true;
            if(cellsToFigures.get(c) != null)
                break;
        }
        for (int d = 1; d <= 7; d++) {
            Cell c = Cell.fromInt(position.getX() + d, position.getY());
            if (checkCellForCheck(c, color, false))
                return true;
            if (cellsToFigures.get(c) != null)
                break;
        }
        for (int i = 0; i < 8; i++) {
            Cell c = Cell.fromInt(position.getX() + knightMoves[i][0], position.getY() + knightMoves[i][1]);
            if (c != null && cellsToFigures.get(c) != null && cellsToFigures.get(c).color != color && cellsToFigures.get(c) instanceof Knight)
                return true;
        }
        return false;
    }

    /**
     * Make object move
     * @param begin and end of move
     * @return move
     **/
    public Move getMove(Cell begin, Cell end){
        Move move = null;
        String log = begin.toString() + "->" + end.toString();
        System.out.println(log);
        Figure f = cellsToFigures.get(begin);
        if (f != null) {
            List<Move> d = f.getValidMoves(this);
            for (Move m : d) {
                if (m.begin == begin && m.end == end) {
                    move = m;
                    break;
                }
                if (cellsToFigures.get(begin) instanceof King && cellsToFigures.get(end) instanceof Rook &&
                        m.type == MoveType.ROQUE &&
                        cellsToFigures.get(begin) == m.figure1 && cellsToFigures.get(end) == m.figure2)
                {
                    move = m;
                    break;
                }
                if (cellsToFigures.get(end) instanceof King && cellsToFigures.get(begin) instanceof Rook &&
                        m.type == MoveType.ROQUE &&
                        cellsToFigures.get(end) == m.figure1 && cellsToFigures.get(begin) == m.figure2)
                {
                    move = m;
                    break;
                }
            }
        }
        return move;
    }
}
