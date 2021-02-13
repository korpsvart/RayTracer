import java.util.Optional;

public abstract class MirrorTransparent implements SceneObject {

    /* Objects which react to light both with
    reflection and refraction (they have some degree
    of transparency)
     */

    @Override
    public Optional<Double> trace(Line3d ray, RayType rayType) {
        if (rayType==RayType.SHADOW) {
            /*Details about the implementation:
            A classic problem is when we are looking at diffuse objects which are
            behind reflection/refraction objects (i.e. objects that have some degree of transparency),
            but the path from the diffuse objects to the light sources is blocked by the transparent object(s).
            As an example: imagine looking at the rocks on the bottom of a lake (rocks are the diffuse objects,
            water is transparent).
            The problem is: if we check visibility by tracing the path of shadow ray and checking if this has some
            intersection with ANY other objects, we won't be able to see the rocks because light is getting "blocked"
            by water, which makes little sense.
            I think a correct solution would be to raytrace again when intersecting diffuse objects "on the bottom",
            the ray would then exit the pool of water and so on. However, I think this would not work with our current
            implementation, which uses a simplified model of reality (and even if it did, it would make everything slower).
            A naive solution would be, thus, just to exclude objects which have some degree of transparency from our visibility check
            (this is the one currently implemented).
            One could be reasonably concerned that this solution doesn't take into account the "distortion" caused by the angle of
            refraction when exiting the pool of water (i.e. we end up only considering the refraction when entering the water).
            However, I think the current approach could be justified in some way by the nature of diffuse object, which (at least ideally)
            should reflect light uniformly in all directions, thus "evening out" the distortion caused by refraction.
             */
            return Optional.empty();
        } else {
            return this.rayIntersection(ray);
        }
    }
}
