package mythicbotany.data.patchouli.content;

import mythicbotany.data.patchouli.page.PageBuilder;

import java.util.List;

public interface Content {
    
    Content EMPTY = new CompositeContent(List.of());
    
    void pages(PageBuilder builder);
    
    default Content with(Content next) {
        return new CompositeContent(List.of(this, next));
    }
}
