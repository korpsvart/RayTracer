package rendering;

public class Matrix4D {

    //A matrix to describe affine transform
    //using homogeneous points

    public static final Matrix4D identity = new Matrix4D(Matrix3D.identity, new Vector3d(0, 0, 0));

    private double mInternal[][] = new double[4][4];
    private Matrix3D a;
    private Vector3d c;

    public Matrix4D(double[][] matrix) {

        mInternal = matrix.clone();
        a = this.getA();
        c = this.getC();
    }

    public Matrix4D(Matrix3D a, Vector3d c) {
        this.a = a;
        this.c = c;
        double[][] m = a.getmInternal();
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                mInternal[i][j] = m[i][j];
            }
        }
        for (int i = 0; i < 3; i++) {
            mInternal[i][3] = c.getElement(i);
        }
        for (int j = 0; j < 3; j++) {
            mInternal[3][j] = 0;
        }
        mInternal[3][3] = 1;
    }

    public Matrix4D(Vector3d c) {
        //Pure translation matrix
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                mInternal[i][j] = 0;
            }
        }
        for (int i = 0; i < 3; i++) {
            mInternal[i][3] = c.getElement(i);
        }
        for (int j = 0; j < 3; j++) {
            mInternal[3][j] = 0;
        }
        mInternal[3][3] = 1;
    }

    public double[][] getmInternal() {
        return mInternal.clone();
    }

    public Matrix3D getA() {
        //get the linear part of affine transform
        double matrix[][] = new double[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                matrix[i][j] = mInternal[i][j];
            }
        }
        return new Matrix3D(matrix);
    }

    public Vector3d getC() {
        //get translational part of affine transform
        Vector3d c = new Vector3d(mInternal[0][3], mInternal[1][3], mInternal[2][3]);
        return c;
    }

    public Vector3d transformVector(Vector3d v) {
        Vector3d vTransform;
        double x = 0;
        double y = 0;
        double z = 0;
        double w = 0;
        for (int j = 0; j < 3; j++) {
            x += mInternal[0][j]*v.getElement(j);
        }
        for (int j = 0; j < 3; j++) {
            y += mInternal[1][j]*v.getElement(j);
        }
        for (int j = 0; j < 3; j++) {
            z += mInternal[2][j]*v.getElement(j);
        }
        for (int j = 0; j < 3; j++) {
            w += mInternal[3][j]*v.getElement(j);
        }
        x+=mInternal[0][3];
        y+=mInternal[1][3];
        z+=mInternal[2][3];
        w+=mInternal[3][3];
        vTransform = new Vector3d(x, y, z);
        if (w != 1 && w != 0) {
            vTransform = vTransform.mul(1/w);
        }
        return vTransform;
    }

    public Vector3d[] transformVector(Vector3d[] v) {
        Vector3d[] transformed = new Vector3d[v.length];
        for (int i = 0; i < transformed.length; i++) {
            transformed[i] = transformVector(v[i]);
        }
        return transformed;
    }

    public Vector3d transformVector(double[] v) {
        Vector3d vector3D = new Vector3d(v);
        return transformVector(vector3D);
    }

    public double[][] transformVector(double[][] v) {
        double[][] result = new double[v.length][3];
        for (int i = 0; i < result.length; i++) {
            Vector3d vector3D = transformVector(v[i]);
            result[i] = new double[]{vector3D.getX(), vector3D.getY(), vector3D.getZ()};
        }
        return result;
    }

    public Vector3d[][] transformVector(Vector3d[][] v) {
        Vector3d[][] transformed = new Vector3d[v.length][v[0].length];
        for (int i = 0; i < transformed.length; i++) {
            transformed[i] = transformVector(v[i]);
        }
        return transformed;
    }




    public Matrix4D matrixMult(Matrix4D m2) {
        double result[][] = new double[4][4];
        double m2Internal[][] = m2.getmInternal();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    result[i][j] += mInternal[i][k]*m2Internal[k][j];
                }
            }
        }
        return new Matrix4D(result);
    }

    public Matrix4D invertRotationMatrix() {
        //see method for 3D matrix
        Matrix3D m = this.getA().invertRotationMatrix();
        return m.get4DMatrix(new Vector3d(0, 0, 0));
    }


}
