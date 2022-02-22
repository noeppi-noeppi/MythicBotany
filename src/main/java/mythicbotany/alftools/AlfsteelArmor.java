package mythicbotany.alftools;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import io.github.noeppi_noeppi.libx.util.LazyValue;
import mythicbotany.ModItems;
import mythicbotany.MythicBotany;
import mythicbotany.config.MythicConfig;
import mythicbotany.pylon.PylonRepairable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.botania.common.item.equipment.armor.terrasteel.ItemTerrasteelArmor;
import vazkii.botania.common.lib.ModTags;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

public class AlfsteelArmor extends ItemTerrasteelArmor implements PylonRepairable {

    private static final LazyValue<ItemStack[]> armorSet = new LazyValue<>(() -> new ItemStack[]{new ItemStack(ModItems.alfsteelHelmet), new ItemStack(ModItems.alfsteelChestplate), new ItemStack(ModItems.alfsteelLeggings), new ItemStack(ModItems.alfsteelBoots)});

    public AlfsteelArmor(EquipmentSlot type, Properties props) {
        super(type, props.durability(5200));
        MinecraftForge.EVENT_BUS.addListener(this::onJump);
    }

    public String getArmorTextureAfterInk(ItemStack stack, EquipmentSlot slot) {
        return MythicBotany.getInstance().modid + ":textures/model/armor_alfsteel.png";
    }

    private void onJump(LivingEvent.LivingJumpEvent event) {
        if (event.getEntityLiving().getItemBySlot(EquipmentSlot.FEET).getItem() == ModItems.alfsteelBoots) {
            LivingEntity entity = event.getEntityLiving();

            float rot = entity.getYRot() * ((float)Math.PI / 180F);
            float xzFactor = entity.isSprinting() ? MythicConfig.alftools.jump_modifier : 0;
            entity.setDeltaMovement(entity.getDeltaMovement().add(-Mth.sin(rot) * xzFactor, MythicConfig.alftools.jump_modifier, Mth.cos(rot) * xzFactor));
        }
    }

    @Nonnull
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@Nonnull EquipmentSlot slot) {
        Multimap<Attribute, AttributeModifier> ret = LinkedHashMultimap.create(super.getDefaultAttributeModifiers(slot));
        ret.removeAll(Attributes.KNOCKBACK_RESISTANCE); // Remove knockback resistance from terrasteel armor.
        if (slot == this.getSlot()) {
            @SuppressWarnings("ConstantConditions")
            UUID uuid = new UUID(ForgeRegistries.ITEMS.getKey(this).hashCode() + slot.toString().hashCode(), 0L);
            if (this == ModItems.alfsteelHelmet) {
                Attribute reachDistance = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation("forge", "reach_distance"));
                if (reachDistance != null)
                    ret.put(reachDistance, new AttributeModifier(uuid, "Alfsteel modifier " + this.type, MythicConfig.alftools.reach_modifier, AttributeModifier.Operation.ADDITION));
            } else if (this == ModItems.alfsteelChestplate) {
                ret.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(uuid, "Alfsteel modifier " + this.type, MythicConfig.alftools.knockback_resistance_modifier, AttributeModifier.Operation.ADDITION));
            } else if (this == ModItems.alfsteelLeggings) {
                ret.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(uuid, "Alfsteel modifier " + this.type, MythicConfig.alftools.speed_modifier, AttributeModifier.Operation.ADDITION));
                Attribute swimSpeed = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation("forge", "swim_speed"));
                if (swimSpeed != null) {
                    @SuppressWarnings("ConstantConditions")
                    UUID uuid2 = new UUID(ForgeRegistries.ITEMS.getKey(this).hashCode() + slot.toString().hashCode(), 1L);
                    ret.put(swimSpeed, new AttributeModifier(uuid2, "Alfsteel modifier swim " + this.type, MythicConfig.alftools.speed_modifier, AttributeModifier.Operation.ADDITION));
                }
            }
        }
        return ret;
    }

    public ItemStack[] getArmorSetStacks() {
        return armorSet.get();
    }

    public boolean hasArmorSetItem(Player player, EquipmentSlot slot) {
        if (player == null) {
            return false;
        } else {
            ItemStack stack = player.getItemBySlot(slot);
            if (stack.isEmpty()) {
                return false;
            } else {
                return switch (slot) {
                    case HEAD -> stack.getItem() == ModItems.alfsteelHelmet;
                    case CHEST -> stack.getItem() == ModItems.alfsteelChestplate;
                    case LEGS -> stack.getItem() == ModItems.alfsteelLeggings;
                    case FEET -> stack.getItem() == ModItems.alfsteelBoots;
                    default -> false;
                };
            }
        }
    }

    public MutableComponent getArmorSetName() {
        return new TranslatableComponent("botania.armorset.terrasteel.name");
    }

    @OnlyIn(Dist.CLIENT)
    public void addArmorSetDescription(ItemStack stack, List<Component> list) {
        super.addArmorSetDescription(stack, list);
        if (stack.getItem() == ModItems.alfsteelHelmet) {
            list.add((new TranslatableComponent("item.mythicbotany.alfsteel_helmet.description")).withStyle(ChatFormatting.GOLD));
        } else if (stack.getItem() == ModItems.alfsteelChestplate) {
            list.add((new TranslatableComponent("item.mythicbotany.alfsteel_chestplate.description")).withStyle(ChatFormatting.GOLD));
        } else if (stack.getItem() == ModItems.alfsteelLeggings) {
            list.add((new TranslatableComponent("item.mythicbotany.alfsteel_leggings.description")).withStyle(ChatFormatting.GOLD));
        } else if (stack.getItem() == ModItems.alfsteelBoots) {
            list.add((new TranslatableComponent("item.mythicbotany.alfsteel_boots.description")).withStyle(ChatFormatting.GOLD));
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
}
