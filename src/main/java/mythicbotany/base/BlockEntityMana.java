package mythicbotany.base;

import com.google.common.base.Predicates;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.noeppi_noeppi.libx.LibX;
import io.github.noeppi_noeppi.libx.base.tile.BlockEntityBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.DistExecutor;
import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.BotaniaForgeClientCapabilities;
import vazkii.botania.api.block.IWandHUD;
import vazkii.botania.api.block.IWandable;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.api.mana.spark.IManaSpark;
import vazkii.botania.api.mana.spark.ISparkAttachable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@OnlyIn(value = Dist.CLIENT, _interface = IWandHUD.class)
public abstract class BlockEntityMana extends BlockEntityBase implements ISparkAttachable, IManaReceiver, IWandHUD, IWandable {

    public final int maxMana;
    private final boolean bursts;
    private final boolean sparks;

    protected int mana;

    public BlockEntityMana(BlockEntityType<?> type, BlockPos pos, BlockState state, int maxMana, boolean bursts, boolean sparks) {
        super(type, pos, state);
        this.maxMana = maxMana;
        this.bursts = bursts;
        this.sparks = sparks;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == BotaniaForgeCapabilities.MANA_RECEIVER || cap == BotaniaForgeCapabilities.WANDABLE) {
            return LazyOptional.of(() -> this).cast();
        } else {
            return DistExecutor.unsafeRunForDist(
                    () -> () -> cap == BotaniaForgeClientCapabilities.WAND_HUD ? LazyOptional.of(() -> this).cast() : super.getCapability(cap, side),
                    () -> () -> super.getCapability(cap, side)
            );
        }
    }
    
    protected abstract boolean canReceive();

    @Override
    public boolean canAttachSpark(ItemStack itemStack) {
        return this.sparks;
    }

    @Override
    public int getAvailableSpaceForMana() {
        return Mth.clamp(this.maxMana - this.mana, 0, this.maxMana);
    }

    @Override
    public IManaSpark getAttachedSpark() {
        @SuppressWarnings("ConstantConditions")
        List<Entity> sparks = this.level.getEntitiesOfClass(Entity.class, new AABB(this.worldPosition.above(), this.worldPosition.above().offset(1, 1, 1)), Predicates.instanceOf(IManaSpark.class));
        if (sparks.size() == 1) {
            Entity e = sparks.get(0);
            return (IManaSpark) e;
        } else {
            return null;
        }
    }

    @Override
    public boolean areIncomingTranfersDone() {
        return this.mana < this.maxMana && this.canReceive();
    }

    @Override
    public boolean isFull() {
        return this.mana >= this.maxMana;
    }

    @Override
    public void receiveMana(int i) {
        this.mana = Mth.clamp(this.mana + i, 0, this.maxMana);
        this.onManaChange();
        this.setChanged();
    }

    @Override
    public boolean canReceiveManaFromBursts() {
        return this.bursts;
    }

    @Override
    public int getCurrentMana() {
        return Mth.clamp(this.mana, 0, this.maxMana);
    }

    @Override
    public void load(@Nonnull CompoundTag nbt) {
        super.load(nbt);
        if (nbt.contains("mana", Tag.TAG_INT)) {
            this.mana = Mth.clamp(nbt.getInt("mana"), 0, this.maxMana);
        } else {
            this.mana = 0;
        }
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putInt("mana", Mth.clamp(this.mana, 0, this.maxMana));
    }

    @Nonnull
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        //noinspection ConstantConditions
        if (!this.level.isClientSide) {
            tag.putInt("mana", this.mana);
        }
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        //noinspection ConstantConditions
        if (this.level.isClientSide) {
            this.mana = Mth.clamp(tag.getInt("mana"), 0, this.maxMana);
        }
    }

    @Override
    public void renderHUD(PoseStack poseStack, Minecraft minecraft) {
        String name = I18n.get(this.blockState.getBlock().getDescriptionId());
        BotaniaAPIClient.instance().drawSimpleManaHUD(poseStack, this.getManaColor(), this.getCurrentMana(), this.maxMana, name);
    }

    @Override
    public boolean onUsedByWand(@Nullable Player player, ItemStack itemStack, Direction direction) {
        if (this.level != null && this.level.isClientSide) {
            LibX.getNetwork().requestBE(this.level, this.worldPosition);
        }
        return true;
    }
    
    protected void onManaChange() {

    }
    
    protected int getManaColor() {
        return 0x0000FF;
    }

    @Override
    public Level getManaReceiverLevel() {
        return this.getLevel();
    }

    @Override
    public BlockPos getManaReceiverPos() {
        return this.getBlockPos();
    }
}
