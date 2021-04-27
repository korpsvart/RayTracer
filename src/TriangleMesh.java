import java.util.Optional;

public class TriangleMesh {

    public TriangleMesh() {

    }

    public class Triangle extends GeometricObject {

        //Vertices must be given in CCW ordering
        //this class is meant to be used in combination with triangle mesh



        private boolean useVertexNormal = false;
        private Vector3f v0;
        private final Vector3f v1;
        private Vector3f v2;
        private TriangleMesh triangleMesh; //corresponding triangle mesh
        private Vector3f faceNormal = null;
        private double area  = -1;
        public Vector3f[] vertexNormal = null;
        private BoundingVolume boundingVolume;

        public void setUseVertexNormal(boolean useVertexNormal) {
            this.useVertexNormal = useVertexNormal;
        }

        public Triangle(Vector3f v0, Vector3f v1, Vector3f v2, TriangleMesh triangleMesh, Matrix4D objectToWorld) {
            this.objectToWorld = objectToWorld;
            if (this.objectToWorld != null) {
                v0 = v0.matrixAffineTransform(objectToWorld);
                v1 = v1.matrixAffineTransform(objectToWorld);
                v2 = v2.matrixAffineTransform(objectToWorld);
            }
            this.v0 = v0;
            this.v1 = v1;
            this.v2 = v2;
            this.triangleMesh = triangleMesh;
            this.boundingVolume = new BoundingVolume(new Vector3f[]{v0, v1, v2});
        }

        public Triangle(Vector3f v0, Vector3f v1, Vector3f v2, TriangleMesh triangleMesh, Matrix4D objectToWorld, Vector3f vertexNormal[]) {
            this(v0, v1, v2, triangleMesh, objectToWorld);
            this.vertexNormal = vertexNormal.clone();
        }

        @Override
        public Optional<IntersectionData> rayIntersection2(Line3d ray) {
            //Using local coordinates u,v
            //solving linear system with Cramer's rule
            //(akin to Moller-Trumbore algorithm)

            //Convention used is:
            //P = A + u*AB + v*AC
            //A should correspond to v0,
            //B to v1, C to v2

            Main.getRayTriangleTests().getAndIncrement();


            Vector3f e1 = v1.moveTo(v0);
            Vector3f e2 = v2.moveTo(v0);
            Vector3f h = e1.crossProduct(e2);

            Vector3f d = ray.getDirection();

            double denom = d.dotProduct(h);

            if (Math.abs(denom-h.magnitude())<10e-6) {
                //In this case the ray is (almost) perpendicular to the triangle plane
                //often this leads to an annoying effect when using triangulation and when
                //two adjacent triangles belong to the same plane (typical example: triangulation of a cube):
                //the separation between the two triangles becomes visible as a thin line.
                //This obviously happen because the ray will sometimes miss both triangles, because of numerical imprecision.
                //How to solve this??
            }

            if (Math.abs(denom) < 10e-6) {
                //if denom is really close to 0 it means
                //ray direction and normal are perpendicular
                //(t goes to infinity)
                return Optional.empty();
            }

            if (Scene.isBackFaceCulling() && denom > 0) {
                //if denom is > 0
                //(ray and normal are facing in the same direction)
                //object is backfacing
                //don't show if back face culling is enabled
                return Optional.empty();
            }

            Vector3f t = ray.getPoint().moveTo(v0);
            Vector3f k = d.crossProduct(t);

            double u = e2.dotProduct(k) / denom;

            if (u < 0 || u > 1) {
                return Optional.empty();
            }

            double v = e1.mul(-1).dotProduct(k) / denom;


            if (v < 0 || (u + v) > 1) {
                return Optional.empty();
            } else {
                double distance = t.dotProduct(h) / denom;
                if (distance > 0) {
                    Main.getRayTriangleIntersections().getAndIncrement();
                    return Optional.of(new IntersectionData(distance, u, v));
                } else {
                    return Optional.empty();
                }
            }


        }

        @Override
        public boolean boxCheck(Line3d ray) {
            return triangleMesh.boxCheck(ray);
        }

        @Override
        public boolean boundingVolumeCheck(Line3d ray) {
            return this.boundingVolume.intersect(ray);
        }

