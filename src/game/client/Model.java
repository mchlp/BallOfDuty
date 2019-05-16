package game.client;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class Model {
    private ArrayList<ModelVertex> verticies;
    private ArrayList<ModelFace> faces;

    private Model() {
        this.verticies = new ArrayList<>();
        this.faces = new ArrayList<>();
    }

    public void render() {
        glBegin(GL_TRIANGLES);

        for (ModelFace face : faces) {
            face.a.glVertex();
            face.b.glVertex();
            face.c.glVertex();
        }

        glEnd();
    }

    public static Model loadOBJ(String obj) {
        obj = obj.replace('\r', '\n');
        String[] lines = obj.split("\n");

        Model model = new Model();

        for (String line : lines) {
            String[] parts = line.split(" ");
            if (parts[0].equals("v")) {
                ModelVertex vertex = new ModelVertex();

                vertex.x = Double.parseDouble(parts[1]);
                vertex.y = Double.parseDouble(parts[2]);
                vertex.z = Double.parseDouble(parts[3]);

                model.verticies.add(vertex);
            } else if (parts[0].equals("f")) {
                ModelFace face = new ModelFace();

                face.a = model.verticies.get(Integer.parseInt(parts[1]) - 1);
                face.b = model.verticies.get(Integer.parseInt(parts[2]) - 1);
                face.c = model.verticies.get(Integer.parseInt(parts[3]) - 1);

                model.faces.add(face);
            }
        }

        return model;
    }
}
