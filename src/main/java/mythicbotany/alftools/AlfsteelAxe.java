package mythicbotany.alftools;

import mythicbotany.MythicBotany;
import mythicbotany.config.MythicConfig;
import mythicbotany.pylon.PylonRepairable;
import mythicbotany.register.ModItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.moddingx.libx.registration.Registerable;
import org.moddingx.libx.registration.SetupContext;
import vazkii.botania.common.item.equipment.tool.terrasteel.TerraTruncatorItem;
import vazkii.botania.common.lib.BotaniaTags;

import javax.annotation.Nonnull;
import java.util.List;

public class AlfsteelAxe extends TerraTruncatorItem implements PylonRepairable, Registerable {

    public static final int ITEM_COLLECT_RANGE = 8;

    public AlfsteelAxe(Properties props) {
        super(props.durability(MythicConfig.alftools.durability.axe.max_durability()));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void registerClient(SetupContext ctx) {
        ctx.enqueue(() -> ItemProperties.register(ModItems.alfsteelAxe, MythicBotany.getInstance().resource("active"), (stack, world, entity, seed) -> entity instanceof Player && !shouldBreak((Player) entity) ? 0 : 1));
    }

    @Override
    public int getManaPerDamage() {
        return MythicConfig.alftools.durability.axe.mana_per_durability();
    }

    @Nonnull
    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Player player = ctx.getPlayer();
        if (player != null) {
            if (!player.isShiftKeyDown()) {
                return super.useOn(ctx);
            }
        }
        return InteractionResult.PASS;
    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, Player player, @Nonnull InteractionHand hand) {
        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();
        if (!level.isClientSide) {
            ItemStack stack = player.getItemInHand(hand);
            stack.hurtAndBreak(1, player, _x -> {});
            player.setItemInHand(hand, stack);
        }
        List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, new AABB(x - ITEM_COLLECT_RANGE, y - ITEM_COLLECT_RANGE, z - ITEM_COLLECT_RANGE, x + ITEM_COLLECT_RANGE, y + ITEM_COLLECT_RANGE, z + ITEM_COLLECT_RANGE));
        for (ItemEntity item : items) {
            item.moveTo(x + level.random.nextFloat() - 0.5f, y + level.random.nextFloat(), z + level.random.nextFloat() - 0.5f, item.getYRot(), item.getXRot());
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    @Override
    public boolean isValidRepairItem(@Nonnull ItemStack toRepair, @Nonnull ItemStack repair) {
        return repair.getItem() == ModItems.alfsteelIngot || (!Ingredient.of(BotaniaTags.Items.INGOTS_TERRASTEEL).test(repair) && super.isValidRepairItem(toRepair, repair));
    }

    @Override
    public int getRepairManaPerTick(ItemStack stack) {
        return (int) (2.5 * this.getManaPerDamage());
    }

    @Override
    public ItemStack repairOneTick(ItemStack stack) {
        stack.setDamageValue(Math.max(0, stack.getDamageValue() - 5));
        return stack;
    }
}
