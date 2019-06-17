package game.client.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;

public class Model {

    public static final String OBJ_DIR = "obj";

    private ArrayList<ModelVertex> verticies;
    private ArrayList<ModelFace> faces;
    private ArrayList<ModelUV> uvs;

    private HashMap<String, Material> materials = new HashMap<>();

    private int displayList;

    private Model() {
        this.verticies = new ArrayList<>();
        this.faces = new ArrayList<>();
        this.uvs = new ArrayList<>();
    }

    public void render() {
        glCallList(displayList);
    }

    public static Model loadOBJ(String objFile) {
        String obj;
        try {
            obj = new String(Files.readAllBytes(new File(OBJ_DIR, objFile).toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        obj = obj.replace('\r', '\n');
        String[] lines = obj.split("\n");

        Model model = new Model();

        boolean startedDrawing = false;
        boolean drawing = false;

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
                if(!startedDrawing) {
                    model.displayList = glGenLists(1);
                    glNewList(model.displayList, GL_COMPILE);
                    startedDrawing = true;
                }
                if(!drawing) {
                    glBegin(GL_TRIANGLES);
                    drawing = true;
                }
                ModelFace f = makeFace(model, parts);
                f.draw();
            } else if (parts[0].equals("usemtl")) {
                if(!startedDrawing) {
                    model.displayList = glGenLists(1);
                    glNewList(model.displayList, GL_COMPILE);
                    startedDrawing = true;
                }
                if(drawing) {
                    glEnd();
                    drawing = false;
                }
                try {
                    model.materials.get(parts[1]).apply();
                } catch(NullPointerException e) {
                    System.err.println("Referenced material " + parts[1] + " does not exist");
                }
            } else if (parts[0].equals("mtllib")) {
                model.materials = loadMTL(parts[1]);
            }
        }

        if(drawing) {
            glEnd();
        }

        if(startedDrawing) {
            glEndList();
        }

        return model;
    }

    private static HashMap<String, Material> loadMTL(String mtlFile) {
        String mtl;
        try {
            mtl = new String(Files.readAllBytes(new File(OBJ_DIR, mtlFile).toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        HashMap<String, Material> materials = new HashMap<>();

        mtl = mtl.replace('\r', '\n');
        String[] lines = mtl.split("\n");

        Material currMtl = null;

        for (String line : lines) {
        	String[] parts = line.split(" ");
        	if(parts[0].equals("newmtl")) {
        	    currMtl = new Material();
        	    materials.put(parts[1], currMtl);
            } else if(parts[0].equals("map_Kd")) {
                try {
                    String filename = parts[1];
                    if(parts[1].equals("-s")) {
                        filename = parts[5];
                    }
                    Texture texture = Texture.loadTexture(new File(OBJ_DIR, filename));
                    currMtl.setDiffuseMap(texture);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if(parts[0].equals("Kd")) {
        		currMtl.setDiffuseRGB(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3]));
            }
        }

        return materials;
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

    private static ModelFace makeFace(Model model, String[] parts) {
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

        return face;
    }

    @Override
    public void finalize() {
        glDeleteLists(displayList, 1);
    }

    public ArrayList<ModelVertex> getVerticies() {
        return verticies;
    }

    public ArrayList<ModelFace> getFaces() {
        return faces;
    }

    public ArrayList<ModelUV> getUvs() {
        return uvs;
    }
}
