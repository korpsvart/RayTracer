public class PolygonMesh {

    private int faceIndex[];
    private int vertexIndex[];
    private Vector3f vertex[];
    private int nFaces;

    public PolygonMesh(int[] faceIndex, int[] vertexIndex, Vector3f[] vertex) {
        this.faceIndex = faceIndex.clone();
        this.vertexIndex = vertexIndex.clone();
        this.vertex = vertex.clone();
        nFaces = faceIndex.length;
    }




}
