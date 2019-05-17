package game.client.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class Model {
    private ArrayList<ModelVertex> verticies;
    private ArrayList<ModelFace> faces;
    private ArrayList<ModelUV> uvs;

    private Texture texture;

    private int displayList;

    private Model() {
        this.verticies = new ArrayList<>();
        this.faces = new ArrayList<>();
        this.uvs = new ArrayList<>();
    }

    public void render() {
        texture.bind();
        glCallList(displayList);
    }

    private void buildList() {
        displayList = glGenLists(1);
        glNewList(displayList, GL_COMPILE);
        glBegin(GL_TRIANGLES);

        for (ModelFace face : faces) {
            face.draw();
        }

        glEnd();
        glEndList();
    }

    public static Model loadOBJ(File objFile, File textureFile) {
        String obj;
        Texture texture;
        try {
            obj = new String(Files.readAllBytes(objFile.toPath()));
            texture = Texture.loadTexture(textureFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        obj = obj.replace('\r', '\n');
        String[] lines = obj.split("\n");

        Model model = new Model();
        model.texture = texture;

        for (String line : lines) {
            String[] parts = line.split(" ");
            if (parts[0].equals("v")) {
                ModelVertex vertex = new ModelVertex();

                vertex.x = Double.parseDouble(parts[1]);
                vertex.y = Double.parseDouble(parts[2]);
                vertex.z = Double.parseDouble(parts[3]);

                model.verticies.add(vertex);
            } else if (parts[0].equals("vt")) {
                ModelUV uv = new ModelUV();

                uv.u = Double.parseDouble(parts[1]);
                uv.v = Double.parseDouble(parts[2]);

                model.uvs.add(uv);
            } else if (parts[0].equals("f")) {
                makeFace(model, parts);
            }
        }

        model.buildList();

        return model;
    }

    private static <T> T fetchArray(ArrayList<T> array, String[] comps, int index) {
        if (index >= comps.length) return null;

        int n;

        try {
            n = Integer.parseInt(comps[index]);
        } catch (NumberFormatException e) {
            return null;
        }

        return array.get(n - 1);
    }

    private static void makeFace(Model model, String[] parts) {
        ModelFace face = new ModelFace();

        for (int i = 1; i <= 3; i++) {
            String[] comps = parts[i].split("/");

            ModelVertex vertex = fetchArray(model.verticies, comps, 0);
            ModelUV uv = fetchArray(model.uvs, comps, 1);

            switch (i) {
                case 1:
                    face.a = vertex;
                    face.d = uv;
                    break;
                case 2:
                    face.b = vertex;
                    face.e = uv;
                    break;
                case 3:
                    face.c = vertex;
                    face.f = uv;
                    break;
            }
        }

        model.faces.add(face);
    }

    @Override
    public void finalize() {
        glDeleteLists(displayList, 1);
    }
}
