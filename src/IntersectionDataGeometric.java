public class IntersectionDataGeometric {

    private Double t;

    public Double getT() {
        return t;
    }

    public GeometricObject getGeometricObject() {
        return geometricObject;
    }

    private GeometricObject geometricObject;

    public IntersectionDataGeometric(Double t, GeometricObject geometricObject) {
        this.t = t;
        this.geometricObject = geometricObject;
    }
}
