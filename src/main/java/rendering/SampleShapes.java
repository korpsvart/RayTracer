package rendering;

public class SampleShapes {

    //static class containing some samples shapes
    //for demonstration purposes

    private final static Sphere[] sphere = new Sphere[]{
            //just a bunch of sphere scattered around space
            //with varying radius
            new Sphere(new Vector3d(0.3,0,-4), 0.2),
            new Sphere(new Vector3d(-0.5, 0.5, -8), 1),
            new Sphere(new Vector3d(2.6, 0.3, -7), 0.6),
            new Sphere(new Vector3d(0, -2, -7), 0.5),
            new Sphere(new Vector3d(0, -2, -4), 0.3),
            new Sphere(new Vector3d(0, -2, -11), 0.6)
    };


    public static BezierPatchesData getTeapot() {
        int numPatches = 32;
        int numCP = 306;
        double[][] cP = new double[][] {
                { 1.4000,  0.0000,  2.4000},
                { 1.4000, -0.7840,  2.4000},
                { 0.7840, -1.4000,  2.4000},
                { 0.0000, -1.4000,  2.4000},
                { 1.3375,  0.0000,  2.5312},
                { 1.3375, -0.7490,  2.5312},
                { 0.7490, -1.3375,  2.5312},
                { 0.0000, -1.3375,  2.5312},
                { 1.4375,  0.0000,  2.5312},
                { 1.4375, -0.8050,  2.5312},
                { 0.8050, -1.4375,  2.5312},
                { 0.0000, -1.4375,  2.5312},
                { 1.5000,  0.0000,  2.4000},
                { 1.5000, -0.8400,  2.4000},
                { 0.8400, -1.5000,  2.4000},
                { 0.0000, -1.5000,  2.4000},
                {-0.7840, -1.4000,  2.4000},
                {-1.4000, -0.7840,  2.4000},
                {-1.4000,  0.0000,  2.4000},
                {-0.7490, -1.3375,  2.5312},
                {-1.3375, -0.7490,  2.5312},
                {-1.3375,  0.0000,  2.5312},
                {-0.8050, -1.4375,  2.5312},
                {-1.4375, -0.8050,  2.5312},
                {-1.4375,  0.0000,  2.5312},
                {-0.8400, -1.5000,  2.4000},
                {-1.5000, -0.8400,  2.4000},
                {-1.5000,  0.0000,  2.4000},
                {-1.4000,  0.7840,  2.4000},
                {-0.7840,  1.4000,  2.4000},
                { 0.0000,  1.4000,  2.4000},
                {-1.3375,  0.7490,  2.5312},
                {-0.7490,  1.3375,  2.5312},
                { 0.0000,  1.3375,  2.5312},
                {-1.4375,  0.8050,  2.5312},
                {-0.8050,  1.4375,  2.5312},
                { 0.0000,  1.4375,  2.5312},
                {-1.5000,  0.8400,  2.4000},
                {-0.8400,  1.5000,  2.4000},
                { 0.0000,  1.5000,  2.4000},
                { 0.7840,  1.4000,  2.4000},
                { 1.4000,  0.7840,  2.4000},
                { 0.7490,  1.3375,  2.5312},
                { 1.3375,  0.7490,  2.5312},
                { 0.8050,  1.4375,  2.5312},
                { 1.4375,  0.8050,  2.5312},
                { 0.8400,  1.5000,  2.4000},
                { 1.5000,  0.8400,  2.4000},
                { 1.7500,  0.0000,  1.8750},
                { 1.7500, -0.9800,  1.8750},
                { 0.9800, -1.7500,  1.8750},
                { 0.0000, -1.7500,  1.8750},
                { 2.0000,  0.0000,  1.3500},
                { 2.0000, -1.1200,  1.3500},
                { 1.1200, -2.0000,  1.3500},
                { 0.0000, -2.0000,  1.3500},
                { 2.0000,  0.0000,  0.9000},
                { 2.0000, -1.1200,  0.9000},
                { 1.1200, -2.0000,  0.9000},
                { 0.0000, -2.0000,  0.9000},
                {-0.9800, -1.7500,  1.8750},
                {-1.7500, -0.9800,  1.8750},
                {-1.7500,  0.0000,  1.8750},
                {-1.1200, -2.0000,  1.3500},
                {-2.0000, -1.1200,  1.3500},
                {-2.0000,  0.0000,  1.3500},
                {-1.1200, -2.0000,  0.9000},
                {-2.0000, -1.1200,  0.9000},
                {-2.0000,  0.0000,  0.9000},
                {-1.7500,  0.9800,  1.8750},
                {-0.9800,  1.7500,  1.8750},
                { 0.0000,  1.7500,  1.8750},
                {-2.0000,  1.1200,  1.3500},
                {-1.1200,  2.0000,  1.3500},
                { 0.0000,  2.0000,  1.3500},
                {-2.0000,  1.1200,  0.9000},
                {-1.1200,  2.0000,  0.9000},
                { 0.0000,  2.0000,  0.9000},
                { 0.9800,  1.7500,  1.8750},
                { 1.7500,  0.9800,  1.8750},
                { 1.1200,  2.0000,  1.3500},
                { 2.0000,  1.1200,  1.3500},
                { 1.1200,  2.0000,  0.9000},
                { 2.0000,  1.1200,  0.9000},
                { 2.0000,  0.0000,  0.4500},
                { 2.0000, -1.1200,  0.4500},
                { 1.1200, -2.0000,  0.4500},
                { 0.0000, -2.0000,  0.4500},
                { 1.5000,  0.0000,  0.2250},
                { 1.5000, -0.8400,  0.2250},
                { 0.8400, -1.5000,  0.2250},
                { 0.0000, -1.5000,  0.2250},
                { 1.5000,  0.0000,  0.1500},
                { 1.5000, -0.8400,  0.1500},
                { 0.8400, -1.5000,  0.1500},
                { 0.0000, -1.5000,  0.1500},
                {-1.1200, -2.0000,  0.4500},
                {-2.0000, -1.1200,  0.4500},
                {-2.0000,  0.0000,  0.4500},
                {-0.8400, -1.5000,  0.2250},
                {-1.5000, -0.8400,  0.2250},
                {-1.5000,  0.0000,  0.2250},
                {-0.8400, -1.5000,  0.1500},
                {-1.5000, -0.8400,  0.1500},
                {-1.5000,  0.0000,  0.1500},
                {-2.0000,  1.1200,  0.4500},
                {-1.1200,  2.0000,  0.4500},
                { 0.0000,  2.0000,  0.4500},
                {-1.5000,  0.8400,  0.2250},
                {-0.8400,  1.5000,  0.2250},
                { 0.0000,  1.5000,  0.2250},
                {-1.5000,  0.8400,  0.1500},
                {-0.8400,  1.5000,  0.1500},
                { 0.0000,  1.5000,  0.1500},
                { 1.1200,  2.0000,  0.4500},
                { 2.0000,  1.1200,  0.4500},
                { 0.8400,  1.5000,  0.2250},
                { 1.5000,  0.8400,  0.2250},
                { 0.8400,  1.5000,  0.1500},
                { 1.5000,  0.8400,  0.1500},
                {-1.6000,  0.0000,  2.0250},
                {-1.6000, -0.3000,  2.0250},
                {-1.5000, -0.3000,  2.2500},
                {-1.5000,  0.0000,  2.2500},
                {-2.3000,  0.0000,  2.0250},
                {-2.3000, -0.3000,  2.0250},
                {-2.5000, -0.3000,  2.2500},
                {-2.5000,  0.0000,  2.2500},
                {-2.7000,  0.0000,  2.0250},
                {-2.7000, -0.3000,  2.0250},
                {-3.0000, -0.3000,  2.2500},
                {-3.0000,  0.0000,  2.2500},
                {-2.7000,  0.0000,  1.8000},
                {-2.7000, -0.3000,  1.8000},
                {-3.0000, -0.3000,  1.8000},
                {-3.0000,  0.0000,  1.8000},
                {-1.5000,  0.3000,  2.2500},
                {-1.6000,  0.3000,  2.0250},
                {-2.5000,  0.3000,  2.2500},
                {-2.3000,  0.3000,  2.0250},
                {-3.0000,  0.3000,  2.2500},
                {-2.7000,  0.3000,  2.0250},
                {-3.0000,  0.3000,  1.8000},
                {-2.7000,  0.3000,  1.8000},
                {-2.7000,  0.0000,  1.5750},
                {-2.7000, -0.3000,  1.5750},
                {-3.0000, -0.3000,  1.3500},
                {-3.0000,  0.0000,  1.3500},
                {-2.5000,  0.0000,  1.1250},
                {-2.5000, -0.3000,  1.1250},
                {-2.6500, -0.3000,  0.9375},
                {-2.6500,  0.0000,  0.9375},
                {-2.0000, -0.3000,  0.9000},
                {-1.9000, -0.3000,  0.6000},
                {-1.9000,  0.0000,  0.6000},
                {-3.0000,  0.3000,  1.3500},
                {-2.7000,  0.3000,  1.5750},
                {-2.6500,  0.3000,  0.9375},
                {-2.5000,  0.3000,  1.1250},
                {-1.9000,  0.3000,  0.6000},
                {-2.0000,  0.3000,  0.9000},
                { 1.7000,  0.0000,  1.4250},
                { 1.7000, -0.6600,  1.4250},
                { 1.7000, -0.6600,  0.6000},
                { 1.7000,  0.0000,  0.6000},
                { 2.6000,  0.0000,  1.4250},
                { 2.6000, -0.6600,  1.4250},
                { 3.1000, -0.6600,  0.8250},
                { 3.1000,  0.0000,  0.8250},
                { 2.3000,  0.0000,  2.1000},
                { 2.3000, -0.2500,  2.1000},
                { 2.4000, -0.2500,  2.0250},
                { 2.4000,  0.0000,  2.0250},
                { 2.7000,  0.0000,  2.4000},
                { 2.7000, -0.2500,  2.4000},
                { 3.3000, -0.2500,  2.4000},
                { 3.3000,  0.0000,  2.4000},
                { 1.7000,  0.6600,  0.6000},
                { 1.7000,  0.6600,  1.4250},
                { 3.1000,  0.6600,  0.8250},
                { 2.6000,  0.6600,  1.4250},
                { 2.4000,  0.2500,  2.0250},
                { 2.3000,  0.2500,  2.1000},
                { 3.3000,  0.2500,  2.4000},
                { 2.7000,  0.2500,  2.4000},
                { 2.8000,  0.0000,  2.4750},
                { 2.8000, -0.2500,  2.4750},
                { 3.5250, -0.2500,  2.4938},
                { 3.5250,  0.0000,  2.4938},
                { 2.9000,  0.0000,  2.4750},
                { 2.9000, -0.1500,  2.4750},
                { 3.4500, -0.1500,  2.5125},
                { 3.4500,  0.0000,  2.5125},
                { 2.8000,  0.0000,  2.4000},
                { 2.8000, -0.1500,  2.4000},
                { 3.2000, -0.1500,  2.4000},
                { 3.2000,  0.0000,  2.4000},
                { 3.5250,  0.2500,  2.4938},
                { 2.8000,  0.2500,  2.4750},
                { 3.4500,  0.1500,  2.5125},
                { 2.9000,  0.1500,  2.4750},
                { 3.2000,  0.1500,  2.4000},
                { 2.8000,  0.1500,  2.4000},
                { 0.0000,  0.0000,  3.1500},
                { 0.0000, -0.0020,  3.1500},
                { 0.0020,  0.0000,  3.1500},
                { 0.8000,  0.0000,  3.1500},
                { 0.8000, -0.4500,  3.1500},
                { 0.4500, -0.8000,  3.1500},
                { 0.0000, -0.8000,  3.1500},
                { 0.0000,  0.0000,  2.8500},
                { 0.2000,  0.0000,  2.7000},
                { 0.2000, -0.1120,  2.7000},
                { 0.1120, -0.2000,  2.7000},
                { 0.0000, -0.2000,  2.7000},
                {-0.0020,  0.0000,  3.1500},
                {-0.4500, -0.8000,  3.1500},
                {-0.8000, -0.4500,  3.1500},
                {-0.8000,  0.0000,  3.1500},
                {-0.1120, -0.2000,  2.7000},
                {-0.2000, -0.1120,  2.7000},
                {-0.2000,  0.0000,  2.7000},
                { 0.0000,  0.0020,  3.1500},
                {-0.8000,  0.4500,  3.1500},
                {-0.4500,  0.8000,  3.1500},
                { 0.0000,  0.8000,  3.1500},
                {-0.2000,  0.1120,  2.7000},
                {-0.1120,  0.2000,  2.7000},
                { 0.0000,  0.2000,  2.7000},
                { 0.4500,  0.8000,  3.1500},
                { 0.8000,  0.4500,  3.1500},
                { 0.1120,  0.2000,  2.7000},
                { 0.2000,  0.1120,  2.7000},
                { 0.4000,  0.0000,  2.5500},
                { 0.4000, -0.2240,  2.5500},
                { 0.2240, -0.4000,  2.5500},
                { 0.0000, -0.4000,  2.5500},
                { 1.3000,  0.0000,  2.5500},
                { 1.3000, -0.7280,  2.5500},
                { 0.7280, -1.3000,  2.5500},
                { 0.0000, -1.3000,  2.5500},
                { 1.3000,  0.0000,  2.4000},
                { 1.3000, -0.7280,  2.4000},
                { 0.7280, -1.3000,  2.4000},
                { 0.0000, -1.3000,  2.4000},
                {-0.2240, -0.4000,  2.5500},
                {-0.4000, -0.2240,  2.5500},
                {-0.4000,  0.0000,  2.5500},
                {-0.7280, -1.3000,  2.5500},
                {-1.3000, -0.7280,  2.5500},
                {-1.3000,  0.0000,  2.5500},
                {-0.7280, -1.3000,  2.4000},
                {-1.3000, -0.7280,  2.4000},
                {-1.3000,  0.0000,  2.4000},
                {-0.4000,  0.2240,  2.5500},
                {-0.2240,  0.4000,  2.5500},
                { 0.0000,  0.4000,  2.5500},
                {-1.3000,  0.7280,  2.5500},
                {-0.7280,  1.3000,  2.5500},
                { 0.0000,  1.3000,  2.5500},
                {-1.3000,  0.7280,  2.4000},
                {-0.7280,  1.3000,  2.4000},
                { 0.0000,  1.3000,  2.4000},
                { 0.2240,  0.4000,  2.5500},
                { 0.4000,  0.2240,  2.5500},
                { 0.7280,  1.3000,  2.5500},
                { 1.3000,  0.7280,  2.5500},
                { 0.7280,  1.3000,  2.4000},
                { 1.3000,  0.7280,  2.4000},
                { 0.0000,  0.0000,  0.0000},
                { 1.5000,  0.0000,  0.1500},
                { 1.5000,  0.8400,  0.1500},
                { 0.8400,  1.5000,  0.1500},
                { 0.0000,  1.5000,  0.1500},
                { 1.5000,  0.0000,  0.0750},
                { 1.5000,  0.8400,  0.0750},
                { 0.8400,  1.5000,  0.0750},
                { 0.0000,  1.5000,  0.0750},
                { 1.4250,  0.0000,  0.0000},
                { 1.4250,  0.7980,  0.0000},
                { 0.7980,  1.4250,  0.0000},
                { 0.0000,  1.4250,  0.0000},
                {-0.8400,  1.5000,  0.1500},
                {-1.5000,  0.8400,  0.1500},
                {-1.5000,  0.0000,  0.1500},
                {-0.8400,  1.5000,  0.0750},
                {-1.5000,  0.8400,  0.0750},
                {-1.5000,  0.0000,  0.0750},
                {-0.7980,  1.4250,  0.0000},
                {-1.4250,  0.7980,  0.0000},
                {-1.4250,  0.0000,  0.0000},
                {-1.5000, -0.8400,  0.1500},
                {-0.8400, -1.5000,  0.1500},
                { 0.0000, -1.5000,  0.1500},
                {-1.5000, -0.8400,  0.0750},
                {-0.8400, -1.5000,  0.0750},
                { 0.0000, -1.5000,  0.0750},
                {-1.4250, -0.7980,  0.0000},
                {-0.7980, -1.4250,  0.0000},
                { 0.0000, -1.4250,  0.0000},
                { 0.8400, -1.5000,  0.1500},
                { 1.5000, -0.8400,  0.1500},
                { 0.8400, -1.5000,  0.0750},
                { 1.5000, -0.8400,  0.0750},
                { 0.7980, -1.4250,  0.0000},
                { 1.4250, -0.7980,  0.0000}
        };
        int[][] patchesCP = new int[][]{
                {  1,   2,   3,   4,   5,   6,   7,   8,   9,  10,  11,  12,  13,  14,  15,  16},
                {  4,  17,  18,  19,   8,  20,  21,  22,  12,  23,  24,  25,  16,  26,  27,  28},
                { 19,  29,  30,  31,  22,  32,  33,  34,  25,  35,  36,  37,  28,  38,  39,  40},
                { 31,  41,  42,   1,  34,  43,  44,   5,  37,  45,  46,   9,  40,  47,  48,  13},
                { 13,  14,  15,  16,  49,  50,  51,  52,  53,  54,  55,  56,  57,  58,  59,  60},
                { 16,  26,  27,  28,  52,  61,  62,  63,  56,  64,  65,  66,  60,  67,  68,  69},
                { 28,  38,  39,  40,  63,  70,  71,  72,  66,  73,  74,  75,  69,  76,  77,  78},
                { 40,  47,  48,  13,  72,  79,  80,  49,  75,  81,  82,  53,  78,  83,  84,  57},
                { 57,  58,  59,  60,  85,  86,  87,  88,  89,  90,  91,  92,  93,  94,  95,  96},
                { 60,  67,  68,  69,  88,  97,  98,  99,  92, 100, 101, 102,  96, 103, 104, 105},
                { 69,  76,  77,  78,  99, 106, 107, 108, 102, 109, 110, 111, 105, 112, 113, 114},
                { 78,  83,  84,  57, 108, 115, 116,  85, 111, 117, 118,  89, 114, 119, 120,  93},
                {121, 122, 123, 124, 125, 126, 127, 128, 129, 130, 131, 132, 133, 134, 135, 136},
                {124, 137, 138, 121, 128, 139, 140, 125, 132, 141, 142, 129, 136, 143, 144, 133},
                {133, 134, 135, 136, 145, 146, 147, 148, 149, 150, 151, 152,  69, 153, 154, 155},
                {136, 143, 144, 133, 148, 156, 157, 145, 152, 158, 159, 149, 155, 160, 161,  69},
                {162, 163, 164, 165, 166, 167, 168, 169, 170, 171, 172, 173, 174, 175, 176, 177},
                {165, 178, 179, 162, 169, 180, 181, 166, 173, 182, 183, 170, 177, 184, 185, 174},
                {174, 175, 176, 177, 186, 187, 188, 189, 190, 191, 192, 193, 194, 195, 196, 197},
                {177, 184, 185, 174, 189, 198, 199, 186, 193, 200, 201, 190, 197, 202, 203, 194},
                {204, 204, 204, 204, 207, 208, 209, 210, 211, 211, 211, 211, 212, 213, 214, 215},
                {204, 204, 204, 204, 210, 217, 218, 219, 211, 211, 211, 211, 215, 220, 221, 222},
                {204, 204, 204, 204, 219, 224, 225, 226, 211, 211, 211, 211, 222, 227, 228, 229},
                {204, 204, 204, 204, 226, 230, 231, 207, 211, 211, 211, 211, 229, 232, 233, 212},
                {212, 213, 214, 215, 234, 235, 236, 237, 238, 239, 240, 241, 242, 243, 244, 245},
                {215, 220, 221, 222, 237, 246, 247, 248, 241, 249, 250, 251, 245, 252, 253, 254},
                {222, 227, 228, 229, 248, 255, 256, 257, 251, 258, 259, 260, 254, 261, 262, 263},
                {229, 232, 233, 212, 257, 264, 265, 234, 260, 266, 267, 238, 263, 268, 269, 242},
                {270, 270, 270, 270, 279, 280, 281, 282, 275, 276, 277, 278, 271, 272, 273, 274},
                {270, 270, 270, 270, 282, 289, 290, 291, 278, 286, 287, 288, 274, 283, 284, 285},
                {270, 270, 270, 270, 291, 298, 299, 300, 288, 295, 296, 297, 285, 292, 293, 294},
                {270, 270, 270, 270, 300, 305, 306, 279, 297, 303, 304, 275, 294, 301, 302, 271}
        };
        Matrix4D otw_internal = new Matrix4D(new double[][]{
                {-0.3, 0, 0, 0},
                {0, 0, 0.3, 0},
                {0, 0.3, 0, 0},
                {0, 0, 0, 1}
        });
        cP = otw_internal.transformVector(cP);
        Matrix4D otw = new Matrix4D(Matrix3D.identity, new Vector3d(0, 0, -5));
        BezierPatchesData bpd = new BezierPatchesData(numPatches, numCP, patchesCP, cP, otw_internal);
        bpd.setObjectToWorld(otw);
        return bpd;
    }

