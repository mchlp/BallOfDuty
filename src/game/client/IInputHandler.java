/*
 *  Author: Henry Gu
 *  Teacher: Mr. Radulovich
 *  Date: 2019/6/18
 *  Course: ICS4U
 */

package game.client;

public interface IInputHandler {
    public void onKey(int key, int scancode, int action, int mods);
    public void onCursorPos(double x, double y);
    public void onMouseButton(int button, int action, int mods);
}
