package Server;

import java.util.HashMap;
import java.util.Map;

public class Main {
    private final Map<Integer, Integer> wincounts = new HashMap<>();
    private final Map<Integer, String> players = new HashMap<>();
    private final Map<Integer, Game> games = new HashMap<>();
    private int newGameID = 0;
    private int waitingPlayer = -1;
    private final TCP tcp = new TCP();

    public static void main(String[] args){
        Main m = new Main();
        m.start();
    }

    public void start() {
        System.out.println("start");
        while (true) {
//            System.out.println("kolo");
            tcp.tryConnection();
            for (Game g : games.values()) {
                if (g.moveTimer + 12000 < System.currentTimeMillis() / 1000) {
                    g.timeout();
                }
            }
            Message incoming = tcp.getPackage(); // TODO: read incoming
            if (incoming == null)
                continue;
            if (incoming instanceof WaitForGameMessage) {
                WaitForGameMessage w = (WaitForGameMessage) incoming;
                int id = -1;
                for (Map.Entry<Integer, String> nick : players.entrySet()){
                    if(nick.getValue().equals(w.playerName)){
                        id = nick.getKey();
                        break;
                    }
                }
                if(id == -1){
                    id = players.size();
                    players.put(id, w.playerName);
                    wincounts.put(id, 0);
                }
                requestGame(id);
            }
            else if (incoming instanceof MoveMessage) {
                MoveMessage m = (MoveMessage) incoming;
                if (games.containsKey(m.gameId))
                    games.get(m.gameId).makeMove(m);
            }
            else if (incoming instanceof GameFinishedMessage) {
                GameFinishedMessage f = (GameFinishedMessage) incoming;
                if (games.containsKey(f.gameId))
                {
                    Game game = games.get(f.gameId);
                    int winner;
                    if (f.result)
                        winner = f.player;
                    else
                        winner = f.player == game.player0 ? game.player1 : game.player0;
                    games.get(f.gameId).finishGame(winner);
                }
                // log
                for (Map.Entry<Integer, String> nick : players.entrySet()){
                    for(Map.Entry<Integer, Integer> entry : wincounts.entrySet()){
                        if(nick.getKey() == entry.getKey()){
                            System.out.println("id:" + entry.getKey().toString() + " Nickname: " + nick.getValue() +
                                    " number of wins: " + entry.getValue().toString());
                        }
                    }
                }
            }
        }

    }

    public void requestGame(int player) {
        if (waitingPlayer != -1) {
            Game game = new Game(this, waitingPlayer, player, newGameID);
            games.put(newGameID, game);
            newGameID++;
            waitingPlayer = -1;
        } else {
            waitingPlayer = player;
        }
    }

    public void updateWincounts(int player) {
        wincounts.put(player, (wincounts.getOrDefault(player, 0)) + 1);
    }

    public void gameFinished(int id) {
        games.remove(id);
    }

    public TCP getTcp() {
        return tcp;
    }
}
