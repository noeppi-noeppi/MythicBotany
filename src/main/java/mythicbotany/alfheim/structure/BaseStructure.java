package mythicbotany.alfheim.structure;

import mythicbotany.MythicBotany;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.structures.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;

import javax.annotation.Nonnull;
import java.util.Optional;

public class BaseStructure extends StructureFeature<JigsawConfiguration> {

    public BaseStructure(String structureId) {
        super(JigsawConfiguration.CODEC, new PlacementFactory(structureId));
    }

    @Nonnull
    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }

    private static class PlacementFactory implements PieceGeneratorSupplier<JigsawConfiguration> {

        private final String structureId;

        private PlacementFactory(String structureId) {
            this.structureId = structureId;
        }

        @Nonnull
        @Override
        public Optional<PieceGenerator<JigsawConfiguration>> createGenerator(@Nonnull Context<JigsawConfiguration> context) {
            BlockPos centerOfChunk = context.chunkPos().getMiddleBlockPosition(0);
            int landHeight = context.chunkGenerator().getFirstOccupiedHeight(centerOfChunk.getX(), centerOfChunk.getZ(), Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor());
            NoiseColumn columnOfBlocks = context.chunkGenerator().getBaseColumn(centerOfChunk.getX(), centerOfChunk.getZ(), context.heightAccessor());
            BlockState topBlock = columnOfBlocks.getBlock(landHeight);
            if (!topBlock.getFluidState().isEmpty()) return Optional.empty();

            JigsawConfiguration config = new JigsawConfiguration(
                    () -> context.registryAccess().ownedRegistryOrThrow(Registry.TEMPLATE_POOL_REGISTRY)
                            .get(MythicBotany.getInstance().resource(structureId)),
                    10
            );

            return JigsawPlacement.addPieces(
                    StructureUtils.withConfig(context, config), PoolElementStructurePiece::new,
                    context.chunkPos().getMiddleBlockPosition(0), false, true
            );
        }
    }
}
