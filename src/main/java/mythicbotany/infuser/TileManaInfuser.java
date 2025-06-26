package mythicbotany.infuser;

import com.google.common.base.Predicates;
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
import org.moddingx.libx.base.tile.BlockEntityBase;
import org.moddingx.libx.base.tile.TickingBlock;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.mana.ManaPool;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.api.mana.spark.ManaSpark;
import vazkii.botania.api.mana.spark.SparkAttachable;
import vazkii.botania.api.mana.spark.SparkHelper;
import vazkii.botania.common.block.BotaniaBlocks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

public class TileManaInfuser extends BlockEntityBase implements SparkAttachable, ManaReceiver, TickingBlock {

    private int mana;
    @Nullable private transient InfuserRecipe recipe;
    @Nullable private ItemStack output;

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
            this.recipe = null;
            this.fromColor = -1;
            this.toColor = -1;
            this.mana = 0;
            this.maxMana = 0;
            this.output = null;
            return;
        }
        if (this.recipe != null && this.mana > 0) {
            MythicBotany.getNetwork().spawnInfusionParticles(this.level, this.worldPosition, this.mana / (float) this.recipe.getManaUsage(), this.recipe.fromColor(), this.recipe.toColor());
            this.receiveManaFromSparks();
        }
        List<ItemEntity> items = this.getItems();
        items.forEach(MythicBotany.getNetwork()::setItemMagnetImmune);
        List<ItemStack> stacks = items.stream().map(ItemEntity::getItem).collect(Collectors.toList());
        if (this.recipe != null && this.output != null) {
            if (this.recipe.result(stacks).isEmpty()) {
                this.recipe = null;
                this.fromColor = -1;
                this.toColor = -1;
                this.maxMana = 0;
                this.output = null;
                this.level.updateNeighbourForOutputSignal(this.worldPosition, this.getBlockState().getBlock());
                this.setChanged();
            } else if (this.mana >= this.recipe.getManaUsage()) {
                this.recipe = null;
                this.fromColor = -1;
                this.toColor = -1;
                this.mana = 0;
                this.maxMana = 0;
                this.level.updateNeighbourForOutputSignal(this.worldPosition, this.getBlockState().getBlock());
                items.forEach(ie -> ie.remove(Entity.RemovalReason.DISCARDED));
                ItemEntity outItem = new ItemEntity(this.level, this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5, this.output);
                this.level.addFreshEntity(outItem);
                MythicBotany.getNetwork().setItemMagnetImmune(outItem);
                this.output = null;
                this.setChanged();
            } else {
                items.forEach(ie -> MythicBotany.getNetwork().setItemMagnetImmune(ie));
            }
        } else {
            Pair<InfuserRecipe, ItemStack> match = InfuserRecipe.getOutput(this.level, stacks);
            if (match != null) {
                this.recipe = match.getLeft();
                this.mana = Mth.clamp(this.mana, 0, this.recipe.getManaUsage());
                this.maxMana = this.recipe.getManaUsage();
                this.fromColor = this.recipe.fromColor();
                this.toColor = this.recipe.toColor();
                this.output = match.getRight();
                this.level.updateNeighbourForOutputSignal(this.worldPosition, this.getBlockState().getBlock());
                this.setChanged();
            } else if (this.recipe != null || this.output != null) {
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
        return this.level.getBlockState(center).getBlock() == BotaniaBlocks.shimmerrock
                && this.level.getBlockState(center.north().west()).getBlock() == BotaniaBlocks.shimmerrock
                && this.level.getBlockState(center.north().east()).getBlock() == BotaniaBlocks.shimmerrock
                && this.level.getBlockState(center.south().west()).getBlock() == BotaniaBlocks.shimmerrock
                && this.level.getBlockState(center.south().east()).getBlock() == BotaniaBlocks.shimmerrock
                && this.level.getBlockState(center.north()).getBlock() == Blocks.GOLD_BLOCK
                && this.level.getBlockState(center.east()).getBlock() == Blocks.GOLD_BLOCK
                && this.level.getBlockState(center.south()).getBlock() == Blocks.GOLD_BLOCK
                && this.level.getBlockState(center.west()).getBlock() == Blocks.GOLD_BLOCK;
    }
    
    private void receiveManaFromSparks() {
        ManaSpark spark = this.getAttachedSpark();
        if (this.level != null && spark != null) {
            var otherSparks = SparkHelper.getSparksAround(this.level, this.getBlockPos().getX() + 0.5, this.getBlockPos().getY() + 0.5, this.getBlockPos().getZ() + 0.5, spark.getNetwork());
            for (var otherSpark : otherSparks) {
                if (spark != otherSpark && otherSpark.getAttachedManaReceiver() instanceof ManaPool) {
                    otherSpark.registerTransfer(spark);
                }
            }
        }
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
    public ManaSpark getAttachedSpark() {
        if (this.level == null) return null;
        List<Entity> sparks = this.level.getEntitiesOfClass(Entity.class, new AABB(this.worldPosition.above(), this.worldPosition.above().offset(1, 1, 1)), Predicates.instanceOf(ManaSpark.class));
        if (sparks.size() == 1) {
            Entity e = sparks.get(0);
            return (ManaSpark) e;
        } else {
            return null;
        }
    }

    @Override
    public boolean areIncomingTranfersDone() {
        return this.recipe == null;
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
    public void receiveMana(int mana) {
        if (this.recipe != null) {
            this.mana = Mth.clamp(this.mana + mana, 0, this.recipe.getManaUsage());
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
        return this.recipe != null;
    }

    @Override
    public int getCurrentMana() {
        return this.recipe != null ? this.mana : 0;
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
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putInt("mana", this.mana);
        if (this.output != null) {
            nbt.put("output", this.output.save(new CompoundTag()));
        }
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
