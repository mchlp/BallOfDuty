package game.world;

import static org.lwjgl.opengl.GL11.*;

public class World {
    public World() {

    }

    public void render() {
        glPushMatrix();

        glBegin(GL_QUADS);

        glColor3d(1, 1, 1);

        glColor3d(1, 1, 1);
        glVertex3d(-1, -1, -1);
        glColor3d(1, 0, 1);
        glVertex3d(-1, 1, -1);
        glColor3d(1, 1, 0);
        glVertex3d(1, 1, -1);
        glColor3d(0, 1, 1);
        glVertex3d(1, -1, -1);

        glColor3d(1, 1, 1);
        glVertex3d(-1, -1, 1);
        glColor3d(1, 0, 1);
        glVertex3d(-1, 1, 1);
        glColor3d(1, 1, 0);
        glVertex3d(1, 1, 1);
        glColor3d(0, 1, 1);
        glVertex3d(1, -1, 1);

        glColor3d(1, 1, 1);
        glVertex3d(-1, -1, -1);
        glColor3d(1, 0, 1);
        glVertex3d(-1, -1, 1);
        glColor3d(1, 1, 0);
        glVertex3d(1, -1, 1);
        glColor3d(0, 1, 1);
        glVertex3d(1, -1, -1);

        glColor3d(1, 1, 1);
        glVertex3d(-1, 1, -1);
        glColor3d(1, 0, 1);
        glVertex3d(-1, 1, 1);
        glColor3d(1, 1, 0);
        glVertex3d(1, 1, 1);
        glColor3d(0, 1, 1);
        glVertex3d(1, 1, -1);

        glColor3d(1, 1, 1);
        glVertex3d(-1, -1, -1);
        glColor3d(1, 0, 1);
        glVertex3d(-1, -1, 1);
        glColor3d(1, 1, 0);
        glVertex3d(-1, 1, 1);
        glColor3d(0, 1, 1);
        glVertex3d(-1, 1, -1);

        glColor3d(1, 1, 1);
        glVertex3d(1, -1, -1);
        glColor3d(1, 0, 1);
        glVertex3d(1, -1, 1);
        glColor3d(1, 1, 0);
        glVertex3d(1, 1, 1);
        glColor3d(0, 1, 1);
        glVertex3d(1, 1, -1);

        glEnd();
        glPopMatrix();
    }
}