    public static BezierSurface33 getBezierSurfaceSample() {
        return new BezierSurface33(new Vector3d[][]{
                new Vector3d[]{new Vector3d(0, 0, 0),
                        new Vector3d(0, 0.5, 0),
                        new Vector3d(0.1, 0.8, 0),
                        new Vector3d(0, 1, 0),},

                new Vector3d[]{new Vector3d(0.3, 0, -0.5),
                        new Vector3d(0.3, 0.6, -0.2),
                        new Vector3d(0.4, 0.7, -0.5),
                        new Vector3d(0.3, 0.9, -0.5),
                },
                new Vector3d[]{
                        new Vector3d(0.7, 0.4, -0.7),
                        new Vector3d(0.5, 0.5, -0.3),
                        new Vector3d(0.7, 0.8, -0.7),
                        new Vector3d(0.8, 1, -0.5),
                },
                new Vector3d[]{
                        new Vector3d(1, 0.2, -0.7),
                        new Vector3d(1, 0.4, -0.5),
                        new Vector3d(1.1, 0.7, -0.3),
                        new Vector3d(0.9, 1.2, -0.8)
                }
        });
    }

    public static Vector3d[][] getBezierSurfaceSampleCP() {
        return new Vector3d[][]{
                new Vector3d[]{new Vector3d(0, 0, 0),
                        new Vector3d(0, 0.5, 0),
                        new Vector3d(0.1, 0.8, 0),
                        new Vector3d(0, 1, 0),},

                new Vector3d[]{new Vector3d(0.3, 0, -0.5),
                        new Vector3d(0.3, 0.6, -0.2),
                        new Vector3d(0.4, 0.7, -0.5),
                        new Vector3d(0.3, 0.9, -0.5),
                },
                new Vector3d[]{
                        new Vector3d(0.7, 0.4, -0.7),
                        new Vector3d(0.5, 0.5, -0.3),
                        new Vector3d(0.7, 0.8, -0.7),
                        new Vector3d(0.8, 1, -0.5),
                },
                new Vector3d[]{
                        new Vector3d(1, 0.2, -0.7),
                        new Vector3d(1, 0.4, -0.5),
                        new Vector3d(1.1, 0.7, -0.3),
                        new Vector3d(0.9, 1.2, -0.8)
                }
        };
    }

