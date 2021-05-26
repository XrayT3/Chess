package Figures;

import Client.Field;
import Client.Move;
import Client.MoveType;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Figure {
    public boolean promoted = false;
    public Figure promotedto = null;
    public Pawn(figureColor color, Cell position){
        if(color == figureColor.BLACK)
            image = "bp.png";
        else
            image = "wp.png";
        startPosition = position;
        super.position = position;
        super.color = color;
    }

    @Override
    public void setPosition(Cell cell, Field field) {
        super.setPosition(cell, field);
        if (promoted) {
            promotedto.position = position;
        }
    }

    public void promote(Figure to) {
        promoted = true;
        promotedto = to;
        promotedto.startPosition = position;
        image = promotedto.image;
        Image l = new Image(getClass().getClassLoader().getResource(image).toString());
        img.setFill(new ImagePattern(l));
    }

    public void demote() {
        promoted = false;
        promotedto = null;
        if(color == figureColor.BLACK)
            image = "bp.png";
        else
            image = "wp.png";
        Image l = new Image(getClass().getClassLoader().getResource(image).toString());
        img.setFill(new ImagePattern(l));
    }

    @Override
    public List<Move> getValidMoves(Field field) {
        if (slain)
            return new ArrayList<>();
        List<Move> result = new ArrayList<>();
        if (promoted)
        {
            result = promotedto.getValidMoves(field);
            for (Move m : result) {
                m.figure1 = this;
            }
            return result;
        }
        int dir = color == figureColor.WHITE ? -1 : 1;
        if (position.getY() + dir > 7 || position.getY() + dir < 0)
            return result;
        Cell endpos = Cell.fromInt(position.getX(), position.getY() + dir);
        if (endpos != null && field.cellsToFigures.get(endpos) == null)
        {
            MoveType t = ((color == figureColor.WHITE) && (position.getY() + dir == 0)) ||
                    ((color == figureColor.BLACK) && (position.getY() + dir == 7))  ? MoveType.PROMOTION : MoveType.MOVEMENT;
            Move m = new Move(position, endpos, t, this, null);
            result.add(m);
        }
        endpos = Cell.fromInt(position.getX(), position.getY() + 2*dir);
        if (!moved && endpos != null && field.cellsToFigures.get(endpos) == null)
        {
            Move m = new Move(position, endpos, MoveType.MOVEMENT, this, null);
            result.add(m);
        }
        endpos = Cell.fromInt(position.getX() + 1, position.getY() + dir);
        if (endpos != null)
        {
            if (field.cellsToFigures.get(endpos) != null) {
                if (field.cellsToFigures.get(endpos).color != color) {

                    MoveType t = ((color == figureColor.WHITE) && (position.getY() + dir == 0)) ||
                            ((color == figureColor.BLACK) && (position.getY() + dir == 7))  ? MoveType.PROMOTION : MoveType.ATTACK;
                    Move m = new Move(position, endpos, t, this, field.cellsToFigures.get(endpos));
                    result.add(m);
                }
            }
            else
            {
                Move last = field.getLastMove();
                if (last != null && last.figure1 instanceof Pawn &&
                        last.begin == Cell.fromInt(endpos.getX(), endpos.getY() + dir) &&
                        last.end == Cell.fromInt(endpos.getX(), endpos.getY() - dir)) {
                    Move m = new Move(position, endpos, MoveType.ENPASSANT, this, last.figure1);
                    result.add(m);
                }
            }
        }
        endpos = Cell.fromInt(position.getX() - 1, position.getY() + dir);
        if (endpos != null)
        {
            if (field.cellsToFigures.get(endpos) != null) {
                if (field.cellsToFigures.get(endpos).color != color) {

                    MoveType t = ((color == figureColor.WHITE) && (position.getY() + dir == 0)) ||
                            ((color == figureColor.BLACK) && (position.getY() + dir == 7))  ? MoveType.PROMOTION : MoveType.ATTACK;
                    Move m = new Move(position, endpos, t, this, field.cellsToFigures.get(endpos));
                    result.add(m);
                }
            }
            else
            {
                Move last = field.getLastMove();
                if (last != null && last.figure1 instanceof Pawn &&
                        last.begin == Cell.fromInt(endpos.getX(), endpos.getY() + dir) &&
                        last.end == Cell.fromInt(endpos.getX(), endpos.getY() - dir)) {
                    Move m = new Move(position, endpos, MoveType.ENPASSANT, this, last.figure1);
                    result.add(m);
                }
            }
        }
        return result;
    }
}
