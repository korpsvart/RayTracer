public class QueueElement implements Comparable<QueueElement> {

    private OctreeV2.OctreeNode octreeNode;
    private double t;

    public QueueElement(OctreeV2.OctreeNode octreeNode, double t) {
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

    public OctreeV2.OctreeNode getOctreeNode() {
        return octreeNode;
    }
}