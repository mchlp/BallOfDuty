package game.client.model;

public class ModelFace {
    public ModelVertex a, b, c;
    public ModelUV d, e, f;

    public ModelFace(ModelVertex a, ModelVertex b, ModelVertex c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public ModelFace(ModelVertex a, ModelVertex b, ModelVertex c, ModelUV d, ModelUV e, ModelUV f) {
        this.a = a;
        this.b = b;
        this.c = c;

        this.d = d;
        this.e = e;
        this.f = f;
    }

    public ModelFace() {
    }

    public ModelVertex getA() {
        return a;
    }

    public void setA(ModelVertex a) {
        this.a = a;
    }

    public ModelVertex getB() {
        return b;
    }

    public void setB(ModelVertex b) {
        this.b = b;
    }

    public ModelVertex getC() {
        return c;
    }

    public void setC(ModelVertex c) {
        this.c = c;
    }

    public ModelUV getUVA() {
        return d;
    }

    public void setUVA(ModelUV d) {
        this.d = d;
    }

    public ModelUV getUVB() {
        return e;
    }

    public void setUVB(ModelUV e) {
        this.e = e;
    }

    public ModelUV getUVC() {
        return f;
    }

    public void setUVC(ModelUV f) {
        this.f = f;
    }

    public void draw(){
        if (d != null) d.glTexCoord();
        a.glVertex();
        if (e != null) e.glTexCoord();
        b.glVertex();
        if (f != null) f.glTexCoord();
        c.glVertex();
    }
}
