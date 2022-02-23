package mythicbotany.alfheim.teleporter;

import com.google.common.collect.ImmutableSet;
import io.github.noeppi_noeppi.libx.mod.ModX;
import io.github.noeppi_noeppi.libx.base.tile.BlockBE;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.function.Consumer;

public class BlockReturnPortal extends BlockBE<TileReturnPortal> {

    public BlockReturnPortal(ModX mod, Properties properties) {
        super(mod, TileReturnPortal.class, properties);
    }

    public BlockReturnPortal(ModX mod, Properties properties, Item.Properties itemProperties) {
        super(mod, TileReturnPortal.class, properties, itemProperties);
    }

    @Override
    public Set<Object> getAdditionalRegisters(ResourceLocation id) {
        return super.getAdditionalRegisters(id).stream()
                .filter(o -> !(o instanceof BlockItem))
                .collect(ImmutableSet.toImmutableSet());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void registerClient(ResourceLocation id, Consumer<Runnable> defer) {
        BlockEntityRenderers.register(this.getBlockEntityType(), mgr -> new RenderReturnPortal());
    }
    
    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public RenderShape getRenderShape(@Nonnull BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
}
