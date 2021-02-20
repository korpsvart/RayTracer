public class IntersectionDataScene {

    private Double t;

    public Double getT() {
        return t;
    }

    public SceneObject getSceneObject() {
        return sceneObject;
    }

    private SceneObject sceneObject;

    public IntersectionDataScene(Double t, SceneObject sceneObject) {
        this.t = t;
        this.sceneObject = sceneObject;
    }
}
