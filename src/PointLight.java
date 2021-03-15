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


    @Override
    public LightInfo getDirectionAndDistance(Vector3f hitPoint) {
        Vector3f lightDir = hitPoint.moveTo(position);
        double distance = lightDir.magnitude();
        return new LightInfo(lightDir.normalize(), distance);
    }

    public Vector3f illuminate(double distance) {
        double intensity = this.getIntensity()/(4*Math.PI*Math.pow(distance, 2));
        return Vector3f.colorToVector(this.getColor()).mul(intensity);
    }


}
