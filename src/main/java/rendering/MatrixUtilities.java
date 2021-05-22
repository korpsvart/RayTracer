package rendering;

import static java.util.stream.IntStream.range;

public class MatrixUtilities {






    static double dotProduct(double[] a, double[] b) {
        return range(0, a.length).mapToDouble(i -> a[i] * b[i]).sum();
    }

    static void transpose(double[][] m) {
        for (int i = 0; i < m[0].length; i++) {
            for (int j = 0; j < m.length; j++) {
                m[i][j] = m[j][i];
            }
        }
    }

    static double[][] transpose2(double[][] m) {
        //not in place
        double[][] t = new double[m[0].length][m.length];
        for (int i = 0; i < t.length; i++) {
            for (int j = 0; j < t[0].length; j++) {
                t[i][j] = m[j][i];
            }
        }
        return t;
    }

    static Vector3d[][] transpose2(Vector3d[][] m) {
        //not in place
        Vector3d[][] t = new Vector3d[m[0].length][m.length];
        for (int i = 0; i < t.length; i++) {
            for (int j = 0; j < t[0].length; j++) {
                t[i][j] = m[j][i];
            }
        }
        return t;
    }

    public static double[][] matrixMult(double[][] m1, double[][] m2) {
        double result[][] = new double[m1.length][m2[0].length];
        for (int i = 0; i < m1.length; i++) {
            for (int j = 0; j < m2[0].length; j++) {
                for (int k = 0; k < m1[0].length; k++) {
                    result[i][j] += m1[i][k]*m2[k][j];
                }
            }
        }
        return result;
    }

    public static double[][] matrixMult(double[][] m1, double[] m2) {
        double result[][] = new double[m1.length][1];
        for (int i = 0; i < m1.length; i++) {
                for (int k = 0; k < m1[0].length; k++) {
                    result[i][0] += m1[i][k]*m2[k];
                }
        }
        return result;
    }

    static double[][] matrixMul(double[][] A, double[][] B) {
        double[][] result = new double[A.length][B[0].length];
        double[] aux = new double[B.length];

        for (int j = 0; j < B[0].length; j++) {

            for (int k = 0; k < B.length; k++)
                aux[k] = B[k][j];

            for (int i = 0; i < A.length; i++)
                result[i][j] = dotProduct(A[i], aux);
        }
        return result;
    }

    static double[][] pivotize(double[][] m) {
        int n = m.length;
        double[][] id = range(0, n).mapToObj(j -> range(0, n)
                .mapToDouble(i -> i == j ? 1 : 0).toArray())
                .toArray(double[][]::new);

        for (int i = 0; i < n; i++) {
            double maxm = m[i][i];
            int row = i;
            for (int j = i; j < n; j++)
                if (m[j][i] > maxm) {
                    maxm = m[j][i];
                    row = j;
                }

            if (i != row) {
                double[] tmp = id[i];
                id[i] = id[row];
                id[row] = tmp;
            }
        }
        return id;
    }

    static double[][][] lu(double[][] A) {
        int n = A.length;
        double[][] L = new double[n][n];
        double[][] U = new double[n][n];
        double[][] P = pivotize(A);
        double[][] A2 = matrixMul(P, A);

        for (int j = 0; j < n; j++) {
            L[j][j] = 1;
            for (int i = 0; i < j + 1; i++) {
                double s1 = 0;
                for (int k = 0; k < i; k++)
                    s1 += U[k][j] * L[i][k];
                U[i][j] = A2[i][j] - s1;
            }
            for (int i = j; i < n; i++) {
                double s2 = 0;
                for (int k = 0; k < j; k++)
                    s2 += U[k][j] * L[i][k];
                L[i][j] = (A2[i][j] - s2) / U[j][j];
            }
        }
        return new double[][][]{L, U, P};
    }


    public static double[][] solve(double[][] a, double[][] b) {
        int n = a.length;
        int h = b[0].length;
        double[][][] lup = lu(a);
        double[][] l = lup[0];
        double[][] u = lup[1];
        double[][] p = lup[2];
        //if we dont care about preserving the b matrix, we can use it
        //for both the intermediate y matrix and the final x matrix

       return backForwardSubstitution(l, u, p, b);

    }

    public static double[][] backForwardSubstitution(double[][] l, double[][] u, double[][] p, double[][] b) {
        //solve systems of linear equations from already available LUP decomposition
        //(Useful when we can pre-compute a decomposition and re-use it for multiple computations)


        //forward substitution
        //we use b as auxiliary matrix y
        int n = l.length; //need adjustment if we want to handle non-square matrices?
        int h = b[0].length;
        for (int j = 0; j < h; j++) {
            b[0][j] /= l[0][0];
            for (int i = 1; i < n; i++) {
                double c = 0;
                for (int k = 0; k <= i-1; k++) {
                    c+=l[i][k]*b[k][j];
                }
                b[i][j] = (b[i][j]-c)/l[i][i];
            }
        }

        //backward substitution
        //y values get replaced with final x solution values
        for (int j = 0; j <h; j++) {
            b[n - 1][j] /= u[n - 1][n - 1];
            for (int i = n - 2; i >= 0; i--) {
                double c = 0;
                for (int k = i + 1; k < n; k++) {
                    c += u[i][k] * b[k][j];
                }
                b[i][j] = (b[i][j] - c) / u[i][i];
            }
        }

        //invert permutation matrix p
        //(i.e. transpose)
        transpose(p);
        return matrixMult(p, b);
    }


    public static double[][] solve(double[][] a, double[] b) {
        int n = a.length;
        double[][][] lup = lu(a);
        double[][] l = lup[0];
        double[][] u = lup[1];
        double[][] p = lup[2];
        //if we dont care about preserving the b matrix, we can use it
        //for both the intermediate y matrix and the final x matrix

        //forward substitution
        //we use b as auxiliary matrix y
            b[0] /= l[0][0];
            for (int i = 1; i < n; i++) {
                double c = 0;
                for (int k = 0; k <= i-1; k++) {
                    c+=l[i][k]*b[k];
                }
                b[i] = (b[i]-c)/l[i][i];
            }

        //backward substitution
        //y values get replaced with final x solution values
            b[n-1]/= u[n-1][n-1];
            for (int i = n-2; i >=0; i--) {
                double c = 0;
                for (int k = i+1; k < n; k++) {
                    c+=u[i][k]*b[k];
                }
                b[i] = (b[i]-c)/u[i][i];
            }
        //invert permutation matrix p
        //(i.e. transpose)
        transpose(p);
        return matrixMult(p, b);


    }

}
