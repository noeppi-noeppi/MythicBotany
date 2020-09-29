package mythicbotany.collector;

import mythicbotany.base.TileEntityMana;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.IManaCollector;
import vazkii.botania.api.mana.ManaNetworkEvent;
import vazkii.botania.common.core.handler.ManaNetworkHandler;

public class TileManaCollector extends TileEntityMana implements IManaCollector, ITickableTileEntity {

    public TileManaCollector(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn, 10000, true, true);
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
        return maxMana;
    }

    public void remove() {
        super.remove();
        ManaNetworkEvent.removeCollector(this);
    }

    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        ManaNetworkEvent.removeCollector(this);
    }

    public void tick() {
        boolean inNetwork = ManaNetworkHandler.instance.isCollectorIn(this);
        if (!inNetwork && !this.isRemoved()) {
            ManaNetworkEvent.addCollector(this);
        }
    }
}
