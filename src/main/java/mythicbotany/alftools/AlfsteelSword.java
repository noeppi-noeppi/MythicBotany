package mythicbotany.alftools;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import mythicbotany.ModItems;
import mythicbotany.MythicBotany;
import mythicbotany.pylon.PylonRepairable;
import mythicbotany.network.AlfSwordLeftClickSerializer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import vazkii.botania.common.entity.EntityManaBurst;
import vazkii.botania.common.handler.ModSounds;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraSword;
import vazkii.botania.common.lib.ModTags;

import javax.annotation.Nonnull;

public class AlfsteelSword extends ItemTerraSword implements PylonRepairable {

    public static final int MANA_PER_DURABILITY = 200;
    private final Multimap<Attribute, AttributeModifier> attributeModifiers;

    public AlfsteelSword(Properties props) {
        super(props.durability(4600));
        MinecraftForge.EVENT_BUS.addListener(this::onLeftClick);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", this.getDamage(), AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", 2.4, AttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
    }

    private void onLeftClick(PlayerInteractEvent.LeftClickEmpty evt) {
        if (!evt.getItemStack().isEmpty() && evt.getItemStack().getItem() == this) {
            MythicBotany.getNetwork().channel.sendToServer(new AlfSwordLeftClickSerializer.AlfSwordLeftClickMessage());
        }
    }

    @Override
    public int getManaPerDamage() {
        return 2 * super.getManaPerDamage();
    }

    @Override
    public float getDamage() {
        return 12;
    }

    @Nonnull
    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@Nonnull EquipmentSlot equipmentSlot) {
        return equipmentSlot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getDefaultAttributeModifiers(equipmentSlot);
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
        return (int) (2.5 * MANA_PER_DURABILITY);
    }

    @Override
    public ItemStack repairOneTick(ItemStack stack) {
        stack.setDamageValue(Math.max(0, stack.getDamageValue() - 5));
        return stack;
    }

    public EntityManaBurst getAlfBurst(Player player, ItemStack stack) {
        EntityManaBurst burst = ItemTerraSword.getBurst(player, stack);
        burst.setColor(0xF79100);
        burst.setMana(this.getManaPerDamage());
        burst.setStartingMana(this.getManaPerDamage());
        burst.setMinManaLoss(20);
        burst.setManaLossPerTick(2.0F);
        return burst;
    }
    
    public void trySpawnAlfBurst(Player player) {
        if ((player.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == ModItems.alfsteelSword || player.getItemBySlot(EquipmentSlot.OFFHAND).getItem() == ModItems.alfsteelSword) && player.getAttackStrengthScale(0) == 1) {
            EntityManaBurst burst = this.getAlfBurst(player, player.getMainHandItem());
            player.level.addFreshEntity(burst);
            player.getMainHandItem().hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(InteractionHand.MAIN_HAND));
            player.level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.terraBlade, SoundSource.PLAYERS, 1, 1);
        }
    }
}
