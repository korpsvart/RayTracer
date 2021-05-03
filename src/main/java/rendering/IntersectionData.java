package main.java.rendering;

public class IntersectionData {

    //ideally, we would use the same intersection primitive
    //for every scene geometric object, that is the line-triangle intersection.
    //Thus every intersection should always be done with a triangle
    //and the return values should be t, u, v (u and v are the barycentric coordinates
    //useful for shading)
    //However, for now we admit the presence of other geometrical primitives
    //we simply ignore the u, v values when not needed

    private double t, u, v;

    public IntersectionData(double t, double u, double v) {
        this.t = t;
        this.u = u;
        this.v = v;
    }

    public IntersectionData(double t) {
        this(t, -1, -1);
    }

    public double getT() {
        return t;
    }

    public double getU() {
        return u;
    }

    public double getV() {
        return v;
    }

}
