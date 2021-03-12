public class BezierPatchesData {

    private int numPatches;
    private int numCP;
    private int[][] patchesCP;
    private double[][] cP;



    public static BezierPatchesData createTeapot() {
        BezierPatchesData teaPot = new BezierPatchesData();
        teaPot.numPatches = 32;
        teaPot.numCP = 306;
        teaPot.cP = new double[][] {
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
        teaPot.patchesCP = new int[][]{
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
        return teaPot;
    }

    public static Matrix4D getTeapotOTW() {
        Matrix4D otw = new Matrix4D(new double[][]{
                {0.3, 0, 0, 1.6},
                {0, 0, 0.3, 0},
                {0, 0.3, 0, -5},
                {0, 0, 0, 1}
        });
        return otw;
    }

    public static Matrix4D getTeapotCTW() {
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
            Vector3f controlPoints[] = new Vector3f[16];
            for (int j = 0; j < 16; j++) {
                controlPoints[j] = new Vector3f(cP[patchesCP[i][j]-1]);
            }
            BezierSurface33 bSurface = new BezierSurface33(controlPoints);
            patches[i] = bSurface;
        }
        return patches;
    }

    public BezierSurface33[] getSurfaces(Matrix4D objectToWorld) {
        BezierSurface33[] patches = new BezierSurface33[numPatches];
        for (int i = 0; i < numPatches; i++) {
            Vector3f controlPoints[] = new Vector3f[16];
            for (int j = 0; j < 16; j++) {
                controlPoints[j] = new Vector3f(cP[patchesCP[i][j]-1]);
            }
            BezierSurface33 bSurface = new BezierSurface33(controlPoints, objectToWorld);
            patches[i] = bSurface;
        }
        return patches;
    }

}