    public static Sphere getSphereSample(int i) {
        return sphere[i];
    }

    public static BSurface getBSplineSample1() {
            int p = 3;
            int q = 3;
            Vector3d[][] controlPointsSurface = {
                    {
                            new Vector3d(-1, -1, 0),
                            new Vector3d(-0.9, -0.8, -0.2),
                            new Vector3d(-0.8, -0.6, -0.2),
                            new Vector3d(-0.8, -0.2, -0.3),
                            new Vector3d(-0.7, 0, -0.2)
                    },
                    {
                            new Vector3d(-0.5, -1.2, -0.5),
                            new Vector3d(-0.4, -0.9, -0.6),
                            new Vector3d(-0.43, -0.7, -0.6),
                            new Vector3d(-0.38, -0.3, -0.5),
                            new Vector3d(-0.34, -0.1, -0.7)
                    },
                    {
                            new Vector3d(-0.2, -1, -0.1),
                            new Vector3d(-0.25, -0.8, -0.9),
                            new Vector3d(-0.27, -0.5, -0.7),
                            new Vector3d(-0.21, -0.4, -0.9),
                            new Vector3d(-0.18, -0.2, -1)
                    },
                    {
                            new Vector3d(0, -1, -0.5),
                            new Vector3d(0.1, -0.7, -0.6),
                            new Vector3d(-0.1, -0.5, -0.2),
                            new Vector3d(0, -0.2, -0.1),
                            new Vector3d(0.1, 0, -0.3)
                    },
                    {
                            new Vector3d(0.3, -1.3, -0.2),
                            new Vector3d(0.4, -1, -0.8),
                            new Vector3d(0.4, -0.8, -0.3),
                            new Vector3d(0.6, -0.5, -0.0),
                            new Vector3d(0.5, -0.2, -0.1)
                    },
            };
            double[] knotsU = {0, 0, 0, 0, 0.5, 1, 1, 1, 1};
            double[] knotsV = knotsU.clone();
            Matrix4D objectToWorld = new Matrix4D(Matrix3D.identity, new Vector3d(0, 1.4, -5));
            return new BSurface(controlPointsSurface, knotsU, knotsV, p, q, objectToWorld);
    }

