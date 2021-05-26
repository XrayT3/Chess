package Client;

import Figures.*;
import Server.GameFinishedMessage;
import Server.Message;
import Server.StartGameMessage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.geometry.Pos;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.application.Application;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Game extends Application {

    public static int cell_size = 56;
    private static final int text_size = cell_size/2;
    private Field logicField = new Field();
    private boolean startGame = false;
    private boolean versusComp = false;
    private boolean gameOver = false;
    private boolean onlineGame = false;
    private final AI computer = new AI(figureColor.BLACK);
    private static final Logger LOG = Logger.getLogger(Game.class.getName());
    private Text text = new Text();
    private final Button undobtn = new Button("Undo last move");
    private figureColor myColor = figureColor.NULL;
    private TCP tcp = null;

    /**
     * Make cell rectangle
     **/
    private Rectangle getCell(int x, int y) {
        Rectangle cell = new Rectangle();
        cell.setX(x * cell_size);
        cell.setY(y * cell_size);
        cell.setHeight(cell_size);
        cell.setWidth(cell_size);
        if ((x + y) % 2 == 0) {
            cell.setFill(Color.WHITE);
        } else {
            cell.setFill(Color.GRAY);
        }
        cell.setStroke(Color.BLACK);
        return cell;
    }

    /**
     * @return cell at this coordinate
     **/
    private Cell getCoordinate(int x, int y){
        x /= cell_size;
        y /= cell_size;
        return Cell.fromInt(x,y);
    }

    /**
     * Check if this is the end of game
     **/
    private boolean isOver(){
        figureColor color = logicField.lastMove == figureColor.BLACK ? figureColor.WHITE : figureColor.BLACK;
        AI comp = new AI(color);
        Move move = comp.findMove(logicField);
        return move == null;
    }

    /**
     * If your color is black, gets the first move from the opponent
     **/
    private void firstMove(Pane chessboard){
        Move move = tcp.getMove();
        move = logicField.getMove(move.begin, move.end);
        System.out.println("Move: " + move.begin.toString() + "->" + move.end.toString());
        //logic move
        logicField.makeMove(move);
        //draw move
        redrawField(chessboard);
    }

    /**
     * End of the game. Send last message.
     **/
    private void overOnlineGame(){
        GameFinishedMessage message = new GameFinishedMessage();
        message.gameId = tcp.gameId;
        message.player = tcp.player;
        message.result = myColor == logicField.lastMove;
        tcp.sendPackage(message);
    }

    /**
     * Draw figure at the chessboard
     * @param figure we want to draw
     * @param chessboard on which we want to draw figure
     **/
    private void putFigure(Figure figure, Pane chessboard){
        Rectangle fig = new Rectangle();
        figure.img = fig;
        //make a new figure
        fig.setX(figure.getPosition().getX() * cell_size);
        fig.setY(figure.getPosition().getY() * cell_size);
        fig.setHeight(cell_size);
        fig.setWidth(cell_size);
        String image = figure.getImage();
        Image img = new Image(getClass().getClassLoader().getResource(image).toString());
        fig.setFill(new ImagePattern(img));
        fig.setOnMouseDragged(
                event -> {
                    if (gameOver)
                        return;
                    if (startGame && figure.color == logicField.lastMove)
                        return;
                    if (startGame && (figure.getPosition() == null))
                        return;
                    if (event.getX() < 0 || event.getY() < 0)
                        return;
                    if (startGame && (event.getX() >= cell_size*8))
                        return;
                    if (event.getX() >= cell_size*18 || event.getY() >= cell_size*8)
                        return;
                    fig.setX(event.getX() - cell_size / 2);
                    fig.setY(event.getY() - cell_size / 2);
                }
        );
        Rectangle copyFig = new Rectangle();
        fig.setOnMousePressed(
                event -> {
                    copyFig.setX(event.getX());
                    copyFig.setY(event.getY());
                }
        );
        fig.setOnMouseReleased(
                event -> {
                    Cell begin = getCoordinate((int)copyFig.getX(), (int)copyFig.getY());
                    Cell end = getCoordinate((int)event.getX(), (int)event.getY());
                    Move move = null;
                    if (begin != null && end != null){
                        move = logicField.getMove(begin, end);
                    }
                    if(startGame) {
                        if(logicField.isValidMove(move, false) && !gameOver){
                            //player's move
                            logicField.makeMove(move);
                            if (move != null && move.type == MoveType.PROMOTION)
                            {
                                String opts[] = { "Bishop", "Knight", "Queen", "Rook"};

                                // create a choice dialog
                                ChoiceDialog d = new ChoiceDialog(opts[0], opts);
                                d.showAndWait();
                                System.out.println(d.getResult());
                                String res = (String)d.getResult();
                                if (res == null || res.equals("Queen")) {
                                    ((Pawn)move.figure1).promote(new Queen(move.figure1.color, move.figure1.getPosition()));
                                }
                                else if (res.equals("Bishop")) {
                                    ((Pawn)move.figure1).promote(new Bishop(move.figure1.color, move.figure1.getPosition()));

                                }
                                else if (res.equals("Knight")) {
                                    ((Pawn)move.figure1).promote(new Knight(move.figure1.color, move.figure1.getPosition()));

                                }
                                else if (res.equals("Rook")) {
                                    ((Pawn)move.figure1).promote(new Rook(move.figure1.color, move.figure1.getPosition()));

                                }
                            }
                            if(logicField.isCheck(figureColor.WHITE)){
                                System.out.println("White check");
                            }
                            if(logicField.isCheck(figureColor.BLACK)){
                                System.out.println("Black check");
                            }
                            if(onlineGame){
                                tcp.sendMove(begin, end);
                                if(isOver()){
                                    gameOver = true;
                                    overOnlineGame();
                                } else{
                                    move = tcp.getMove();
                                    move = logicField.getMove(move.begin, move.end);
                                    logicField.makeMove(move);
                                    if (move.type == MoveType.PROMOTION)
                                    {
                                        ((Pawn)move.figure1).promote(new Queen(move.figure1.color, move.figure1.getPosition()));
                                    }
                                }
                            }
                            //computer's move
                            if(versusComp){
                                move = computer.findMove(logicField);
                                if(move != null){
                                    logicField.makeMove(move);
                                    if(logicField.isCheck(figureColor.WHITE)){
                                        System.out.println("White check");
                                    }
                                    if(logicField.isCheck(figureColor.BLACK)){
                                        System.out.println("Black check");
                                    }
                                    if (move.type == MoveType.PROMOTION)
                                    {
                                        ((Pawn)move.figure1).promote(new Queen(move.figure1.color, move.figure1.getPosition()));
                                    }
                                } else {
                                    gameOver = true;
                                    LOG.log(Level.WARNING, "AI did not find a move");
                                }
                            }
                            if(isOver() && !gameOver){
                                gameOver = true;
                                if(onlineGame){
                                    overOnlineGame();
                                }
                            }
                        }
                        if(!gameOver) {
                            text.setText(logicField.lastMove == figureColor.BLACK ? "Whites move" : "Blacks move");
                            if (logicField.isCheck(logicField.lastMove.flip()))
                                text.setText(text.getText() + ". Check!");
                        } else {
                            if (versusComp)
                                text.setText("AI gave up. You win!");
                            else
                                text.setText(logicField.lastMove == figureColor.BLACK ? "Blacks are victorious" : "Whites are victorious");
                        }
                    } else { //put the figure in a new place
                        if (event.getX() >= cell_size*8 || event.getY() >= cell_size*8) {
                            figure.setPosition(null, logicField);
                        }
                        if (logicField.cellsToFigures.get(end) != null)
                            logicField.cellsToFigures.get(end).setPosition(null, logicField);
                        figure.setPosition(end, logicField);
                    }
                    redrawField(chessboard);
                    undobtn.setDisable(onlineGame || !startGame || logicField.getLastMove() == null);
                }
        );
        chessboard.getChildren().add(fig);
    }

    /**
     * Draws a chessboard
     * @return grid
     **/
    public Pane drawGrid(){
        Pane grid = new Pane();
        String alphabet = "abcdefgh";
        for(int i = 0; i < 8; i++){
            //draw cells
            for(int j = 0; j < 8; j++) {
                grid.getChildren().add(
                        getCell(j, i)
                );
            }
            //draw numbers
            Text digit = new Text("" + (8-i));
            digit.setX(cell_size * 8 + text_size/2);
            digit.setY(cell_size * i + text_size + 5);
            digit.setFont(Font.font("verdana",text_size));
            grid.getChildren().add(digit);
        }
        //draw letters
        for(int i = 0; i < 8; i++) {
            Text letter = new Text("" + alphabet.charAt(i));
            letter.setX(i * cell_size + cell_size/4);
            letter.setY(8 * cell_size + text_size);
            letter.setFont(Font.font("verdana",text_size));
            grid.getChildren().add(letter);
        }
        return grid;
    }

    public void redrawField(Pane chessboard) {
        for (Figure f : logicField.figures) {
            if (f.getPosition() == null)
            {
                f.img.setX((f.startPosition.getX() + 10) * Game.cell_size );
                f.img.setY(f.startPosition.getY() * Game.cell_size );
                continue;
            }
            f.img.setX(f.getPosition().getX() * Game.cell_size );
            f.img.setY(f.getPosition().getY() * Game.cell_size );
        }
    }

    /**
     * Draws a new chess board for new game
     **/
    public void drawFigures(Pane chessboard){
        //draw figures
        for (Figure f : logicField.figures) {
            if (f.getPosition() != null){
                putFigure(f, chessboard);
            }
        }
    }

    /**
     * Resets the chess board for a new game
     **/
    private void newGame(final BorderPane main_win){
        gameOver = false;
        logicField = new Field();
        Pane chessboard = drawGrid();
        drawFigures(chessboard);
        main_win.setCenter(chessboard);
        HBox timers = makeTimer();
        main_win.setBottom(timers);
    }

    private HBox makeTimer(){
        Timer timer = new Timer();
        timer.timerWhite.setFont(Font.font("verdana",text_size));
        timer.timerBlack.setFont(Font.font("verdana",text_size));
        HBox timers = new HBox();
        timers.setAlignment(Pos.CENTER);
        timers.setPrefHeight(100);
        timers.setSpacing(100);
        timers.getChildren().addAll(timer.timerBlack, timer.timerWhite);
        return timers;
    }

    public void openGameWindow(Stage stage, String mode){
        if(mode.equals("Online")){
            versusComp = false;
            onlineGame = true;
            System.out.println("Online game");
        }
        if(mode.equals("Player")){
            onlineGame = false;
            versusComp = false;
            System.out.println("Game versus player");
        }
        if(mode.equals("AI")){
            onlineGame = false;
            versusComp = true;
            System.out.println("Game versus AI");
        }
        BorderPane main_win = new BorderPane();
        text.setText("Put figures to desired positions and press start");
        startGame = false;
//        logicField.isGameStarted = false;
        gameOver = false;
        newGame(main_win);
        Button btnNewGame = new Button("New game");
        Button btnStart = new Button("Start");
        btnStart.setOnMouseClicked(
                event -> {
                    text.setText("Whites move");
                    gameOver = false;
                    startGame = true;
                    HBox timers = makeTimer();
                    main_win.setBottom(timers);
//                    logicField.isGameStarted = true;
                }
        );
        Button btnExit = new Button("Exit");
        btnExit.setOnMouseClicked(
                event -> {
                    if(tcp != null){
                        tcp.disconnection();
                    }
                    start(stage);
                }
        );
        //start game
        Pane chessboard = drawGrid();
        drawFigures(chessboard);

        FlowPane start = new FlowPane();
        HBox gap = new HBox();
        Text textGap = new Text();
        gap.setPrefHeight(60);
        gap.getChildren().add(textGap);
        start.setHgap(20);
        start.setAlignment(Pos.TOP_CENTER);
        undobtn.setAlignment(Pos.BOTTOM_LEFT);
        undobtn.setDisable(true);
        undobtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                logicField.undoMove();
                if (versusComp)
                    logicField.undoMove();
                redrawField(chessboard);
                undobtn.setDisable(!startGame || logicField.getLastMove() == null);
            }
        });
        TextField textField = new TextField();
        textField.setPrefColumnCount(11);
        Button click = new Button("Click");
        click.setOnAction(
                event -> {
//                    logicField.lastMove = figureColor.BLACK;
                    // TCP
                    if(tcp == null)
                        tcp = new TCP();
                    System.out.println("tcp connect");
                    tcp.sendGameRequest(textField.getText());
                    StartGameMessage startMsg = (StartGameMessage) tcp.getPackage();
                    myColor = startMsg.color;
                    tcp.player = startMsg.player;
                    tcp.gameId = startMsg.gameId;
                    System.out.println("my color: " + myColor);
                    gameOver = false;
                    startGame = true;
                    // make timer
                    HBox timers = makeTimer();
                    main_win.setBottom(timers);

                    if(myColor == figureColor.BLACK)
                        firstMove(chessboard);
                }
        );
        if(onlineGame){
            text.setText("Insert your nick");
            start.getChildren().addAll(gap, text, textField, click, btnNewGame, btnExit);
        }else {
            text.setText("Put figures to desired positions and press start");
            start.getChildren().addAll(gap, text, btnNewGame, btnStart, btnExit, undobtn);
        }

        main_win.setTop(start);
        main_win.setCenter(chessboard);
        btnNewGame.setOnMouseClicked(
                event -> {
                    if(!onlineGame)
                        text.setText("Put figures to desired positions and press start");
                    startGame = false;
//                    logicField.isGameStarted = false;
                    gameOver = true;
                    newGame(main_win);
                }
        );

        Scene scene = new Scene(main_win);
        stage.setScene(scene);
        stage.setTitle("Chess");
        stage.setWidth(cell_size*18);
        stage.setHeight(cell_size*12);
        stage.show();
    }

    public void start(Stage stage){
        Text text = new Text("Choose mode");
        Button btnVsAI = new Button("Versus computer");
        btnVsAI.setOnMouseClicked(
                event -> { openGameWindow(stage, "AI"); }
        );
        Button btnVsPlayer = new Button("Versus player");
        btnVsPlayer.setOnMouseClicked(
                event -> { openGameWindow(stage, "Player"); }
        );
        Button btnOnline = new Button("Online");
        btnOnline.setOnMouseClicked(
                event -> { openGameWindow(stage, "Online"); }
        );

        VBox start = new VBox(30, text, btnOnline, btnVsPlayer, btnVsAI);
        start.setAlignment(Pos.CENTER);

        BorderPane main_win = new BorderPane();
        main_win.setCenter(start);

        Scene scene = new Scene(main_win);
        stage.setScene(scene);
        stage.setTitle("Chess");
        stage.setWidth(300);
        stage.setHeight(300);
        stage.show();
    }

    private class Timer {
        Text timerWhite = new Text();
        Text timerBlack = new Text();
        int count = 3600;// 1800 - 30 min
        /**
         * Makes Timer
         * */
        Timer(){
            final Object sync = new Object();

            Thread TimerWhite = new Thread(new Runnable() {
                @Override
                public void run() {
                    long startTime;
                    long elapsedSeconds = 0;
                    long elapsedTime = 0;
                    while (elapsedSeconds != count && !gameOver && startGame) {
                        synchronized (sync){
                            if(logicField.lastMove == figureColor.WHITE){
                                LOG.fine("timerWhite: notifyALL");
                                sync.notifyAll();
                                try {
                                    LOG.fine("timerWhite is waiting");
                                    sync.wait();

                                } catch (InterruptedException e) {
                                    LOG.log(Level.SEVERE, "Eror messege", e);
                                }
                            }
                        }
                        startTime = System.currentTimeMillis();
                        try {
                            LOG.fine("Sleep for a second for timer");
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            LOG.log(Level.SEVERE, "Eror messege", e);
                        }

                        elapsedTime += System.currentTimeMillis() - startTime;

                        elapsedSeconds = elapsedTime / 1000;
                        long secondsDisplay = elapsedSeconds % 60;
                        long elapsedMinutes = elapsedSeconds / 60;

                        Timer.this.timerWhite.setText("White " + elapsedMinutes + ":" + secondsDisplay);
                    }
//                    LOG.info("End of timer...");
                    // you lose
                }
            });
            Thread TimerBlack = new Thread(new Runnable() {
                @Override
                public void run() {
                    long startTime;
                    long Seconds = 0;
                    long Time = 0;
                    while (Seconds != count && !gameOver && startGame) {
                        synchronized (sync){
                            if(logicField.lastMove == figureColor.BLACK){
                                LOG.fine("timerBlack: notifyALL");
                                sync.notifyAll();
                                try {
                                    LOG.fine("timerBlack is waiting");
                                    sync.wait();
                                } catch (InterruptedException e) {
                                    LOG.log(Level.SEVERE, "Eror messege", e);
                                }
                            }
                        }
                        startTime = System.currentTimeMillis();
                        try {
                            LOG.fine("Sleep for a second for timer");
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            LOG.log(Level.SEVERE, "Eror messege", e);
                        }
                        Time += System.currentTimeMillis() - startTime;

                        Seconds = Time / 1000;
                        long secondsDisplay = Seconds % 60;
                        long elapsedMinutes = Seconds / 60;

                        Timer.this.timerBlack.setText("Black " + elapsedMinutes + ":" + secondsDisplay);
                    }
//                    LOG.info("End of timer...");
                }
            });
            TimerWhite.start();
            TimerBlack.start();
        }
    }

}
