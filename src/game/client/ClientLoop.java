package game.client;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class ClientLoop implements IInputHandler {

    private Window window;

    public ClientLoop() {
        window = new Window();
        window.setKeyCallback(this);

        window.setVisible(true);
    }

    public void run() {
        window.makeGLContextCurrent();

        while (!window.shouldWindowClose()) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            window.swapBuffers();

            Window.pollEvents();
        }
    }

    @Override
    public void onCursorPos(double x, double y) {

    }

    @Override
    public void onMouseButton(int button, int action, int mods) {
        if (!window.getCursorLock()) {
            window.setCursorLock(true);
        }
    }

    @Override
    public void onKey(int key, int scancode, int action, int mods) {
        if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
            window.setCursorLock(false);
        }

    }
}
