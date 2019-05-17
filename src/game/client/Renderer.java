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

        Window.Dimension windowsize = window.getWindowSize();

        glViewport(0, 0, windowsize.width, windowsize.height);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        perspective(100, windowsize.getAspectRatio(), 0.1, 100);

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        loop.getLocalPlayer().applyCamera();

        glScaled(1, -1, 1);
        loop.getWorld().render();

        Window.pollEvents();
    }

    public Window getWindow() {
        return window;
    }
}