    public static Vector3d[][] getBSplineSample1CP() {
        Vector3d[][] cp =  new Vector3d[][] {
                {
                        new Vector3d(-1, -1, 0),
                        new Vector3d(-0.9, -0.8, -0.2),
                        new Vector3d(-0.8, -0.6, -0.2),
                        new Vector3d(-0.8, -0.2, -0.3),
                        new Vector3d(-0.7, 0, -0.2)
                },
                {
                        new Vector3d(-0.5, -1.2, -0.5),
                        new Vector3d(-0.4, -0.9, -0.6),
                        new Vector3d(-0.43, -0.7, -0.6),
                        new Vector3d(-0.38, -0.3, -0.5),
                        new Vector3d(-0.34, -0.1, -0.7)
                },
                {
                        new Vector3d(-0.2, -1, -0.1),
                        new Vector3d(-0.25, -0.8, -0.9),
                        new Vector3d(-0.27, -0.5, -0.7),
                        new Vector3d(-0.21, -0.4, -0.9),
                        new Vector3d(-0.18, -0.2, -1)
                },
                {
                        new Vector3d(0, -1, -0.5),
                        new Vector3d(0.1, -0.7, -0.6),
                        new Vector3d(-0.1, -0.5, -0.2),
                        new Vector3d(0, -0.2, -0.1),
                        new Vector3d(0.1, 0, -0.3)
                },
                {
                        new Vector3d(0.3, -1.3, -0.2),
                        new Vector3d(0.4, -1, -0.8),
                        new Vector3d(0.4, -0.8, -0.3),
                        new Vector3d(0.6, -0.5, -0.0),
                        new Vector3d(0.5, -0.2, -0.1)
                },
        };
        return getBSplineSample1OTW().transformVector(cp);
    }

