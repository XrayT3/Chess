package Figures;

public enum figureColor{
    WHITE, BLACK, NULL;

    /**
     * Function to change the color during the game
     * @return Color, if current color is black, FlipColor changes it white and vice versa
     * */
    public figureColor flip() {
        if (this == figureColor.BLACK) return figureColor.WHITE;
        if (this == figureColor.WHITE) return figureColor.BLACK;
        return figureColor.NULL;
    }


}
