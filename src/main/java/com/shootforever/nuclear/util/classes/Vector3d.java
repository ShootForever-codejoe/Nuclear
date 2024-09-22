package com.shootforever.nuclear.util.classes;

public class Vector3d {
    public double x;
    public double y;
    public double z;

    public Vector3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3d add(double x, double y, double z) {
        return new Vector3d(x + x, y + y, z + z);
    }

    public Vector3d add(Vector3d vector) {
        return add(vector.x, vector.y, vector.z);
    }

    public Vector3d subtract(double x, double y, double z) {
        return add(-x, -y, -z);
    }

    public Vector3d subtract(Vector3d vector) {
        return add(-vector.x, -vector.y, -vector.z);
    }

    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public Vector3d multiply(double v) {
        return new Vector3d(x * v, y * v, z * v);
    }

    public double distance(Vector3d vector3d) {
        return Math.sqrt(Math.pow(vector3d.x - x, 2.0) + Math.pow(vector3d.y - y, 2.0) + Math.pow(vector3d.z - z, 2.0));
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Vector3d vector
                && Math.floor(x) == Math.floor(vector.x)
                && Math.floor(y) == Math.floor(vector.y)
                && Math.floor(z) == Math.floor(vector.z);
    }


    public void setX(double x) {
        this.x = x;
    }


    public void setY(double y) {
        this.y = y;
    }


    public void setZ(double z) {
        this.z = z;
    }
}
