package rendering;

import java.util.Optional;

public abstract class GeometricObject {

    /* Geometric object, not considering its material properties
    with respect to light. This class can be directly inherited by geometric
    objects, which only needs to supply methods regarding geometric properties.
    (This acts like the "Component" in a Decorator Pattern, the concrete components
    are the geometric objects, which you can decorate to add "material" properties to them.
    You MUST add material properties to be able to render them in the Scene).
     */

    protected Matrix4D objectToWorld = null;
    private Vector3d rotationData;
    protected int divs = 16; //default value


    private Vector3d scalingData;
    protected boolean triangulated = true;
    private boolean useBVH = false;//set to true if you wish to use bvh to accelerate intersection
    //1)do not use bvh acceleration with plane
    //2)it's not recommended for simple object, such as spheres

    public void setUseBVH(boolean useBVH) {
        this.useBVH = useBVH;
    }

    public boolean isUseBVH() {
        return useBVH;
    }


    public Matrix4D getObjectToWorld() {
        if (objectToWorld == null) {
            //return identity affine transform
            return Matrix4D.identity;
        } else {
            return objectToWorld;
        }
    }

    public void setObjectToWorld(Matrix4D objectToWorld) {
        this.objectToWorld = objectToWorld;
    }

    public Optional<IntersectionData> rayIntersection2(Ray ray) {
        //default implementation
        //for objects for which we are not able to calculate directly intersection
        //(i.e. bezier surfaces, which are always triangulated)

        return Optional.empty();
    }

    public Optional<IntersectionData> rayIntersection(Ray ray) {
        //this is the version which should be always called from outside
        //which always includes the bounding box check
        if (this.boundingVolumeCheck(ray)) {
            return this.rayIntersection2(ray);
        } else {
            return Optional.empty();
        }
    }

    public Vector3d getSurfaceNormal(Vector3d point, double u, double v) {
        return null;
    }


    public Vector3d[] getLocalCartesianSystem(Vector3d point, double u, double v) {
        //get local cartesian coordinate system
        //Since we only have interpolated normals data
        //we can't exploit the actual parametric surface tangent and bitangent
        //(even if we know what kind of parametric surface it is, such as a bezier surface)
        //Thus we use a different and simple method

        //get normal
        Vector3d normal = getSurfaceNormal(point, u, v);
        Vector3d Nt;

        //here we should check that normal doesnt already have same direction as y axis
        //...

        //check if Ny > Nx
        if (Math.abs(normal.getX()) > Math.abs(normal.getY())) {
            Nt = new Vector3d(normal.getZ(), 0, -normal.getX()).normalize();
        } else {
            //else consider vector in the plane satisfying x = 0
            Nt = new Vector3d(0, -normal.getZ(), normal.getY()).normalize();
        }
        Vector3d Nb = normal.crossProduct(Nt);
        return new Vector3d[]{Nt, normal, Nb};
    }

    public boolean boxCheck(Ray ray) {
        //default implementation for objects
        //for which box check is overkill
        return true;
    }

    public boolean boundingVolumeCheck(Ray ray) {
        return true;
    }

    public TriangleMesh triangulate() {
        return null;
    }

    public BoundingVolume getBoundingVolume() {
        //default implementation for objects for which
        //we don't want to implement bounding volumes
        return BoundingVolume.createNullBoundingvolume();
    }

    public boolean isTriangulated() {
        return triangulated;
    }

    public Vector3d getRotationData() {
        return rotationData;
    }

    public void setRotationData(Vector3d rotationData) {
        this.rotationData = rotationData;
    }


    public Vector3d getScalingData() {
        return scalingData;
    }

    public void setScalingData(Vector3d scalingData) {
        this.scalingData = scalingData;
    }

    public Vector3d getTranslationData() {
        if (objectToWorld != null)
            return objectToWorld.getC();
        else
            return new Vector3d(0,0,0);
    }

    public int getDivs() {
        return divs;
    }

    public void setDivs(int divs) {
        this.divs = divs;
    }


    @Override
    public String toString() {
        return "Translation: " + getTranslationData();
    }
}
