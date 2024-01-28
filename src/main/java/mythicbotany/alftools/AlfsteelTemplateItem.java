package mythicbotany.alftools;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.SmithingTemplateItem;

import java.util.List;

public class AlfsteelTemplateItem extends SmithingTemplateItem {
    
    public AlfsteelTemplateItem() {
        super(
                Component.translatable("tooltip.mythicbotany.alfsteel_template.applies_to").withStyle(ChatFormatting.BLUE),
                Component.translatable("item.mythicbotany.alfsteel_ingot"),
                Component.translatable("tooltip.mythicbotany.alfsteel_template.upgrade").withStyle(ChatFormatting.BLUE),
                Component.translatable("tooltip.mythicbotany.alfsteel_template.slot_description", Component.translatable("tooltip.mythicbotany.alfsteel_template.slot_base")),
                Component.translatable("tooltip.mythicbotany.alfsteel_template.slot_description", Component.translatable("item.mythicbotany.alfsteel_ingot")),
                List.of(EMPTY_SLOT_SWORD, EMPTY_SLOT_PICKAXE, EMPTY_SLOT_AXE, EMPTY_SLOT_HELMET, EMPTY_SLOT_CHESTPLATE, EMPTY_SLOT_LEGGINGS, EMPTY_SLOT_BOOTS),
                List.of(EMPTY_SLOT_INGOT)
        );
    }
}
