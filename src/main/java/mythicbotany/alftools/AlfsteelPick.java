package mythicbotany.alftools;

import com.google.common.collect.ImmutableSet;
import io.github.noeppi_noeppi.libx.mod.registration.Registerable;
import mythicbotany.ModItems;
import mythicbotany.MythicBotany;
import mythicbotany.pylon.PylonRepairable;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.common.helper.PlayerHelper;
import vazkii.botania.common.item.ItemTemperanceStone;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraPick;
import vazkii.botania.common.item.relic.ItemThorRing;
import vazkii.botania.common.lib.ModTags;
import vazkii.botania.common.lib.ResourceLocationHelper;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.function.Consumer;

public class AlfsteelPick extends ItemTerraPick implements PylonRepairable, Registerable {

    private static final Set<Material> MATERIALS = ImmutableSet.of(Material.STONE, Material.METAL, Material.ICE, Material.GLASS, Material.PISTON, Material.HEAVY_METAL, Material.GRASS, Material.DIRT, Material.SAND, Material.TOP_SNOW, Material.SNOW, Material.CLAY);
    
    public AlfsteelPick(Properties props) {
        super(props.durability(4600));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void registerClient(ResourceLocation id, Consumer<Runnable> defer) {
        defer.accept(() -> {
            ItemProperties.register(ModItems.alfsteelPick, new ResourceLocation(MythicBotany.getInstance().modid, "tipped"), (stack, level, entity, seed) -> isTipped(stack) ? 1 : 0);
            ItemProperties.register(ModItems.alfsteelPick, new ResourceLocation(MythicBotany.getInstance().modid, "active"), (stack, level, entity, seed) -> isEnabled(stack) ? 1 : 0);
        });
    }

    @Override
    public int getManaPerDamage() {
        return 2 * super.getManaPerDamage();
    }

    @Override
    public void breakOtherBlock(Player player, ItemStack stack, BlockPos pos, BlockPos originPos, Direction side) {
        if (isEnabled(stack)) {
            Level level = player.level;
            BlockState state = level.getBlockState(pos);
            if (MATERIALS.contains(state.getMaterial()) || stack.getDestroySpeed(state) > 1) {
                if (!level.isEmptyBlock(pos)) {
                    boolean thor = !ItemThorRing.getThorRing(player).isEmpty();
                    boolean doX = thor || side.getStepX() == 0;
                    boolean doY = thor || side.getStepY() == 0;
                    boolean doZ = thor || side.getStepZ() == 0;
                    int origLevel = getLevel(stack);
                    int miningLevel = origLevel + (thor ? 1 : 0);
                    int rangeDepth = miningLevel / 2;
                    if (ItemTemperanceStone.hasTemperanceActive(player) && miningLevel > 2) {
                        miningLevel = 2;
                        rangeDepth = 0;
                    }

                    int range = miningLevel - 1;
                    int rangeY = Math.max(1, range);
                    if (range != 0 || miningLevel == 1) {
                        Vec3i beginDiff = new Vec3i(doX ? -range : 0, doY ? -1 : 0, doZ ? -range : 0);
                        Vec3i endDiff = new Vec3i(doX ? range : rangeDepth * -side.getStepX(), doY ? rangeY * 2 - 1 : 0, doZ ? range : rangeDepth * -side.getStepZ());
                        ToolCommons.removeBlocksInIteration(player, stack, level, pos, beginDiff, endDiff, s -> (!state.requiresCorrectToolForDrops() || stack.isCorrectToolForDrops(state)) && stack.getDestroySpeed(state) > 1.0F || state.is(BlockTags.MINEABLE_WITH_SHOVEL));
                        if (origLevel == 5) {
                            PlayerHelper.grantCriterion((ServerPlayer)player, ResourceLocationHelper.prefix("challenge/rank_ss_pick"), "code_triggered");
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean isValidRepairItem(@Nonnull ItemStack toRepair, @Nonnull ItemStack repair) {
        return repair.getItem() == ModItems.alfsteelIngot || (!Ingredient.of(ModTags.Items.INGOTS_TERRASTEEL).test(repair) && super.isValidRepairItem(toRepair, repair));
    }

    @Override
    public boolean canRepairPylon(ItemStack stack) {
        return stack.getDamageValue() > 0;
    }

    @Override
    public int getRepairManaPerTick(ItemStack stack) {
        return (int) (2.5 * AlfsteelSword.MANA_PER_DURABILITY);
    }

    @Override
    public ItemStack repairOneTick(ItemStack stack) {
        stack.setDamageValue(Math.max(0, stack.getDamageValue() - 5));
        return stack;
    }

    @Override
    public boolean canReceiveManaFromItem(ItemStack stack, ItemStack otherStack) {
        return ModTags.Items.TERRA_PICK_BLACKLIST.contains(otherStack.getItem());
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, Level level) {
        return Integer.MAX_VALUE;
    }
}
