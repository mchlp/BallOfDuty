package game.world;

import game.client.model.Model;

import java.io.File;

public class World {
    Model test;

    public World() {
    }

    public void init(){
        test = Model.loadOBJ(new File("obj/test.obj"), new File("obj/terrain.png"));
    }

    public void render() {
        test.render();
    }
}
