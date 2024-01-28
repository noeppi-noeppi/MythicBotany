package mythicbotany.data;

import mythicbotany.functionalflora.base.BlockFunctionalFlower;
import mythicbotany.register.ModBlocks;
import mythicbotany.register.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import org.moddingx.libx.datagen.DatagenContext;
import org.moddingx.libx.datagen.provider.model.ItemModelProviderBase;

public class ItemModelProvider extends ItemModelProviderBase {

    public ItemModelProvider(DatagenContext ctx) {
        super(ctx);
    }

    @Override
    protected void setup() {
        this.handheld(ModItems.alfsteelSword);
        this.manualModel(ModItems.alfsteelPick);
        this.manualModel(ModItems.alfsteelAxe);
        this.manualModel(ModItems.fadedNetherStar);
        this.manualModel(ModBlocks.alfsteelPylon.asItem());
    }

    @Override
    protected void defaultBlock(ResourceLocation id, BlockItem item) {
        if (item.getBlock() instanceof BlockFunctionalFlower<?>) {
            this.withExistingParent(id.getPath(), GENERATED).texture("layer0", new ResourceLocation(id.getNamespace(), "block/" + id.getPath()));
        } else {
            super.defaultBlock(id, item);
        }
    }
}
