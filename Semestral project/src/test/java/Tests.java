
import Client.AI;
import Client.Field;
import Client.Move;
import Figures.Cell;
import Figures.Figure;
import Figures.figureColor;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

public class Tests {

    /**
     * Checking the "isCheck" function
     * */
    @Test
    public void testIsCheck() {
        Field field = new Field();
        field.makeMove(field.getMove(Cell.E2, Cell.E4));
        field.makeMove(field.getMove(Cell.F7, Cell.F5));
        field.makeMove(field.getMove(Cell.F2, Cell.F4));
        field.makeMove(field.getMove(Cell.D1, Cell.H5));
        assertTrue(field.isCheck(figureColor.BLACK));
    }

    /**
     * Checking the "makeMove" function
     * */
    @Test
    public void testMakeMove() {
        Field field = new Field();
        Figure pawn = field.cellsToFigures.get(Cell.A2);
        Move move = field.getMove(Cell.A2, Cell.A4);

        field.makeMove(move);

        assertSame(field.cellsToFigures.get(Cell.A4), pawn);
        assertEquals(0, move.end.getX());
        assertEquals(4, move.end.getY());
        assertEquals(0, move.begin.getX());
        assertEquals(6, move.begin.getY());
    }

    /**
     * Checking the basic rules of the game: first move, cut your own, pawn moves
     * */
    @Test
    public void testIsValidMove() {
        Field field = new Field();

        Move move1 = field.getMove(Cell.F7, Cell.F6);
        Move move2 = field.getMove(Cell.A1, Cell.A2);
        Move move3 = field.getMove(Cell.A2, Cell.A4);
        Move move4 = field.getMove(Cell.G1, Cell.F3);

        assertFalse(field.isValidMove(move1, false));
        assertFalse(field.isValidMove(move2, false));
        assertTrue(field.isValidMove(move3, false));
        assertTrue(field.isValidMove(move4, false));
    }

    /**
     * AI check for correct moves
     * */
    @Test
    public void testAI() {
        Field field = new Field();
        AI comp = new AI(figureColor.BLACK);

        Move move1 = field.getMove(Cell.E2, Cell.E4);
        field.makeMove(move1);
        Move move2 = comp.findMove(field);
        assertTrue(field.isValidMove(move2, false));

        field.makeMove(move2);
        Move move3 = field.getMove(Cell.D1, Cell.H5);
        field.makeMove(move3);
        Move move4 = comp.findMove(field);
        assertTrue(field.isValidMove(move4, false));
    }

}
