package game.client.model;

import static org.lwjgl.opengl.GL11.glTexCoord2dv;

public class ModelUV {
    public double u, v;

    public ModelUV(double u, double v) {
        this.u = u;
        this.v = v;
    }

    public ModelUV() {
    }

    public double getU() {
        return u;
    }

    public void setU(double u) {
        this.u = u;
    }

    public double getV() {
        return v;
    }

    public void setV(double v) {
        this.v = v;
    }

    public void glTexCoord() {
        glTexCoord2dv(new double[]{1-v, u});
    }
}
