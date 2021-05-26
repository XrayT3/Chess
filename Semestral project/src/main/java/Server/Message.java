package Server;

public abstract class Message {
    int receiver;
    public abstract String msgToBuffer();
}
