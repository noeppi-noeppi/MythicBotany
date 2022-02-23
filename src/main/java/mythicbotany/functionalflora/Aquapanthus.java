package mythicbotany.functionalflora;

import com.google.common.collect.ImmutableSet;
import io.github.noeppi_noeppi.libx.LibX;
import mythicbotany.functionalflora.base.FunctionalFlowerBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import vazkii.botania.api.block.IPetalApothecary;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.client.fx.WispParticleData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

public class Aquapanthus extends FunctionalFlowerBase {

    public static final int MAX_TICK_TO_NEXT_CHECK = 5;
    public static final int MANA_PER_TICK = 2;
    public static final int TICKS_TO_FILL = 20;
    public static final Set<ResourceLocation> FILLING_SLOW_IDS = ImmutableSet.of(
            new ResourceLocation("exnihilosequentia", "barrel_wood"),
            new ResourceLocation("exnihilosequentia", "barrel_stone"),
            new ResourceLocation("excompressum", "oak_crucible"),
            new ResourceLocation("excompressum", "spruce_crucible"),
            new ResourceLocation("excompressum", "birch_crucible"),
            new ResourceLocation("excompressum", "jungle_crucible"),
            new ResourceLocation("excompressum", "acacia_crucible"),
            new ResourceLocation("excompressum", "dark_oak_crucible")
    );
    public static final Set<ResourceLocation> FILLING_FAST_IDS = ImmutableSet.of(
            new ResourceLocation("exnihilosequentia", "crucible_wood"),
            new ResourceLocation("exnihilosequentia", "crucible_fired")
    );

    private transient int tickToNextCheck = 0;
    @Nullable
    private BlockPos currentlyFilling = null;
    private int fillingSince = 0;

