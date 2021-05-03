package rendering;

import java.util.Optional;

public class ConceptualObject extends Diffuse {


    public ConceptualObject(GeometricObject geometricObject) {
        super(geometricObject);
        setAlbedo(new Vector3f(1, 0, 0));
    }


    @Override
    public Vector3f computeColor(IntersectionData intersectionData, Line3d ray, int rayDepth, Scene currentScene) {
        return getAlbedo();
    }
}
