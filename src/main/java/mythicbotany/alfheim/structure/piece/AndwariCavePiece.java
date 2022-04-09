package mythicbotany.alfheim.structure.piece;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.Nonnull;

public class AndwariCavePiece extends BaseStructurePiece {

    public static final StructurePoolElementType<AndwariCavePiece> TYPE = type(AndwariCavePiece::new);

    protected AndwariCavePiece(Either<ResourceLocation, StructureTemplate> template, Holder<StructureProcessorList> processors, StructureTemplatePool.Projection projection) {
        super(template, processors, projection);
    }

    @Nonnull
    @Override
    public StructurePoolElementType<?> getType() {
        return TYPE;
    }

    @Override
    protected Vec3i placementOffset(WorldGenLevel level, ChunkGenerator generator) {
        return new Vec3i(0, -6, 0);
    }
}
