package mythicbotany.alftools;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import mythicbotany.ModItems;
import mythicbotany.MythicBotany;
import mythicbotany.pylon.PylonRepairable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.equipment.armor.terrasteel.ItemTerrasteelHelm;
import vazkii.botania.common.lib.ModTags;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

// Exactly the same code as AlfsteelArmor.java. We need to subclass ItemTerrasteelHelm
// to allow the ancient wills to be used with the alfsteel armor.
public class AlfsteelHelm extends ItemTerrasteelHelm implements PylonRepairable {

    private static final float JUMP_FACTOR = 0.025f;
    private static final LazyValue<ItemStack[]> armorSet = new LazyValue<>(() -> new ItemStack[]{new ItemStack(ModItems.alfsteelHelmet), new ItemStack(ModItems.alfsteelChest), new ItemStack(ModItems.alfsteelLegs), new ItemStack(ModItems.alfsteelBoots)});

    public AlfsteelHelm(Properties props) {
        super(props.maxDamage(5200));
        MinecraftForge.EVENT_BUS.addListener(this::onJump);
    }

    public String getArmorTextureAfterInk(ItemStack stack, EquipmentSlotType slot) {
        return ConfigHandler.CLIENT.enableArmorModels.get() ? MythicBotany.getInstance().modid + ":textures/model/armor_alfsteel.png" : (slot == EquipmentSlotType.CHEST ? MythicBotany.getInstance().modid + ":textures/model/alfsteel_1.png" : MythicBotany.getInstance().modid + ":textures/model/alfsteel_0.png");
    }

    private void onJump(LivingEvent.LivingJumpEvent event) {
        if (event.getEntityLiving().getItemStackFromSlot(EquipmentSlotType.FEET).getItem() == ModItems.alfsteelBoots) {
            LivingEntity entity = event.getEntityLiving();

            float rot = entity.rotationYaw * ((float)Math.PI / 180F);
            float xzFactor = entity.isSprinting() ? JUMP_FACTOR : 0;
            entity.setMotion(entity.getMotion().add(-MathHelper.sin(rot) * xzFactor, JUMP_FACTOR, MathHelper.cos(rot) * xzFactor));
        }
    }

    @Nonnull
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(@Nonnull EquipmentSlotType slot) {
        Multimap<Attribute, AttributeModifier> ret = LinkedHashMultimap.create(super.getAttributeModifiers(slot));
        ret.removeAll(Attributes.KNOCKBACK_RESISTANCE); // Remove knockback resistance from terrasteel armor.
        if (slot == this.getEquipmentSlot()) {
            @SuppressWarnings("ConstantConditions")
            UUID uuid = new UUID(ForgeRegistries.ITEMS.getKey(this).hashCode() + slot.toString().hashCode(), 0L);
            if (this == ModItems.alfsteelHelmet) {
                Attribute reachDistance = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation("forge", "reach_distance"));
                if (reachDistance != null)
                    ret.put(reachDistance, new AttributeModifier(uuid, "Alfsteel modifier " + this.type, 2, AttributeModifier.Operation.ADDITION));
            } else if (this == ModItems.alfsteelChest) {
                ret.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(uuid, "Alfsteel modifier " + this.type, 1, AttributeModifier.Operation.ADDITION));
            } else if (this == ModItems.alfsteelLegs) {
                ret.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(uuid, "Alfsteel modifier " + this.type, 0.05, AttributeModifier.Operation.ADDITION));
                Attribute swimSpeed = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation("forge", "swim_speed"));
                if (swimSpeed != null) {
                    @SuppressWarnings("ConstantConditions")
                    UUID uuid2 = new UUID(ForgeRegistries.ITEMS.getKey(this).hashCode() + slot.toString().hashCode(), 1L);
                    ret.put(swimSpeed, new AttributeModifier(uuid2, "Alfsteel modifier swim " + this.type, 0.05, AttributeModifier.Operation.ADDITION));
                }
            }
        }
        return ret;
    }

    public ItemStack[] getArmorSetStacks() {
        return armorSet.getValue();
    }

    public boolean hasArmorSetItem(PlayerEntity player, EquipmentSlotType slot) {
        if (player == null) {
            return false;
        } else {
            ItemStack stack = player.getItemStackFromSlot(slot);
            if (stack.isEmpty()) {
                return false;
            } else {
                switch (slot) {
                    case HEAD:
                        return stack.getItem() == ModItems.alfsteelHelmet;
                    case CHEST:
                        return stack.getItem() == ModItems.alfsteelChest;
                    case LEGS:
                        return stack.getItem() == ModItems.alfsteelLegs;
                    case FEET:
                        return stack.getItem() == ModItems.alfsteelBoots;
                    default:
                        return false;
                }
            }
        }
    }

    public IFormattableTextComponent getArmorSetName() {
        return new TranslationTextComponent("botania.armorset.terrasteel.name");
    }

    @OnlyIn(Dist.CLIENT)
    public void addArmorSetDescription(ItemStack stack, List<ITextComponent> list) {
        super.addArmorSetDescription(stack, list);
        if (stack.getItem() == ModItems.alfsteelHelmet) {
            list.add((new TranslationTextComponent("item.mythicbotany.alfsteel_helmet.description")).mergeStyle(TextFormatting.GOLD));
        } else if (stack.getItem() == ModItems.alfsteelChest) {
            list.add((new TranslationTextComponent("item.mythicbotany.alfsteel_chestplate.description")).mergeStyle(TextFormatting.GOLD));
        } else if (stack.getItem() == ModItems.alfsteelLegs) {
            list.add((new TranslationTextComponent("item.mythicbotany.alfsteel_leggings.description")).mergeStyle(TextFormatting.GOLD));
        } else if (stack.getItem() == ModItems.alfsteelBoots) {
            list.add((new TranslationTextComponent("item.mythicbotany.alfsteel_boots.description")).mergeStyle(TextFormatting.GOLD));
        }
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
        return (int) (2.5 * AlfsteelSword.MANA_PER_DURABILITY);
    }

    @Override
    public ItemStack repairOneTick(ItemStack stack) {
        stack.setDamage(Math.max(0, stack.getDamage() - 5));
        return stack;
    }
}
