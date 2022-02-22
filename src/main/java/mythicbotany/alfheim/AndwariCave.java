package mythicbotany.alfheim;

import io.github.noeppi_noeppi.libx.mod.registration.Registerable;
import io.github.noeppi_noeppi.libx.util.NBTX;
import mythicbotany.MythicBotany;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Random;
import java.util.function.Consumer;

public class AndwariCave extends StructureFeature<NoneFeatureConfiguration> implements Registerable {

    private static final ResourceLocation TEMPLATE = new ResourceLocation(MythicBotany.getInstance().modid, "andwari_cave");
    
    public AndwariCave() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public void registerCommon(ResourceLocation id, Consumer<Runnable> defer) {
        defer.accept(() -> StructureFeature.STRUCTURES_REGISTRY.put(id.toString(), this));
    }

    @Nonnull
    public StructureFeature.StructureStartFactory<NoneFeatureConfiguration> getStartFactory() {
        return Start::new;
    }

    @Nonnull
    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }
    
    public static class Start extends StructureStart<NoneFeatureConfiguration> {
        
        public Start(StructureFeature<NoneFeatureConfiguration> structure, int x, int z, BoundingBox box, int refCount, long seed) {
            super(structure, x, z, box, refCount, seed);
        }

        @Override
        public void generatePieces(@Nonnull RegistryAccess registries, @Nonnull ChunkGenerator generator, @Nonnull StructureManager templates, int chunkX, int chunkZ, @Nonnull Biome biome, @Nonnull NoneFeatureConfiguration config) {
            int x = (chunkX * 16) + random.nextInt(16) - 8;
            int z = (chunkZ * 16) + random.nextInt(16) - 8;
            Piece piece = new Piece(templates, new BlockPos(chunkX * 16, generator.getBaseHeight(x, z, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES) - 7, z), Rotation.getRandom(random));
            this.pieces.add(piece);
            this.calculateBoundingBox();
        }
    }
    
    public static class Piece extends TemplateStructurePiece {
        
        public Piece(StructureManager templates, BlockPos pos, Rotation rot) {
            super(AlfheimFeatures.ANDWARI_CAVE_PIECE, 0);
            this.setup(Objects.requireNonNull(templates.get(TEMPLATE), "Andwari Cave template not found."), pos, (new StructurePlaceSettings()).setRotation(rot).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK));
        }

        public Piece(StructureManager templates, CompoundTag nbt) {
            super(AlfheimFeatures.ANDWARI_CAVE_PIECE, nbt);
            this.setup(Objects.requireNonNull(templates.get(TEMPLATE), "Andwari Cave template not found."), NBTX.getPos(nbt, "StructurePos", BlockPos.ZERO), (new StructurePlaceSettings()).setRotation(Rotation.values()[nbt.getInt("StructureRotation")]).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK));
        }

        @Override
        protected void handleDataMarker(@Nonnull String function, @Nonnull BlockPos pos, @Nonnull ServerLevelAccessor level, @Nonnull Random random, @Nonnull BoundingBox sbb) {
            //
        }

        @Override
        protected void addAdditionalSaveData(@Nonnull CompoundTag nbt) {
            super.addAdditionalSaveData(nbt);
            NBTX.putPos(nbt, "StructurePos", templatePosition);
            nbt.putInt("StructureRotation", placeSettings.getRotation().ordinal());
        }
        
        @Override
        public boolean postProcess(@Nonnull WorldGenLevel level, @Nonnull StructureFeatureManager structures, @Nonnull ChunkGenerator generator, @Nonnull Random rand, @Nonnull BoundingBox box, @Nonnull ChunkPos chunk, @Nonnull BlockPos pos) {
            this.templatePosition = AlfheimWorldGen.highestFreeBlock(level, this.templatePosition, AlfheimWorldGen::passReplaceableAndLeaves).below(8);
            return super.postProcess(level, structures, generator, rand, box, chunk, pos);
        }
    }
}
