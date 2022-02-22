package mythicbotany.pylon;

import io.github.noeppi_noeppi.libx.base.tile.TickableBlock;
import mythicbotany.MythicBotany;
import mythicbotany.advancement.ModCriteria;
import mythicbotany.base.BlockEntityMana;
import mythicbotany.network.PylonSerializer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;
import java.util.UUID;

public class TileAlfsteelPylon extends BlockEntityMana implements TickableBlock {

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
        if (!level.isClientSide) {
            List<ItemEntity> items = getItems();
            if (items.size() == 1) {
                ItemEntity item = items.get(0);
                ItemStack stack = item.getItem();
                PylonRepairable repairable = PylonRepairables.getRepairInfo(stack);
                if (repairable != null && stack.getCount() == 1) {
                    int manaCost = repairable.getRepairManaPerTick(stack);
                    if (mana >= manaCost) {
                        UUID throwerId = item.getThrower();
                        Player thrower = throwerId == null ? null : level.getPlayerByUUID(throwerId);
                        if (thrower instanceof ServerPlayer) {
                            ModCriteria.ALF_REPAIR.trigger((ServerPlayer) thrower, stack);
                        }
                        mana -= manaCost;
                        stack = repairable.repairOneTick(stack);
                        item.setItem(stack);
                        if (stack.getDamageValue() > 0) {
                            MythicBotany.getNetwork().setItemMagnetImmune(item);
                        } else {
                            MythicBotany.getNetwork().removeItemMagnetImmune(item);
                        }
                        setChanged();
                        MythicBotany.getNetwork().channel.send(PacketDistributor.TRACKING_CHUNK.with(() -> level.getChunkAt(worldPosition)), new PylonSerializer.PylonMessage(level.dimension().getRegistryName(), worldPosition));
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
