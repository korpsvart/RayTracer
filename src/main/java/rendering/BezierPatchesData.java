package rendering;

public class BezierPatchesData extends GeometricObject {

    private int numPatches;
    private int numCP;
    private int[][] patchesCP;
    private double[][] cP;
    private Matrix4D objectToWorldInternal;


    public BezierPatchesData(int numPatches, int numCP, int[][] patchesCP, double[][] cP, Matrix4D objectToWorldInternal) {
        this.numPatches = numPatches;
        this.numCP = numCP;
        this.patchesCP = patchesCP;
        this.cP = cP;
        this.objectToWorldInternal = objectToWorldInternal;
    }

    public void setObjectToWorld(Matrix4D objectToWorld) {
        this.objectToWorld = objectToWorld;
    }

    public Matrix4D getObjectToWorld() {
        return objectToWorld;
    }

    public static Matrix4D getTeapotCTW() {
        //TODO: delete this
        Matrix4D ctw = new Matrix4D(new double[][] {
                {0.897258, 0, -0.441506, 0},
                {-0.288129, 0.757698, -0.585556, 0},
                {0.334528, 0.652606, 0.679851, 0},
                {5.439442, 11.080794, 10.381341, 1}
        });
        return ctw;
    }


    public BezierSurface33[] getSurfaces() {
        BezierSurface33[] patches = new BezierSurface33[numPatches];
        for (int i = 0; i < numPatches; i++) {
            Vector3d controlPoints[] = new Vector3d[16];
            for (int j = 0; j < 16; j++) {
                controlPoints[j] = new Vector3d(cP[patchesCP[i][j]-1]);
            }
            BezierSurface33 bSurface = new BezierSurface33(controlPoints, objectToWorld);
            bSurface.setDivs(divs);
            patches[i] = bSurface;
        }
        return patches;
    }

    @Override
    public TriangleMesh triangulate() {
        BezierSurface33[] surfaces = getSurfaces();
        TriangleMesh[] meshes = new TriangleMesh[surfaces.length];
        for (int i = 0; i < meshes.length; i++) {
            meshes[i] = surfaces[i].triangulate();
        }
        TriangleMesh merged = TriangleMesh.merge(meshes);
        return merged;
    }

    @Override
    public String toString() {
        return "Bézier patches object; " + "Translation: " + getTranslationData() + "; " + "Rotation: " + getRotationData();
    }
}
