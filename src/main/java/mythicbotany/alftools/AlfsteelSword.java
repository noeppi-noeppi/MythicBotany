package mythicbotany.alftools;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import mythicbotany.ModItems;
import mythicbotany.MythicBotany;
import mythicbotany.pylon.PylonRepairable;
import mythicbotany.network.AlfSwordLeftClickHandler;
import mythicbotany.network.MythicNetwork;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import vazkii.botania.common.entity.EntityManaBurst;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraSword;
import vazkii.botania.common.lib.ModTags;

import javax.annotation.Nonnull;

public class AlfsteelSword extends ItemTerraSword implements PylonRepairable {

    public static final int MANA_PER_DURABILITY = 200;
    private final Multimap<Attribute, AttributeModifier> attributeModifiers;

    public AlfsteelSword(Properties props) {
        super(props.maxDamage(4600));
        MinecraftForge.EVENT_BUS.addListener(this::leftClick);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", getAttackDamage(), AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", 2.4, AttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
    }

    private void leftClick(PlayerInteractEvent.LeftClickEmpty evt) {
        if (!evt.getItemStack().isEmpty() && evt.getItemStack().getItem() == this) {
            MythicBotany.getNetwork().instance.sendToServer(new AlfSwordLeftClickHandler.AlfSwordLeftClickMessage());
        }
    }

    @Override
    public int getManaPerDamage() {
        return 2 * super.getManaPerDamage();
    }

    @Override
    public EntityManaBurst getBurst(PlayerEntity player, ItemStack stack) {
        EntityManaBurst burst = super.getBurst(player, stack);
        if (burst != null) {
            burst.setColor(0xF79100);
            burst.setMana(getManaPerDamage());
            burst.setStartingMana(getManaPerDamage());
            burst.setMinManaLoss(20);
            burst.setManaLossPerTick(2.0F);
        }
        return burst;
    }

    @Override
    public float getAttackDamage() {
        return 12;
    }

    @Nonnull
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(@Nonnull EquipmentSlotType equipmentSlot) {
        return equipmentSlot == EquipmentSlotType.MAINHAND ? this.attributeModifiers : super.getAttributeModifiers(equipmentSlot);
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
        return AlfsteelSword.MANA_PER_DURABILITY;
    }
}
