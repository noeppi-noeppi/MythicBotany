package mythicbotany.mjoellnir;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import mythicbotany.register.ModEnchantments;
import mythicbotany.config.MythicConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public class ItemMjoellnir extends BlockItem {
    
    public ItemMjoellnir(Block blockIn, Properties builder) {
        super(blockIn, builder);
    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand hand) {
        if (!player.isShiftKeyDown()) {
            this.throwHammer(level, player, hand);
            if (MythicConfig.mjoellnir.ranged_cooldown > 0) {
                player.getCooldowns().addCooldown(this, MythicConfig.mjoellnir.ranged_cooldown);
            }
            return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide);
        } else {
            return super.use(level, player, hand);
        }
    }

    @Nonnull
    @Override
    public InteractionResult useOn(@Nonnull UseOnContext context) {
        // the hammer can only be placed with shift
        if (context.getPlayer() != null && context.getPlayer().isShiftKeyDown()) {
            return super.useOn(context);
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    protected boolean placeBlock(BlockPlaceContext context, @Nonnull BlockState state) {
        return BlockMjoellnir.placeInWorld(context.getItemInHand(), context.getLevel(), context.getClickedPos());
    }

    @Override
    public void inventoryTick(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull Entity entity, int itemSlot, boolean isSelected) {
        if (!level.isClientSide && entity instanceof Player player) {
            if (!BlockMjoellnir.canHold(player)) {
                BlockMjoellnir.putInWorld(stack.copy(), level, player.blockPosition());
                stack.shrink(stack.getCount());
                player.sendSystemMessage(Component.translatable("message.mythicbotany.mjoellnir_heavy_drop").withStyle(ChatFormatting.GRAY));
            }
        }
    }

    private void throwHammer(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            ItemStack hammer = player.getItemInHand(hand).copy();
            player.setItemInHand(hand, ItemStack.EMPTY);
            Mjoellnir projectile = new Mjoellnir(level);
            projectile.setPos(player.getX(), player.getEyeY(), player.getZ());
            projectile.setStack(hammer);
            projectile.setThrower(player);
            projectile.setThrowPos(player.position());
            projectile.setHotBarSlot(BlockMjoellnir.getHotbarSlot(player, hand));
            projectile.setDeltaMovement(player.getLookAngle().multiply(1.2, 1.2, 1.2));
            level.addFreshEntity(projectile);
        }
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, Level level) {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return (enchantment.category == EnchantmentCategory.WEAPON || enchantment.category == ModEnchantments.MJOELLNIR_ENCHANTS
                || enchantment == Enchantments.POWER_ARROWS || enchantment == Enchantments.PUNCH_ARROWS
                || enchantment == Enchantments.FLAMING_ARROWS || enchantment == Enchantments.LOYALTY)
                && enchantment != Enchantments.SWEEPING_EDGE && enchantment != Enchantments.SMITE
                && enchantment != Enchantments.BANE_OF_ARTHROPODS;
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return 1;
    }

    @Override
    public boolean isEnchantable(@Nonnull ItemStack stack) {
        return true;
    }

    @Nonnull
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot == EquipmentSlot.MAINHAND) {
            float dmgModifier = EnchantmentHelper.getDamageBonus(stack, MobType.UNDEFINED);
            float speedModifier = MythicConfig.mjoellnir.attack_speed_multiplier * stack.getEnchantmentLevel(ModEnchantments.hammerMobility);
            ImmutableMultimap.Builder<Attribute, AttributeModifier> attributeBuilder = ImmutableMultimap.builder();
            attributeBuilder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "mjoellnir_damage_modifier", (MythicConfig.mjoellnir.base_damage_melee - 1) + ((MythicConfig.mjoellnir.enchantment_multiplier - 1) * dmgModifier), AttributeModifier.Operation.ADDITION));
            attributeBuilder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "mjoellnir_attack_speed_modifier", MythicConfig.mjoellnir.base_attack_speed + speedModifier, AttributeModifier.Operation.ADDITION));
            return attributeBuilder.build();
        } else {
            return super.getAttributeModifiers(slot, stack);
        }
    }
}
