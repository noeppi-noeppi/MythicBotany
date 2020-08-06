package mythicbotany.alftools;

import mythicbotany.ModItems;
import mythicbotany.pylon.PylonRepairable;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraAxe;
import vazkii.botania.common.lib.ModTags;

import javax.annotation.Nonnull;
import java.util.List;

public class AlfsteelAxe extends ItemTerraAxe implements PylonRepairable {

    public static final int ITEM_COLLECT_RANGE = 8;

    public AlfsteelAxe(Properties props) {
        super(props.maxDamage(4600));
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
    public int getRepairManaPerTick(ItemStack stack) {
        return AlfsteelSword.MANA_PER_DURABILITY;
    }

    @Override
    public int getRepairAmountPerTick() {
        return 5;
    }
}
