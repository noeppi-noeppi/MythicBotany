package mythicbotany.data.tags;

import mythicbotany.functionalflora.base.BlockFloatingFunctionalFlower;
import mythicbotany.functionalflora.base.BlockFunctionalFlower;
import mythicbotany.register.ModBlocks;
import mythicbotany.register.ModItems;
import mythicbotany.register.tags.ModBlockTags;
import mythicbotany.register.tags.ModItemTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import org.moddingx.libx.datagen.DatagenContext;
import org.moddingx.libx.datagen.provider.tags.CommonTagsProviderBase;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.material.RuneItem;
import vazkii.botania.common.lib.BotaniaTags;

import javax.annotation.Nullable;

public class CommonTagsProvider extends CommonTagsProviderBase {
    
    public CommonTagsProvider(DatagenContext ctx) {
        super(ctx);
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
        this.item(ItemTags.create(new ResourceLocation("curios", "ring"))).add(
                ModItems.manaRingGreatest, ModItems.auraRingGreatest, ModItems.fireRing, ModItems.iceRing,
                ModItems.andwariRing, ModItems.cursedAndwariRing
        );
        this.item(ModItemTags.ELEMENTIUM_WEAPONS).add(BotaniaItems.elementiumSword);

        this.block(BlockTags.BEACON_BASE_BLOCKS).add(ModBlocks.alfsteelBlock);
        this.block(ModBlockTags.ALFHEIM_LOGS).add(BotaniaBlocks.dreamwood);
        this.block(ModBlockTags.ALFHEIM_LEAVES).add(ModBlocks.dreamwoodLeaves);
        this.block(ModBlockTags.BASE_STONE_ALFHEIM).add(
                BotaniaBlocks.livingrock,
                BotaniaBlocks.biomeStoneForest,
                BotaniaBlocks.biomeStoneMountain,
                BotaniaBlocks.biomeStoneFungal,
                BotaniaBlocks.biomeStoneSwamp,
                BotaniaBlocks.biomeStoneDesert,
                BotaniaBlocks.biomeStoneTaiga,
                BotaniaBlocks.biomeStoneMesa
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

        this.item(BotaniaTags.Items.MANA_USING_ITEMS).add(
                ModItems.alfsteelSword,
                ModItems.alfsteelAxe,
                ModItems.alfsteelPick,
                ModItems.alfsteelHelmet,
                ModItems.alfsteelChestplate,
                ModItems.alfsteelLeggings,
                ModItems.alfsteelBoots,
                ModBlocks.mjoellnir.asItem());
        
        this.item(Tags.Items.ARMORS_BOOTS).add(ModItems.alfsteelBoots);
        this.item(Tags.Items.ARMORS_LEGGINGS).add(ModItems.alfsteelLeggings);
        this.item(Tags.Items.ARMORS_CHESTPLATES).add(ModItems.alfsteelChestplate);
        this.item(Tags.Items.ARMORS_HELMETS).add(ModItems.alfsteelHelmet);
        
        this.item(ItemTags.SWORDS).add(ModItems.alfsteelSword);
        this.item(ItemTags.AXES).add(ModItems.alfsteelAxe);
        this.item(ItemTags.PICKAXES).add(ModItems.alfsteelPick);
        this.item(Tags.Items.TOOLS).add(ModBlocks.mjoellnir.asItem());
        
        this.item(Tags.Items.INGOTS).add(ModItems.alfsteelIngot);
        this.item(Tags.Items.NUGGETS).add(ModItems.alfsteelNugget);
        
        this.item(Tags.Items.STORAGE_BLOCKS).add(
                ModBlocks.alfsteelBlock.asItem(),
                ModBlocks.rawElementiumBlock.asItem());
    }

    @Override
    public void defaultItemTags(Item item) {
        if (item instanceof RuneItem) {
            this.item(BotaniaTags.Items.RUNES).add(item);
        }
        if (item instanceof ArmorItem) {
            this.item(BotaniaTags.Items.MANA_USING_ITEMS).add(item);
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
