import java.awt.*;

import static java.lang.StrictMath.sqrt;

public class Vector3f{

    public static final double epsilon = 10e-9; //tolerance for equality comparison

    private final double x;
    private final double y;
    private final double z;

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double getElement(int i) {
        if (i==0) {
            return x;
        } else if (i==1) {
            return y;
        } else if (i==2) {
            return z;
        } else {
            throw new IllegalArgumentException("Illegal element number for 3d vector");
        }
    }



    public Vector3f(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3f(double val[]) {
        this.x = val[0];
        this.y = val[1];
        this.z = val[2];
    }
    public Vector3f(float val[]) {
        this.x = val[0];
        this.y = val[1];
        this.z = val[2];
    }

    public Vector3f add(Vector3f v) {
        return new Vector3f(this.x + v.x, this.y + v.y, this.z + v.z);
    }

    public Vector3f mul(double k) {
        return new Vector3f(this.x * k, this.y * k, this.z * k);
    }

    public double dotProduct(Vector3f v) {
        return (this.x*v.x + this.y*v.y + this.z*v.z);
    }

    public double magnitude() {
        return sqrt(Math.pow(this.x,2) + Math.pow(this.y,2) + Math.pow(this.z, 2));
    }

    public double magnitudeSquared() {
        return (Math.pow(this.x,2) + Math.pow(this.y,2) + Math.pow(this.z, 2));
    }

    public Vector3f normalize() throws ArithmeticException{
        try {
            return this.mul(1/this.magnitude());
        } catch (ArithmeticException ae) {
            ae.printStackTrace();
            throw new ArithmeticException("Error! Trying to normalize zero vector");
        }
    }

    public Vector3f moveTo(Vector3f v) {
        return v.add(this.mul(-1));
    }

    public Vector3f project(Vector3f v) {
        //project this vector onto vector v
        double c = dotProduct(v)/v.magnitudeSquared();
        return v.mul(c);
    }

    public Vector3f matrixLinearTransform(Matrix3D m) {
        return m.transformVector(this);
    }

    public Vector3f matrixAffineTransform(Matrix4D m) { return m.transformVector(this); }

    public Vector3f matrixAffineTransform(Matrix3D m) {
        //get corresponding affine transform by
        //adding column vector c=0
        Matrix4D m4d = m.get4DMatrix(new Vector3f(0,0, 0));
        return m4d.transformVector(this);
    }


    public Vector3f rotateAroundzAxis(double angle) {
        return this.matrixLinearTransform(Matrix3D.rotationAroundzAxis(angle));
    }

    public Vector3f rotateAroundxAxis(double angle) {
        return this.matrixLinearTransform(Matrix3D.rotationAroundxAxis(angle));
    }
    public Vector3f rotateAroundyAxis(double angle) {
        return this.matrixLinearTransform(Matrix3D.rotationAroundyAxis(angle));
    }

    public double getSphericalPolar() {
        Vector3f v = this.normalize();
        return Math.acos(v.z);

    }

    public double getSphericalAzimuth() {
        //The result is inside [-pi, pi]
        //We remap to [0, 2*pi]
        double az = Math.atan2(y, x);
        return az > 0 ? az : az + 2*Math.PI;
    }


    public static Vector3f sphericToCartesian(double polar, double azimuth, double r) {
        double x = r*Math.sin(polar)*Math.cos(azimuth);
        double y = r*Math.sin(polar)*Math.sin(azimuth);
        double z = r*Math.cos(polar);
        return new Vector3f(x, y, z);
    }

    @Override
    public String toString() {
        return "Vector3f{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    public static Vector3f colorToVector(Color color) {
        return new Vector3f(color.getColorComponents(null));
    }

    public Color vectorToColor() {
        return new Color(Math.min(1f, (float)x), Math.min(1f,(float)y), Math.min(1f,(float)z));
    }


    public Vector3f crossProduct(Vector3f v) {
        double x = this.y*v.z - this.z*v.y;
        double y = this.z*v.x - this.x*v.z;
        double z = this.x*v.y - this.y*v.x;
        return new Vector3f(x, y, z);
    }

    public Vector3f invert() {
        return new Vector3f(1/x, 1/y, 1/z);
    }

    public Vector3f abs() {
        return new Vector3f(Math.abs(this.x), Math.abs(this.y), Math.abs(this.z));
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vector3f) {
            Vector3f v = (Vector3f)obj;
            Vector3f delta = this.add(v.mul(-1)).abs();
            if (delta.x < epsilon && delta.y < epsilon && delta.z < epsilon) {
                return true;
            } else {
                return false;
            }
        } else {
            return this.equals(obj);
        }
    }

    public Vector3f mix(Vector3f v, double t) {
        return this.mul(1-t).add(v.mul(t));
    }

    public Vector3f elementWiseMul(Vector3f v) {
        return new Vector3f(this.x*v.x, this.y*v.y, this.z*v.z);
    }

}
