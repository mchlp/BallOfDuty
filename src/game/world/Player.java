package game.world;

import game.client.ClientLoop;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glRotated;
import static org.lwjgl.opengl.GL11.glTranslated;

public class Player implements ITickable {
    private double x, y, z;
    private double pitch, yaw;
    private double vx, vy, vz;
    private ClientLoop loop;

    public Player(ClientLoop loop) {
        this.loop = loop;
        z = -15;
    }

    public void applyCamera() {
        glRotated(pitch, 1, 0, 0);
        glRotated(yaw, 0, 1, 0);
        glTranslated(x, y + 1, z);
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
        this.pitch = Math.min(Math.max(pitch, -180), 180);
    }

    public void addPitch(double pitch) {
        this.pitch = Math.min(Math.max(this.pitch + pitch, -180), 180);
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
            if (Math.abs(cdx) > 0) addYaw(-cdx / 5);
            if (Math.abs(cdy) > 0) addPitch(-cdy / 5);
        }

        if (loop.getWindow().getKey(GLFW_KEY_W) == GLFW_PRESS) {
            addX(-Math.sin(Math.toRadians(getYaw())) / 15);
            addZ(Math.cos(Math.toRadians(getYaw())) / 15);
        }

        if (loop.getWindow().getKey(GLFW_KEY_S) == GLFW_PRESS) {
            addX(Math.sin(Math.toRadians(getYaw())) / 15);
            addZ(-Math.cos(Math.toRadians(getYaw())) / 15);
        }

        if (loop.getWindow().getKey(GLFW_KEY_A) == GLFW_PRESS) {
            addX(-Math.cos(Math.toRadians(getYaw())) / 15);
            addZ(-Math.sin(Math.toRadians(getYaw())) / 15);
        }

        if (loop.getWindow().getKey(GLFW_KEY_D) == GLFW_PRESS) {
            addX(Math.cos(Math.toRadians(getYaw())) / 15);
            addZ(Math.sin(Math.toRadians(getYaw())) / 15);
        }

        if (loop.getWindow().getKey(GLFW_KEY_SPACE) == GLFW_PRESS && y == 0) {
            vy = 0.15;
        }

        x += vx;
        y += vy;
        z += vz;

        vy -= 0.01;

        if (y < 0) {
            y = 0;
            vy = 0;
        }
    }
}
