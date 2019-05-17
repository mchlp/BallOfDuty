package game.world;

import game.client.model.Model;
import game.client.model.Texture;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.*;

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
