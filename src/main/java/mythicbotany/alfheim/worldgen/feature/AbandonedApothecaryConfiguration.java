package mythicbotany.alfheim.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import mythicbotany.MythicBotany;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraftforge.registries.ForgeRegistries;
import org.moddingx.libx.annotation.api.Codecs;
import org.moddingx.libx.annotation.codec.Dynamic;
import org.moddingx.libx.annotation.codec.PrimaryConstructor;

import java.util.List;

@PrimaryConstructor
public record AbandonedApothecaryConfiguration(List<BlockState> states, @Dynamic(AbandonedApothecaryConfiguration.class) List<Item> petals) implements FeatureConfiguration {
    
    public static final Codec<AbandonedApothecaryConfiguration> CODEC = Codecs.get(MythicBotany.class, AbandonedApothecaryConfiguration.class);
    
    public static MapCodec<List<Item>> fieldOf(String name) {
        return ForgeRegistries.ITEMS.getCodec().listOf().fieldOf(name);
    }
}
