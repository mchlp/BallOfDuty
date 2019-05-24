package game.network.packets;

public class PacketBodyCoordinate extends PacketBody {

    public final double x;
    public final double y;
    public final String player;

    public PacketBodyCoordinate(String player, double x, double y) {
        super();
        this.player = player;
        this.x = x;
        this.y = y;
        setSerializedBody();
    }

    public static PacketBodyCoordinate fromSerialized(String serializedForm) {
        String[] split = serializedForm.split(",");
        return new PacketBodyCoordinate(split[0].trim(), Double.parseDouble(split[1].trim()),
                Double.parseDouble(split[2].trim()));
    }

    @Override
    protected void setSerializedBody() {
        this.serializedBody = player + "," + x + "," + y;
    }

    @Override
    public String toString() {
        return player + " - (" + x + ", " + y + ")";
    }
}