package Client;

import Figures.Cell;
import Figures.Figure;
import Figures.figureColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class AI{

    public figureColor color;
    public String nickname;

    public AI(figureColor color){
        this.color = color;
        this.nickname = "AI";
    }
    private static final Logger LOG = Logger.getLogger(AI.class.getName());

    /**
     * Make random move
     * @param field, which contains all shapes
     * @return correct move
     **/
    public Move findMove(Field field){
        Figure figure;
        List<Move> options = new ArrayList<>();
        //choose random figure
        for(Cell cell : Cell.values()){
            figure = field.cellsToFigures.get(cell);
            if(figure == null)
                continue;
            if (figure.color == color){
                for (Move m : figure.getValidMoves(field))
                {
                    if(field.isValidMove(m, false)){
                        options.add(m);
                    }
                }
            }
        }
        if (options.size() > 0)
        {
            Random r = new Random();
            return options.get(r.nextInt(options.size()));
        }
        return null;
    }
}
