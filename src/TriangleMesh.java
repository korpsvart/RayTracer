import java.util.Optional;

public class TriangleMesh extends GeometricObject {


    //create triangle mesh from arbitrary
    //N-vertices polygon mesh

    //Face index array is not needed
    private int vertexIndex[];
    private Vector3f[] vertex;
    private int numTriangles;
    private int numVertices;
    private BoundingBox boundingBox;

    public TriangleMesh(int faceIndex[], int vertexIndex[], Vector3f vertex[]) {
        //This is an algorithm I came up with
        //Don't know if it's correct and how fast it is

        //First loop is only to allocate exact size
        //for vertexIndex array
        this.vertex = vertex.clone();
        numVertices = 0;
        numTriangles = 0;
        for (int i = 0; i < faceIndex.length; i++) {
            numTriangles += faceIndex[i]-2;
        }
        numVertices = numTriangles*3;
        this.vertexIndex = new int[numVertices];

        int triPerFace = 0;
        int localFaceOffset = 0;
        int polygonFaceOffset = 0;
        for (int i = 0; i < faceIndex.length; i++) {
            triPerFace = faceIndex[i] - 2;
            for (int j = 0; j < triPerFace; j++) {
                this.vertexIndex[localFaceOffset + j*3] = vertexIndex[polygonFaceOffset];
                this.vertexIndex[localFaceOffset + j*3 + 1] = vertexIndex[polygonFaceOffset + j + 1];
                this.vertexIndex[localFaceOffset + j*3 + 2] = vertexIndex[polygonFaceOffset + j + 2];
            }
            localFaceOffset+=triPerFace*3;
            polygonFaceOffset+=faceIndex[i];
        }

        makeTriangles();

        //If bounding box is not given explicitly by caller
        //compute it here from vertices
        boundingBox = new BoundingBox(vertex);

    }

    public TriangleMesh(int faceIndex[], int vertexIndex[], Vector3f vertex[], BoundingBox boundingBox) {
        //bounding box given explicitly (ex. for bezier surfaces)
        this(faceIndex, vertexIndex, vertex);
        this.boundingBox = boundingBox;
    }

    public void makeTriangles() {
        for (int i = 0; i < numTriangles; i++) {
            Triangle triangle = new Triangle(vertex[vertexIndex[i*3]], vertex[vertexIndex[i*3+1]], vertex[vertexIndex[i*3+2]]);

        }
    }

    @Override
    public Optional<IntersectionDataGeometric> rayIntersection(Line3d ray) {
        //loop over all triangles
        //return minimum t (distance of intersection)
        //and corresponding triangle
        //(if found anything)

        //bounding box check
        if (!this.boxCheck(ray)) {
            return Optional.empty();
        }

        double minT = Double.POSITIVE_INFINITY;
        Optional<IntersectionDataGeometric> intersectionDataGeometricMin = Optional.empty();
        for (Triangle triangle :
                triangles) {
            Optional<IntersectionDataGeometric> intersectionDataGeometric = triangle.rayIntersection(ray);
            if (intersectionDataGeometric.isPresent()) {
                double t = intersectionDataGeometric.get().getT();
                if (t < minT) {
                    minT = t;
                    intersectionDataGeometricMin = intersectionDataGeometric;
                }
            }
        }
        return intersectionDataGeometricMin;
    }

    @Override
    public boolean boxCheck(Line3d ray) {
        return this.boundingBox.rayIntersection(ray);
    }

    @Override
    public Vector3f getSurfaceNormal(Vector3f point) {
        return null;
    }
}
