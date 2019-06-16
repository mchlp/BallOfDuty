package game.server;

public class ClientIDGenerator {
    public int next;

    public ClientIDGenerator() {
        next = 0;
    }

    public int nextID() {
        return next++;
    }
}
