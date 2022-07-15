package mythicbotany.alftools;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import mythicbotany.ModItems;
import mythicbotany.config.MythicConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.botania.common.item.equipment.armor.terrasteel.ItemTerrasteelArmor;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class CommonAlfsteelArmor {
    
    public static Multimap<Attribute, AttributeModifier> applyModifiers(ItemTerrasteelArmor item, Multimap<Attribute, AttributeModifier> map, @Nullable EquipmentSlot slot) {
        Multimap<Attribute, AttributeModifier> ret = LinkedHashMultimap.create(map);
        ret.removeAll(Attributes.KNOCKBACK_RESISTANCE); // Remove knockback resistance from terrasteel armor.
        if (slot == item.getSlot()) {
            @SuppressWarnings("ConstantConditions")
            UUID uuid = new UUID(ForgeRegistries.ITEMS.getKey(item).hashCode() + slot.toString().hashCode(), 0L);
            if (item == ModItems.alfsteelHelmet) {
                Attribute reachDistance = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation("forge", "reach_distance"));
                if (reachDistance != null)
                    ret.put(reachDistance, new AttributeModifier(uuid, "Alfsteel modifier " + item.type, MythicConfig.alftools.reach_modifier, AttributeModifier.Operation.ADDITION));
            } else if (item == ModItems.alfsteelChestplate) {
                ret.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(uuid, "Alfsteel modifier " + item.type, MythicConfig.alftools.knockback_resistance_modifier, AttributeModifier.Operation.ADDITION));
            } else if (item == ModItems.alfsteelLeggings) {
                ret.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(uuid, "Alfsteel modifier " + item.type, MythicConfig.alftools.speed_modifier, AttributeModifier.Operation.ADDITION));
                Attribute swimSpeed = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation("forge", "swim_speed"));
                if (swimSpeed != null) {
                    @SuppressWarnings("ConstantConditions")
                    UUID uuid2 = new UUID(ForgeRegistries.ITEMS.getKey(item).hashCode() + slot.toString().hashCode(), 1L);
                    ret.put(swimSpeed, new AttributeModifier(uuid2, "Alfsteel modifier swim " + item.type, MythicConfig.alftools.speed_modifier, AttributeModifier.Operation.ADDITION));
                }
            }
        }
        return ret;
    }
    
    public static void addArmorSetDescription(ItemStack stack, List<Component> list) {
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
    
    public static boolean hasArmorSetItem(Player player, EquipmentSlot slot) {
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
    
    public static int getDefense(EquipmentSlot slot) {
        return switch (slot) {
            case HEAD -> MythicConfig.alftools.armor_values.helmet.defense();
            case CHEST -> MythicConfig.alftools.armor_values.chestplate.defense();
            case LEGS -> MythicConfig.alftools.armor_values.leggings.defense();
            case FEET -> MythicConfig.alftools.armor_values.boots.defense();
            default -> 0;
        };
    }
    
    public static float getToughness(EquipmentSlot slot) {
        return switch (slot) {
            case HEAD -> MythicConfig.alftools.armor_values.helmet.toughness();
            case CHEST -> MythicConfig.alftools.armor_values.chestplate.toughness();
            case LEGS -> MythicConfig.alftools.armor_values.leggings.toughness();
            case FEET -> MythicConfig.alftools.armor_values.boots.toughness();
            default -> 0;
        };
    }
}
