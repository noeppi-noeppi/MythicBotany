package mythicbotany.data;

import net.minecraftforge.registries.ForgeRegistries;
import org.moddingx.libx.annotation.data.Datagen;
import net.minecraftforge.client.model.generators.ModelFile.UncheckedModelFile;
import org.moddingx.libx.datagen.provider.BlockStateProviderBase;
import org.moddingx.libx.mod.ModX;
import mythicbotany.register.ModBlocks;
import mythicbotany.data.custom.FloatingFlowerModelBuilder;
import mythicbotany.functionalflora.base.BlockFloatingFunctionalFlower;
import mythicbotany.functionalflora.base.BlockFunctionalFlower;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Objects;

@Datagen
public class BlockStateProvider extends BlockStateProviderBase {

    public BlockStateProvider(ModX mod, DataGenerator generator, ExistingFileHelper fileHelper) {
        super(mod, generator, fileHelper);
    }

    @Override
    protected void setup() {
        this.manualModel(ModBlocks.manaInfuser);
        this.manualModel(ModBlocks.alfsteelPylon);
        this.manualModel(ModBlocks.yggdrasilBranch);
        this.manualModel(ModBlocks.runeHolder);
        this.manualModel(ModBlocks.centralRuneHolder);
        this.manualModel(ModBlocks.mjoellnir);
        this.manualModel(ModBlocks.returnPortal, this.models().getBuilder(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(ModBlocks.returnPortal)).getPath())
                .texture("particle", blockTexture(Blocks.GLASS)));
    }

    @Override
    protected ModelFile defaultModel(ResourceLocation id, Block block) {
        if (block instanceof BlockFunctionalFlower<?>) {
            return this.models().getBuilder(id.getPath()).parent(new UncheckedModelFile(new ResourceLocation("botania", "block/shapes/cross")))
                    .renderType(RenderTypes.CUTOUT_MIPPED)
                    .texture("cross", new ResourceLocation(id.getNamespace(), "block/" + id.getPath()));
        } else if (block instanceof BlockFloatingFunctionalFlower<?>) {
            //noinspection ConstantConditions
            return FloatingFlowerModelBuilder.create(this.models(), id.getPath())
                    .flower(ForgeRegistries.BLOCKS.getKey(((BlockFloatingFunctionalFlower<?>) block).getNonFloatingBlock()))
                    .renderType(RenderTypes.CUTOUT_MIPPED)
                    .parent(new UncheckedModelFile(new ResourceLocation("minecraft", "block/block")));
        } else {
            return super.defaultModel(id, block);
        }
    }
}
