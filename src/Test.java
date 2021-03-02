import java.util.Scanner;


public class Test {

    public static void main(String[] args) {
        int l = 4;
        double z = -5;
        Vector3f[] cP = {
                new Vector3f(0,0,z),
                new Vector3f(0.3,0.3,z),
                new Vector3f(0.5,0.2,z),
                new Vector3f(0.7,-1,z),
                new Vector3f(1, -0.2, z),
                new Vector3f(1.2, -1, z),
                new Vector3f(1.4, 0, z),
                new Vector3f(1.6, -1.2, z),
                new Vector3f(1.7, -0.5, z),
                new Vector3f(1.8, -1.2, z)
        };
        double[] nodes = new double[5];
        for (int i = 0; i < 5; i++) {
            nodes[i] = (double)i/4;
        }
        BezierSpline3 bezierSpline3 = BezierSpline3.CreateC1Spline(l, nodes, cP);
        Vector3f result = bezierSpline3.evaluate((double)1/4);
        System.out.println("aaaa");
    }
}
