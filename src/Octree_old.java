import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Optional;

public class Octree_old {

    private static final int MAX_TREE_DEPTH = 16;

    class OctreeNode {
        private OctreeNode[] child;
        private BoundingVolume totalExtent;
        private ArrayDeque<BoundingVolume> boundingVolumes;
        private boolean isLeaf;
        private int depth;

        public OctreeNode() {
            this.isLeaf = true;
            this.child = new OctreeNode[8];
            this.totalExtent = new BoundingVolume();
            this.boundingVolumes = new ArrayDeque<>();
        }

        public Optional<Double> intersect(Line3d ray, double[][] precalculated) {
            return totalExtent.intersect(ray, precalculated);
        }

        public boolean isLeaf() {
            return isLeaf;
        }

        public ArrayDeque<BoundingVolume> getBoundingVolumes() {
            return boundingVolumes;
        }

        public OctreeNode[] getChild() {
            return child;
        }
    }



    private OctreeNode root;
    private Vector3f[] bounds = new Vector3f[2];

    public Octree(BoundingVolume totalExtent) {

        double[] extentDNear = totalExtent.getdNear();
        double[] extentDFar = totalExtent.getdFar();

        double xdiff = extentDFar[0] - extentDNear[0];
        double ydiff = extentDFar[1] - extentDNear[1];
        double zdiff = extentDFar[2] - extentDNear[2];
        double dim = Math.max(zdiff, Math.max(xdiff, ydiff));
        Vector3f centroid = new Vector3f(extentDNear[0]+extentDFar[0], extentDNear[1]+extentDFar[1], extentDNear[2]+extentDFar[2]);
        bounds[0] = centroid.add(new Vector3f(dim, dim, dim).mul(-1)).mul(0.5);
        bounds[1]  = centroid.add(new Vector3f(dim, dim, dim)).mul(0.5);
        this.root = new OctreeNode();

    }


    public OctreeNode getRoot() {
        return root;
    }

    public void insert(BoundingVolume boundingVolume) {
        this.insert(root, boundingVolume, bounds[0], bounds[1], 0);
    }


    private void insert(OctreeNode octreeNode, BoundingVolume boundingVolume, Vector3f minBound, Vector3f maxBound, int depth) {
        if (octreeNode.isLeaf) {
            if (octreeNode.boundingVolumes.size() == 0 || depth == MAX_TREE_DEPTH) {
                octreeNode.boundingVolumes.add(boundingVolume);
            } else {
                octreeNode.isLeaf = false;
                while (!octreeNode.boundingVolumes.isEmpty()) {
                    BoundingVolume b = octreeNode.boundingVolumes.pop();
                    insert(root, b, minBound, maxBound, 0);
                }
                insert(root, boundingVolume, minBound, maxBound, 0);
            }
        } else {
            //if node is not a leaf
            double dNear[] = boundingVolume.getdNear();
            double dFar[] = boundingVolume.getdFar();
            Vector3f newCentroid = new Vector3f(dNear[0]+dFar[0], dNear[1]+dFar[1], dNear[2]+dFar[2]).mul(0.5);
            Vector3f boundCentroid = minBound.add(maxBound).mul(0.5);
            int childIndex = 0;
            //least significant bit tells us if centroid is behind (0) or in front (1)
            //second bit from the right tells us if it's below (0) or above (1)
            //third bit tells us if its on the left (0) or on the right (1)
            if (newCentroid.getX() > boundCentroid.getX()) childIndex+=4;
            if (newCentroid.getY() > boundCentroid.getY()) childIndex+=2;
            if (newCentroid.getZ() > boundCentroid.getZ()) childIndex+=1;
            Vector3f newBounds[] = calculateChildBounds(childIndex, minBound, maxBound, boundCentroid);
            if (octreeNode.child[childIndex] == null) {
                octreeNode.child[childIndex] = new OctreeNode();
                octreeNode.child[childIndex].depth=depth+1;
            }
            insert(octreeNode.child[childIndex], boundingVolume, newBounds[0], newBounds[1], depth+1);
        }
    }



    public static Vector3f[] calculateChildBounds(int childIndex, Vector3f minBound, Vector3f maxBound, Vector3f boundCentroid) {
        double[][] bounds = new double[3][2];

        bounds[0][0] = ((childIndex & 4) != 0) ? boundCentroid.getX() : minBound.getX();
        bounds[0][1] = ((childIndex & 4) != 0) ? maxBound.getX() : boundCentroid.getX();
        bounds[1][0] = ((childIndex & 2) != 0) ? boundCentroid.getY() : minBound.getY();
        bounds[1][1] = ((childIndex & 2) != 0) ? maxBound.getY() : boundCentroid.getY();
        bounds[2][0] = ((childIndex & 1) != 0) ? boundCentroid.getZ() : minBound.getZ();
        bounds[2][1] = ((childIndex & 1) != 0) ? maxBound.getZ() : boundCentroid.getZ();
        return new Vector3f[] {new Vector3f(bounds[0][0], bounds[1][0], bounds[2][0]), new Vector3f(bounds[0][1], bounds[1][1], bounds[2][1])};
    }

    public void build() {
        build(root);
    }

    private void build(OctreeNode octreeNode) {
        if (octreeNode.isLeaf) {
            for (BoundingVolume boundingVolume:
                 octreeNode.boundingVolumes) {
                octreeNode.totalExtent.extendBy(boundingVolume);
            }
        } else {
            for (int i = 0; i < 8; i++) {
                if (octreeNode.child[i] != null) {
                    build(octreeNode.child[i]);
                    octreeNode.totalExtent.extendBy(octreeNode.child[i].totalExtent);
                }
            }
        }
    }


}
