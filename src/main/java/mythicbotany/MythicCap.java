package mythicbotany;

import io.github.noeppi_noeppi.libx.util.LazyValue;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Supplier;

public class MythicCap<A> implements ICapabilityProvider {
    
    @Nullable 
    private final ICapabilityProvider parent;
    private final Capability<A> capability;
    private final LazyValue<A> value;

    public MythicCap(@Nullable ICapabilityProvider parent, Capability<A> capability, Supplier<A> value) {
        this(parent, capability, new LazyValue<>(value));
    }
    
    public MythicCap(@Nullable ICapabilityProvider parent, Capability<A> capability, LazyValue<A> value) {
        this.parent = parent;
        this.capability = capability;
        this.value = value;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (this.capability == cap) {
            return LazyOptional.of(() -> Objects.requireNonNull(this.value.get())).cast();
        } else {
            return this.parent != null ? this.parent.getCapability(cap, side) : LazyOptional.empty();
        }
    }
}
