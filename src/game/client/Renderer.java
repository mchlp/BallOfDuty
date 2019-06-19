/*
 *  Author: Henry Gu
 *  Teacher: Mr. Radulovich
 *  Date: 2019/6/18
 *  Course: ICS4U
 */

package game.client;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {
    private ClientLoop loop;
    private Window window;

    public Renderer(ClientLoop loop, Window window) {
        this.loop = loop;

        this.window = window;
        window.setVisible(true);
    }

    private void perspective(double fovy, double aspect, double znear, double zfar) {
        double f = 1 / Math.tan(fovy / 2);
        glMultMatrixd(new double[]{
                f / aspect, 0, 0, 0,
                0, f, 0, 0,
                0, 0, (znear + zfar) / (znear - zfar), -1,
                0, 0, (2 * znear * zfar) / (znear - zfar), 0
        });
    }

    public void init() {
        window.makeGLContextCurrent();
    }

    public void invoke() {
        window.makeGLContextCurrent();
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        glFrontFace(GL_CW);
        Window.Dimension windowsize = window.getWindowSize();

        glViewport(0, 0, windowsize.width, windowsize.height);
        glClear(GL_DEPTH_BUFFER_BIT);

        glClearColor(0.690196078f, 0.878431373f, 0.901960784f, 1);
        glClear(GL_COLOR_BUFFER_BIT);
        glColor3d(1, 1, 1);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        perspective(100, windowsize.getAspectRatio(), 0.1, 10000);

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        loop.getLocalPlayer().applyCamera();

        glEnable(GL_TEXTURE_2D);
        loop.getWorld().render();
        glDisable(GL_TEXTURE_2D);
        HUD.render(loop.getLocalPlayer(), windowsize);

        Window.pollEvents();
        window.swapBuffers();
    }

    public Window getWindow() {
        return window;
    }
}
