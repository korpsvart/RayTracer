import java.util.ArrayDeque;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;

public class BoundingVolumesHierarchy_old {

    Scene renderingScene;
    BoundingVolume sceneExtent;
    Octree octree;

    public BoundingVolumesHierarchy(Scene renderingScene) {
        this.renderingScene = renderingScene;
        this.sceneExtent = new BoundingVolume();
        for (SceneObject sO:
             renderingScene.getSceneObjects()) {
            BoundingVolume b = sO.getBoundingVolume();
            this.sceneExtent.extendBy(b);
        }

        this.octree = new Octree(sceneExtent);
        for (SceneObject sO :
                renderingScene.getSceneObjects()) {
            BoundingVolume b = sO.getBoundingVolume();
            octree.insert(b);
        }
        octree.build();
    }

    public Optional<IntersectionDataPlusObject> intersect(Line3d ray, RayType rayType) {
        double[][] precalculated = BoundingVolume.precalculateForIntersection(ray);
        PriorityQueue<QueueElement> pQueue = new PriorityQueue<>();
        double tMin = Double.POSITIVE_INFINITY;
        Optional<IntersectionDataPlusObject> ret = Optional.empty();
        Optional<Double> x = octree.getRoot().intersect(ray, precalculated);
        if (!x.isPresent()) return Optional.empty();
        pQueue.add(new QueueElement(octree.getRoot(), Double.NEGATIVE_INFINITY));
        while(!pQueue.isEmpty() && pQueue.peek().getT() < tMin) { //the t check here should be redundant
            QueueElement queueElement = pQueue.poll();
            Octree.OctreeNode octreeNode = queueElement.getOctreeNode();
            if (octreeNode.isLeaf()) {
                ArrayDeque<BoundingVolume> boundingVolumes = octreeNode.getBoundingVolumes();
                for (BoundingVolume b :
                        boundingVolumes) {
                    SceneObject sO = b.getSceneObject();
                    Optional<IntersectionData> t = sO.trace(ray, rayType);
                    if (t.isPresent() && t.get().getT() < tMin) {
                        tMin = t.get().getT();
                        ret = Optional.of(new IntersectionDataPlusObject(t.get(), sO));
                    }
                }
            } else {
                //node is not a leaf
                Octree.OctreeNode[] child = octreeNode.getChild();
                for (int i = 0; i < 8; i++) {
                    if (child[i] != null) {
                        Optional<Double> t = child[i].intersect(ray, precalculated);
                        if (t.isPresent() && t.get() < tMin) pQueue.add(new QueueElement(child[i], t.get()));
                    }
                }
            }
        }
        return ret;
    }
}
