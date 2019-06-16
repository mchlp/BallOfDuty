package game.client;

import game.client.model.Model;
import game.network.ClientReceiver;
import game.network.packets.*;
import game.util.TimeSync;
import game.world.Player;
import game.world.World;

import java.io.File;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

public class ClientLoop implements IInputHandler {
    private Renderer renderer;
    private World world;
    private ClientState state;
    private ClientReceiver receiver;
    private Model mapModel;

    private Window window;

    private double clx;
    private double cly;

    public ClientLoop() {
        window = new Window("Ball of Duty");
        window.setInputCallback(this);
        renderer = new Renderer(this, window);
        receiver = new ClientReceiver("localhost", 8861);

        renderer.init();

        this.mapModel = Model.loadOBJ(new File("obj/map.obj"), new File("obj/terrain.png"));
    }

    public boolean shouldRun() {
        return !renderer.getWindow().shouldWindowClose();
    }

    public void run() {
        new Thread(this::tick).start();
        TimeSync sync = new TimeSync(6000000); // 60 fps

        while (shouldRun()) {
            if (this.world == null) {
                glClear(GL_COLOR_BUFFER_BIT);
                window.swapBuffers();
            } else {
                try {
                    renderer.invoke();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Window.pollEvents();
            sync.sync();
        }
    }

    private void tick() {
        TimeSync sync = new TimeSync(10000000); // 100 r/s
        while (shouldRun()) {
            if (receiver.isConnected()) {
                ArrayList<Packet> packets = receiver.checkForPackets();

                for (Packet p : packets) {
                    if (p.type == PacketType.PLAYER_MOVE) {
                        PacketBodyCoordinate coords = (PacketBodyCoordinate) p.body;

                        int playerid = Integer.parseInt(coords.player);
                        Player player = world.obtainPlayer(playerid);
                        player.setX(coords.x);
                        player.setY(coords.y);
                        player.setZ(coords.z);

                        System.out.println("Received move for player id: " + playerid);
                    }
                }

                if (receiver.isConnected()) {
                    this.world.tick();
                    sync.sync();

                    continue;
                }
            }

            System.out.println("Disconnected");
            this.world = null;

            while (!receiver.isConnected()) {
                receiver.attemptReconnect();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Connected");

            receiver.sendPacket(new Packet(PacketType.PLAYER_REQUEST_JOIN, PacketBody.EMPTY_BODY));

            ArrayList<Packet> packets;
            do {
                packets = receiver.checkForPackets();
            } while (packets.size() == 0);

            Packet joinresponse = packets.get(0); // First packet is guaranteed to be a join response
            PacketBodyText text = (PacketBodyText) joinresponse.body;

            int localplayerid = Integer.parseInt(text.text);

            World w = new World(this);
            w.setLocalPlayer(new Player(this));
            w.getPlayers().put(localplayerid, w.getLocalPlayer());
            w.init(mapModel);
            this.world = w;
        }
    }

    @Override
    public void onCursorPos(double x, double y) {
        double cdx = x - clx;
        double cdy = y - cly;
        if (getWindow().getCursorLock() && this.world != null) {
            if (Math.abs(cdx) > 0) getLocalPlayer().addYaw(-cdx / 3);
            if (Math.abs(cdy) > 0) getLocalPlayer().addPitch(-cdy / 3);
        }

        clx = x;
        cly = y;
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

    public Player getLocalPlayer() {
        return this.world.getLocalPlayer();
    }

    public void setLocalPlayer(Player localPlayer) {
        this.world.setLocalPlayer(localPlayer);
    }

    public Window getWindow() {
        return window;
    }

    public void setWindow(Window window) {
        this.window = window;
    }
}
