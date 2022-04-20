package mythicbotany.data.patchouli.content;

import mythicbotany.data.patchouli.page.PageBuilder;

// Adds nothing but does a page flip.
public class FlipContent implements Content {

    public static final FlipContent INSTANCE = new FlipContent();

    private FlipContent() {

    }

    @Override
    public void pages(PageBuilder builder) {
        //
    }
}
