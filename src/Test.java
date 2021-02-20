import java.util.Scanner;


public class Test {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        double[] coords = new double[3];
        String s;
        String[] values = new String[3];

        System.out.println("Insert origin of ray");
        s = sc.nextLine();
        values = s.split(",");
        for (int i = 0; i < 3; i++) {
            coords[i] = Double.parseDouble(values[i]);
        }
        Vector3f point = new Vector3f(coords[0], coords[1], coords[2]);
        System.out.println("Insert direction vector");
        s = sc.nextLine();
        values = s.split(",");
        for (int i = 0; i < 3; i++) {
            coords[i] = Double.parseDouble(values[i]);
        }
        Vector3f direction = new Vector3f(coords[0], coords[1], coords[2]);
        System.out.println("Insert centre of sphere");
        s = sc.nextLine();
        values = s.split(",");
        for (int i = 0; i < 3; i++) {
            coords[i] = Double.parseDouble(values[i]);
        }
        Vector3f centre = new Vector3f(coords[0], coords[1], coords[2]);
        System.out.println("Insert radius of sphere");
        s = sc.nextLine();
        double radius = Double.parseDouble(s);


        Line3d ray = new Line3d(point, direction);
/*        Sphere sphere = new Sphere(centre, radius, color);

        ArrayList<Vector3f> intercepts = ray.sphereRayIntersect(sphere);

        for (Vector3f x:
                    intercepts) {
            System.out.println(x.toString());*/
    }
}
