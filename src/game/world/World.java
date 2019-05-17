package game.world;

import game.client.model.Model;

import java.io.File;

public class World {
    private Model model;

    public World() {
    }

    public void init(){
        model = Model.loadOBJ(new File("obj/model.obj"), new File("obj/terrain.png"));
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
}
