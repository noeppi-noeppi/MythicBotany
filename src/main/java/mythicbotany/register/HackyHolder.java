package mythicbotany.register;

import com.mojang.datafixers.util.Either;
import io.github.noeppi_noeppi.libx.util.Misc;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class HackyHolder<T> implements Holder<T> {
    
    private final ResourceKey<? extends Registry<T>> registry;
    private final T value;
    
    @Nullable
    private Holder<T> holder;
    
    public HackyHolder(ResourceKey<? extends Registry<T>> registry, T value) {
        this.registry = registry;
        this.value = value;
        this.holder = null;
    }
    
    public void register(Registry<T> registry, ResourceLocation id) {
        if (this.holder == null) {
            this.holder = registry.getOrCreateHolder(ResourceKey.create(this.registry, id));
        }
    }

    @Nullable
    public Optional<Holder<T>> delegate() {
        return Optional.ofNullable(this.holder);
    }

    @Nonnull
    @Override
    public T value() {
        return this.holder == null ? this.value : this.holder.value();
    }

    @Override
    public boolean isBound() {
        return this.holder != null && this.holder.isBound();
    }

    @Override
    public boolean is(@Nonnull ResourceLocation id) {
        return this.holder != null && this.holder.is(id);
    }

    @Override
    public boolean is(@Nonnull ResourceKey<T> key) {
        return this.holder != null && this.holder.is(key);
    }

    @Override
    public boolean is(@Nonnull Predicate<ResourceKey<T>> predicate) {
        return this.holder != null && this.holder.is(predicate);
    }

    @Override
    public boolean is(@Nonnull TagKey<T> tag) {
        return this.holder != null && this.holder.is(tag);
    }

    @Nonnull
    @Override
    public Stream<TagKey<T>> tags() {
        return this.holder == null ? Stream.empty() : this.holder.tags();
    }

    @Nonnull
    @Override
    public Either<ResourceKey<T>, T> unwrap() {
        return this.holder == null ? Either.right(this.value) : this.holder.unwrap();
    }

    @Nonnull
    @Override
    public Optional<ResourceKey<T>> unwrapKey() {
        return this.holder == null ? Optional.empty() : this.holder.unwrapKey();
    }

    @Nonnull
    @Override
    public Kind kind() {
        return Kind.REFERENCE;
    }

    @Override
    public boolean isValidInRegistry(@Nonnull Registry<T> registry) {
        //noinspection unchecked
        return this.holder == null ? (((Registry<Registry<T>>) Registry.REGISTRY).getResourceKey(registry).orElse(null) == this.registry) : this.holder.isValidInRegistry(registry);
    }
    
    // Bind an intrusive holder to a non existent key, so forge does not complain that it was not registered
    public static <T> void bindIntrusive(ResourceKey<? extends Registry<T>> registry, Holder<T> holder) {
        if (holder instanceof Holder.Reference<T> ref && ref.getType() == Reference.Type.INTRUSIVE && !ref.isBound()) {
            ref.bind(ResourceKey.create(registry, Misc.MISSIGNO), holder.value());
        }
    }
}
