import java.awt.*;
import java.util.Optional;

public class PointLight extends LightSource {
    //PointLight are unaffected by rotation
    //(and by scaling, as any other light)
    //Only translation is relevant
    //Thus we only care about the translation part (c vector)
    //of the affine matrix

    private final Vector3f position;

    public Vector3f getPosition() {
        return position;
    }


    public PointLight(Color color, float intensity, Matrix4D lightToWorld) {
        super(color, intensity, lightToWorld);
        position = lightToWorld.getC();
    }

    public PointLight(Color color, float intensity, Vector3f position) {
        super(color, intensity, new Matrix4D(position));
        this.position = position;
    }


}
