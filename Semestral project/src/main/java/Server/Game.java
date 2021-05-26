package Server;

import Client.Move;
import Figures.figureColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    private final Main main;
    final int id;
    long moveTimer;
    private final List<Move> movesHistory = new ArrayList<>();
    public final int player0, player1;
    private int playerToGo;

    Game(Main main, int pl0, int pl1, int id) {
        this.main = main;
        this.id = id;
        Random rand = new Random();
        boolean first = rand.nextBoolean();
        player0 = first ? pl0 : pl1;
        player1 = first ?  pl1 : pl0;
        playerToGo = first ? pl0 : pl1;
        StartGameMessage msg = new StartGameMessage();
        msg.receiver = player0;
        msg.player = player0;
        msg.color = first ? figureColor.WHITE : figureColor.BLACK;
        msg.gameId = id;
        main.getTcp().sendPackage(msg);
        msg = new StartGameMessage();
        msg.receiver = player1;
        msg.player = player1;
        msg.color = first ? figureColor.BLACK : figureColor.WHITE;
        msg.gameId = id;
        main.getTcp().sendPackage(msg);
        moveTimer = System.currentTimeMillis() / 1000;
    }

    void makeMove(MoveMessage received) {
        movesHistory.add(new Move(received.begin, received.end));
        playerToGo = (received.player == player0) ? player1 : player0;
        received.player = playerToGo;
        received.receiver = playerToGo;
        main.getTcp().sendPackage(received);
        moveTimer = System.currentTimeMillis() / 1000;
    }

    public void timeout() {
        finishGame(playerToGo == player0 ? player1: player0);
    }

    void finishGame(int winner) {
        GameFinishedMessage f = new GameFinishedMessage();
        f.receiver = player0;
        f.gameId = id;
        f.player = player0;
        if (player0 == winner) {
            main.updateWincounts(player0);
        }
        f.result = player0 == winner;
//        main.getTcp().sendPackage(f);
        f = new GameFinishedMessage();
        f.receiver = player1;
        f.gameId = id;
        f.player = player1;
        if (player1 == winner) {
            main.updateWincounts(player1);
        }
        f.result = player1 == winner;
//        main.getTcp().sendPackage(f);
        main.gameFinished(id);
    }
}
