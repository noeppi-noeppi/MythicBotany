package mythicbotany.pylon;

import mythicbotany.MythicBotany;
import mythicbotany.advancement.ModCriteria;
import mythicbotany.base.BlockEntityMana;
import mythicbotany.network.PylonMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.network.PacketDistributor;
import org.moddingx.libx.base.tile.TickingBlock;

import java.util.List;

public class TileAlfsteelPylon extends BlockEntityMana implements TickingBlock {

    public static final int MAX_MANA = 100000;

    public TileAlfsteelPylon(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state, MAX_MANA, true, false);
    }

    @Override
    protected boolean canReceive() {
        return true;
    }

    @Override
    public void tick() {
        //noinspection ConstantConditions
        if (!this.level.isClientSide) {
            List<ItemEntity> items = this.getItems();
            if (items.size() == 1) {
                ItemEntity item = items.get(0);
                ItemStack stack = item.getItem();
                PylonRepairable repairable = PylonRepairables.getRepairInfo(stack);
                if (repairable != null && stack.getCount() == 1) {
                    int manaCost = repairable.getRepairManaPerTick(stack);
                    if (this.mana >= manaCost) {
                        if (item.getOwner() instanceof ServerPlayer player) {
                            ModCriteria.ALF_REPAIR.trigger(player, stack);
                        }
                        this.mana -= manaCost;
                        stack = repairable.repairOneTick(stack);
                        item.setItem(stack);
                        if (stack.getDamageValue() > 0) {
                            MythicBotany.getNetwork().setItemMagnetImmune(item);
                        } else {
                            MythicBotany.getNetwork().removeItemMagnetImmune(item);
                        }
                        this.setChanged();
                        MythicBotany.getNetwork().channel.send(PacketDistributor.TRACKING_CHUNK.with(() -> this.level.getChunkAt(this.worldPosition)), new PylonMessage(this.worldPosition));
                    }
                }
            }
        }
    }

    private List<ItemEntity> getItems() {
        //noinspection ConstantConditions
        return this.level.getEntitiesOfClass(ItemEntity.class, new AABB(this.worldPosition.offset(0, 1, 0), this.worldPosition.offset(1, 2, 1)));
    }

    @Override
    protected int getManaColor() {
        return 0xEE7C00;
    }
}
