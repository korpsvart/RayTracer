import java.awt.*;
import java.util.Optional;

public interface SceneObject {

    double albedo = 0.3; //default value

    public abstract Color getColor();
    public abstract double getAlbedo();
    public abstract double getIor();
    public abstract boolean isDiffuse();
    public abstract Optional<Double> rayIntersection(Line3d ray);
    public abstract Vector3f getSurfaceNormal(Vector3f point);

}
