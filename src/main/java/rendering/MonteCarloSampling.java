package rendering;

import java.util.Random;

public class MonteCarloSampling {

    public static int getSamplingN() {
        return SAMPLING_N;
    }

    public static void setSamplingN(int samplingN) {
        SAMPLING_N = samplingN;
    }

    private static int SAMPLING_N = 32;

    public static Vector3f uniformSamplingHemisphere(double r1, double r2) {
        //given two random numbers r1, r2 in [0, 1]
        //uniformly samples direction inside hemisphere
        double sinTheta = Math.sqrt(1 - Math.pow(r1, 2));
        double phi = r2*2*Math.PI;
        double x = sinTheta*Math.cos(phi);
        double z = sinTheta*Math.sin(phi);
        return new Vector3f(x, r1, z);
    }



    public static Vector3f calculateIndirectDiffuse(Scene currentScene, Vector3f hitPoint, Vector3f[] localCoordinateSystem, int rayDepth) {
        Vector3f indirectDiffuse = new Vector3f(0, 0, 0);
        Matrix3D sampleToWorld = new Matrix3D(new Vector3f[]{localCoordinateSystem[0], localCoordinateSystem[1], localCoordinateSystem[2]}, Matrix3D.COL_VECTOR);
        for (int i = 0; i < SAMPLING_N; i++) {
            Random random = new Random();
            double r1 = random.nextDouble();
            double r2 = random.nextDouble();
            Vector3f sampleDirection = uniformSamplingHemisphere(r1, r2);
            Vector3f sampleDirectionWorld = sampleDirection.matrixLinearTransform(sampleToWorld);
            assert(Math.abs(sampleDirectionWorld.magnitude() - 1) < 10e-2);
            hitPoint = hitPoint.add(sampleDirectionWorld.mul(10e-3)); //equivalent of depth bias
            Line3d indirectDiffuseRay = new Line3d(hitPoint, sampleDirectionWorld);
            Vector3f color = currentScene.rayTraceWithBVH(indirectDiffuseRay, rayDepth+1, RayType.INDIRECT_DIFFUSE);
            //apply cosine law
            //we should multiply the color obtained by the dot product of ray direction and surface normal
            //that is, multiplying by cos(theta) where theta is the angle between the two vectors (they are normalized)
            //But that is equivalent to multiply by r1
            color = color.mul(r1);
            //we should also divide by the pdf, but because it's uniform we can factor it out and divide at the end
            //the pdf if 1/(2*pi)
            //Actually, since we will also multiply the final color by the BDRF, that is albedo/pi for diffuse objects
            //we are gonna just multiply indirect diffuse by 2
            indirectDiffuse = indirectDiffuse.add(color);
        }
        return indirectDiffuse.mul((float)1/SAMPLING_N);
    }


}
