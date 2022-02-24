package mythicbotany.data;

import io.github.noeppi_noeppi.libx.annotation.data.Datagen;
import io.github.noeppi_noeppi.libx.data.provider.CommonTagsProviderBase;
import io.github.noeppi_noeppi.libx.mod.ModX;
import mythicbotany.ModBlockTags;
import mythicbotany.ModBlocks;
import mythicbotany.ModItemTags;
import mythicbotany.ModItems;
import mythicbotany.functionalflora.base.BlockFloatingFunctionalFlower;
import mythicbotany.functionalflora.base.BlockFunctionalFlower;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.item.material.ItemRune;
import vazkii.botania.common.lib.ModTags;

import javax.annotation.Nullable;

@Datagen
public class CommonTagsProvider extends CommonTagsProviderBase {
    
    public CommonTagsProvider(ModX mod, DataGenerator generator, ExistingFileHelper fileHelper) {
        super(mod, generator, fileHelper);
    }

    @Override
    public void setup() {
        this.item(ModTags.Items.TERRA_PICK_BLACKLIST).add(ModItems.auraRingGreatest);
        this.item(ModTags.Items.TERRA_PICK_BLACKLIST).add(ModItems.alfsteelHelmet);
        this.item(ModItemTags.RITUAL_RUNES).addTag(ModTags.Items.RUNES);
        this.item(ModItemTags.RITUAL_RUNES).add(ModItems.fimbultyrTablet);
        this.item(ItemTags.GOLD_ORES).add(ModBlocks.goldOre.asItem());
        this.item(Tags.Items.ORES).addTag(ModItemTags.ALFHEIM_ORES);
        this.item(Tags.Items.RAW_MATERIALS).add(ModItems.rawElementium);

        this.block(BlockTags.BEACON_BASE_BLOCKS).add(ModBlocks.alfsteelBlock);
        this.block(ModBlockTags.ALFHEIM_LOGS).add(vazkii.botania.common.block.ModBlocks.dreamwood);
        this.block(ModBlockTags.ALFHEIM_LEAVES).add(ModBlocks.dreamwoodLeaves);
        this.block(ModBlockTags.BASE_STONE_ALFHEIM).add(
                vazkii.botania.common.block.ModBlocks.livingrock,
                ModFluffBlocks.biomeStoneForest,
                ModFluffBlocks.biomeStoneMountain,
                ModFluffBlocks.biomeStoneFungal,
                ModFluffBlocks.biomeStoneSwamp,
                ModFluffBlocks.biomeStoneDesert,
                ModFluffBlocks.biomeStoneTaiga,
                ModFluffBlocks.biomeStoneMesa
        );
        this.block(BlockTags.GOLD_ORES).add(ModBlocks.goldOre);
        this.block(ModBlockTags.ALFHEIM_ORES).add(
                ModBlocks.elementiumOre,
                ModBlocks.dragonstoneOre,
                ModBlocks.goldOre
        );
        this.block(Tags.Blocks.ORES).addTag(ModBlockTags.ALFHEIM_ORES);
        this.block(BlockTags.LEAVES).addTag(ModBlockTags.ALFHEIM_LEAVES);
        
        this.copyBlock(ModBlockTags.ALFHEIM_ORES, ModItemTags.ALFHEIM_ORES);
        
        this.tool(ModBlocks.manaInfuser, BlockTags.MINEABLE_WITH_PICKAXE, null);
        this.tool(ModBlocks.alfsteelBlock, BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_IRON_TOOL);
        this.tool(ModBlocks.alfsteelPylon, BlockTags.MINEABLE_WITH_PICKAXE, null);
        this.tool(ModBlocks.manaCollector, BlockTags.MINEABLE_WITH_AXE, null);
        this.tool(ModBlocks.yggdrasilBranch, BlockTags.MINEABLE_WITH_AXE, null);
        this.tool(ModBlocks.elementiumOre, BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_IRON_TOOL);
        this.tool(ModBlocks.dragonstoneOre, BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_DIAMOND_TOOL);
        this.tool(ModBlocks.goldOre, BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_IRON_TOOL);
        this.tool(ModBlocks.rawElementiumBlock, BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_IRON_TOOL);
        this.tool(ModBlocks.runeHolder, BlockTags.MINEABLE_WITH_PICKAXE, null);
        this.tool(ModBlocks.centralRuneHolder, BlockTags.MINEABLE_WITH_PICKAXE, null);
    }

    @Override
    public void defaultItemTags(Item item) {
        if (item instanceof ItemRune) {
            this.item(ModTags.Items.RUNES).add(item);
        }
    }

    @Override
    public void defaultBlockTags(Block block) {
        if (block instanceof BlockFunctionalFlower<?>) {
            this.item(ModTags.Items.SPECIAL_FLOWERS).add(block.asItem());
            if (((BlockFunctionalFlower<?>) block).isGenerating) {
                this.item(ModTags.Items.GENERATING_SPECIAL_FLOWERS).add(block.asItem());
            } else {
                this.item(ModTags.Items.FUNCTIONAL_SPECIAL_FLOWERS).add(block.asItem());
            }
        } else if (block instanceof BlockFloatingFunctionalFlower<?>) {
            this.item(ModTags.Items.FLOATING_FLOWERS).add(block.asItem());
            this.item(ModTags.Items.SPECIAL_FLOATING_FLOWERS).add(block.asItem());
        }
    }

    private void tool(Block block, Tag.Named<Block> tool, @Nullable Tag.Named<Block> level) {
        this.block(tool).add(block);
        if (level != null) this.block(level).add(block);
    }
}
