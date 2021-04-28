package mythicbotany.alfheim;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.noeppi_noeppi.libx.world.WorldSeedHolder;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.NoiseChunkGenerator;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import vazkii.botania.client.render.world.SkyblockSkyRenderer;
import vazkii.botania.common.block.ModBlocks;

import java.util.Optional;

public class AlfheimChunkGenerator {

    public static final Codec<NoiseChunkGenerator> CODEC = RecordCodecBuilder.create((p_236091_0_) -> p_236091_0_.group(
            BiomeProvider.CODEC.fieldOf("biome_source").forGetter(ChunkGenerator::getBiomeProvider),
            WorldSeedHolder.CODEC.fieldOf("seed").forGetter((p_236093_0_) -> p_236093_0_.field_236084_w_)
    ).apply(p_236091_0_, p_236091_0_.stable((b, s) -> new NoiseChunkGenerator(b, s, AlfheimChunkGenerator::settings))));

    private static DimensionSettings settings() {
        DimensionSettings defaultSettings = DimensionSettings.func_242746_i();
        //noinspection deprecation
        return new DimensionSettings(structures(defaultSettings.getStructures()), defaultSettings.getNoise(),
                ModBlocks.livingrock.getDefaultState(), Blocks.WATER.getDefaultState(), defaultSettings.func_236117_e_(),
                defaultSettings.func_236118_f_(), defaultSettings.func_236119_g_(), defaultSettings.func_236120_h_());
    }
    
    private static DimensionStructuresSettings structures(DimensionStructuresSettings parent) {
        return new DimensionStructuresSettings(Optional.ofNullable(parent.func_236199_b_()), ImmutableMap.<Structure<?>, StructureSeparationSettings>builder()
                .put(AlfheimWorldGen.andwariCave, new StructureSeparationSettings(200, 100, 438))
                .build()
        );
    }
}
