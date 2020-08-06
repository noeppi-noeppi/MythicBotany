package mythicbotany.data;

import mythicbotany.ModBlocks;
import mythicbotany.MythicBotany;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.client.model.generators.ExistingFileHelper;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.stream.Collectors;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

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

        remainingBlocks.remove(ModBlocks.manaInfuser);
        remainingBlocks.remove(ModBlocks.alfsteelPylon);
        //manualModel(remainingBlocks, runeAltar);

        remainingBlocks.forEach(this::simpleBlock);
    }

    private void manualModel(Set<Block> blocks, Block b) {
        String name = Registry.BLOCK.getKey(b).getPath();
        simpleBlock(b, models().getExistingFile(prefix("block/" + name)));
        blocks.remove(b);
    }
}