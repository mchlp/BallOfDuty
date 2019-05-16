package game.world;

import game.client.Model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.*;

public class World {
    Model monkey;

    public World() {
        String str = null;
        try {
            str = new String(Files.readAllBytes(Paths.get("obj/monkey.obj")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        monkey = Model.loadOBJ(str);
    }

    public void render() {
        glPushMatrix();

        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        monkey.render();

        glPopMatrix();
    }
}
