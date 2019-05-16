package game.client;

import game.world.World;

import static org.lwjgl.glfw.GLFW.*;

public class ClientLoop implements IInputHandler {
    private Renderer renderer;
    private World world;
    private ClientState state;

    double x;
    double y;

    double lx;
    double ly;

    public ClientLoop() {
        renderer = new Renderer(this);
        world = new World();

        renderer.init();
    }

    public boolean shouldRun() {
        return !renderer.getWindow().shouldWindowClose();
    }

    public void run() {
        while (shouldRun()) {
            renderer.invoke();
        }
    }

    @Override
    public void onCursorPos(double x, double y) {
        lx = this.x;
        ly = this.y;

        this.x = x;
        this.y = y;
    }

    @Override
    public void onMouseButton(int button, int action, int mods) {
        if (!renderer.getWindow().getCursorLock()) {
            renderer.getWindow().setCursorLock(true);
        }
    }

    @Override
    public void onKey(int key, int scancode, int action, int mods) {
        if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
            renderer.getWindow().setCursorLock(false);
        }
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }
}
