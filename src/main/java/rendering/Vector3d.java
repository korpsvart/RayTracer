package rendering;
import java.awt.*;
import java.util.Random;

import static java.lang.StrictMath.sqrt;

public class Vector3d {

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



    public Vector3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3d(double val[]) {
        this.x = val[0];
        this.y = val[1];
        this.z = val[2];
    }
    public Vector3d(float val[]) {
        this.x = val[0];
        this.y = val[1];
        this.z = val[2];
    }

    public Vector3d add(Vector3d v) {
        return new Vector3d(this.x + v.x, this.y + v.y, this.z + v.z);
    }

    public Vector3d mul(double k) {
        return new Vector3d(this.x * k, this.y * k, this.z * k);
    }

    public double dotProduct(Vector3d v) {
        return (this.x*v.x + this.y*v.y + this.z*v.z);
    }

    public double magnitude() {
        return sqrt(Math.pow(this.x,2) + Math.pow(this.y,2) + Math.pow(this.z, 2));
    }

    public double magnitudeSquared() {
        return (Math.pow(this.x,2) + Math.pow(this.y,2) + Math.pow(this.z, 2));
    }

    public Vector3d normalize() throws ArithmeticException{
        try {
            return this.mul(1/this.magnitude());
        } catch (ArithmeticException ae) {
            ae.printStackTrace();
            throw new ArithmeticException("Error! Trying to normalize zero vector");
        }
    }

    public Vector3d moveTo(Vector3d v) {
        return v.add(this.mul(-1));
    }

    public Vector3d project(Vector3d v) {
        //project this vector onto vector v
        double c = dotProduct(v)/v.magnitudeSquared();
        return v.mul(c);
    }

    public double[] getArray() {
        return new double[]{x,y,z};
    }
    public Vector3d matrixLinearTransform(Matrix3D m) {
        return m.transformVector(this);
    }

    public Vector3d matrixAffineTransform(Matrix4D m) { return m.transformVector(this); }

    public Vector3d matrixAffineTransform(Matrix3D m) {
        //get corresponding affine transform by
        //adding column vector c=0
        Matrix4D m4d = m.get4DMatrix(new Vector3d(0,0, 0));
        return m4d.transformVector(this);
    }


    public Vector3d rotateAroundzAxis(double angle) {
        return this.matrixLinearTransform(Matrix3D.rotationAroundzAxis(angle));
    }

    public Vector3d rotateAroundxAxis(double angle) {
        return this.matrixLinearTransform(Matrix3D.rotationAroundxAxis(angle));
    }
    public Vector3d rotateAroundyAxis(double angle) {
        return this.matrixLinearTransform(Matrix3D.rotationAroundyAxis(angle));
    }

    public double getSphericalPolar() {
        Vector3d v = this.normalize();
        return Math.acos(v.z);

    }

    public double getSphericalAzimuth() {
        //The result is inside [-pi, pi]
        //We remap to [0, 2*pi]
        double az = Math.atan2(y, x);
        return az > 0 ? az : az + 2*Math.PI;
    }


    public static Vector3d sphericToCartesian(double polar, double azimuth, double r) {
        double x = r*Math.sin(polar)*Math.cos(azimuth);
        double y = r*Math.sin(polar)*Math.sin(azimuth);
        double z = r*Math.cos(polar);
        return new Vector3d(x, y, z);
    }

    @Override
    public String toString() {
        return "{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    public static Vector3d colorToVector(Color color) {
        return new Vector3d(color.getColorComponents(null));
    }

    public Color vectorToColor() {
        return new Color(Math.min(1f, (float)x), Math.min(1f,(float)y), Math.min(1f,(float)z));
    }


    public Vector3d crossProduct(Vector3d v) {
        double x = this.y*v.z - this.z*v.y;
        double y = this.z*v.x - this.x*v.z;
        double z = this.x*v.y - this.y*v.x;
        return new Vector3d(x, y, z);
    }

    public Vector3d invert() {
        return new Vector3d(1/x, 1/y, 1/z);
    }

    public Vector3d abs() {
        return new Vector3d(Math.abs(this.x), Math.abs(this.y), Math.abs(this.z));
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vector3d) {
            Vector3d v = (Vector3d)obj;
            Vector3d delta = this.add(v.mul(-1)).abs();
            if (delta.x < epsilon && delta.y < epsilon && delta.z < epsilon) {
                return true;
            } else {
                return false;
            }
        } else {
            return super.equals(obj);
        }
    }

    public Vector3d mix(Vector3d v, double t) {
        return this.mul(1-t).add(v.mul(t));
    }

    public Vector3d elementWiseMul(Vector3d v) {
        return new Vector3d(this.x*v.x, this.y*v.y, this.z*v.z);
    }


    public double distance(Vector3d point) {
        return this.add(point.mul(-1)).magnitude();
    }

    public Vector3d randomUnitVector() {
        Random random = new Random();
        return new Vector3d(random.nextDouble(), random.nextDouble(), random.nextDouble()).normalize();
    }

}
