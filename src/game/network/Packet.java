/*
 *  Author: Michael Pu
 *  Teacher: Mr. Radulovic
 *  Course: ICS4U
 */

package game.network;

import java.io.Serializable;

public class Packet implements Serializable {
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
