/*
 *  Author: Michael Pu
 *  Teacher: Mr. Radulovich
 *  Date: 2019/6/17
 *  Course: ICS4U
 */

package game.network.packets;

/**
 * A packet body which stores plain text.
 */
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
