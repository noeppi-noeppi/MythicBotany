package mythicbotany.alfheim.teleporter;

import com.google.common.collect.ImmutableSet;
import io.github.noeppi_noeppi.libx.mod.ModX;
import io.github.noeppi_noeppi.libx.mod.registration.BlockTE;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.function.Consumer;

public class BlockReturnPortal extends BlockTE<TileReturnPortal> {

    public BlockReturnPortal(ModX mod, Properties properties) {
        super(mod, TileReturnPortal.class, properties);
    }

    public BlockReturnPortal(ModX mod, Properties properties, Item.Properties itemProperties) {
        super(mod, TileReturnPortal.class, properties, itemProperties);
    }

    @Override
    public Set<Object> getAdditionalRegisters() {
        // We remove the item here so there's no item registered.
        //noinspection UnstableApiUsage
        return super.getAdditionalRegisters().stream()
                .filter(o -> !(o instanceof BlockItem))
                .collect(ImmutableSet.toImmutableSet());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void registerClient(ResourceLocation id, Consumer<Runnable> defer) {
        ClientRegistry.bindTileEntityRenderer(getTileType(), RenderReturnPortal::new);
    }
    
    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public BlockRenderType getRenderType(@Nonnull BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }
}
