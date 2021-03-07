import java.util.Optional;
import java.util.PriorityQueue;

public class BVH {

    private Scene scene;
    OctreeV2 octree;

    public BVH(Scene scene, Vector3f minBound, Vector3f maxBound) {
        this.scene = scene;
        this.octree = new OctreeV2(minBound, maxBound);
        for (SceneObject object :
                scene.getSceneObjects()) {
            octree.insert(object);
        }
        octree.build();
    }

    Optional<IntersectionDataPlusObject> rayIntersect(Line3d ray, RayType rayType) {
        double[][] precalculated = BoundingVolume.precalculateForIntersection(ray);
        Optional<IntersectionDataPlusObject> ret = Optional.empty();
        IntersectionData intersectionDataMin = null;
        SceneObject sceneObjectMin = null;
        double tMin = Double.POSITIVE_INFINITY;
        PriorityQueue<QueueElement> pQueue = new PriorityQueue<>();
        pQueue.add(new QueueElement(octree.getRoot(), 0));
        while(!pQueue.isEmpty() && pQueue.peek().getT() < tMin) {
            OctreeV2.OctreeNode octreeNode = pQueue.poll().getOctreeNode();
            if (octreeNode.isLeaf()) {
                for (SceneObject sceneObject :
                        octreeNode.getObjects()) {
                    Optional<IntersectionData> intersectionData = sceneObject.rayIntersection(ray);
                    if (intersectionData.isPresent() && intersectionData.get().getT() < tMin) {
                        intersectionDataMin = intersectionData.get();
                        sceneObjectMin = sceneObject;
                    }
                }
            } else {
                OctreeV2.OctreeNode[] child = octreeNode.getChild();
                for (int i = 0; i < 8; i++) {
                    if (child[i]!=null) {
                        Optional<Double> t = child[i].rayIntersection(ray, precalculated);
                        if (t.isPresent() && t.get() < tMin) pQueue.add(new QueueElement(child[i], t.get()));
                    }
                }
            }
        }
        if (sceneObjectMin != null) {
            ret = Optional.of(new IntersectionDataPlusObject(intersectionDataMin, sceneObjectMin));
        }
        return ret;
    }

}
