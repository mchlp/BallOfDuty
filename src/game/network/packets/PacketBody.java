package game.network.packets;

public abstract class PacketBody {

    public static final PacketBody EMPTY_BODY = new PacketBody() {
        @Override
        protected void setSerializedBody() {
            return;
        }

        @Override
        public String toString() {
            return "";
        }
    };

    protected String serializedBody;

    protected abstract void setSerializedBody();

    public abstract String toString();
}
