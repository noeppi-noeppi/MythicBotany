package mythicbotany.pylon;

import mythicbotany.ModBlocks;
import mythicbotany.base.TileEntityMana;
import mythicbotany.network.MythicNetwork;
import mythicbotany.network.PylonHandler;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.List;

public class TileAlfsteelPylon extends TileEntityMana implements ITickableTileEntity {

    public static final int MAX_MANA = 100000;

    public TileAlfsteelPylon(TileEntityType<?> tileEntityType) {
        super(tileEntityType, MAX_MANA, true, false);
    }

    @Override
    protected boolean canReceive() {
        return true;
    }

    @Override
    public void tick() {
        //noinspection ConstantConditions
        if (!world.isRemote) {
            List<ItemEntity> items = getItems();
            if (items.size() == 1) {
                ItemEntity item = items.get(0);
                ItemStack stack = item.getItem();
                PylonRepairable repairable = PylonRepairables.getRepairInfo(stack);
                if (repairable != null && stack.getCount() == 1) {
                    int manaCost = repairable.getRepairManaPerTick(stack);
                    if (mana >= manaCost) {
                            mana -= manaCost;
                            stack = repairable.repairOneTick(stack);
                            item.setItem(stack);
                            markDirty();
                            MythicNetwork.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> world.getChunkAt(pos)), new PylonHandler.PylonMessage(world.func_234923_W_().getRegistryName(), pos));
                        }
                }
            }
        }
    }

    private List<ItemEntity> getItems() {
        //noinspection ConstantConditions
        return this.world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(this.pos.add(0, 1, 0), this.pos.add(1, 2, 1)));
    }

    @Override
    public void onLoad() {
        //noinspection ConstantConditions
        if (world.isRemote) {
            MythicNetwork.requestTE(world, pos);
        }
    }
}