        @Override
        public BoundingVolume getBoundingVolume() {
            return this.boundingVolume;
        }

        @Override
        public Vector3f getSurfaceNormal(Vector3f point, double u, double v) {
            if (!useVertexNormal || u <0 || v <0) {
                if (this.faceNormal == null) {
                    //also precompute area for performance reasons
                    Vector3f e1 = v0.moveTo(v1);
                    Vector3f e2 = v0.moveTo(v2);
                    Vector3f n = e1.crossProduct(e2); //area of parallelogram
                    faceNormal = n.normalize();
                    area = n.magnitude()/2;
                }
                return faceNormal;
            } else {
                //vertexNormal[0] should be normal data
                //for vertex v0, aka vertex A
                //vertexNormal[1] for vertex v1 (aka B)
                //and so on...
                //w = 1 - u - v
                //If u=1 then we are on vertex B
                //If v =1 then we are on vertex C
                double w = 1-u-v;
                return (vertexNormal[0].mul(w).add(vertexNormal[1].mul(u).add(vertexNormal[2].mul(v)))).normalize();
            }
        }

        public double getArea() {
            if (area < 0) {
                //also precompute facenormal for performance reasons
                Vector3f e1 = v0.moveTo(v1);
                Vector3f e2 = v0.moveTo(v2);
                Vector3f n = e1.crossProduct(e2); //area of parallelogram
                faceNormal = n.normalize();
                area = n.magnitude()/2;
            }
            return area;
        }


    }


    //create triangle mesh from arbitrary
    //N-vertices polygon mesh

