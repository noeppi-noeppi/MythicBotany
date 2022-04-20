package mythicbotany.data.patchouli.page;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.client.StringSplitter;
import net.minecraft.client.gui.font.glyphs.MissingGlyph;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PageJson {
    
    public static JsonElement stack(ItemStack stack) {
        StringBuilder sb = new StringBuilder();
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (id == null) throw new IllegalStateException("Item not registered: " + stack);
        sb.append(id.getNamespace());
        sb.append(":");
        sb.append(id.getPath());
        if (stack.getCount() != 1) {
            sb.append("#");
            sb.append(stack.getCount());
        }
        if (stack.hasTag() && !stack.getOrCreateTag().isEmpty()) {
            sb.append(stack.getOrCreateTag());
        }
        return new JsonPrimitive(sb.toString());
    }
    
    public static List<String> splitText(String text) {
        return splitText(text, false);
    }
    
    public static List<String> splitText(String text, boolean withInit) {
        return splitText(text, withInit ? 14 : 16, 16);
    }
    
    public static List<String> splitText(String text, int skip) {
        return splitText(text, Math.max(16 - skip, 1), 16);
    }
    
    public static List<String> splitText(String text, int linesHead, int linesTail) {
        StringSplitter splitter = new StringSplitter((codePoint, style) -> MissingGlyph.INSTANCE.getAdvance(style.isBold()));
        Component component = new TextComponent(text);
        List<String> lines = splitter.splitLines(text, Math.round(116 * 1.5f), Style.EMPTY).stream().map(FormattedText::getString).map(String::strip).filter(s -> !s.isEmpty()).toList();
        List<String> pages = new ArrayList<>();
        boolean first = true;
        while (!lines.isEmpty()) {
            pages.add(lines.stream().limit(first ? linesHead : linesTail).collect(Collectors.joining(" ")));
            lines = lines.stream().skip(first ? linesHead : linesTail).toList();
            first = false;
        }
        return List.copyOf(pages);
    }
}
