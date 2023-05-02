package mythicbotany.data;

import org.moddingx.libx.annotation.data.Datagen;
import org.moddingx.libx.datagen.provider.CommonTagsProviderBase;
import org.moddingx.libx.mod.ModX;
import mythicbotany.register.tags.ModBlockTags;
import mythicbotany.register.ModBlocks;
import mythicbotany.register.tags.ModItemTags;
import mythicbotany.register.ModItems;
import mythicbotany.functionalflora.base.BlockFloatingFunctionalFlower;
import mythicbotany.functionalflora.base.BlockFunctionalFlower;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.BotaniaFluffBlocks;
import vazkii.botania.common.item.material.RuneItem;
import vazkii.botania.common.lib.BotaniaTags;

import javax.annotation.Nullable;

@Datagen
public class CommonTagsProvider extends CommonTagsProviderBase {
    
    public CommonTagsProvider(ModX mod, DataGenerator generator, ExistingFileHelper fileHelper) {
        super(mod, generator, fileHelper);
    }

    @Override
    public void setup() {
        this.item(BotaniaTags.Items.TERRA_PICK_BLACKLIST).add(ModItems.auraRingGreatest);
        this.item(BotaniaTags.Items.TERRA_PICK_BLACKLIST).add(ModItems.alfsteelHelmet);
        this.item(ModItemTags.RITUAL_RUNES).addTag(BotaniaTags.Items.RUNES);
        this.item(ModItemTags.RITUAL_RUNES).add(ModItems.fimbultyrTablet);
        this.item(ItemTags.GOLD_ORES).add(ModBlocks.goldOre.asItem());
        this.item(Tags.Items.ORES).addTag(ModItemTags.ALFHEIM_ORES);
        this.item(Tags.Items.RAW_MATERIALS).add(ModItems.rawElementium);

        this.block(BlockTags.BEACON_BASE_BLOCKS).add(ModBlocks.alfsteelBlock);
        this.block(ModBlockTags.ALFHEIM_LOGS).add(BotaniaBlocks.dreamwood);
        this.block(ModBlockTags.ALFHEIM_LEAVES).add(ModBlocks.dreamwoodLeaves);
        this.block(ModBlockTags.BASE_STONE_ALFHEIM).add(
                BotaniaBlocks.livingrock,
                BotaniaFluffBlocks.biomeStoneForest,
                BotaniaFluffBlocks.biomeStoneMountain,
                BotaniaFluffBlocks.biomeStoneFungal,
                BotaniaFluffBlocks.biomeStoneSwamp,
                BotaniaFluffBlocks.biomeStoneDesert,
                BotaniaFluffBlocks.biomeStoneTaiga,
                BotaniaFluffBlocks.biomeStoneMesa
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
        if (item instanceof RuneItem) {
            this.item(BotaniaTags.Items.RUNES).add(item);
        }
    }

    @Override
    public void defaultBlockTags(Block block) {
        if (block instanceof BlockFunctionalFlower<?>) {
            this.item(BotaniaTags.Items.SPECIAL_FLOWERS).add(block.asItem());
            if (((BlockFunctionalFlower<?>) block).isGenerating) {
                this.item(BotaniaTags.Items.GENERATING_SPECIAL_FLOWERS).add(block.asItem());
            } else {
                this.item(BotaniaTags.Items.FUNCTIONAL_SPECIAL_FLOWERS).add(block.asItem());
            }
        } else if (block instanceof BlockFloatingFunctionalFlower<?>) {
            this.item(BotaniaTags.Items.FLOATING_FLOWERS).add(block.asItem());
            this.item(BotaniaTags.Items.SPECIAL_FLOATING_FLOWERS).add(block.asItem());
        }
    }

    private void tool(Block block, TagKey<Block> tool, @Nullable TagKey<Block> level) {
        this.block(tool).add(block);
        if (level != null) this.block(level).add(block);
    }
}
