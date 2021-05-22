package rendering;

public class ConceptualObject extends Diffuse {


    public ConceptualObject(GeometricObject geometricObject) {
        super(geometricObject);
        setAlbedo(new Vector3d(1, 0, 0));
    }


    @Override
    public Vector3d computeColor(IntersectionData intersectionData, Ray ray, int rayDepth, Scene currentScene) {
        return getAlbedo();
    }
}
