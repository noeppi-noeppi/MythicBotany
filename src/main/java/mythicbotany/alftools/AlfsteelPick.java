package mythicbotany.alftools;

import io.github.noeppi_noeppi.libx.mod.registration.Registerable;
import mythicbotany.ModItems;
import mythicbotany.MythicBotany;
import mythicbotany.pylon.PylonRepairable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.item.ItemTemperanceStone;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraPick;
import vazkii.botania.common.item.relic.ItemThorRing;
import vazkii.botania.common.lib.ModTags;
import vazkii.botania.common.lib.ResourceLocationHelper;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class AlfsteelPick extends ItemTerraPick implements PylonRepairable, Registerable {

    private static final List<Material> MATERIALS = Arrays.asList(Material.ROCK, Material.IRON, Material.ICE, Material.GLASS, Material.PISTON, Material.ANVIL, Material.ORGANIC, Material.EARTH, Material.SAND, Material.SNOW, Material.SNOW_BLOCK, Material.CLAY);

    public AlfsteelPick(Properties props) {
        super(props.maxDamage(4600));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void registerClient(ResourceLocation id) {
        ItemModelsProperties.func_239418_a_(ModItems.alfsteelPick, new ResourceLocation(MythicBotany.getInstance().modid, "tipped"), (stack, world, entity) -> isTipped(stack) ? 1 : 0);
        ItemModelsProperties.func_239418_a_(ModItems.alfsteelPick, new ResourceLocation(MythicBotany.getInstance().modid, "active"), (stack, world, entity) -> isEnabled(stack) ? 1 : 0);
    }

    @Override
    public int getManaPerDamage() {
        return 2 * super.getManaPerDamage();
    }

    @Override
    public void breakOtherBlock(PlayerEntity player, ItemStack stack, BlockPos pos, BlockPos originPos, Direction side) {
        if (isEnabled(stack)) {
            World world = player.world;
            Material mat = world.getBlockState(pos).getMaterial();
            if (MATERIALS.contains(mat)) {
                if (!world.isAirBlock(pos)) {
                    boolean thor = !ItemThorRing.getThorRing(player).isEmpty();
                    boolean doX = thor || side.getXOffset() == 0;
                    boolean doY = thor || side.getYOffset() == 0;
                    boolean doZ = thor || side.getZOffset() == 0;
                    int origLevel = getLevel(stack);
                    int level = origLevel + (thor ? 1 : 0);
                    int rangeDepth = level / 2;
                    if (ItemTemperanceStone.hasTemperanceActive(player) && level > 2) {
                        level = 2;
                        rangeDepth = 0;
                    }

                    int range = level - 1;
                    int rangeY = Math.max(1, range);
                    if (range != 0 || level == 1) {
                        Vector3i beginDiff = new Vector3i(doX ? -range : 0, doY ? -1 : 0, doZ ? -range : 0);
                        Vector3i endDiff = new Vector3i(doX ? range : rangeDepth * -side.getXOffset(), doY ? rangeY * 2 - 1 : 0, doZ ? range : rangeDepth * -side.getZOffset());
                        ToolCommons.removeBlocksInIteration(player, stack, world, pos, beginDiff, endDiff, (state) -> MATERIALS.contains(state.getMaterial()), isTipped(stack));
                        if (origLevel == 5) {
                            PlayerHelper.grantCriterion((ServerPlayerEntity)player, ResourceLocationHelper.prefix("challenge/rank_ss_pick"), "code_triggered");
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean getIsRepairable(@Nonnull ItemStack toRepair, @Nonnull ItemStack repair) {
        return repair.getItem() == ModItems.alfsteelIngot || (!Ingredient.fromTag(ModTags.Items.INGOTS_TERRASTEEL).test(repair) && super.getIsRepairable(toRepair, repair));
    }

    @Override
    public boolean canRepairPylon(ItemStack stack) {
        return stack.getDamage() > 0;
    }

    @Override
    public int getRepairManaPerTick(ItemStack stack) {
        return (int) (2.5 * AlfsteelSword.MANA_PER_DURABILITY);
    }

    @Override
    public ItemStack repairOneTick(ItemStack stack) {
        stack.setDamage(Math.max(0, stack.getDamage() - 5));
        return stack;
    }
}
