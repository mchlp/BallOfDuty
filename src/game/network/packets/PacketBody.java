/*
 *  Author: Michael Pu
 *  Teacher: Mr. Radulovich
 *  Date: 2019/6/17
 *  Course: ICS4U
 */

package game.network.packets;

/**
 * Class to be implemented by other classes that can be sent as the body of a packet.
 */
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

    protected PacketBody() {
        serializedBody = "";
    }

    protected abstract void setSerializedBody();

    public abstract String toString();
}