    public static int getBSplineSample1P() {
        return 3;
    }

    public static int getBSplineSample1Q() {
        return 3;
    }

    public static double[] getBSplineSample1U() {
        return new double[]{0, 0, 0, 0, 0.5, 1, 1, 1, 1};
    }

    public static double[] getBSplineSample1V() {
        return new double[]{0, 0, 0, 0, 0.5, 1, 1, 1, 1};
    }

    public static Matrix4D getBSplineSample1OTW() {
        return new Matrix4D(Matrix3D.identity, new Vector3d(0, 1, 0));
    }


    public static Vector3d[][] getBSurfaceInterpolationSample1CP() {
        Vector3d[][] dataPoints = MatrixUtilities.transpose2(new Vector3d[][]{
                {
                        new Vector3d(-0.9, -1, -0.25),
                        new Vector3d(-0.8, -0.92, -0.2),
                        new Vector3d(-0.7, -0.9, -0.2),
                        new Vector3d(-0.6, -0.88, -0.26),
                },
                {
                        new Vector3d(-0.88, -0.77, -0.25),
                        new Vector3d(-0.8, -0.69, -0.3),
                        new Vector3d(-0.7, -0.72, -0.2),
                        new Vector3d(-0.66, -0.68, -0.21),
                },
                {
                        new Vector3d(-0.86, -0.6, -0.33),
                        new Vector3d(-0.8, -0.58, -0.3),
                        new Vector3d(-0.75, -0.55, -0.2),
                        new Vector3d(-0.66, -0.55, -0.25),
                },
                {
                        new Vector3d(-0.9, -0.52, -0.28),
                        new Vector3d(-0.82, -0.44, -0.3),
                        new Vector3d(-0.77, -0.42, -0.22),
                        new Vector3d(-0.65, -0.47, -0.28),
                }
        });
        //move to default position more or less centered in the screen
        //this is actually not really formally correct since this interpolation
        // its not invariant under affine transform
        Matrix4D internalOTW = new Matrix4D(new double[][]{
                {3, 0, 0, 1.5},
                {0, 3, 0, 2.5},
                {0, 0, 3, 0},
                {0, 0, 0, 1}
        });
        return internalOTW.transformVector(dataPoints);
    }

