package mythicbotany.register.tags;

import mythicbotany.MythicBotany;
import net.minecraft.core.Registry;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class ModBiomeTags {

    public static final TagKey<Biome> ALFHEIM = TagKey.create(Registry.BIOME_REGISTRY, MythicBotany.getInstance().resource("alfheim"));
    public static final TagKey<Biome> ANDWARI_CAVE = BiomeTags.create(MythicBotany.getInstance().resource("andwari_cave").toString());
}
