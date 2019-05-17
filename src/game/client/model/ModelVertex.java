package game.client.model;

import game.vec.Vec3;

import static org.lwjgl.opengl.GL11.glVertex3d;

public class ModelVertex {
    public double x;
    public double y;
    public double z;

    public ModelVertex(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public ModelVertex() {
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void glVertex() {
        glVertex3d(x, y, z);
    }

    public Vec3 toVec3() {
        return new Vec3(x, y, z);
    }
}
