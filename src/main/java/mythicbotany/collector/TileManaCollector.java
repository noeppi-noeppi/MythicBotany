package mythicbotany.collector;

import io.github.noeppi_noeppi.libx.base.tile.TickableBlock;
import mythicbotany.base.BlockEntityMana;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.*;
import vazkii.botania.common.handler.ManaNetworkHandler;

public class TileManaCollector extends BlockEntityMana implements IManaCollector, TickableBlock {

    public TileManaCollector(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state, 10000, true, true);
    }

    @Override
    protected boolean canReceive() {
        return false; // This is just for sparks. We don't want mana from sparks.
    }

    @Override
    public void onClientDisplayTick() {
        //
    }

    @Override
    public float getManaYieldMultiplier(IManaBurst iManaBurst) {
        return 1;
    }

    @Override
    public int getMaxMana() {
        return this.maxMana;
    }

    public void setRemoved() {
        super.setRemoved();
        MinecraftForge.EVENT_BUS.post(new ManaNetworkEvent(this, ManaBlockType.COLLECTOR, ManaNetworkAction.REMOVE));
    }

    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        MinecraftForge.EVENT_BUS.post(new ManaNetworkEvent(this, ManaBlockType.COLLECTOR, ManaNetworkAction.REMOVE));
    }

    public void tick() {
        boolean inNetwork = ManaNetworkHandler.instance.isCollectorIn(this);
        if (!inNetwork && !this.isRemoved()) {
            MinecraftForge.EVENT_BUS.post(new ManaNetworkEvent(this, ManaBlockType.COLLECTOR, ManaNetworkAction.ADD));
        }
    }
}
