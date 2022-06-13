package mythicbotany.alfheim.teleporter;

import com.google.common.collect.ImmutableSet;
import io.github.noeppi_noeppi.libx.base.tile.BlockBE;
import io.github.noeppi_noeppi.libx.mod.ModX;
import mythicbotany.register.HackyHolder;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
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
    @SuppressWarnings("deprecation")
    public Set<Object> getAdditionalRegisters(ResourceLocation id) {
        Set<Object> parent = super.getAdditionalRegisters(id);
        parent.stream().filter(e -> e instanceof BlockItem).forEach(e -> HackyHolder.bindIntrusive(Registry.ITEM_REGISTRY, ((BlockItem) e).builtInRegistryHolder()));
        return parent.stream()
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
