package game.client;

import game.util.TimeSync;
import game.world.Player;
import game.world.World;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

public class ClientLoop implements IInputHandler {
    private Renderer renderer;
    private World world;
    private Player localPlayer;
    private ClientState state;

    private Window window;

    private double cx;
    private double cy;
    private double clx;
    private double cly;
    private double cdx;
    private double cdy;

    private TimeSync sync;

    public ClientLoop() {
        window = new Window();
        window.setInputCallback(this);
        renderer = new Renderer(this, window);
        world = new World();
        localPlayer = new Player(this);
        sync = new TimeSync(10000000);

        renderer.init();
    }

    public boolean shouldRun() {
        return !renderer.getWindow().shouldWindowClose();
    }

    public void run() {
        world.init();

        while (shouldRun()) {
            renderer.invoke();
            localPlayer.tick();

            window.swapBuffers(); // TODO: move back to Renderer.invoke()

            clearTick();
            sync.sync();
        }
    }

    private void clearTick() {
        clx = cx;
        cly = cy;
        cdx = 0;
        cdy = 0;
    }

    @Override
    public void onCursorPos(double x, double y) {
        clx = cx;
        cly = cy;
        cx = x;
        cy = y;
        cdx = cx - clx;
        cdy = cy - cly;
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

    public double getCursorDeltaX() {
        return cdx;
    }

    public double getCursorDeltaY() {
        return cdy;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public Player getLocalPlayer() {
        return localPlayer;
    }

    public void setLocalPlayer(Player localPlayer) {
        this.localPlayer = localPlayer;
    }

    public Window getWindow() {
        return window;
    }

    public void setWindow(Window window) {
        this.window = window;
    }
}