    public static int getBSurfaceInterpolationSample1P() {
        return 2;
    }

    public static int getBSurfaceInterpolationSample1Q() {
        return 2;
    }

    public static Matrix4D getBSurfaceInterpolationSample1OTW() {
        return new Matrix4D(Matrix3D.identity, new Vector3d(0, 0, -4.5));
    }


    public static Vector3d[][] getInterpolatingSurfaceDonutDP() {
        //return a B-Spline surface obtained via interpolation of data points
        Vector3d[][] dataPoints = {{new Vector3d(2, 0, 0), new Vector3d(
                ((double)2/3)* (2 + Math.cos(1)), 0, ((double)2/3)* Math.sin(1)), new Vector3d(
                ((double)2/3)* (2 + Math.cos(2)), 0, ((double)2/3)* Math.sin(2)), new Vector3d(
                ((double)2/3)* (2 + Math.cos(3)), 0, ((double)2/3)* Math.sin(3)), new Vector3d(
                ((double)2/3)* (2 + Math.cos(4)), 0, ((double)2/3)* Math.sin(4)), new Vector3d(
                ((double)2/3)* (2 + Math.cos(5)), 0, ((double)2/3)* Math.sin(5)), new Vector3d(
                ((double)2/3)* (2 + Math.cos(6)), 0, ((double)2/3)* Math.sin(6))}, {new Vector3d(
                2*Math.cos(1), 2*Math.sin(1),
                0), new Vector3d((((double)2/3)* Math.cos(1))*(2 + Math.cos(1)), (
                ((double)2/3)* (2 + Math.cos(1)))*Math.sin(1), ((double)2/3)*
                Math.sin(1)), new Vector3d((((double)2/3)* Math.cos(1))*(2 + Math.cos(2)), (
                ((double)2/3)* (2 + Math.cos(2)))*Math.sin(1), ((double)2/3)*
                Math.sin(2)), new Vector3d((((double)2/3)* Math.cos(1))*(2 + Math.cos(3)), (
                ((double)2/3)* (2 + Math.cos(3)))*Math.sin(1), ((double)2/3)*
                Math.sin(3)), new Vector3d((((double)2/3)* Math.cos(1))*(2 + Math.cos(4)), (
                ((double)2/3)* (2 + Math.cos(4)))*Math.sin(1), ((double)2/3)*
                Math.sin(4)), new Vector3d((((double)2/3)* Math.cos(1))*(2 + Math.cos(5)), (
                ((double)2/3)* (2 + Math.cos(5)))*Math.sin(1), ((double)2/3)*
                Math.sin(5)), new Vector3d((((double)2/3)* Math.cos(1))*(2 + Math.cos(6)), (
                ((double)2/3)* (2 + Math.cos(6)))*Math.sin(1), ((double)2/3)* Math.sin(6))}, {new Vector3d(
                2*Math.cos(2), 2*Math.sin(2), 0), new Vector3d((((double)2/3)* (2 + Math.cos(1)))*
                Math.cos(2), (((double)2/3)* (2 + Math.cos(1)))*Math.sin(2), ((double)2/3)*
                Math.sin(1)), new Vector3d((((double)2/3)* Math.cos(2))*(2 + Math.cos(2)), (
                ((double)2/3)* (2 + Math.cos(2)))*Math.sin(2), ((double)2/3)*
                Math.sin(2)), new Vector3d((((double)2/3)* Math.cos(2))*(2 + Math.cos(3)), (
                ((double)2/3)* (2 + Math.cos(3)))*Math.sin(2), ((double)2/3)*
                Math.sin(3)), new Vector3d((((double)2/3)* Math.cos(2))*(2 + Math.cos(4)), (
                ((double)2/3)* (2 + Math.cos(4)))*Math.sin(2), ((double)2/3)*
                Math.sin(4)), new Vector3d((((double)2/3)* Math.cos(2))*(2 + Math.cos(5)), (
                ((double)2/3)* (2 + Math.cos(5)))*Math.sin(2), ((double)2/3)*
                Math.sin(5)), new Vector3d((((double)2/3)* Math.cos(2))*(2 + Math.cos(6)), (
                ((double)2/3)* (2 + Math.cos(6)))*Math.sin(2), ((double)2/3)* Math.sin(6))}, {new Vector3d(
                2*Math.cos(3), 2*Math.sin(3), 0), new Vector3d((((double)2/3)* (2 + Math.cos(1)))*
                Math.cos(3), (((double)2/3)* (2 + Math.cos(1)))*Math.sin(3), ((double)2/3)*
                Math.sin(1)), new Vector3d((((double)2/3)* (2 + Math.cos(2)))*
                Math.cos(3), (((double)2/3)* (2 + Math.cos(2)))*Math.sin(3), ((double)2/3)*
                Math.sin(2)), new Vector3d((((double)2/3)* Math.cos(3))*(2 + Math.cos(3)), (
                ((double)2/3)* (2 + Math.cos(3)))*Math.sin(3), ((double)2/3)*
                Math.sin(3)), new Vector3d((((double)2/3)* Math.cos(3))*(2 + Math.cos(4)), (
                ((double)2/3)* (2 + Math.cos(4)))*Math.sin(3), ((double)2/3)*
                Math.sin(4)), new Vector3d((((double)2/3)* Math.cos(3))*(2 + Math.cos(5)), (
                ((double)2/3)* (2 + Math.cos(5)))*Math.sin(3), ((double)2/3)*
                Math.sin(5)), new Vector3d((((double)2/3)* Math.cos(3))*(2 + Math.cos(6)), (
                ((double)2/3)* (2 + Math.cos(6)))*Math.sin(3), ((double)2/3)* Math.sin(6))}, {new Vector3d(
                2*Math.cos(4), 2*Math.sin(4), 0), new Vector3d((((double)2/3)* (2 + Math.cos(1)))*
                Math.cos(4), (((double)2/3)* (2 + Math.cos(1)))*Math.sin(4), ((double)2/3)*
                Math.sin(1)), new Vector3d((((double)2/3)* (2 + Math.cos(2)))*
                Math.cos(4), (((double)2/3)* (2 + Math.cos(2)))*Math.sin(4), ((double)2/3)*
                Math.sin(2)), new Vector3d((((double)2/3)* (2 + Math.cos(3)))*
                Math.cos(4), (((double)2/3)* (2 + Math.cos(3)))*Math.sin(4), ((double)2/3)*
                Math.sin(3)), new Vector3d((((double)2/3)* Math.cos(4))*(2 + Math.cos(4)), (
                ((double)2/3)* (2 + Math.cos(4)))*Math.sin(4), ((double)2/3)*
                Math.sin(4)), new Vector3d((((double)2/3)* Math.cos(4))* (2 + Math.cos(5)), (
                ((double)2/3)* (2 + Math.cos(5)))* Math.sin(4), ((double)2/3)*
                Math.sin(5)), new Vector3d((((double)2/3)* Math.cos(4)) *(2 + Math.cos(6)), (
                ((double)2/3)* (2 + Math.cos(6))) *Math.sin(4), ((double)2/3)* Math.sin(6))}, {new Vector3d(
                2 *Math.cos(5), 2 *Math.sin(5), 0), new Vector3d((((double)2/3)* (2 + Math.cos(1)))*
                Math.cos(5), (((double)2/3)* (2 + Math.cos(1))) *Math.sin(5), ((double)2/3)*
                Math.sin(1)), new Vector3d((((double)2/3)* (2 + Math.cos(2)))*
                Math.cos(5), (((double)2/3)* (2 + Math.cos(2))) *Math.sin(5), ((double)2/3)*
                Math.sin(2)), new Vector3d((((double)2/3)* (2 + Math.cos(3)))*
                Math.cos(5), (((double)2/3)* (2 + Math.cos(3))) *Math.sin(5), ((double)2/3)*
                Math.sin(3)), new Vector3d((((double)2/3)* (2 + Math.cos(4)))*
                Math.cos(5), (((double)2/3)* (2 + Math.cos(4))) *Math.sin(5), ((double)2/3)*
                Math.sin(4)), new Vector3d((((double)2/3)* Math.cos(5)) *(2 + Math.cos(5)), (
                ((double)2/3)* (2 + Math.cos(5))) *Math.sin(5), ((double)2/3)*
                Math.sin(5)), new Vector3d((((double)2/3)* Math.cos(5)) *(2 + Math.cos(6)), (
                ((double)2/3)* (2 + Math.cos(6))) *Math.sin(5), ((double)2/3)* Math.sin(6))}, {new Vector3d(
                2 *Math.cos(6), 2 *Math.sin(6), 0), new Vector3d((((double)2/3)* (2 + Math.cos(1)))*
                Math.cos(6), (((double)2/3)* (2 + Math.cos(1))) *Math.sin(6), ((double)2/3)*
                Math.sin(1)), new Vector3d((((double)2/3)* (2 + Math.cos(2)))*
                Math.cos(6), (((double)2/3)* (2 + Math.cos(2))) *Math.sin(6), ((double)2/3)*
                Math.sin(2)), new Vector3d((((double)2/3)* (2 + Math.cos(3)))*
                Math.cos(6), (((double)2/3)* (2 + Math.cos(3))) *Math.sin(6), ((double)2/3)*
                Math.sin(3)), new Vector3d((((double)2/3)* (2 + Math.cos(4)))*
                Math.cos(6), (((double)2/3)* (2 + Math.cos(4))) *Math.sin(6), ((double)2/3)*
                Math.sin(4)), new Vector3d((((double)2/3)* (2 + Math.cos(5)))*
                Math.cos(6), (((double)2/3)* (2 + Math.cos(5))) *Math.sin(6), ((double)2/3)*
                Math.sin(5)), new Vector3d((((double)2/3)* Math.cos(6)) *(2 + Math.cos(6)), (
                ((double)2/3)* (2 + Math.cos(6))) *Math.sin(6), ((double)2/3)* Math.sin(6))}};
        Matrix4D objectToWorld_internal = new Matrix4D(new Matrix3D(new double[][]{
                {0, 1, 0},
                {-1, 0, 0},
                {0, 0, 1}
        }), new Vector3d(0, 0, 0));
        //note that depending on the order in which we gave the data points,
        //it could be necessary to transpose the data points matrix
        //my function assumes that the u direction increases with the row index
        //while it usually comes more natural to write the points manually with u increasing with column index
        dataPoints = objectToWorld_internal.transformVector(dataPoints);
        return dataPoints;
    }

    public static int getInterpolatingSurfaceDonutP() {
        return 6;
    }

    public static int getInterpolatingSurfaceDonutQ() {
        return 6;
    }

    public static Matrix4D getInterpolatingSurfaceDonutOTW() {
        return new Matrix4D(Matrix3D.identity, new Vector3d(0, 1, -10));
    }

}
