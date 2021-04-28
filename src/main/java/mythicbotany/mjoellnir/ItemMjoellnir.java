package mythicbotany.mjoellnir;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import mythicbotany.ModMisc;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemMjoellnir extends BlockItem {
    
    public ItemMjoellnir(Block blockIn, Properties builder) {
        super(blockIn, builder);
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, @Nonnull PlayerEntity player, @Nonnull Hand hand) {
        if (!player.isSneaking()) {
            throwHammer(world, player, hand);
            player.getCooldownTracker().setCooldown(this, 20 * 6);
            return ActionResult.func_233538_a_(player.getHeldItem(hand), world.isRemote);
        } else {
            return super.onItemRightClick(world, player, hand);
        }
    }

    @Nonnull
    @Override
    public ActionResultType onItemUse(@Nonnull ItemUseContext context) {
        // the hammer can only be placed with shift
        if (context.getPlayer() != null && context.getPlayer().isSneaking()) {
            return super.onItemUse(context);
        } else {
            return ActionResultType.PASS;
        }
    }

    @Override
    protected boolean placeBlock(BlockItemUseContext ctx, @Nonnull BlockState state) {
        return BlockMjoellnir.placeInWorld(ctx.getItem(), ctx.getWorld(), ctx.getPos());
    }

    @Override
    public void inventoryTick(@Nonnull ItemStack stack, @Nonnull World world, @Nonnull Entity entity, int itemSlot, boolean isSelected) {
        if (!world.isRemote && entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            if (!BlockMjoellnir.canHold(player)) {
                BlockMjoellnir.putInWorld(stack.copy(), world, player.getPosition());
                stack.shrink(stack.getCount());
                player.sendMessage(new TranslationTextComponent("message.mythicbotany.mjoellnir_heavy_drop").mergeStyle(TextFormatting.GRAY), player.getUniqueID());
            }
        }
    }

    private void throwHammer(World world, PlayerEntity player, Hand hand) {
        if (!world.isRemote) {
            ItemStack hammer = player.getHeldItem(hand).copy();
            player.setHeldItem(hand, ItemStack.EMPTY);
            EntityMjoellnir projectile = new EntityMjoellnir(world);
            projectile.setPosition(player.getPosX(), player.getPosYEye(), player.getPosZ());
            projectile.setStack(hammer);
            projectile.setThrower(player);
            projectile.setThrowPos(player.getPositionVec());
            projectile.setHotbarSlot(BlockMjoellnir.getHotbarSlot(player, hand));
            projectile.setMotion(player.getLookVec().mul(1.2, 1.2, 1.2));
            world.addEntity(projectile);
        }
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, World world) {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return (enchantment.type == EnchantmentType.WEAPON || enchantment.type == ModMisc.MJOELLNIR_ENCHANTS
                || enchantment == Enchantments.POWER || enchantment == Enchantments.PUNCH
                || enchantment == Enchantments.FLAME || enchantment == Enchantments.LOYALTY)
                && enchantment != Enchantments.SWEEPING && enchantment != Enchantments.SMITE
                && enchantment != Enchantments.BANE_OF_ARTHROPODS;
    }

    @Override
    public int getItemEnchantability() {
        return 1;
    }

    @Override
    public boolean isEnchantable(@Nonnull ItemStack stack) {
        return true;
    }

    @Nonnull
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
        if (slot == EquipmentSlotType.MAINHAND) {
            float dmgModifier = EnchantmentHelper.getModifierForCreature(stack, CreatureAttribute.UNDEFINED);
            float speedModifier = 0.2f * EnchantmentHelper.getEnchantmentLevel(ModMisc.hammerMobility, stack);
                ImmutableMultimap.Builder<Attribute, AttributeModifier> attributeBuilder = ImmutableMultimap.builder();
                attributeBuilder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "mjoellnir_damage_modifier", 24 + (4 * dmgModifier), AttributeModifier.Operation.ADDITION));
                attributeBuilder.put(Attributes.ATTACK_SPEED, new AttributeModifier(ATTACK_SPEED_MODIFIER, "mjoellnir_attack_speed_modifier", -3.5 + speedModifier, AttributeModifier.Operation.ADDITION));
                return attributeBuilder.build();
        } else {
            return super.getAttributeModifiers(slot, stack);
        }
    }
}