    //Face index array is not needed
    private int vertexIndex[];
    private Vector3f[] vertex;
    private Vector3f[] vertexNormal;
    private int numTriangles;
    private int numVertices;
    private BoundingBox boundingBox;
    private BoundingVolume boundingVolume;
    private Matrix4D objectToWorld = null;
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
                if (j==0) {
                    this.vertexIndex[localFaceOffset + j*3] = vertexIndex[polygonFaceOffset+j];
                    this.vertexIndex[localFaceOffset + j*3 + 1] = vertexIndex[polygonFaceOffset + j + 1];
                    this.vertexIndex[localFaceOffset + j*3 + 2] = vertexIndex[polygonFaceOffset + j + 2];
                } else {
                    this.vertexIndex[localFaceOffset + j*3] = vertexIndex[polygonFaceOffset+j];
                    this.vertexIndex[localFaceOffset + j*3 + 1] = vertexIndex[polygonFaceOffset + j + 2];
                    this.vertexIndex[localFaceOffset + j*3 + 2] = vertexIndex[polygonFaceOffset + j + 1];
                }
            }
            localFaceOffset+=triPerFace*3;
            polygonFaceOffset+=faceIndex[i];
        }

        triangles = new Triangle[numTriangles];
        makeTriangles(false);

        //If bounding box is not given explicitly by caller
        //compute it here from vertices
        //Same for bounding volume
        boundingBox = new BoundingBox(vertex);
        boundingVolume = new BoundingVolume(vertex);

    }

    public TriangleMesh(int faceIndex[], int vertexIndex[], Vector3f vertex[], Matrix4D objectToWorld) {
        //This is an algorithm I came up with
        //Don't know if it's correct and how fast it is

        //First loop is only to allocate exact size
        //for vertexIndex array
        this.vertex = vertex.clone();
        this.objectToWorld = objectToWorld;
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
                if (j==0) {
                    this.vertexIndex[localFaceOffset + j*3] = vertexIndex[polygonFaceOffset+j];
                    this.vertexIndex[localFaceOffset + j*3 + 1] = vertexIndex[polygonFaceOffset + j + 1];
                    this.vertexIndex[localFaceOffset + j*3 + 2] = vertexIndex[polygonFaceOffset + j + 2];
                } else {
                    this.vertexIndex[localFaceOffset + j*3] = vertexIndex[polygonFaceOffset+j];
                    this.vertexIndex[localFaceOffset + j*3 + 1] = vertexIndex[polygonFaceOffset + j + 2];
                    this.vertexIndex[localFaceOffset + j*3 + 2] = vertexIndex[polygonFaceOffset + j + 1];
                }
            }
            localFaceOffset+=triPerFace*3;
            polygonFaceOffset+=faceIndex[i];
        }

        triangles = new Triangle[numTriangles];
        makeTriangles(false);

        //If bounding box is not given explicitly by caller
        //compute it here from vertices
        //Same for bounding volume
        boundingBox = new BoundingBox(vertex);
        boundingVolume = new BoundingVolume(vertex);


    }

    public TriangleMesh(int faceIndex[], int vertexIndex[], Vector3f vertex[], BoundingBox boundingBox) {
        //bounding box given explicitly (ex. for bezier surfaces)

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
                if (j==0) {
                    this.vertexIndex[localFaceOffset + j*3] = vertexIndex[polygonFaceOffset+j];
                    this.vertexIndex[localFaceOffset + j*3 + 1] = vertexIndex[polygonFaceOffset + j + 1];
                    this.vertexIndex[localFaceOffset + j*3 + 2] = vertexIndex[polygonFaceOffset + j + 2];
                } else {
                    this.vertexIndex[localFaceOffset + j*3] = vertexIndex[polygonFaceOffset+j];
                    this.vertexIndex[localFaceOffset + j*3 + 1] = vertexIndex[polygonFaceOffset + j + 2];
                    this.vertexIndex[localFaceOffset + j*3 + 2] = vertexIndex[polygonFaceOffset + j + 1];
                }
            }
            localFaceOffset+=triPerFace*3;
            polygonFaceOffset+=faceIndex[i];
        }

        triangles = new Triangle[numTriangles];
        makeTriangles(false);

        this.boundingBox = boundingBox;
    }

    public TriangleMesh(int faceIndex[], int vertexIndex[], Vector3f vertex[], Vector3f[] vertexNormal, BoundingBox boundingBox) {
        //This is an algorithm I came up with
        //Don't know if it's correct and how fast it is

        //First loop is only to allocate exact size
        //for vertexIndex array
        this.vertex = vertex.clone();
        this.vertexNormal = vertexNormal.clone();
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
                if (j==0) {
                    this.vertexIndex[localFaceOffset + j*3] = vertexIndex[polygonFaceOffset+j];
                    this.vertexIndex[localFaceOffset + j*3 + 1] = vertexIndex[polygonFaceOffset + j + 1];
                    this.vertexIndex[localFaceOffset + j*3 + 2] = vertexIndex[polygonFaceOffset + j + 2];
                } else {
                    this.vertexIndex[localFaceOffset + j*3] = vertexIndex[polygonFaceOffset+j];
                    this.vertexIndex[localFaceOffset + j*3 + 1] = vertexIndex[polygonFaceOffset + j + 2];
                    this.vertexIndex[localFaceOffset + j*3 + 2] = vertexIndex[polygonFaceOffset + j + 1];
                }
            }
            localFaceOffset+=triPerFace*3;
            polygonFaceOffset+=faceIndex[i];
        }

        triangles = new Triangle[numTriangles];
        makeTriangles(true);

        this.boundingBox = boundingBox;
    }

    public TriangleMesh(int faceIndex[], int vertexIndex[], Vector3f vertex[], Vector3f[] vertexNormal, BoundingVolume boundingVolume) {
        //This is an algorithm I came up with
        //Don't know if it's correct and how fast it is

        //First loop is only to allocate exact size
        //for vertexIndex array
        this.vertex = vertex.clone();
        this.vertexNormal = vertexNormal.clone();
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
                if (j==0) {
                    this.vertexIndex[localFaceOffset + j*3] = vertexIndex[polygonFaceOffset+j];
                    this.vertexIndex[localFaceOffset + j*3 + 1] = vertexIndex[polygonFaceOffset + j + 1];
                    this.vertexIndex[localFaceOffset + j*3 + 2] = vertexIndex[polygonFaceOffset + j + 2];
                } else {
                    this.vertexIndex[localFaceOffset + j*3] = vertexIndex[polygonFaceOffset+j];
                    this.vertexIndex[localFaceOffset + j*3 + 1] = vertexIndex[polygonFaceOffset + j + 2];
                    this.vertexIndex[localFaceOffset + j*3 + 2] = vertexIndex[polygonFaceOffset + j + 1];
                }
            }
            localFaceOffset+=triPerFace*3;
            polygonFaceOffset+=faceIndex[i];
        }

        triangles = new Triangle[numTriangles];
        makeTriangles(true);

        //bounding volume given explicitly (e.g. for bezier surfaces)
        //(note: there still might be pieces of codes using bounding box check
        //so there's a chance using this constructor will lead to a null pointer exception)
        this.boundingVolume = boundingVolume;
    }

    public TriangleMesh(int faceIndex[], int vertexIndex[], Vector3f vertex[], Vector3f[] vertexNormal) {
        //This is an algorithm I came up with
        //Don't know if it's correct and how fast it is

        //First loop is only to allocate exact size
        //for vertexIndex array
        this.vertex = vertex.clone();
        this.vertexNormal = vertexNormal.clone();
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
                if (j==0) {
                    this.vertexIndex[localFaceOffset + j*3] = vertexIndex[polygonFaceOffset+j];
                    this.vertexIndex[localFaceOffset + j*3 + 1] = vertexIndex[polygonFaceOffset + j + 1];
                    this.vertexIndex[localFaceOffset + j*3 + 2] = vertexIndex[polygonFaceOffset + j + 2];
                } else {
                    this.vertexIndex[localFaceOffset + j*3] = vertexIndex[polygonFaceOffset+j];
                    this.vertexIndex[localFaceOffset + j*3 + 1] = vertexIndex[polygonFaceOffset + j + 2];
                    this.vertexIndex[localFaceOffset + j*3 + 2] = vertexIndex[polygonFaceOffset + j + 1];
                }
            }
            localFaceOffset+=triPerFace*3;
            polygonFaceOffset+=faceIndex[i];
        }

        triangles = new Triangle[numTriangles];
        makeTriangles(true);

        //this version is most likely the one that should always be used now
        //We don't need a bounding volume cause in the bounding volume hierarchy
        //we calculate the bounding volume per single triangle, not for the whole triangle mesh!
        //(It's not even needed as an "upper-level" bounding volume, since the BVH
        //automatically calculates the necessary upper levels)
    }

    public void makeTriangles(boolean useVertexNormal) {
        if (!useVertexNormal) {
            for (int i = 0; i < numTriangles; i++) {
                Triangle triangle = new Triangle(vertex[vertexIndex[i*3]], vertex[vertexIndex[i*3+1]], vertex[vertexIndex[i*3+2]], this, objectToWorld);
                triangles[i] = triangle;
            }
        } else {
            for (int i = 0; i < numTriangles; i++) {
                int index1 = vertexIndex[i*3];
                int index2 = vertexIndex[i*3+1];
                int index3 = vertexIndex[i*3+2];
                Vector3f[] triangleVertexNormal = {vertexNormal[index1], vertexNormal[index2], vertexNormal[index3]};
                Triangle triangle = new Triangle(vertex[index1], vertex[index2], vertex[index3], this, objectToWorld,triangleVertexNormal);
                triangle.setUseVertexNormal(true);
                triangle.setUseBVH(true);
                triangles[i] = triangle;
            }
        }
    }





    public boolean boxCheck(Line3d ray) {
        return this.boundingBox.rayIntersection(ray);
    }

    public boolean boundingVolumeCheck(Line3d ray) {
        return this.boundingVolume.intersect(ray);
    }

    public static TriangleMesh merge(TriangleMesh[] meshes) {
        //merge multiple already existing triangle meshes
        TriangleMesh result = new TriangleMesh();
        int numTriangles = 0;
        for (TriangleMesh mesh :
                meshes) {
            numTriangles += mesh.numTriangles;
        }
        result.numTriangles = numTriangles;
        result.triangles = new Triangle[numTriangles];
        int c = 0, j=0;
        for (int i = 0; i < meshes.length; i++) {
            Triangle[] triangles = meshes[i].getTriangles();
            for (j = 0; j < triangles.length; j++) {
                result.triangles[j+c] = triangles[j];
            }
            c+=j;
        }
        return result;
    }




}
