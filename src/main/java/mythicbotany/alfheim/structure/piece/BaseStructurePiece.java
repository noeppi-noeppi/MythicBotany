package mythicbotany.alfheim.structure.piece;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.noeppi_noeppi.libx.fi.Function3;
import mythicbotany.MythicBotany;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.structures.SinglePoolElement;
import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElementType;
import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;

import javax.annotation.Nonnull;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class BaseStructurePiece extends SinglePoolElement {
    
    public static <T extends BaseStructurePiece> Codec<T> codec(Function3<Either<ResourceLocation, StructureTemplate>, Supplier<StructureProcessorList>, StructureTemplatePool.Projection, T> ctor) {
        return RecordCodecBuilder.create((builder) -> builder.group(
                templateCodec(),
                processorsCodec(),
                projectionCodec()
        ).apply(builder, ctor::apply));
    }

    public static <T extends BaseStructurePiece> StructurePoolElementType<T> type(Function3<Either<ResourceLocation, StructureTemplate>, Supplier<StructureProcessorList>, StructureTemplatePool.Projection, T> ctor) {
        Codec<T> codec = codec(ctor);
        return () -> codec;
    }

    protected BaseStructurePiece(Either<ResourceLocation, StructureTemplate> template, Supplier<StructureProcessorList> processors, StructureTemplatePool.Projection projection) {
        super(template, processors, projection);
    }

    @Override
    public boolean place(@Nonnull StructureManager templates, @Nonnull WorldGenLevel level, @Nonnull StructureFeatureManager structures, @Nonnull ChunkGenerator generator, @Nonnull BlockPos pos, @Nonnull BlockPos processorPos, @Nonnull Rotation rot, @Nonnull BoundingBox box, @Nonnull Random random, boolean jigsaw) {
        Vec3i offset = this.placementOffset(level, generator);
        StructureTemplate template = this.template.map(templates::getOrCreate, Function.identity());
        StructurePlaceSettings settings = this.getSettings(rot, box, jigsaw);
        if (!template.placeInWorld(level, pos.offset(offset), processorPos.offset(offset), settings, random, 18)) {
            return false;
        } else {
            for (StructureTemplate.StructureBlockInfo info : StructureTemplate.processBlockInfos(level, pos.offset(offset), processorPos.offset(offset), settings, this.getDataMarkers(templates, pos.offset(offset), rot, false), template)) {
                this.handleCustomDataMarker(templates, structures, level, info, info.pos, rot, random, box);
            }
            return true;
        }
    }

    @Nonnull
    @Override
    protected StructurePlaceSettings getSettings(@Nonnull Rotation rot, @Nonnull BoundingBox box, boolean jigsaw) {
        StructurePlaceSettings settings = super.getSettings(rot, box, jigsaw);
        // Don't ignore structure blocks
        settings.popProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
        return settings;
    }

    // Same as handleDataMarker but will get the template manager
    public void handleCustomDataMarker(StructureManager templates, StructureFeatureManager structures, WorldGenLevel level, StructureTemplate.StructureBlockInfo block, BlockPos pos, Rotation rot, Random random, BoundingBox box) {
        //noinspection ConstantConditions
        String data = block.nbt == null ? "" : block.nbt.getString("metadata");
        // Replace structure block in all cases
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
        onDataMarker(data, templates, structures, level, block, pos, rot, random, box);
        super.handleDataMarker(level, block, pos, rot, random, box);
    }

    @Nonnull
    @Override
    public abstract StructurePoolElementType<?> getType();
    
    protected void onDataMarker(String name, StructureManager templates, StructureFeatureManager structures, WorldGenLevel level, StructureTemplate.StructureBlockInfo block, BlockPos pos, Rotation rot, Random random, BoundingBox box) {
        
    }
    
    protected Vec3i placementOffset(WorldGenLevel level, ChunkGenerator generator) {
        return Vec3i.ZERO;
    }

    protected final void placePiece(StructureManager templates, WorldGenLevel level, String name, BlockPos pos, Random random) {
        StructureTemplate template = templates.getOrCreate(MythicBotany.getInstance().resource("parts/" + name));
        template.placeInWorld(level, pos, pos, new StructurePlaceSettings(), random, 4);
    }

    protected final void addEntity(WorldGenLevel level, Entity entity) {
        if (level instanceof WorldGenRegion region) {
            int x = ((int) Math.floor(entity.getX())) >> 4;
            int z = ((int) Math.floor(entity.getZ())) >> 4;
            if (region.getCenter().x == x && region.getCenter().z == z) {
                level.addFreshEntity(entity);
            }
        } else {
            level.addFreshEntity(entity);
        }
    }
}
