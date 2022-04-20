package mythicbotany.data.patchouli.content;

import mythicbotany.data.patchouli.page.PageBuilder;
import mythicbotany.data.patchouli.page.PageJson;

import javax.annotation.Nullable;
import java.util.List;

public abstract class CaptionContent implements Content {
    
    @Nullable
    protected final String caption;

    protected CaptionContent(@Nullable String caption) {
        this.caption = caption;
    }
    
    protected abstract int lineSkip();
    protected abstract CaptionContent withCaption(String caption);
    protected abstract void specialPage(PageBuilder builder, @Nullable String caption);
    
    protected boolean canTakeRegularText() {
        return false;
    }
    
    @Override
    public void pages(PageBuilder builder) {
        List<String> pages = this.caption == null ? List.of() : PageJson.splitText(this.caption, this.lineSkip());
        if (pages.isEmpty()) {
            specialPage(builder, null);
        } else if (pages.size() == 1) {
            specialPage(builder, pages.get(0));
        } else {
            specialPage(builder, pages.get(0));
            TextContent.addTextPages(builder, pages, false);
        }
    }

    @Override
    public Content with(Content next) {
        if (next instanceof TextContent tc && (this.canTakeRegularText() || tc.caption())) {
            return this.withCaption(this.caption == null ? tc.text() : this.caption + " " + tc.text());
        } else {
            return Content.super.with(next);
        }
    }
}
