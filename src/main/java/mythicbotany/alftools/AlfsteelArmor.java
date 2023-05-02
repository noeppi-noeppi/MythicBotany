package mythicbotany.alftools;

import com.google.common.collect.Multimap;
import org.moddingx.libx.util.lazy.LazyValue;
import mythicbotany.register.ModItems;
import mythicbotany.MythicBotany;
import mythicbotany.config.MythicConfig;
import mythicbotany.pylon.PylonRepairable;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.equipment.armor.terrasteel.TerrasteelArmorItem;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.lib.BotaniaTags;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Consumer;

public class AlfsteelArmor extends TerrasteelArmorItem implements PylonRepairable {

    private static final LazyValue<ItemStack[]> armorSet = new LazyValue<>(() -> new ItemStack[]{new ItemStack(ModItems.alfsteelHelmet), new ItemStack(ModItems.alfsteelChestplate), new ItemStack(ModItems.alfsteelLeggings), new ItemStack(ModItems.alfsteelBoots)});

    public AlfsteelArmor(EquipmentSlot type, Properties props) {
        super(type, props.durability(MythicConfig.alftools.durability.armor.max_durability()));
        MinecraftForge.EVENT_BUS.addListener(this::onJump);
    }

    public String getArmorTextureAfterInk(ItemStack stack, EquipmentSlot slot) {
        return MythicBotany.getInstance().modid + ":textures/model/armor_alfsteel.png";
    }

    private void onJump(LivingEvent.LivingJumpEvent event) {
        if (event.getEntity().getItemBySlot(EquipmentSlot.FEET).getItem() == ModItems.alfsteelBoots) {
            LivingEntity entity = event.getEntity();

            float rot = entity.getYRot() * ((float)Math.PI / 180F);
            float xzFactor = entity.isSprinting() ? MythicConfig.alftools.jump_modifier : 0;
            entity.setDeltaMovement(entity.getDeltaMovement().add(-Mth.sin(rot) * xzFactor, MythicConfig.alftools.jump_modifier, Mth.cos(rot) * xzFactor));
        }
    }

    @Nonnull
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@Nonnull EquipmentSlot slot) {
        return CommonAlfsteelArmor.applyModifiers(this, super.getDefaultAttributeModifiers(slot), slot);
    }

    public int getManaPerDamage() {
        return MythicConfig.alftools.durability.armor.mana_per_durability();
    }
    
    @Override
    public void onArmorTick(ItemStack stack, Level world, Player player) {
        if (!world.isClientSide && stack.getDamageValue() > 0 && ManaItemHandler.instance().requestManaExact(stack, player, this.getManaPerDamage() * 2, true)) {
            stack.setDamageValue(Math.max(0, stack.getDamageValue() - 2));
        }
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        return ToolCommons.damageItemIfPossible(stack, amount, entity, this.getManaPerDamage());
    }

    @Override
    public ItemStack[] getArmorSetStacks() {
        return armorSet.get();
    }

    @Override
    public boolean hasArmorSetItem(Player player, EquipmentSlot slot) {
        return CommonAlfsteelArmor.hasArmorSetItem(player, slot);
    }

    @OnlyIn(Dist.CLIENT)
    public void addArmorSetDescription(ItemStack stack, List<Component> list) {
        super.addArmorSetDescription(stack, list);
        CommonAlfsteelArmor.addArmorSetDescription(stack, list);
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

    @Override
    public int getDefense() {
        return CommonAlfsteelArmor.getDefense(this.getSlot());
    }

    @Override
    public float getToughness() {
        return CommonAlfsteelArmor.getToughness(this.getSlot());
    }
}
