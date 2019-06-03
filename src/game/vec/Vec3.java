package game.vec;

public class Vec3 {
    public double x, y, z;

    public Vec3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3(Vec3 other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
    }

    public Vec3() {
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public Vec3 add(Vec3 other) {
        Vec3 vec = this.clone();
        vec.x += other.x;
        vec.y += other.y;
        vec.z += other.z;
        return vec;
    }

    public Vec3 sub(Vec3 other) {
        Vec3 vec = this.clone();
        vec.x -= other.x;
        vec.y -= other.y;
        vec.z -= other.z;
        return vec;
    }

    public Vec3 mul(double s) {
        Vec3 vec = this.clone();
        vec.x *= s;
        vec.y *= s;
        vec.z *= s;
        return vec;
    }

    public Vec3 div(double s) {
        Vec3 vec = this.clone();
        vec.x /= s;
        vec.y /= s;
        vec.z /= s;
        return vec;
    }

    public double dot(Vec3 vec) {
        return this.x * vec.x + this.y * vec.y + this.z * vec.z;
    }

    public Vec3 cross(Vec3 other) {
        Vec3 vec = this.clone();

        vec.x = y * other.z - z * other.y;
        vec.y = z * other.x - x * other.z;
        vec.z = x * other.y - y * other.x;

        return vec;
    }

    public Vec3 project(Vec3 other) {
        return other.mul(this.dot(other) / other.magnitudeSq());
    }

    public double magnitudeSq() {
        return x * x + y * y + z * z;
    }

    public double magnitude() {
        return Math.sqrt(this.magnitudeSq());
    }

    public Vec3 normalize() {
        return this.div(magnitude());
    }

    @Override
    public String toString() {
        return String.format("(%f, %f, %f)", x, y, z);
    }

    @Override
    public Vec3 clone() {
        return new Vec3(this);
    }
}
