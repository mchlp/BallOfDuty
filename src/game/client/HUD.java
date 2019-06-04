package game.client;

import game.world.Player;

import static org.lwjgl.opengl.GL11.*;

public class HUD {
    public static void render(Player player, Window.Dimension windowsize) {
        glDisable(GL_DEPTH_TEST);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, windowsize.width, windowsize.height, 0, -0.1, 0.1);

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        glColor3d(1, 1, 1);
        glBegin(GL_QUADS);
        glVertex2d(windowsize.width / 2 - 10, windowsize.height / 2);
        glVertex2d(windowsize.width / 2, windowsize.height / 2 + 10);
        glVertex2d(windowsize.width / 2 + 10, windowsize.height / 2);
        glVertex2d(windowsize.width / 2, windowsize.height / 2 - 10);
        glEnd();
    }
}
