package game.client;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private static boolean init = false;

    private static void glfwInit() {
        if (!init) init = true;
        else return;

        GLFWErrorCallback.createPrint(System.err).set();

        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
    }

    public static void pollEvents() {
        glfwPollEvents();
    }

    private final long window;

    public Window(String title, int width, int height) {
        glfwInit();

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 2);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        window = glfwCreateWindow(width, height, title, NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");
    }

    public Window(String title) {
        this(title, 960, 540);
    }

    public Window(int width, int height) {
        this("", width, height);
    }

    public Window() {
        this("");
    }

    public void setTitle(String title) {
        glfwSetWindowTitle(window, title);
    }

    public void setVisible(boolean visible) {
        if (visible) glfwShowWindow(window);
        else glfwHideWindow(window);
    }

    public void makeGLContextCurrent() {
        glfwMakeContextCurrent(window);
        GL.createCapabilities();
    }

    public boolean shouldWindowClose() {
        return glfwWindowShouldClose(window);
    }

    public void swapBuffers() {
        glfwSwapBuffers(window);
    }

    public void setKeyCallback(IInputHandler callback) {
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            callback.onKey(key, scancode, action, mods);
        });

        glfwSetCursorPosCallback(window, (window, x, y) -> {
            callback.onCursorPos(x, y);
        });

        glfwSetMouseButtonCallback(window, (window, button, action, mods) -> {
            callback.onMouseButton(button, action, mods);
        });
    }

    public void setCursorLock(boolean locked) {
        glfwSetInputMode(window, GLFW_CURSOR, locked ? GLFW_CURSOR_DISABLED : GLFW_CURSOR_NORMAL);
    }

    public boolean getCursorLock() {
        return glfwGetInputMode(window, GLFW_CURSOR) == GLFW_CURSOR_DISABLED;
    }

    @Override
    public void finalize() {
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
    }
}
