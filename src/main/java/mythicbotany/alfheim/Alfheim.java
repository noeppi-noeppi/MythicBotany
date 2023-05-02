package mythicbotany.alfheim;

import mythicbotany.MythicBotany;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

// TODO 1.19.4 use a biome layer tag
public class Alfheim {

    public static final ResourceKey<Level> DIMENSION = ResourceKey.create(Registry.DIMENSION_REGISTRY, MythicBotany.getInstance().resource("alfheim"));
    
    // TODO 1.19.4 reference the biome provider directly to get the biome ids in the tag provider
    public static final ResourceKey<Biome> ALFHEIM_PLAINS = ResourceKey.create(Registry.BIOME_REGISTRY, MythicBotany.getInstance().resource("alfheim_plains"));
    public static final ResourceKey<Biome> ALFHEIM_HILLS = ResourceKey.create(Registry.BIOME_REGISTRY, MythicBotany.getInstance().resource("alfheim_hills"));
    public static final ResourceKey<Biome> DREAMWOOD_FOREST = ResourceKey.create(Registry.BIOME_REGISTRY, MythicBotany.getInstance().resource("dreamwood_forest"));
    public static final ResourceKey<Biome> GOLDEN_FIELDS = ResourceKey.create(Registry.BIOME_REGISTRY, MythicBotany.getInstance().resource("golden_fields"));
    public static final ResourceKey<Biome> ALFHEIM_LAKES = ResourceKey.create(Registry.BIOME_REGISTRY, MythicBotany.getInstance().resource("alfheim_lakes"));
}
