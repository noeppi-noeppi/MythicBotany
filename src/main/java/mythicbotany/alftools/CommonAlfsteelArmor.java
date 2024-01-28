package mythicbotany.alftools;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import mythicbotany.config.MythicConfig;
import mythicbotany.register.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.botania.common.item.equipment.armor.terrasteel.TerrasteelArmorItem;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class CommonAlfsteelArmor {

    private static final List<UUID> ARMOR_ATTRIBUTE_SLOT_UIDS = List.of(
            UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"),
            UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"),
            UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"),
            UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")
    );
    
    public static Multimap<Attribute, AttributeModifier> applyModifiers(TerrasteelArmorItem item, Multimap<Attribute, AttributeModifier> map, @Nullable EquipmentSlot slot) {
        Multimap<Attribute, AttributeModifier> ret = LinkedHashMultimap.create(map);
        
        ret.removeAll(Attributes.ARMOR); // Remove armor attributes as these use the material stats
        ret.removeAll(Attributes.ARMOR_TOUGHNESS);
        ret.removeAll(Attributes.KNOCKBACK_RESISTANCE); // Remove knockback resistance from terrasteel armor.
        if (slot == item.getType().getSlot()) {
            ret.put(Attributes.ARMOR, new AttributeModifier(ARMOR_ATTRIBUTE_SLOT_UIDS.get(slot.getIndex()), "Armor modifier", item.getDefense(), AttributeModifier.Operation.ADDITION));
            ret.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(ARMOR_ATTRIBUTE_SLOT_UIDS.get(slot.getIndex()), "Armor toughness", item.getToughness(), AttributeModifier.Operation.ADDITION));

            @SuppressWarnings("ConstantConditions")
            UUID uuid = new UUID(ForgeRegistries.ITEMS.getKey(item).hashCode() + slot.name().hashCode(), 0L);
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
                    UUID uuid2 = new UUID(ForgeRegistries.ITEMS.getKey(item).hashCode() + slot.name().hashCode(), 1L);
                    ret.put(swimSpeed, new AttributeModifier(uuid2, "Alfsteel modifier swim " + item.type, MythicConfig.alftools.speed_modifier, AttributeModifier.Operation.ADDITION));
                }
            }
        }
        return ret;
    }
    
    public static void addArmorSetDescription(ItemStack stack, List<Component> list) {
        if (stack.getItem() == ModItems.alfsteelHelmet) {
            list.add(Component.translatable("item.mythicbotany.alfsteel_helmet.description").withStyle(ChatFormatting.GOLD));
        } else if (stack.getItem() == ModItems.alfsteelChestplate) {
            list.add(Component.translatable("item.mythicbotany.alfsteel_chestplate.description").withStyle(ChatFormatting.GOLD));
        } else if (stack.getItem() == ModItems.alfsteelLeggings) {
            list.add(Component.translatable("item.mythicbotany.alfsteel_leggings.description").withStyle(ChatFormatting.GOLD));
        } else if (stack.getItem() == ModItems.alfsteelBoots) {
            list.add(Component.translatable("item.mythicbotany.alfsteel_boots.description").withStyle(ChatFormatting.GOLD));
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
    
    public static int getDefense(ArmorItem.Type type) {
        return switch (type) {
            case HELMET -> MythicConfig.alftools.armor_values.helmet.defense();
            case CHESTPLATE -> MythicConfig.alftools.armor_values.chestplate.defense();
            case LEGGINGS -> MythicConfig.alftools.armor_values.leggings.defense();
            case BOOTS -> MythicConfig.alftools.armor_values.boots.defense();
        };
    }
    
    public static float getToughness(ArmorItem.Type type) {
        return switch (type) {
            case HELMET -> MythicConfig.alftools.armor_values.helmet.toughness();
            case CHESTPLATE -> MythicConfig.alftools.armor_values.chestplate.toughness();
            case LEGGINGS -> MythicConfig.alftools.armor_values.leggings.toughness();
            case BOOTS -> MythicConfig.alftools.armor_values.boots.toughness();
        };
    }
}
