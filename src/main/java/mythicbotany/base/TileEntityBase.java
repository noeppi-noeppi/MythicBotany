package mythicbotany.base;

import com.google.common.collect.ImmutableSet;
import mythicbotany.annotation.KeepConstructor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

public class TileEntityBase extends TileEntity {

    private final Set<Capability<?>> caps;

    @KeepConstructor
    public TileEntityBase(TileEntityType<?> tileEntityTypeIn) {
        this(tileEntityTypeIn, new Capability[0]);
    }

    // Give it a set of capabilities this class implements.
    public TileEntityBase(TileEntityType<?> tileEntityTypeIn, Capability<?>... caps) {
        super(tileEntityTypeIn);
        this.caps = ImmutableSet.copyOf(caps);

    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (caps.contains(cap)) {
            //noinspection unchecked
            return LazyOptional.of(() -> (T) this);
        } else {
            return super.getCapability(cap, side);
        }
    }
}
