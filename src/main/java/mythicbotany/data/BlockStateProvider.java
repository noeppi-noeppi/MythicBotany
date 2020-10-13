package mythicbotany.data;

import io.github.noeppi_noeppi.libx.data.AlwaysExistentModelFile;
import io.github.noeppi_noeppi.libx.data.provider.BlockStateProviderBase;
import io.github.noeppi_noeppi.libx.mod.ModX;
import mythicbotany.ModBlocks;
import mythicbotany.data.custom.FloatingFlowerModelBuilder;
import mythicbotany.functionalflora.base.BlockFloatingFunctionalFlower;
import mythicbotany.functionalflora.base.BlockFunctionalFlower;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStateProvider extends BlockStateProviderBase {

    public BlockStateProvider(ModX mod, DataGenerator generator, ExistingFileHelper fileHelper) {
        super(mod, generator, fileHelper);

        manualModel(ModBlocks.manaInfuser);
        manualModel(ModBlocks.alfsteelPylon);
    }

    @Override
    protected ModelFile defaultModel(ResourceLocation id, Block block) {
        if (block instanceof BlockFunctionalFlower<?>) {
            return models().getBuilder(id.getPath()).parent(new AlwaysExistentModelFile(new ResourceLocation("botania", "block/shapes/cross")))
                    .texture("cross", new ResourceLocation(id.getNamespace(), "block/" + id.getPath()));
        } else if (block instanceof BlockFloatingFunctionalFlower<?>) {
            //noinspection ConstantConditions
            return FloatingFlowerModelBuilder.create(models(), id.getPath())
                    .flower(((BlockFloatingFunctionalFlower<?>) block).getNonFloatingBlock().getRegistryName())
                    .parent(new AlwaysExistentModelFile(new ResourceLocation("minecraft", "block/block")));
        } else {
            return super.defaultModel(id, block);
        }
    }
}