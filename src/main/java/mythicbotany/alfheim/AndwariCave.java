package mythicbotany.alfheim;

import io.github.noeppi_noeppi.libx.mod.registration.Registerable;
import io.github.noeppi_noeppi.libx.util.NBTX;
import mythicbotany.MythicBotany;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.TemplateManager;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Random;
import java.util.function.Consumer;

public class AndwariCave extends Structure<NoFeatureConfig> implements Registerable {

    private static final ResourceLocation TEMPLATE = new ResourceLocation(MythicBotany.getInstance().modid, "andwari_cave");
    
    public AndwariCave() {
        super(NoFeatureConfig.CODEC);
    }

    @Override
    public void registerCommon(ResourceLocation id, Consumer<Runnable> defer) {
        defer.accept(() -> Structure.NAME_STRUCTURE_BIMAP.put(id.toString(), this));
    }

    @Nonnull
    public Structure.IStartFactory<NoFeatureConfig> getStartFactory() {
        return Start::new;
    }

    @Nonnull
    @Override
    public GenerationStage.Decoration getDecorationStage() {
        return GenerationStage.Decoration.SURFACE_STRUCTURES;
    }
    
    public static class Start extends StructureStart<NoFeatureConfig> {
        
        public Start(Structure<NoFeatureConfig> structure, int x, int z, MutableBoundingBox box, int refCount, long seed) {
            super(structure, x, z, box, refCount, seed);
        }

        @Override
        public void addStructurePieces(@Nonnull DynamicRegistries registries, @Nonnull ChunkGenerator generator, @Nonnull TemplateManager templates, int chunkX, int chunkZ, @Nonnull Biome biome, @Nonnull NoFeatureConfig config) {
            int x = (chunkX * 16) + rand.nextInt(16) - 8;
            int z = (chunkZ * 16) + rand.nextInt(16) - 8;
            Piece piece = new Piece(templates, new BlockPos(chunkX * 16, generator.getHeight(x, z, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES) - 7, z), Rotation.randomRotation(rand));
            this.components.add(piece);
            this.recalculateStructureSize();
        }
    }
    
    public static class Piece extends TemplateStructurePiece {
        
        public Piece(TemplateManager templates, BlockPos pos, Rotation rot) {
            super(AlfheimFeatures.ANDWARI_CAVE_PIECE, 0);
            this.setup(Objects.requireNonNull(templates.getTemplate(TEMPLATE), "Andwari Cave template not found."), pos, (new PlacementSettings()).setRotation(rot).setMirror(Mirror.NONE).addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK));
        }

        public Piece(TemplateManager templates, CompoundNBT nbt) {
            super(AlfheimFeatures.ANDWARI_CAVE_PIECE, nbt);
            this.setup(Objects.requireNonNull(templates.getTemplate(TEMPLATE), "Andwari Cave template not found."), NBTX.getPos(nbt, "StructurePos", BlockPos.ZERO), (new PlacementSettings()).setRotation(Rotation.values()[nbt.getInt("StructureRotation")]).setMirror(Mirror.NONE).addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK));
        }

        @Override
        protected void handleDataMarker(@Nonnull String function, @Nonnull BlockPos pos, @Nonnull IServerWorld world, @Nonnull Random rand, @Nonnull MutableBoundingBox box) {
            //
        }

        @Override
        protected void readAdditional(@Nonnull CompoundNBT nbt) {
            super.readAdditional(nbt);
            NBTX.putPos(nbt, "StructurePos", templatePosition);
            nbt.putInt("StructureRotation", placeSettings.getRotation().ordinal());
        }
        
        @Override
        public boolean generateStructurePiece(@Nonnull ISeedReader world, @Nonnull StructureManager structures, @Nonnull ChunkGenerator generator, @Nonnull Random rand, @Nonnull MutableBoundingBox box, @Nonnull ChunkPos chunk, @Nonnull BlockPos pos) {
            this.templatePosition = AlfheimWorldGen.highestFreeBlock(world, this.templatePosition, AlfheimWorldGen::passReplaceableAndLeaves).down(8);
            return super.generateStructurePiece(world, structures, generator, rand, box, chunk, pos);
        }
    }
}
