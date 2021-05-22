package rendering;

public class PhysicalBox extends GeometricObject {

    //simple parallelepiped defined by min and max
    //we use triangulation for this
    //but no vertex normal since it makes no sense
    private final static Vector3d ADJUST_X = new Vector3d(1e-7, 0, 0);
    private final Vector3d min;
    private final Vector3d max;
//    private final Matrix4D objectToWorld;

    public PhysicalBox(Vector3d min, Vector3d max, Matrix4D objectToWorld) {
        this.min = min.add(ADJUST_X);
        this.max = max.add(ADJUST_X);
        this.objectToWorld = objectToWorld;
    }

    public TriangleMesh triangulate() {
        //in this case the number of divs makes no sense
        //We simply need to give the right vertices to the triangle mesh routine

        double deltaX = max.getX() - min.getX();
        double deltaY = max.getY() - min.getY();
        double deltaZ = max.getZ() - min.getZ();

        Vector3d vertex[] = new Vector3d[] {
                min,
                min.add(new Vector3d(deltaX, 0,0)),
                min.add(new Vector3d(0, deltaY,0)),
                min.add(new Vector3d(0, 0,deltaZ)),
                min.add(new Vector3d(deltaX, deltaY,0)),
                min.add(new Vector3d(deltaX, 0,deltaZ)),
                min.add(new Vector3d(0, deltaY,deltaZ)),
                max
        };

        int[] vertexIndex = new int[] {
                0, 2, 1, 4,
                2, 6, 4, 7,
                1, 4, 5, 7,
                0, 1, 3, 5,
                0, 3, 2, 6,
                3, 5, 6, 7
        };

        int[] faceIndex = new int[6];
        for (int i = 0; i < 6; i++) {
            faceIndex[i]=4;
        }

        return new TriangleMesh(faceIndex,vertexIndex,vertex,objectToWorld);

    }

    public Vector3d getMin() {
        return min.add(ADJUST_X.mul(-1));
    }

    public Vector3d getMax() {
        return max.add(ADJUST_X.mul(-1));
    }

    @Override
    public String toString() {
        return "Box; " + "Translation: " + getTranslationData() + "; " + "Rotation: " + getRotationData();
    }
}
