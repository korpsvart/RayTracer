import java.awt.*;
import java.util.Optional;

public class PlaneObject extends Plane3d implements SceneObject{

    private Color color;
    private boolean isDiffuse = true;

    private double ior = 1;


    public void setIor(double ior) {
        this.ior = ior;
    }


    @Override
    public double getIor() {
        return ior;
    }


    public PlaneObject(Vector3f point, Vector3f e1, Vector3f e2, Color color) throws ArithmeticException {
        super(point, e1, e2);
        this.color = color;
    }


    public PlaneObject(double a, double b, double c, double d, Color color) throws Exception {
        super(a, b, c, d);
        this.color = color;
    }

    public PlaneObject(Vector3f point, Vector3f n, Color color) throws Exception {
        super(point, n);
        this.color = color;
    }

    public PlaneObject(Vector3f point, Vector3f n, Color color, boolean isDiffuse) throws Exception {
        this(point, n, color);
        this.isDiffuse = isDiffuse;
    }


    @Override
    public Color getColor() {
        return this.color;
    }

    @Override
    public double getAlbedo() {
        return this.albedo;
    }



    @Override
    public boolean isDiffuse() {
        return isDiffuse;
    }


    @Override
    public Vector3f getSurfaceNormal(Vector3f point) {
        return this.n;
    }
}
