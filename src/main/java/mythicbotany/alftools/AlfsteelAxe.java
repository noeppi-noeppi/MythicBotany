package mythicbotany.alftools;

import io.github.noeppi_noeppi.libx.mod.registration.Registerable;
import mythicbotany.ModItems;
import mythicbotany.MythicBotany;
import mythicbotany.pylon.PylonRepairable;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraAxe;
import vazkii.botania.common.lib.ModTags;

import javax.annotation.Nonnull;
import java.util.List;

public class AlfsteelAxe extends ItemTerraAxe implements PylonRepairable, Registerable {

    public static final int ITEM_COLLECT_RANGE = 8;

    public AlfsteelAxe(Properties props) {
        super(props.maxDamage(4600));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void registerClient(ResourceLocation id) {
        ItemModelsProperties.registerProperty(ModItems.alfsteelAxe, new ResourceLocation(MythicBotany.getInstance().modid, "active"), (stack, world, entity) -> entity instanceof PlayerEntity && !shouldBreak((PlayerEntity) entity) ? 0.0F : 1.0F);
    }

    @Nonnull
    @Override
    public ActionResultType onItemUse(ItemUseContext ctx) {
        PlayerEntity player = ctx.getPlayer();
        if (player != null) {
            if (!player.isSneaking()) {
                return super.onItemUse(ctx);
            }
        }
        return ActionResultType.PASS;
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, PlayerEntity player, @Nonnull Hand hand) {
        double x = player.getPosX();
        double y = player.getPosY();
        double z = player.getPosZ();
        if (!world.isRemote) {
            ItemStack stack = player.getHeldItem(hand);
            stack.damageItem(1, player, _x -> {});
            player.setHeldItem(hand, stack);
        }
        List<ItemEntity> items = world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(x - ITEM_COLLECT_RANGE, y - ITEM_COLLECT_RANGE, z - ITEM_COLLECT_RANGE, x + ITEM_COLLECT_RANGE, y + ITEM_COLLECT_RANGE, z + ITEM_COLLECT_RANGE));
        for (ItemEntity item : items) {
            item.setLocationAndAngles(x + world.rand.nextFloat() - 0.5f, y + world.rand.nextFloat(), z + world.rand.nextFloat() - 0.5f, item.rotationYaw, item.rotationPitch);
        }
        return ActionResult.resultSuccess(player.getHeldItem(hand));
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
