package game.world;

import game.client.ClientLoop;
import game.client.model.Model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class World {
    private Model model;
    private Player localPlayer;
    private ConcurrentHashMap<Integer, Player> players;
    private ClientLoop loop;
    private int localid;

    public World(ClientLoop loop) {
        this.loop = loop;
        players = new ConcurrentHashMap<>();
    }

    public void init(Model model){
        this.model = model;
    }

    public void render() {
        model.render();
        renderAllPlayers();
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

    public void renderAllPlayers() {
        for (Player player : players.values()){
            if (player == localPlayer) {
                continue;
            }

            player.render();
        }
    }

    public ConcurrentHashMap<Integer, Player> getPlayers() {
        return players;
    }

    public Player obtainPlayer(int playerid) {
        if (players.containsKey(playerid)) {
            return players.get(playerid);
        }

        Player player = new Player(playerid, loop);
        players.put(playerid, player);
        return player;
    }

    public int getLocalid() {
        return localid;
    }

    public void setLocalid(int localid) {
        this.localid = localid;
    }
}
