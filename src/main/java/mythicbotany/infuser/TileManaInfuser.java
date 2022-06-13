package mythicbotany.infuser;

import com.google.common.base.Predicates;
import io.github.noeppi_noeppi.libx.base.tile.BlockEntityBase;
import io.github.noeppi_noeppi.libx.base.tile.TickableBlock;
import mythicbotany.MythicBotany;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.apache.commons.lang3.tuple.Pair;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.api.mana.spark.IManaSpark;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.common.block.ModBlocks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

public class TileManaInfuser extends BlockEntityBase implements ISparkAttachable, IManaReceiver, TickableBlock {

    private int mana;
    private boolean active;
    @Nullable
    private transient IInfuserRecipe recipe;
    @Nullable
    private ItemStack output;

    // The following are just there for the client
    private transient int maxMana;
    private transient int fromColor = -1;
    private transient int toColor = -1;

    public TileManaInfuser(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state, BotaniaForgeCapabilities.MANA_RECEIVER, BotaniaForgeCapabilities.SPARK_ATTACHABLE);
    }

    @Override
    public void tick() {
        //noinspection ConstantConditions
        if (this.level.isClientSide) {
            return;
        }
        if (!this.hasValidPlatform()) {
            this.active = false;
            this.recipe = null;
            this.fromColor = -1;
            this.toColor = -1;
            this.mana = 0;
            this.maxMana = 0;
            this.output = null;
            return;
        }
        if (this.active && this.recipe != null && this.mana > 0) {
            MythicBotany.getNetwork().spawnInfusionParticles(this.level, this.worldPosition, this.mana / (float) this.recipe.getManaUsage(), this.recipe.fromColor(), this.recipe.toColor());
        }
        List<ItemEntity> items = this.getItems();
        List<ItemStack> stacks = items.stream().map(ItemEntity::getItem).collect(Collectors.toList());
        if (this.active && this.recipe != null && this.output != null) {
            if (this.recipe.result(stacks).isEmpty()) {
                this.active = false;
                this.recipe = null;
                this.fromColor = -1;
                this.toColor = -1;
                this.maxMana = 0;
                this.output = null;
                this.level.updateNeighbourForOutputSignal(this.worldPosition, this.getBlockState().getBlock());
                this.setChanged();
            } else if (this.mana >= this.recipe.getManaUsage()) {
                this.active = false;
                this.recipe = null;
                this.fromColor = -1;
                this.toColor = -1;
                this.mana = 0;
                this.maxMana = 0;
                this.level.updateNeighbourForOutputSignal(this.worldPosition, this.getBlockState().getBlock());
                items.forEach(ie -> ie.remove(Entity.RemovalReason.DISCARDED));
                ItemEntity outItem = new ItemEntity(this.level, this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5, this.output);
                this.level.addFreshEntity(outItem);
                this.output = null;
                this.setChanged();
            } else {
                items.forEach(ie -> MythicBotany.getNetwork().setItemMagnetImmune(ie));
            }
        } else {
            Pair<IInfuserRecipe, ItemStack> match = InfuserRecipe.getOutput(this.level, stacks);
            if (match != null) {
                if (!this.active) {
                    this.active = true;
                    this.recipe = match.getLeft();
                    this.mana = Math.min(this.mana, Math.round(this.recipe.getManaUsage() * (9 / 10f)));
                } else {
                    this.recipe = match.getLeft();
                    this.mana = Mth.clamp(this.mana, 0, this.recipe.getManaUsage());
                }
                this.maxMana = this.recipe.getManaUsage();
                this.fromColor = this.recipe.fromColor();
                this.toColor = this.recipe.toColor();
                this.output = match.getRight();
                this.level.updateNeighbourForOutputSignal(this.worldPosition, this.getBlockState().getBlock());
                items.forEach(ie -> MythicBotany.getNetwork().setItemMagnetImmune(ie));
                this.setChanged();
            } else if (this.active || this.recipe != null || this.output != null) {
                this.active = false;
                this.recipe = null;
                this.fromColor = -1;
                this.toColor = -1;
                this.maxMana = 0;
                this.output = null;
                this.level.updateNeighbourForOutputSignal(this.worldPosition, this.getBlockState().getBlock());
                this.setChanged();
            }
        }
    }

    private List<ItemEntity> getItems() {
        //noinspection ConstantConditions
        return this.level.getEntitiesOfClass(ItemEntity.class, new AABB(this.worldPosition, this.worldPosition.offset(1, 1, 1)));
    }

    private boolean hasValidPlatform() {
        BlockPos center = this.worldPosition.below();
        //noinspection ConstantConditions
        return this.level.getBlockState(center).getBlock() == ModBlocks.shimmerrock
                && this.level.getBlockState(center.north().west()).getBlock() == ModBlocks.shimmerrock
                && this.level.getBlockState(center.north().east()).getBlock() == ModBlocks.shimmerrock
                && this.level.getBlockState(center.south().west()).getBlock() == ModBlocks.shimmerrock
                && this.level.getBlockState(center.south().east()).getBlock() == ModBlocks.shimmerrock
                && this.level.getBlockState(center.north()).getBlock() == Blocks.GOLD_BLOCK
                && this.level.getBlockState(center.east()).getBlock() == Blocks.GOLD_BLOCK
                && this.level.getBlockState(center.south()).getBlock() == Blocks.GOLD_BLOCK
                && this.level.getBlockState(center.west()).getBlock() == Blocks.GOLD_BLOCK;
    }

    @Override
    public boolean canAttachSpark(ItemStack itemStack) {
        return true;
    }

    @Override
    public int getAvailableSpaceForMana() {
        if (this.recipe != null) {
            return Math.max(0, this.recipe.getManaUsage() - this.mana);
        } else {
            return 0;
        }
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
        return !this.active;
    }

    @Override
    public boolean isFull() {
        if (this.recipe == null) {
            return true;
        } else {
            return this.mana >= this.recipe.getManaUsage();
        }
    }

    @Override
    public void receiveMana(int i) {
        if (this.recipe != null) {
            this.mana = Mth.clamp(this.mana + i, 0, this.recipe.getManaUsage());
            //noinspection ConstantConditions
            this.level.updateNeighbourForOutputSignal(this.worldPosition, this.getBlockState().getBlock());
            this.setChanged();
        }
    }

    @Override
    public Level getManaReceiverLevel() {
        return this.getLevel();
    }

    @Override
    public BlockPos getManaReceiverPos() {
        return this.getBlockPos();
    }

    @Override
    public boolean canReceiveManaFromBursts() {
        return this.active;
    }

    @Override
    public int getCurrentMana() {
        return this.mana;
    }

    @Override
    public void load(@Nonnull CompoundTag nbt) {
        super.load(nbt);
        this.mana = nbt.getInt("mana");
        if (nbt.contains("output")) {
            this.output = ItemStack.of(nbt.getCompound("output"));
        } else {
            this.output = null;
        }
        this.active = nbt.getBoolean("active");
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putInt("mana", this.mana);
        if (this.output != null) {
            nbt.put("output", this.output.save(new CompoundTag()));
        }
        nbt.putBoolean("active", this.active);
    }

    @Nonnull
    @Override
    public CompoundTag getUpdateTag() {
        //noinspection ConstantConditions
        if (this.level.isClientSide)
            return super.getUpdateTag();
        CompoundTag compound = super.getUpdateTag();
        compound.putInt("mana", this.mana);
        compound.putInt("max", this.maxMana);
        if (this.recipe != null) {
            compound.putInt("fromColor", this.fromColor);
            compound.putInt("toColor", this.toColor);
        } else {
            compound.putInt("fromColor", -1);
            compound.putInt("toColor", -1);
        }
        return compound;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        //noinspection ConstantConditions
        if (!this.level.isClientSide)
            return;
        this.mana = tag.getInt("mana");
        this.maxMana = tag.getInt("maxMana");
        this.fromColor = tag.getInt("fromColor");
        this.toColor = tag.getInt("toColor");
    }

    public int getSourceColor() {
        return this.fromColor;
    }

    public int getTargetColor() {
        return this.toColor;
    }

    public double getProgress() {
        if (this.maxMana <= 0) {
            return -1;
        } else {
            return this.mana / (double) this.maxMana;
        }
    }
}
