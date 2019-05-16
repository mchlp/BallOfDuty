package game.client;

public class ModelFace {
    public ModelVertex a, b, c;

    public ModelFace(ModelVertex a, ModelVertex b, ModelVertex c) {
        this.a = a;
        this.b = b;
        this.c = c;
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
}
