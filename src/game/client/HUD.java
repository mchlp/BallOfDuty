package game.client;

import game.world.Player;

import static org.lwjgl.opengl.GL11.*;

public class HUD {
    public static void render(Player player, Window.Dimension windowsize) {
        glColor3d(1, 1, 1);
        glDisable(GL_DEPTH_TEST);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, windowsize.width, windowsize.height, 0, -0.1, 0.1);

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        glColor3d(1, 1, 1);
        glBegin(GL_QUADS);
        glVertex2d(windowsize.width / 2 - 10, windowsize.height / 2 - 10);
        glVertex2d(windowsize.width / 2 + 10, windowsize.height / 2 - 10);
        glVertex2d(windowsize.width / 2 + 10, windowsize.height / 2 + 10);
        glVertex2d(windowsize.width / 2 - 10, windowsize.height / 2 + 10);
        glEnd();
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

        glColor3d(0, 1, 0);
        drawRing(player.getAmmunitionAnimation(), player.getAmmunition(), Player.MAX_AMMUNITION, 35, 45, 0.8, 0.2, 0.2, windowsize);

        glColor3d(1, 0, 0);
        drawRing(player.getHealthAnimation(), player.getHealth(), Player.MAX_HEALTH, 50, 60, 0.8, 0.2, 0.2, windowsize);

        if (player.getDeath() > 0) {
            glColor4d(0.5, 0, 0, 0.5);

            glBegin(GL_QUADS);
            glVertex2d(0, 0);
            glVertex2d(windowsize.width, 0);
            glVertex2d(windowsize.width, windowsize.height);
            glVertex2d(0, windowsize.height);
            glEnd();
        }
    }

    private static void drawRing(double animValue, double trueValue, double maxValue, double inner, double outer, double r, double g, double b, Window.Dimension windowsize) {
        glBegin(GL_QUAD_STRIP);
        for (int i = 0; i <= 1000 * animValue / maxValue; i++) {
            if (i > 1000 * trueValue / maxValue) {
                glColor3d(r, g, b);
            }

            double angle = (i / 500d) * Math.PI;
            glVertex2d(windowsize.width / 2 - outer * Math.sin(angle), windowsize.height / 2 - outer * Math.cos(angle));
            glVertex2d(windowsize.width / 2 - inner * Math.sin(angle), windowsize.height / 2 - inner * Math.cos(angle));
        }
        glEnd();
    }
}
