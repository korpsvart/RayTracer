package rendering;

public enum RayType {
    //Primary ray are those cast from the camera
    //primary rays consider every scene object
    //shadow rays are those cast by diffuse objects in direction of point lights, to check visibility
    //shadow rays ignore transparent objects
    //indirect diffuse rays are used to simulate indirect diffuse lighting
    //indirect diffuse rays ignore all but diffuse objects
    PRIMARY, SHADOW, INDIRECT_DIFFUSE
}
