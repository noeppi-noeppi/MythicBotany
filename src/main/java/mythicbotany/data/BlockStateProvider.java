package mythicbotany.data;

import mythicbotany.ModBlocks;
import mythicbotany.MythicBotany;
import mythicbotany.data.custom.FloatingFlowerModelBuilder;
import mythicbotany.functionalflora.base.BlockFloatingFunctionalFlower;
import mythicbotany.functionalflora.base.BlockFunctionalFlower;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.client.model.generators.ExistingFileHelper;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.stream.Collectors;

public class BlockStateProvider extends net.minecraftforge.client.model.generators.BlockStateProvider {

    public BlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, MythicBotany.MODID, exFileHelper);
    }

    @Nonnull
    @Override
    public String getName() {
        return "MythicBotany Blockstates";
    }

    @Override
    protected void registerStatesAndModels() {
        Set<Block> remainingBlocks = Registry.BLOCK.stream()
                .filter(b -> MythicBotany.MODID.equals(Registry.BLOCK.getKey(b).getNamespace()))
                .collect(Collectors.toSet());

        manualModel(remainingBlocks, ModBlocks.manaInfuser);
        manualModel(remainingBlocks, ModBlocks.alfsteelPylon);

        remainingBlocks.forEach(this::defaultBlock);
    }

    private void manualModel(Set<Block> blocks, Block b) {
        String name = Registry.BLOCK.getKey(b).getPath();
        simpleBlock(b, models().getExistingFile(new ResourceLocation(MythicBotany.MODID, "block/" + name)));
        blocks.remove(b);
    }

    private void defaultBlock(Block block) {
        @SuppressWarnings("deprecation")
        String name = Registry.ITEM.getKey(block.asItem()).getPath();
        if (block instanceof BlockFunctionalFlower<?>) {
            simpleBlock(block, models().getBuilder(name).parent(new AlwaysExistentModelFile(new ResourceLocation("botania", "block/shapes/cross")))
                    .texture("cross", new ResourceLocation(MythicBotany.MODID, "block/" + name)));
        } else if (block instanceof BlockFloatingFunctionalFlower<?>) {
            //noinspection ConstantConditions
            simpleBlock(block, FloatingFlowerModelBuilder.create(models(), name)
                    .flower(((BlockFloatingFunctionalFlower<?>) block).getNonFloatingBlock().getRegistryName())
                    .parent(new AlwaysExistentModelFile(new ResourceLocation("minecraft", "block/block"))));
        } else {
            simpleBlock(block);
        }
    }
}