import java.util.Scanner;


public class Test {

    public static void main(String[] args) {
        int l = 2;
        double nodes[] = {0, 0.5, 1};
        double z = -5;
        Vector3f deBorPoints[]  = {
                new Vector3f(-3, -3, z),
                new Vector3f(-2, -1, z),
                new Vector3f(1, 0, z),
                new Vector3f(1, -2.6, z),
                new Vector3f(1.5, -3.2, z)
        };
        BezierSpline3 bezierSpline3 = new BezierSpline3(l, nodes, deBorPoints);
        Vector3f test = bezierSpline3.evaluate(0);
        System.out.println("aaaa");
    }
}
