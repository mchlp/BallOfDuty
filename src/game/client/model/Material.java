/*
 *  Author: Henry Gu
 *  Teacher: Mr. Radulovich
 *  Date: 2019/6/18
 *  Course: ICS4U
 */

package game.client.model;

import static org.lwjgl.opengl.GL11.*;

public class Material {
    private Texture diffuseMap;

    private double diffuseR;
    private double diffuseG;
    private double diffuseB;

    public Material() {
    }

    public void setDiffuseRGB(double diffuseR, double diffuseG, double diffuseB) {
        this.diffuseR = diffuseR;
        this.diffuseG = diffuseG;
        this.diffuseB = diffuseB;
    }

    public Texture getDiffuseMap() {
        return diffuseMap;
    }

    public void setDiffuseMap(Texture diffuseMap) {
        this.diffuseMap = diffuseMap;
    }

    public void apply() {
        if (diffuseMap != null) {
            diffuseMap.bind();
            glEnable(GL_TEXTURE_2D);
        } else {
            glBindTexture(GL_TEXTURE_2D, 0);
            glDisable(GL_TEXTURE_2D);
            glColor3d(diffuseR, diffuseG, diffuseB);
        }
    }
}
