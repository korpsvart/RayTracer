package rendering;

public class IntersectionDataPlusObject extends IntersectionData {

    private SceneObject sceneObject;

    public IntersectionDataPlusObject(double t, double u, double v, SceneObject sceneObject) {
        super(t, u, v);
        this.sceneObject = sceneObject;
    }

    public IntersectionDataPlusObject(double t, SceneObject sceneObject) {
        super(t);
        this.sceneObject = sceneObject;
    }

    public IntersectionDataPlusObject(IntersectionData intersectionData, SceneObject sceneObject) {
        super(intersectionData.getT(), intersectionData.getU(), intersectionData.getV());
        this.sceneObject = sceneObject;
    }

    public IntersectionData getIntersectionData() {
        return new IntersectionData(this.getT(), this.getU(), this.getV());
    }

    public SceneObject getSceneObject() {
        return sceneObject;
    }
}
