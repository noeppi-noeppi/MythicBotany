package mythicbotany.data.patchouli.content;

import mythicbotany.data.patchouli.page.PageBuilder;

import java.util.List;
import java.util.stream.Stream;

public final class CompositeContent implements Content {

    private final List<Content> children;

    public CompositeContent(List<Content> children) {
        this(children.stream());
    }
    
    private CompositeContent(Stream<Content> children) {
        this.children = children.flatMap(c -> {
            if (c instanceof CompositeContent composite) {
                return composite.children.stream();
            } else {
                return Stream.of(c);
            }
        }).toList();
    }

    @Override
    public void pages(PageBuilder builder) {
        this.children.forEach(c -> c.pages(builder));
    }

    @Override
    public Content with(Content next) {
        if (this.children.isEmpty()) return next;
        // Merge to last child and then join back to composite
        return new CompositeContent(Stream.concat(
                this.children.stream().limit(this.children.size() - 1),
                Stream.of(this.children.get(this.children.size() - 1).with(next))
        ));
    }
}
