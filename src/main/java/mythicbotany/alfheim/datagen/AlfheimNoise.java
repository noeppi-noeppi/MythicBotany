package mythicbotany.alfheim.datagen;

import io.github.noeppi_noeppi.mods.sandbox.datagen.ext.NoiseData;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import vazkii.botania.common.block.BotaniaBlocks;

public class AlfheimNoise extends NoiseData {
    
    // TODO adjust noise router
    public final Holder<NoiseGeneratorSettings> alfheim = this.generator()
            .defaultBlock(BotaniaBlocks.livingrock)
            .disableOreVeins()
            .build();
    
    public AlfheimNoise(Properties properties) {
        super(properties);
    }
}
