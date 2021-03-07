import java.util.ArrayDeque;
import java.util.Optional;

public class Octree {

    class OctreeNode {
        private boolean isLeaf;
        private Vector3f centroid;
        private Vector3f minBound;
        private Vector3f maxBound;
        private BoundingVolume extents;
        private OctreeNode[] child;
        private int depth;
        private ArrayDeque<SceneObject> objects;


        public OctreeNode(Vector3f minBound, Vector3f maxBound) {
            this.centroid = new Vector3f(
                    minBound.add(maxBound).getX()/2,
                    minBound.add(maxBound).getY()/2,
                    minBound.add(maxBound).getZ()/2
            );
            this.minBound = minBound;
            this.maxBound = maxBound;
            this.isLeaf = true;
            this.extents = new BoundingVolume();
        }

        public Optional<Double> rayIntersection(Line3d ray, double[][] precalculated) {
            return this.extents.intersect(ray,precalculated);
        }

        public boolean isLeaf() {
            return isLeaf;
        }

        public ArrayDeque<SceneObject> getObjects() {
            return objects;
        }

        public OctreeNode[] getChild() {
            return child;
        }
    }

    private OctreeNode root;
    private static final int MAX_TREE_DEPTH = 16;

    public Octree(Vector3f minBound, Vector3f maxBound) {
        root = new OctreeNode(minBound, maxBound);
    }

    public OctreeNode getRoot() {
        return root;
    }

    private void insert(OctreeNode octreeNode, SceneObject sceneObject) {
        if (octreeNode.isLeaf) {
            if (octreeNode.objects == null) {
                //node is leaf but empty
                octreeNode.objects = new ArrayDeque<>();
                octreeNode.objects.add(sceneObject);
            } else if (octreeNode.objects.isEmpty() || octreeNode.depth >= MAX_TREE_DEPTH) {
                //node is empty leaf or we reached max tree depth
                octreeNode.objects.add(sceneObject);
            } else {
                //leaf already contains an object and we have not reached max depth
                octreeNode.isLeaf = false;
                octreeNode.child = new OctreeNode[8];
                while(!octreeNode.objects.isEmpty()) {
                    insert(root, octreeNode.objects.poll());
                }
                insert(root, sceneObject);
            }
        } else {
            //node is not a leaf
            //check where to go down the tree by comparing
            //centroid of scene object with centroid of node
            //Space is subdivided in 8 regions
            //we use a bitmask:
            //least significant bit tells us if centroid is behind (0) or in front (1)
            //second bit from the right tells us if it's below (0) or above (1)
            //third bit tells us if its on the left (0) or on the right (1)
            int childIndex = 0;
            Vector3f objectCentroid = sceneObject.getBoundingVolume().getCentroid();
            Vector3f nodeCentroid = octreeNode.centroid;
            if (objectCentroid.getX() > nodeCentroid.getX()) childIndex+=4;
            if (objectCentroid.getY() > nodeCentroid.getY()) childIndex+=2;
            if (objectCentroid.getZ() > nodeCentroid.getZ()) childIndex+=1;
            Vector3f newNodeBounds[] = calculateSubRegionBounds(octreeNode, childIndex);
            if (octreeNode.child[childIndex]==null) {
                octreeNode.child[childIndex]=new OctreeNode(newNodeBounds[0], newNodeBounds[1]);
                octreeNode.child[childIndex].depth = octreeNode.depth+1;
            }
            insert(octreeNode.child[childIndex], sceneObject);
        }
    }

    public void insert(SceneObject sceneObject) {
        insert(root, sceneObject);
    }

    private Vector3f[] calculateSubRegionBounds(OctreeNode octreeNode, int childIndex) {
        Vector3f minBound, maxBound;
        Vector3f nodeCentroid = octreeNode.centroid;
        Vector3f nodeMin = octreeNode.minBound;
        Vector3f nodeMax = octreeNode.maxBound;
        double minX = (childIndex & 4) != 0 ? nodeCentroid.getX() : nodeMin.getX();
        double maxX = (childIndex & 4) != 0 ? nodeMax.getX() : nodeCentroid.getX();
        double minY = (childIndex & 2) != 0 ? nodeCentroid.getY() : nodeMin.getY();
        double maxY = (childIndex & 2) != 0 ? nodeMax.getY() : nodeCentroid.getY();
        double minZ = (childIndex & 1) != 0 ? nodeCentroid.getZ() : nodeMin.getZ();
        double maxZ = (childIndex & 1) != 0 ? nodeMax.getZ() : nodeCentroid.getZ();
        minBound = new Vector3f(minX, minY, minZ);
        maxBound = new Vector3f(maxX, maxY, maxZ);
        return new Vector3f[]{minBound, maxBound};
    }

    public void build() {
        build(root);
    }

    private void build(OctreeNode octreeNode) {
        if (octreeNode.isLeaf) {
            for (SceneObject object :
                    octreeNode.objects) {
                octreeNode.extents.extendBy(object.getBoundingVolume());
            }
        } else {
            for (int i = 0; i < 8; i++) {
                if (octreeNode.child[i]!=null) {
                    build(octreeNode.child[i]);
                    octreeNode.extents.extendBy(octreeNode.child[i].extents);
                }
            }
        }
    }




}
