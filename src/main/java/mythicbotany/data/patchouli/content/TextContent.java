package mythicbotany.data.patchouli.content;

import com.google.gson.JsonObject;
import mythicbotany.data.patchouli.page.PageBuilder;
import mythicbotany.data.patchouli.page.PageJson;

import java.util.List;

public record TextContent(String text, boolean caption) implements Content {

    @Override
    public void pages(PageBuilder builder) {
        List<String> pages = PageJson.splitText(this.text(), builder.isFirst());
        addTextPages(builder, pages, true);
    }

    @Override
    public Content with(Content next) {
        if (next instanceof TextContent tc && (!this.caption() || tc.caption())) {
            return new TextContent(this.text() + " " + tc.text(), this.caption());
        } else {
            return Content.super.with(next);
        }
    }
    
    public static void addTextPages(PageBuilder builder, List<String> pages, boolean includeFirst) {
        for (String page : (includeFirst ? pages : pages.stream().skip(1).toList())) {
            JsonObject json = new JsonObject();
            json.addProperty("type", "patchouli:text");
            json.addProperty("text", builder.translate(page));
            builder.addPage(json);
        }
    }
}
