package mythicbotany.alfheim.structure;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;

public class StructureUtils {
    
    public static <T extends FeatureConfiguration> PieceGeneratorSupplier.Context<T> withConfig(PieceGeneratorSupplier.Context<?> context, T config) {
        return new PieceGeneratorSupplier.Context<>(
                context.chunkGenerator(), context.biomeSource(), context.seed(), context.chunkPos(), config,
                context.heightAccessor(), context.validBiome(), context.structureManager(), context.registryAccess()
        );
    }
}
