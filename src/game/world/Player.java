package game.world;

import game.client.ClientLoop;
import game.client.model.Model;
import game.client.model.ModelFace;
import game.vec.Vec3;

import java.util.ArrayList;
import java.util.Collections;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Player implements ITickable {
    private double x, y, z;
    private double pitch, yaw;
    private double vx, vy, vz;
    private double radius = 1;
    private double speed = 0.05;
    private boolean onGround;
    private ClientLoop loop;

    public Player(ClientLoop loop) {
        this.loop = loop;
        y = 5;
    }

    public void applyCamera() {
        glRotated(pitch, 1, 0, 0);
        glRotated(yaw, 0, 1, 0);
        glScaled(1, -1, 1);
        glTranslated(-x, -y, -z);
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
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
        x += vx;
        y += vy;
        z += vz;

        if (loop.getWindow().getKey(GLFW_KEY_W) == GLFW_PRESS) {
            vx += Math.sin(Math.toRadians(getYaw())) * speed;
            vz -= Math.cos(Math.toRadians(getYaw())) * speed;
        }

        if (loop.getWindow().getKey(GLFW_KEY_S) == GLFW_PRESS) {
            vx -= Math.sin(Math.toRadians(getYaw())) * speed;
            vz += Math.cos(Math.toRadians(getYaw())) * speed;
        }

        if (loop.getWindow().getKey(GLFW_KEY_A) == GLFW_PRESS) {
            vx += Math.cos(Math.toRadians(getYaw())) * speed;
            vz += Math.sin(Math.toRadians(getYaw())) * speed;
        }

        if (loop.getWindow().getKey(GLFW_KEY_D) == GLFW_PRESS) {
            vx -= Math.cos(Math.toRadians(getYaw())) * speed;
            vz -= Math.sin(Math.toRadians(getYaw())) * speed;
        }

        if (loop.getWindow().getKey(GLFW_KEY_SPACE) == GLFW_PRESS && onGround) {
            vy = 1;
        }

//        if (loop.getWindow().getKey(GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS) {
//            vy -= speed;
//        }

        vx *= 0.9;
        vy *= 0.9;
        vz *= 0.9;

        vy -= 0.05;

        if (y < 0.6) {
            y = 0.6;
            vy = 0;
        }

        onGround = false;
        for(int i = 0; i < 8; ++i) {
            collide();
        }
    }

    private void collide() {
        ArrayList<CollisionPlane> planes = new ArrayList<>();

        Model model = loop.getWorld().getModel();
        Vec3 pos = new Vec3(x, y, z);
        Vec3 vel = new Vec3(vx, vy, vz);

//        glPointSize(10);
//        glBegin(GL_POINTS);

//        glColor3d(1, 0, 1);

        for (ModelFace face : model.getFaces()) {
            Vec3 a = face.a.toVec3();
            Vec3 b = face.b.toVec3();
            Vec3 c = face.c.toVec3();

            Vec3 normal = b.sub(a).cross(c.sub(a)).normalize();

            Vec3 nearest = pos.sub(pos.sub(a).project(normal)); // Nearest point on the plane to player center

            Vec3 ab = closestPointOnLine(pos, a, b); // Nearest point on a line segment to player center
            Vec3 ac = closestPointOnLine(pos, a, c);
            Vec3 bc = closestPointOnLine(pos, b, c);

            double abm = pos.sub(ab).magnitudeSq(); // Distances to player (ab, ac, bc)
            double acm = pos.sub(ac).magnitudeSq();
            double bcm = pos.sub(bc).magnitudeSq();

            Vec3 closest;

            if (abm <= acm && abm <= bcm) { // Check which line has a point closest to player
                closest = ab;
            } else if (acm <= abm && acm <= bcm) {
                closest = ac;
            } else {
                closest = bc;
            }

            double sa = b.sub(a).cross(pos.sub(a)).dot(normal); // Determine cross products
            double sb = c.sub(b).cross(pos.sub(b)).dot(normal);
            double sc = a.sub(c).cross(pos.sub(c)).dot(normal);

            if ((sa < 0 && sb < 0 && sc < 0) || (sa > 0 && sb > 0 && sc > 0)) { // Use cross products to determine whether point is inside triangle
                closest = nearest;
            }

            planes.add(new CollisionPlane(closest, pos.sub(closest).magnitude()));

//            glVertex3d(a.x, a.y + 0.05, a.z);
//            glVertex3d(b.x, b.y + 0.05, b.z);
//            glVertex3d(c.x, c.y + 0.05, c.z);
//            glVertex3d(closest.x, closest.y, closest.z);
            //System.out.println(nearest);
        }

//        glColor3d(1, 1, 1);
//        glEnd();

        Collections.sort(planes); // Sort these planes from closest to farthest, to prevent colliding with invisible edges

        // Collide with the nearest plane first
        for(CollisionPlane plane : planes) {
            Vec3 closest = plane.closestPoint;
            //System.out.println(pos.sub(closest).magnitude());
            if (plane.distance < radius) {
                Vec3 toPlayer = pos.sub(closest).normalize();
//                System.out.println(closest);
                pos = toPlayer.mul(radius - 1e-6).add(closest);
                if(vel.dot(toPlayer) < 0) {
                    vel = vel.sub(vel.project(pos.sub(closest)));
                }
                if(toPlayer.y > 0.7) {
                    onGround = true;
                }
                break;
            }
        }

        x = pos.x;
        y = pos.y;
        z = pos.z;

        vx = vel.x;
        vy = vel.y;
        vz = vel.z;
    }

    private class CollisionPlane implements Comparable<CollisionPlane> {
        public Vec3 closestPoint;
        public double distance;
        public CollisionPlane(Vec3 closestPoint, double distance) {
            this.closestPoint = closestPoint;
            this.distance = distance;
        }
        public int compareTo(CollisionPlane other) {
            return Double.compare(distance, other.distance);
        }
    }

    private Vec3 closestPointOnLine(Vec3 point, Vec3 linea, Vec3 lineb) {
        Vec3 line = linea.sub(lineb);

        Vec3 frompoint = linea.sub(point);
        if (line.dot(frompoint) < 0) {
            return linea;
        }

        frompoint = lineb.sub(point);
        if (line.dot(frompoint) > 0) {
            return lineb;
        }

        return lineb.sub(frompoint.project(line));
    }
}
