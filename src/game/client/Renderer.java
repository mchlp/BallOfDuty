package game.client;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {
    private ClientLoop loop;
    private Window window;

    public Renderer(ClientLoop loop) {
        this.loop = loop;

        window = new Window();
        window.setInputCallback(loop);

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
        glEnable(GL_DEPTH_TEST);
        glTranslated(0, 0, -15);
    }

    public void invoke() {
        window.makeGLContextCurrent();

        Window.Dimension windowsize = window.getWindowSize();

        glViewport(0, 0, windowsize.width, windowsize.height);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        System.out.println(windowsize.getAspectRatio());
        perspective(100, windowsize.getAspectRatio(), 0.1, 100);

        double dx = loop.x - loop.lx;
        double dy = loop.y - loop.ly;
        loop.lx = loop.x;
        loop.ly = loop.y;

        double len = Math.hypot(dx, dy);
        System.out.println("len = " + len);

        glMatrixMode(GL_MODELVIEW);

        if(len > 1e-9) {
            double[] mat = new double[16];
            glGetDoublev(GL_MODELVIEW_MATRIX, mat);
            glLoadIdentity();
            glTranslated(0, 0, -15);
            glRotated(len, -dy / len, -dx / len, 0);
            glTranslated(0, 0, 15);
            glMultMatrixd(mat);
        }

        //glRotated(loop.y / 10, 1, 0, 0);

        loop.getWorld().render();


        window.swapBuffers();

        Window.pollEvents();
    }

    public Window getWindow() {
        return window;
    }
}
