package game.client;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    public static class Dimension {
        int width;
        int height;

        public Dimension(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public Dimension() {
            this(0, 0);
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public double getAspectRatio() {
            return (double)width/height;
        }

    }

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
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 1);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);

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

    public void setInputCallback(IInputHandler callback) {
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

    public int getKey(int key) {
        return glfwGetKey(window, key);
    }

    public void setCursorLock(boolean locked) {
        glfwSetInputMode(window, GLFW_CURSOR, locked ? GLFW_CURSOR_DISABLED : GLFW_CURSOR_NORMAL);
    }

    public boolean getCursorLock() {
        return glfwGetInputMode(window, GLFW_CURSOR) == GLFW_CURSOR_DISABLED;
    }

    public Dimension getWindowSize() {
        int[] width = new int[1];
        int[] height = new int[1];
        glfwGetWindowSize(window, width, height);

        return new Dimension(width[0], height[0]);
    }

    @Override
    public void finalize() {
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
    }
}
