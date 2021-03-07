public class QueueElement implements Comparable<QueueElement> {

    private Octree.OctreeNode octreeNode;
    private double t;

    public QueueElement(Octree.OctreeNode octreeNode, double t) {
        this.octreeNode = octreeNode;
        this.t = t;
    }

    @Override
    public int compareTo(QueueElement queueElement) {
        return Double.compare(this.t, queueElement.t);
    }

    public double getT() {
        return t;
    }

    public Octree.OctreeNode getOctreeNode() {
        return octreeNode;
    }
}