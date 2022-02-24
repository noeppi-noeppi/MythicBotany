package mythicbotany.alfheim.surface;

import mythicbotany.alfheim.BiomeConfiguration;

public class BiomeTemplates {
    
    public static void ocean(BiomeConfiguration gen) {
        gen.depth(-0.3f, -0.1f);
        gen.continentalness(-0.03f, 0.01f);
        gen.erosion(-0.5f, -0.3f);
        gen.weirdness(-0.8f, -0.5f);
    }
    
    public static void lake(BiomeConfiguration gen) {
        gen.depth(-0.1f, -0.02f);
        gen.continentalness(-0.04f, -0.01f);
        gen.erosion(-0.01f, 1.01f);
        gen.weirdness(-0.8f, -0.4f);
    }
    
    public static void flat(BiomeConfiguration gen) {
        gen.depth(0.01f, 0.03f);
        gen.continentalness(-0.02f, 0.05f);
        gen.erosion(-0.5f, -0.3f);
        gen.weirdness(-0.6f, -0.3f);
    }
    
    public static void moderate(BiomeConfiguration gen) {
        gen.depth(0.02f, 0.04f);
        gen.continentalness(0.02f, 0.06f);
        gen.erosion(-0.4f, -0.1f);
        gen.weirdness(-0.6f, -0.3f);
    }
    
    public static void hills(BiomeConfiguration gen) {
        gen.depth(0.03f, 0.2f);
        gen.continentalness(0.5f, 0.8f);
        gen.erosion(-0.3f, 0.2f);
        gen.weirdness(-0.3f, 0.2f);
    }
    
    public static void mountains(BiomeConfiguration gen) {
        gen.depth(0.2f, 0.5f);
        gen.continentalness(0.6f, 1f);
        gen.erosion(0.1f, 0.25f);
        gen.weirdness(0.1f, 0.25f);
    }
}
