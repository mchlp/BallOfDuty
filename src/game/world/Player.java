package game.world;

import game.client.ClientLoop;
import game.client.model.Model;
import game.client.model.ModelFace;
import game.vec.Vec3;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Player implements ITickable {
    private double x, y, z;
    private double pitch, yaw;
    private double vx, vy, vz;
    private ClientLoop loop;

    public Player(ClientLoop loop) {
        this.loop = loop;
        z = 15;
    }

    public void applyCamera() {
        glRotated(pitch, 1, 0, 0);
        glRotated(yaw, 0, 1, 0);
        glTranslated(-x, -y + 10, -z);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void addX(double x) {
        this.x += x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void addY(double y) {
        this.y += y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void addZ(double z) {
        this.z += z;
    }

    public double getPitch() {
        return pitch;
    }

    public void setPitch(double pitch) {
        this.pitch = Math.min(Math.max(pitch, -90), 90);
    }

    public void addPitch(double pitch) {
        this.pitch = Math.min(Math.max(this.pitch + pitch, -90), 90);
    }

    public double getYaw() {
        return yaw;
    }

    public void setYaw(double yaw) {
        this.yaw = yaw;
    }

    public void addYaw(double yaw) {
        this.yaw += yaw;
    }

    @Override
    public void tick() {
        double cdx = loop.getCursorDeltaX();
        double cdy = loop.getCursorDeltaY();
        if (loop.getWindow().getCursorLock()) {
            if (Math.abs(cdx) > 0) addYaw(-cdx / 2);
            if (Math.abs(cdy) > 0) addPitch(-cdy / 2);
        }

        if (loop.getWindow().getKey(GLFW_KEY_W) == GLFW_PRESS) {
            addX(Math.sin(Math.toRadians(getYaw())) / 15);
            addZ(-Math.cos(Math.toRadians(getYaw())) / 15);
        }

        if (loop.getWindow().getKey(GLFW_KEY_S) == GLFW_PRESS) {
            addX(-Math.sin(Math.toRadians(getYaw())) / 15);
            addZ(Math.cos(Math.toRadians(getYaw())) / 15);
        }

        if (loop.getWindow().getKey(GLFW_KEY_A) == GLFW_PRESS) {
            addX(Math.cos(Math.toRadians(getYaw())) / 15);
            addZ(Math.sin(Math.toRadians(getYaw())) / 15);
        }

        if (loop.getWindow().getKey(GLFW_KEY_D) == GLFW_PRESS) {
            addX(-Math.cos(Math.toRadians(getYaw())) / 15);
            addZ(-Math.sin(Math.toRadians(getYaw())) / 15);
        }

        if (loop.getWindow().getKey(GLFW_KEY_SPACE) == GLFW_PRESS && y == 0) {
            vy = -0.15;
        }

        x += vx;
        y += vy;
        z += vz;

        vy += 0.01;

        if (y > 0) {
            y = 0;
            vy = 0;
        }

        collide();
    }

    private void collide() {
        Model model = loop.getWorld().getModel();
        Vec3 pos = new Vec3(x, y, z);

        glPointSize(10);
        glBegin(GL_POINTS);

        glColor3d(1, 0, 1);

        for (ModelFace face : model.getFaces()) {
            Vec3 a = face.a.toVec3();
            Vec3 b = face.b.toVec3();
            Vec3 c = face.c.toVec3();

            Vec3 normal = b.sub(a).cross(c.sub(a)).normalize();

            Vec3 nearest = pos.sub(pos.sub(a).project(normal));

            Vec3 ab = closestPointOnLine(pos, a, b);
            Vec3 ac = closestPointOnLine(pos, a, c);
            Vec3 bc = closestPointOnLine(pos, b, c);

            double abm = pos.sub(ab).magnitudeSq();
            double acm = pos.sub(ac).magnitudeSq();
            double bcm = pos.sub(bc).magnitudeSq();

            Vec3 closest;

            if (abm <= acm && abm <= bcm) {
                closest = ab;
            } else if (acm <= abm && acm <= bcm) {
                closest = ac;
            } else {
                closest = bc;
            }

            double sa = b.sub(a).cross(pos.sub(a)).dot(normal);
            double sb = c.sub(b).cross(pos.sub(b)).dot(normal);
            double sc = a.sub(c).cross(pos.sub(c)).dot(normal);

            if ((sa < 0 && sb < 0 && sc < 0) || (sa > 0 || sb > 0 || sc > 0)) {
                closest = nearest;
            }

            glVertex3d(closest.x, closest.y + 0.05, closest.z);
            System.out.println(nearest);
        }

        glColor3d(1, 1, 1);
        glEnd();
    }

    private Vec3 closestPointOnLine(Vec3 point, Vec3 linea, Vec3 lineb) {
        Vec3 line = linea.sub(lineb);

        Vec3 topoint = linea.sub(point);
        if (line.dot(topoint) < 0) {
            return linea;
        }

        topoint = lineb.sub(point);
        if (line.dot(topoint) > 0) {
            return lineb;
        }

        return point.sub(topoint.sub(topoint.project(line)));
    }
}
