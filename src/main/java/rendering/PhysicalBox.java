package main.java.rendering;

public class PhysicalBox extends GeometricObject {

    //simple parallelepiped defined by min and max
    //we use triangulation for this
    //but no vertex normal since it makes no sense
    private final Vector3f min;
    private final Vector3f max;
    private final Matrix4D objectToWorld;

    public PhysicalBox(Vector3f min, Vector3f max, Matrix4D objectToWorld) {
        this.min = min;
        this.max = max;
        this.objectToWorld = objectToWorld;
    }

    public TriangleMesh triangulate(int divs) {
        //in this case the number of divs makes no sense
        //We simply need to give the right vertices to the triangle mesh routine

        double deltaX = max.getX() - min.getX();
        double deltaY = max.getY() - min.getY();
        double deltaZ = max.getZ() - min.getZ();

        Vector3f vertex[] = new Vector3f[] {
                min,
                min.add(new Vector3f(deltaX, 0,0)),
                min.add(new Vector3f(0, deltaY,0)),
                min.add(new Vector3f(0, 0,deltaZ)),
                min.add(new Vector3f(deltaX, deltaY,0)),
                min.add(new Vector3f(deltaX, 0,deltaZ)),
                min.add(new Vector3f(0, deltaY,deltaZ)),
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


}