    public Aquapanthus(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state, 0x4444FF, false);
    }

    @Override
    protected void tickFlower() {
        //noinspection ConstantConditions
        if (!this.level.isClientSide) {
            if (this.currentlyFilling != null) {
                if (this.mana >= MANA_PER_TICK) {
                    if (this.fill()) {
                        this.mana = Mth.clamp(this.mana - MANA_PER_TICK, 0, this.maxMana);
                        this.didWork = true;
                        this.fillingSince += 1;
                    } else {
                        this.fillingSince = 0;
                        this.currentlyFilling = null;
                    }
                }
                LibX.getNetwork().updateBE(this.level, this.worldPosition);
                this.setChanged();
            } else {
                if (this.tickToNextCheck > 0) {
                    this.tickToNextCheck -= 1;
                    return;
                }
                this.tickToNextCheck = MAX_TICK_TO_NEXT_CHECK;

                BlockPos basePos = this.worldPosition.immutable();
                outer: for (int xd = -3; xd <= 3; xd++) {
                    for (int zd = -3; zd <= 3; zd++) {
                        BlockPos pos = basePos.offset(xd, 0, zd);
                        BlockState state = this.level.getBlockState(pos);
                        BlockEntity te = this.level.getBlockEntity(pos);
                        if (this.canFill(state, te)) {
                            this.currentlyFilling = pos;
                            this.fillingSince = 0;
                            this.setChanged();
                            break outer;
                        }
                    }
                }
            }
        } else {
            if (this.currentlyFilling != null && this.fillingSince > 0) {
                double progress = this.fillingSince / (double) TICKS_TO_FILL;

                double x = ((this.currentlyFilling.getX() - this.worldPosition.getX()) * progress) + this.worldPosition.getX() + 0.5;
                double y = this.worldPosition.getY() + (1.5 * Math.sin(progress * Math.PI));
                double z = ((this.currentlyFilling.getZ() - this.worldPosition.getZ()) * progress) + this.worldPosition.getZ() + 0.5;

                double xd = ((this.currentlyFilling.getX() - this.worldPosition.getX()) * progress) / 10;
                double yd = Math.sin(progress * Math.PI) / 10;
                double zd = ((this.currentlyFilling.getZ() - this.worldPosition.getZ()) * progress) / 10;

                WispParticleData data = WispParticleData.wisp(0.85F, 0.1f, 0.1f, 1, 0.25F);
                this.level.addParticle(data, x, y, z, xd, yd, zd);
                data = WispParticleData.wisp((float) Math.random() * 0.1F + 0.1F, 0.2f, 0.2f, 1, 0.9F);
                this.level.addParticle(data, x, y, z, (float) (Math.random() - 0.5) * 0.05F, (float) (Math.random() - 0.5) * 0.05F, (float) (Math.random() - 0.5) * 0.05F);
            }
        }
    }

    private boolean canFill(BlockState state, @Nullable BlockEntity te) {
        if (state.getBlock() == Blocks.CAULDRON || (state.getBlock() == Blocks.WATER_CAULDRON && state.getValue(LayeredCauldronBlock.LEVEL) < 3)) {
            return true;
        } else if (te instanceof IPetalApothecary && ((IPetalApothecary) te).getFluid() == IPetalApothecary.State.EMPTY) {
            return true;
        } else if ((FILLING_SLOW_IDS.contains(state.getBlock().getRegistryName())
                || FILLING_FAST_IDS.contains(state.getBlock().getRegistryName()))
                && te != null) {
            //noinspection ConstantConditions
            IFluidHandler handler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, Direction.UP).orElse(null);
            //noinspection ConstantConditions
            if (handler != null) {
                int filled;
                if (FILLING_FAST_IDS.contains(state.getBlock().getRegistryName())) {
                    filled = handler.fill(new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME), IFluidHandler.FluidAction.SIMULATE);
                } else {
                    filled = handler.fill(new FluidStack(Fluids.WATER, (FluidAttributes.BUCKET_VOLUME / 3) + 1), IFluidHandler.FluidAction.SIMULATE);
                }
                // extra check for capacity is required as excompressum seems to accept more fluid even if full
                return filled > 0 && handler.getFluidInTank(0).getAmount() < handler.getTankCapacity(0);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    
    private boolean fill() {
        //noinspection ConstantConditions
        BlockState state = this.level.getBlockState(this.currentlyFilling);
        BlockEntity be = this.level.getBlockEntity(this.currentlyFilling);
        if (state.getBlock() == Blocks.CAULDRON || (state.getBlock() == Blocks.WATER_CAULDRON && state.getValue(LayeredCauldronBlock.LEVEL) < 3) || (be instanceof IPetalApothecary && ((IPetalApothecary) be).getFluid() == IPetalApothecary.State.EMPTY)) {
            if (this.fillingSince >= TICKS_TO_FILL) {
                if (state.getBlock() == Blocks.CAULDRON) {
                    this.level.setBlockAndUpdate(this.currentlyFilling, Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 1));
                } else if (state.getBlock() == Blocks.WATER_CAULDRON) {
                    this.level.setBlockAndUpdate(this.currentlyFilling, state.setValue(LayeredCauldronBlock.LEVEL, Mth.clamp(state.getValue(LayeredCauldronBlock.LEVEL) + 1, 0, 3)));
                } else if (be instanceof IPetalApothecary) {
                    ((IPetalApothecary) be).setFluid(IPetalApothecary.State.WATER);
                    be.setChanged();
                }
                return false;
            }
            return true;
        } else if ((FILLING_SLOW_IDS.contains(state.getBlock().getRegistryName()) || FILLING_FAST_IDS.contains(state.getBlock().getRegistryName())) && be != null) {
            if (this.fillingSince >= TICKS_TO_FILL) {
                //noinspection ConstantConditions
                IFluidHandler handler = be.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, Direction.UP).orElse(null);
                //noinspection ConstantConditions
                if (handler != null) {
                    if (FILLING_FAST_IDS.contains(state.getBlock().getRegistryName())) {
                        handler.fill(new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME), IFluidHandler.FluidAction.EXECUTE);
                    } else {
                        handler.fill(new FluidStack(Fluids.WATER, (FluidAttributes.BUCKET_VOLUME / 3) + 1), IFluidHandler.FluidAction.EXECUTE);
                    }
                    be.setChanged();
                }
                return false;
            } else {
                return this.canFill(state, be);
            }
        } else {
            return false;
        }
    }

    @Override
    public RadiusDescriptor getRadius() {
        return new RadiusDescriptor.Square(this.worldPosition, 3);
    }

    @Override
    public void load(@Nonnull CompoundTag nbt) {
        super.load(nbt);
        if (nbt.contains("waterFilling")) {
            CompoundTag fillingTag = nbt.getCompound("waterFilling");
            this.currentlyFilling = new BlockPos(fillingTag.getInt("x"), fillingTag.getInt("y"), fillingTag.getInt("z"));
        } else {
            this.currentlyFilling = null;
        }
        this.fillingSince = nbt.getInt("filling_since");
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag nbt) {
        super.saveAdditional(nbt);
        if (this.currentlyFilling != null) {
            CompoundTag fillingTag = new CompoundTag();
            fillingTag.putInt("x", this.currentlyFilling.getX());
            fillingTag.putInt("y", this.currentlyFilling.getY());
            fillingTag.putInt("z", this.currentlyFilling.getZ());
            nbt.put("waterFilling", fillingTag);
            nbt.putInt("filling_since", this.fillingSince);
        }
    }

    @Nonnull
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag updateTag = super.getUpdateTag();
        //noinspection ConstantConditions
        if (!this.level.isClientSide) {
            if (this.currentlyFilling != null) {
                CompoundTag fillingTag = new CompoundTag();
                fillingTag.putInt("x", this.currentlyFilling.getX());
                fillingTag.putInt("y", this.currentlyFilling.getY());
                fillingTag.putInt("z", this.currentlyFilling.getZ());
                updateTag.put("waterFilling", fillingTag);
            }
            updateTag.putInt("filling_since", this.fillingSince);
        }
        return updateTag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        //noinspection ConstantConditions
        if (this.level.isClientSide) {
            if (tag.contains("waterFilling")) {
                CompoundTag fillingTag = tag.getCompound("waterFilling");
                this.currentlyFilling = new BlockPos(fillingTag.getInt("x"), fillingTag.getInt("y"), fillingTag.getInt("z"));
            } else {
                this.currentlyFilling = null;
            }
            this.fillingSince = tag.getInt("filling_since");
        }
        super.handleUpdateTag(tag);
    }
}
