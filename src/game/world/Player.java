package game.world;

import game.client.ClientLoop;
import game.client.model.Model;
import game.client.model.ModelFace;
import game.vec.Vec3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Player implements ITickable {
    private double x, y, z;
    private double tx, ty, tz; // Buffering position to prevent jittering
    private double pitch, yaw;
    private double vx, vy, vz;
    private double radius = 1;
    private double speed = 0.05;
    private int death = 0;
    private int id;
    private boolean onGround;
    private ClientLoop loop;

    private double ammunition = MAX_AMMUNITION;
    private double ammunition_animation = ammunition;
    public static final double MAX_AMMUNITION = 4;

    private double health = MAX_HEALTH;
    private double health_animation = health;
    public static final double MAX_HEALTH = 3;

    private static int PLAYER_MODEL;

    public Player(int id, ClientLoop loop) {
        this.id = id;
        this.loop = loop;
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
        tx = x;
    }

    public void addX(double x) {
        this.x += x;
        tx = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
        ty = y;
    }

    public void addY(double y) {
        this.y += y;
        ty = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
        tz = z;
    }

    public void addZ(double z) {
        this.z += z;
        tz = z;
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

    public void reset(double x, double y, double z) {
        this.ammunition = MAX_AMMUNITION;
        this.ammunition_animation = this.ammunition;
        this.health = MAX_HEALTH;
        this.health_animation = MAX_HEALTH;

        this.setX(x);
        this.setY(y);
        this.setZ(z);
    }

    @Override
    public void tick() {
        if (this.ammunition < this.ammunition_animation) {
            this.ammunition_animation -= 0.05;
        } else {
            this.ammunition_animation = this.ammunition;
        }

        if (this.health < this.health_animation) {
            this.health_animation -= 0.05;
        } else {
            this.health_animation = this.health;
        }

        if (death > 0) {
            death -= 1;

            if (death <= 0) {
                reset(0, 10, 0);
            } else {
                return;
            }
        }

        if (this.ammunition + 0.001 < MAX_AMMUNITION) {
            this.addAmmunition(0.01);
        } else {
            this.ammunition = MAX_AMMUNITION;
        }

        if (this.health + 0.0002 < MAX_HEALTH) {
            this.addHealth(0.001);
        } else {
            this.health = MAX_HEALTH;
        }

        tx = x + vx;
        ty = y + vy;
        tz = z + vz;

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

        vx *= 0.9;
        vy *= 0.9;
        vz *= 0.9;

        vy -= 0.05;

        if (ty < 0.6) {
            ty = 0.6;
            vy = 0;
        }

        onGround = false;
        ArrayList<CollisionPlane> planes = generateCollisionCandidates();
        for (int i = 0; i < 2; ++i) {
            collide(planes);
        }

        x = tx;
        y = ty;
        z = tz;
    }

    private Vec3 closestPointInTriangle(Vec3 a, Vec3 b, Vec3 c, Vec3 p) {
        Vec3 normal = b.sub(a).cross(c.sub(a)).normalize();

        Vec3 nearest = p.sub(p.sub(a).project(normal)); // Nearest point on the plane to player center

        Vec3 ab = closestPointOnLine(p, a, b); // Nearest point on a line segment to player center
        Vec3 ac = closestPointOnLine(p, a, c);
        Vec3 bc = closestPointOnLine(p, b, c);

        double abm = p.sub(ab).magnitudeSq(); // Distances to player (ab, ac, bc)
        double acm = p.sub(ac).magnitudeSq();
        double bcm = p.sub(bc).magnitudeSq();

        if (!pointInTriangle(p, normal, a, b, c)) {
            if (abm <= acm && abm <= bcm) { // Check which line has a point closest to player
                return ab;
            } else if (acm <= abm && acm <= bcm) {
                return ac;
            } else {
                return bc;
            }
        }

        return nearest;
    }

    private boolean pointInTriangle(Vec3 point, Vec3 normal, Vec3 a, Vec3 b, Vec3 c) {
        double sa = b.sub(a).cross(point.sub(a)).dot(normal); // Determine cross products
        double sb = c.sub(b).cross(point.sub(b)).dot(normal);
        double sc = a.sub(c).cross(point.sub(c)).dot(normal);

        return (sa < 0 && sb < 0 && sc < 0) || (sa > 0 && sb > 0 && sc > 0);// Use cross products to determine whether point is inside triangle
    }

    public CollisionTarget rayTrace(Vec3 p, Vec3 dir, Collection<Player> players) {
        CollisionTarget closest = new CollisionTarget();
        for (Player s : players) {
            Vec3 v = new Vec3(s.x, s.y, s.z);
            Vec3 p0 = p.sub(v);
            double a = dir.magnitudeSq();
            double b = 2 * p0.dot(dir);
            double c = p0.magnitudeSq() - s.radius * s.radius;
            double disc = b * b - 4 * a * c;
            if (disc > 0) {
                double root = (-b - Math.sqrt(disc)) / (2 * a);
                if (root < 0) {
                    root = (-b + Math.sqrt(disc)) / (2 * a);
                }
                if (root > 0) {
                    Vec3 collisionPoint = p.add(dir.mul(root));
                    double distance = collisionPoint.sub(p).magnitude();
                    if (distance < closest.distance) {
                        closest.type = CollisionTarget.TargetType.PLAYER;
                        closest.hitFace = null;
                        closest.hitPlayer = s;
                        closest.hitPoint = collisionPoint;
                        closest.distance = distance;
                    }
                }
            }
        }

        dir = dir.normalize();

        Model model = loop.getWorld().getModel();
        for (ModelFace face : model.getFaces()) {
            Vec3 a = face.a.toVec3();
            Vec3 b = face.b.toVec3();
            Vec3 c = face.c.toVec3();

            Vec3 normal = b.sub(a).cross(c.sub(a));

            double d = a.sub(p).dot(normal) / dir.dot(normal);
            if (d > 0) {
                Vec3 pointOnPlane = p.add(dir.mul(d));
                if (pointInTriangle(pointOnPlane, normal, a, b, c)) {
                    double distance = pointOnPlane.sub(p).magnitude();
                    if (distance < closest.distance) {
                        closest.type = CollisionTarget.TargetType.FACE;
                        closest.hitFace = face;
                        closest.hitPlayer = null;
                        closest.hitPoint = pointOnPlane;
                        closest.distance = distance;
                    }
                }
            }
        }
        return closest;
    }

    private ArrayList<CollisionPlane> generateCollisionCandidates() {
        ArrayList<CollisionPlane> planes = new ArrayList<>();

        Vec3 pos = new Vec3(tx, ty, tz);
        Model model = loop.getWorld().getModel();

        for (ModelFace face : model.getFaces()) {
//            if(face.a.toVec3().sub(pos).magnitude() < 3) {
            planes.add(new CollisionPlane(null, 0, face));
//            }
        }

        return planes;
    }

    private void collide(ArrayList<CollisionPlane> planes) {
        Vec3 pos = new Vec3(tx, ty, tz);
        Vec3 vel = new Vec3(vx, vy, vz);

        for (CollisionPlane plane : planes) {
            Vec3 closest = closestPointInTriangle(plane.face.a.toVec3(), plane.face.b.toVec3(), plane.face.c.toVec3(), pos);
            plane.closestPoint = closest;
            plane.distance = pos.sub(closest).magnitude();
        }

        Collections.sort(planes); // Sort these planes from closest to farthest, to prevent colliding with invisible edges

        // Collide with the nearest plane first
        for (CollisionPlane plane : planes) {
            Vec3 closest = plane.closestPoint;
            if (plane.distance < radius) {
                Vec3 toPlayer = pos.sub(closest).normalize();
                pos = toPlayer.mul(radius - 1e-6).add(closest);
                if (vel.dot(toPlayer) < 0) {
                    vel = vel.sub(vel.project(pos.sub(closest)));
                }
                if (toPlayer.y > 0.7) { // Cosine of max slope of the plane > 0.7, jumpable
                    onGround = true;
                }
                break;
            }
        }

        tx = pos.x;
        ty = pos.y;
        tz = pos.z;

        vx = vel.x;
        vy = vel.y;
        vz = vel.z;
    }

    public double getAmmunition() {
        return ammunition;
    }

    public double getAmmunitionAnimation() {
        return ammunition_animation;
    }

    public void setAmmunition(double ammunition) {
        this.ammunition = ammunition;
    }

    public void addAmmunition(double ammunition) {
        this.ammunition += ammunition;
    }

    public double getHealth() {
        return health;
    }

    public double getHealthAnimation() {
        return health_animation;
    }

    public void setHealth(double health) {
        this.health = health;
        if (this.health <= 0) {
            death = 300;
        }
    }

    public void addHealth(double health) {
        this.setHealth(this.getHealth() + health);
    }

    public int getId() {
        return id;
    }

    public int getDeath() {
        return death;
    }

    public void setDeath(int death) {
        this.death = death;
    }

    public static class CollisionTarget {
        public enum TargetType {
            NONE,
            PLAYER,
            FACE
        }

        public TargetType type = TargetType.NONE;

        public Vec3 hitPoint;
        public double distance = Double.POSITIVE_INFINITY;

        public Player hitPlayer;
        public ModelFace hitFace;
    }

    private static class CollisionPlane implements Comparable<CollisionPlane> {
        public Vec3 closestPoint;
        public double distance;
        public ModelFace face;

        public CollisionPlane(Vec3 closestPoint, double distance, ModelFace face) {
            this.closestPoint = closestPoint;
            this.distance = distance;
            this.face = face;
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

    private static int genList() {
        int displayList = glGenLists(1);
        glNewList(displayList, GL_COMPILE);
        glBegin(GL_TRIANGLES);

        int i, j;
        for (i = 0; i <= 20; i++) {
            double lat0 = Math.PI * (-0.5 + (double) (i - 1) / 20);
            double z0 = Math.sin(lat0);
            double zr0 = Math.cos(lat0);

            double lat1 = Math.PI * (-0.5 + (double) i / 20);
            double z1 = Math.sin(lat1);
            double zr1 = Math.cos(lat1);

            glBegin(GL_QUAD_STRIP);
            for (j = 0; j <= 20; j++) {
                double lng = 2 * Math.PI * (double) (j - 1) / 20;
                double x = Math.cos(lng);
                double y = Math.sin(lng);

                glNormal3d(x * zr0, y * zr0, z0);
                glVertex3d(x * zr0, y * zr0, z0);
                glNormal3d(x * zr1, y * zr1, z1);
                glVertex3d(x * zr1, y * zr1, z1);
            }
            glEnd();
        }

        glEnd();
        glEndList();

        return displayList;
    }

    public static void init() {
        PLAYER_MODEL = genList();
    }

    public void render() {
        glPushMatrix();

        glColor3d(1, 1, 1);

        glTranslated(x, y, z);
        glScaled(radius, radius, radius);
        glRotated(yaw, 0, 1, 0);
        glRotated(pitch, 1, 0, 0);

        glCallList(PLAYER_MODEL);

        glPopMatrix();
    }
}
