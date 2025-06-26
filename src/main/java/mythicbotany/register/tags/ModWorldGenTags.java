package mythicbotany.register.tags;

import mythicbotany.MythicBotany;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import org.moddingx.libx.sandbox.SandBox;
import org.moddingx.libx.sandbox.generator.BiomeLayer;

public class ModWorldGenTags {

    public static final TagKey<BiomeLayer> ALFHEIM_LAYERS = TagKey.create(SandBox.BIOME_LAYER, MythicBotany.getInstance().resource("alfheim"));
    
    public static final TagKey<Biome> ALFHEIM = TagKey.create(Registries.BIOME, MythicBotany.getInstance().resource("alfheim"));
    public static final TagKey<Biome> ANDWARI_CAVE = BiomeTags.create(MythicBotany.getInstance().resource("andwari_cave").toString());
    public static final TagKey<Biome> ELVEN_HOUSES = BiomeTags.create(MythicBotany.getInstance().resource("elven_houses").toString());
}
