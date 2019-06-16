package game.world;

import game.client.ClientLoop;
import game.client.model.Model;

import java.io.File;

public class World {
    private Model model;
    private Player localPlayer;

    public World(ClientLoop loop) {
        localPlayer = new Player(loop);
    }

    public void init(Model model){
        this.model = model;
    }

    public void render() {
        model.render();
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Player getLocalPlayer() {
        return localPlayer;
    }

    public void setLocalPlayer(Player localPlayer) {
        this.localPlayer = localPlayer;
    }

    public void tick() {
        this.localPlayer.tick();
    }
}
