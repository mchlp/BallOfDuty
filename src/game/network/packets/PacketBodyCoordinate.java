package game.network.packets;

public class PacketBodyCoordinate extends PacketBody {

    public final double x;
    public final double y;

    public PacketBodyCoordinate(double x, double y) {
        super();
        this.x = x;
        this.y = y;
        setSerializedBody();
    }

    public static PacketBodyCoordinate fromSerialized(String serializedForm) {
        String[] split = serializedForm.split(",");
        return new PacketBodyCoordinate(Double.parseDouble(split[0]), Double.parseDouble(split[1]));
    }

    @Override
    protected void setSerializedBody() {
        this.serializedBody = x + "," + y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}