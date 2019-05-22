package game.network.packets;

public class PacketBodyText extends PacketBody {

    public final String text;

    public PacketBodyText(String text) {
        super();
        this.text = text;
        setSerializedBody();
    }

    public static PacketBodyText fromSerialized(String serializedForm) {
        return new PacketBodyText(serializedForm);
    }

    @Override
    protected void setSerializedBody() {
        serializedBody = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
