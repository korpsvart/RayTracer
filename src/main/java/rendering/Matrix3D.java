package rendering;

public class Matrix3D {

    //A matrix for linear transforms from 3D space into 3D space

    public static final int ROW_VECTOR = 0;
    public static final int COL_VECTOR = 1;
    public static final Matrix3D identity = new Matrix3D(identity());
    private double mInternal[][] = new double[3][3];

    public Matrix3D(double mInternal[][]) {
        if (mInternal.length !=3 || mInternal[0].length != 3) {
            throw new IllegalArgumentException("Matrix argument should be 3x3");
        } else {
            this.mInternal = mInternal.clone();
        }
    }


    public Matrix3D(Vector3d vectors[], int option) {
        if (option==COL_VECTOR) {
            for (int j = 0; j < 3; j++) {
                Vector3d v = vectors[j];
                for (int i = 0; i < 3; i++) {
                    mInternal[i][j] = v.getElement(i);
                }
            }
        } else if (option==ROW_VECTOR) {
            for (int i = 0; i < 3; i++) {
                Vector3d v = vectors[i];
                for (int j = 0; j < 3; j++) {
                    mInternal[i][j] = v.getElement(j);
                }
            }
        } else {
            throw new IllegalArgumentException("illegal option value");
        }
    }

    public double[][] getmInternal() {
        return mInternal.clone();
    }

    public Vector3d getColumnVector(int j) {
        if (j < 0 || j > 2) {
            throw new IllegalArgumentException("column number should be a number from 0 to 2");
        } else {
            double x = mInternal[0][j];
            double y = mInternal[1][j];
            double z = mInternal[2][j];
            return new Vector3d(x, y, z);
        }
    }


    public Vector3d transformVector(Vector3d v) {
        Vector3d c1 = getColumnVector(0).mul(v.getX());
        Vector3d c2 = getColumnVector(1).mul(v.getY());
        Vector3d c3 = getColumnVector(2).mul(v.getZ());
        return c1.add(c2.add(c3));
    }


    public Matrix3D transpose() {
        double matrix[][] = new double[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j <3; j++) {
                matrix[i][j] = mInternal[j][i];
            }
        }
        return new Matrix3D(matrix);
    }

    public static Matrix3D rotationAroundzAxis(double angle) {
        double theta = Math.toRadians(angle);
        Vector3d c1 = new Vector3d(Math.cos(theta), Math.sin(theta), 0);
        Vector3d c2 = new Vector3d(-Math.sin(theta), Math.cos(theta), 0);
        Vector3d c3  = new Vector3d(0, 0, 1);
        return new Matrix3D(new Vector3d[]{c1, c2, c3}, COL_VECTOR);
    }

    public static Matrix3D rotationAroundxAxis(double angle) {
        double theta = Math.toRadians(angle);
        Vector3d c1 = new Vector3d(1, 0, 0);
        Vector3d c2 = new Vector3d(0, Math.cos(theta), Math.sin(theta));
        Vector3d c3  = new Vector3d(0, -Math.sin(theta), Math.cos(theta));
        return new Matrix3D(new Vector3d[]{c1, c2, c3}, COL_VECTOR);
    }

    public static Matrix3D rotationAroundyAxis(double angle) {
        double theta = Math.toRadians(angle);
        Vector3d c1 = new Vector3d(Math.cos(theta), 0, -Math.sin(theta));
        Vector3d c2 = new Vector3d(0, 1, 0);
        Vector3d c3  = new Vector3d(Math.sin(theta), 0, Math.cos(theta));
        return new Matrix3D(new Vector3d[]{c1, c2, c3}, COL_VECTOR);
    }

    public static Matrix3D rotationAroundArbitraryAxis(double angle, Vector3d axis) {
        axis = axis.normalize();
        double theta = Math.toRadians(angle);
        double cos = Math.cos(theta);
        double sin = Math.sin(theta);
        Vector3d c1 = new Vector3d(cos +Math.pow(axis.getX(),2)*(1- cos),
                axis.getY()*axis.getX()*(1- cos)+axis.getZ()* sin,
                axis.getZ()*axis.getX()*(1- cos)-axis.getY()+ sin);
        Vector3d c2 = new Vector3d(axis.getX()*axis.getY()*(1- cos)-axis.getZ()* sin,
                cos +Math.pow(axis.getY(),2)*(1- cos),
                axis.getZ()*axis.getY()*(1- cos)+axis.getX()* sin);
        Vector3d c3 = new Vector3d(axis.getX()*axis.getZ()*(1- cos)+axis.getY()* sin,
                axis.getY()*axis.getZ()*(1- cos)-axis.getX()* sin,
                cos +Math.pow(axis.getZ(),2)*(1- cos));
        return new Matrix3D(new Vector3d[]{c1,c2,c3}, COL_VECTOR);

    }


    public Matrix4D get4DMatrix(Vector3d c) {
        //Add c vector column for affine transform
        //last row defaults to [0 0 0 1]
        double[][] matrix = new double[4][4];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                matrix[i][j] = mInternal[i][j];
            }
        }
        for (int i = 0; i < 3; i++) {
            matrix[i][3] = c.getElement(i);
        }
        for (int j = 0; j < 3; j++) {
            matrix[3][j] = 0;
        }
        matrix[3][3] = 1;
        return new Matrix4D(matrix);
    }

    public Matrix3D matrixMult(Matrix3D m2) {
        double result[][] = new double[3][3];
        double m2Internal[][] = m2.getmInternal();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    result[i][j] += mInternal[i][k]*m2Internal[k][j];
                }
            }
        }
        return new Matrix3D(result);
    }


    public Matrix3D invertRotationMatrix() {
        //A rotation matrix is an orthogonal matrix
        //Thus we just take the transpose
        return this.transpose();
    }

    private static double[][] identity() {
        double m[][] = new double[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i==j) {
                    m[i][j] = 1;
                } else {
                    m[i][j] = 0;
                }
            }
        }
        return m;
    }



}
