import java.awt.*;

public abstract class LightSource {

    //Lights are unaffected by scaling transform

    private final Color color;
    private final float intensity;
    private final Matrix4D lightToWorld;

    public float getIntensity() {
        return intensity;
    }



    protected LightSource(Color color, float intensity, Matrix4D lightToWorld) {
        this.color = color;
        this.intensity = intensity;
        this.lightToWorld = lightToWorld;
    }


    public Color getColor() {
        return color;
    }

}
