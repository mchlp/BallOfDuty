package game.network.packets;

public class PacketBodyCoordinate extends PacketBody {

    public final double x;
    public final double y;
    public final double z;
    public final double yaw;
    public final double pitch;
    public final String player;

    public PacketBodyCoordinate(String player, double x, double y, double z, double yaw, double pitch) {
        super();
        this.player = player;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;

        setSerializedBody();
    }

    public static PacketBodyCoordinate fromSerialized(String serializedForm) {
        String[] split = serializedForm.split(",");
        return new PacketBodyCoordinate(split[0].trim(), Double.parseDouble(split[1].trim()),
                Double.parseDouble(split[2].trim()), Double.parseDouble(split[3].trim()),
                Double.parseDouble(split[4].trim()), Double.parseDouble(split[5].trim()));
    }

    @Override
    protected void setSerializedBody() {
        this.serializedBody = player + "," + x + "," + y + "," + z + "," + yaw + "," + pitch;
    }

    @Override
    public String toString() {
        return player + " - (" + x + ", " + y + ", " + z + ", " + yaw + ", " + pitch + ")";
    }
}