package game.network.packets;

public class PacketBody {

    protected String serializedBody;

    public PacketBody(String text) {
        serializedBody = text;
    }

    public PacketBody() {

    }

    public static PacketBody fromSerialized(String serializedForm) {
        return new PacketBody(serializedForm);
    }

    @Override
    public String toString() {
        return serializedBody;
    }
}
