import java.util.Optional;

public class TriangleMesh {

    public class Triangle extends GeometricObject {

        //Vertices must be given in CCW ordering
        //this class is meant to be used in combination with triangle mesh


        private final Vector3f v0, v1, v2;
        private TriangleMesh triangleMesh; //corresponding triangle mesh

        public Triangle(Vector3f v0, Vector3f v1, Vector3f v2, TriangleMesh triangleMesh) {
            this.v0 = v0;
            this.v1 = v1;
            this.v2 = v2;
            this.triangleMesh = triangleMesh;
        }

        @Override
        public Optional<Double> rayIntersection2(Line3d ray) {
            //Using local coordinates u,v
            //solving linear system with Cramer's rule
            //(akin to Moller-Trumbore algorithm)

            Main.getRayTriangleTests().getAndIncrement();

            Vector3f e1 = v0.moveTo(v1);
            Vector3f e2 = v0.moveTo(v2);
            Vector3f n = e1.crossProduct(e2);

            Vector3f rayDirOpposite = ray.getDirection().mul(-1);

            double denom = rayDirOpposite.dotProduct(n);

            if (Math.abs(denom) < 10e-6) {
                //if denom is really close to 0 it means
                //ray direction and normal are perpendicular
                //(t goes to infinity)
                return Optional.empty();
            }

            if (Scene.isBackFaceCulling() && denom < 0) {
                //if denom is < 0
                //oject is backfacing
                //don't show if back face culling is enalbed
                return Optional.empty();
            }

            Vector3f b = v0.moveTo(ray.getPoint());
            Vector3f q = b.crossProduct(ray.getDirection());

            double u = e2.dotProduct(q) / denom;

            if (u < 0 || u > 1) {
                return Optional.empty();
            }

            double v = e1.mul(-1).dotProduct(q) / denom;

            if (v < 0 || (u + v) > 1) {
                return Optional.empty();
            } else {
                Main.getRayTriangleIntersections().getAndIncrement();
                return Optional.of(b.dotProduct(n) / denom);
            }


        }

        @Override
        public boolean boxCheck(Line3d ray) {
            return triangleMesh.boxCheck(ray);
        }

        @Override
        public Vector3f getSurfaceNormal(Vector3f point) {
            Vector3f e1 = v0.moveTo(v1);
            Vector3f e2 = v0.moveTo(v2);
            return e1.crossProduct(e2).normalize();
        }
    }


    //create triangle mesh from arbitrary
    //N-vertices polygon mesh

    //Face index array is not needed
    private int vertexIndex[];
    private Vector3f[] vertex;
    private int numTriangles;
    private int numVertices;
    private BoundingBox boundingBox;
    private Triangle triangles[];

    public Triangle[] getTriangles() {
        return triangles;
    }


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

        triangles = new Triangle[numTriangles];
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
            Triangle triangle = new Triangle(vertex[vertexIndex[i*3]], vertex[vertexIndex[i*3+1]], vertex[vertexIndex[i*3+2]], this);
            triangles[i] = triangle;
        }
    }


//    @Override
//    public Optional<IntersectionDataGeometric> rayIntersection(Line3d ray) {
//        //loop over all triangles
//        //return minimum t (distance of intersection)
//        //and corresponding triangle
//        //(if found anything)
//
//        //bounding box check
//        if (!this.boxCheck(ray)) {
//            return Optional.empty();
//        }
//
//        double minT = Double.POSITIVE_INFINITY;
//        Optional<IntersectionDataGeometric> intersectionDataGeometricMin = Optional.empty();
//        for (Triangle triangle :
//                triangles) {
//            Optional<IntersectionDataGeometric> intersectionDataGeometric = triangle.rayIntersection(ray);
//            if (intersectionDataGeometric.isPresent()) {
//                double t = intersectionDataGeometric.get().getT();
//                if (t < minT) {
//                    minT = t;
//                    intersectionDataGeometricMin = intersectionDataGeometric;
//                }
//            }
//        }
//        return intersectionDataGeometricMin;
//    }


    public boolean boxCheck(Line3d ray) {
        return this.boundingBox.rayIntersection(ray);
    }

    public void addToScene(Scene currentScene, SceneObject sceneObject) {
        for (Triangle triangle :
                triangles) {
            sceneObject.addToScene(currentScene, triangle);
        }
    }

}
