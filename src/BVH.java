import java.util.Optional;
import java.util.PriorityQueue;

public class BVH {

    private Scene scene;
    Octree octree;

    public BVH(Scene scene, Vector3f minBound, Vector3f maxBound) {
        this.scene = scene;
        this.octree = new Octree(minBound, maxBound);
        for (SceneObject object :
                scene.getSceneObjectsBVH()) {
            octree.insert(object);
        }
        octree.build();
    }

    Optional<IntersectionDataPlusObject> intersect(Line3d ray, RayType rayType) {
        double[][] precalculated = BoundingVolume.precalculateForIntersection(ray);
        Optional<IntersectionDataPlusObject> ret = Optional.empty();
        IntersectionData intersectionDataMin = null;
        SceneObject sceneObjectMin = null;
        double tMin = Double.POSITIVE_INFINITY;
        PriorityQueue<QueueElement> pQueue = new PriorityQueue<>();
        pQueue.add(new QueueElement(octree.getRoot(), 0));
        while(!pQueue.isEmpty() && pQueue.peek().getT() < tMin) {
            Octree.OctreeNode octreeNode = pQueue.poll().getOctreeNode();
            if (octreeNode.isLeaf()) {
                for (SceneObject sceneObject :
                        octreeNode.getObjects()) {
                    Optional<IntersectionData> intersectionData = sceneObject.trace(ray, rayType);
                    if (intersectionData.isPresent() && intersectionData.get().getT() < tMin) {
                        intersectionDataMin = intersectionData.get();
                        tMin = intersectionData.get().getT();
                        sceneObjectMin = sceneObject;
                    }
                }
            } else {
                Octree.OctreeNode[] child = octreeNode.getChild();
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

    public boolean checkVisibility(Line3d ray, double maxDistance, SceneObject caller) {
        //simplified version to
        //check visibility for diffuse objects
        double[][] precalculated = BoundingVolume.precalculateForIntersection(ray);
        double tMin = maxDistance;
        PriorityQueue<QueueElement> pQueue = new PriorityQueue<>();
        pQueue.add(new QueueElement(octree.getRoot(), 0));
        while(!pQueue.isEmpty() && pQueue.peek().getT() < tMin) {
            Octree.OctreeNode octreeNode = pQueue.poll().getOctreeNode();
            if (octreeNode.isLeaf()) {
                for (SceneObject sceneObject :
                        octreeNode.getObjects()) {
                    if (sceneObject != caller) {
                        Optional<IntersectionData> intersectionData = sceneObject.trace(ray, RayType.SHADOW);
                        if (intersectionData.isPresent() && intersectionData.get().getT() < tMin) {
                            return false;
                        }
                    }
                }
            } else {
                Octree.OctreeNode[] child = octreeNode.getChild();
                for (int i = 0; i < 8; i++) {
                    if (child[i]!=null) {
                        Optional<Double> t = child[i].rayIntersection(ray, precalculated);
                        if (t.isPresent() && t.get() < tMin) pQueue.add(new QueueElement(child[i], t.get()));
                    }
                }
            }
        }
        return true;
    }

}
