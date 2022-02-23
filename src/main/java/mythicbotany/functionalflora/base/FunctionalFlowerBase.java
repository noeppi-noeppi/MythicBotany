package mythicbotany.functionalflora.base;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.noeppi_noeppi.libx.LibX;
import io.github.noeppi_noeppi.libx.base.tile.BlockEntityBase;
import io.github.noeppi_noeppi.libx.base.tile.TickableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.BotaniaForgeClientCapabilities;
import vazkii.botania.api.block.IWandBindable;
import vazkii.botania.api.block.IWandHUD;
import vazkii.botania.api.block.IWandable;
import vazkii.botania.api.internal.IManaNetwork;
import vazkii.botania.api.mana.IManaCollector;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.subtile.RadiusDescriptor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@OnlyIn(value = Dist.CLIENT, _interface = IWandHUD.class)
public abstract class FunctionalFlowerBase extends BlockEntityBase implements TickableBlock, IWandBindable, IWandable, IWandHUD {

    public static final ResourceLocation POOL_ID = new ResourceLocation("botania", "mana_pool");
    public static final ResourceLocation SPREADER_ID = new ResourceLocation("botania", "mana_spreader");
    
    public static final int DEFAULT_MAX_MANA = 300;
    public static final int DEFAULT_MAX_TRANSFER = 30;

    public final int maxMana;
    public final int maxTransfer;
    public final int color;
    public final boolean isGenerating;

    @Nullable
    private BlockPos pool = null;
    @Nullable
    private IManaPool poolTile = null;
    @Nullable
    private IManaCollector spreaderTile = null;
    protected int mana = 0;
    private boolean floating = false;

    private transient int sizeLastCheck = -1;
    protected transient int redstoneIn = 0;
    protected transient boolean didWork;

    public FunctionalFlowerBase(BlockEntityType<?> type, BlockPos pos, BlockState state, int color, boolean isGenerating) {
        super(type, pos, state);
        this.color = color;
        this.maxMana = DEFAULT_MAX_MANA;
        this.maxTransfer = isGenerating ? DEFAULT_MAX_TRANSFER : Integer.MAX_VALUE;
        this.isGenerating = isGenerating;
    }

    public FunctionalFlowerBase(BlockEntityType<?> type, BlockPos pos, BlockState state, int color, int maxMana, int maxTransfer, boolean isGenerating) {
        super(type, pos, state);
        this.color = color;
        this.maxMana = maxMana;
        this.maxTransfer = maxTransfer;
        this.isGenerating = isGenerating;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == BotaniaForgeCapabilities.WANDABLE) {
            return LazyOptional.of(() -> this).cast();
        } else {
            return DistExecutor.unsafeRunForDist(
                    () -> () -> cap == BotaniaForgeClientCapabilities.WAND_HUD ? LazyOptional.of(() -> this).cast() : super.getCapability(cap, side),
                    () -> () -> super.getCapability(cap, side)
            );
        }
    }

    @Override
    public final void tick() {
        boolean prevFloating = this.floating;
        this.floating = this.getBlockState().getBlock() instanceof BlockFloatingFunctionalFlower<?>;
        if (prevFloating != this.floating)
            this.setChanged();

        this.linkPool();

        //noinspection ConstantConditions
        if (!this.level.isClientSide) {
            if (this.isGenerating) {
                if (this.spreaderTile != null) {
                    if (this.mana > 0) {
                        int manaTransfer = Math.min(this.maxTransfer, Math.min(this.mana, this.spreaderTile.getMaxMana() - this.spreaderTile.getCurrentMana()));
                        this.spreaderTile.receiveMana(manaTransfer);
                        this.mana = Mth.clamp(this.mana - manaTransfer, 0, this.maxMana);
                        this.setChanged();
                        this.markPoolDirty();
                    }
                }
            } else {
                if (this.poolTile != null) {
                    if (this.mana < this.maxMana) {
                        int manaTransfer = Math.min(this.maxTransfer, Math.min(this.maxMana - this.mana, this.poolTile.getCurrentMana()));
                        this.poolTile.receiveMana(-manaTransfer);
                        this.mana = Mth.clamp(this.mana + manaTransfer, 0, this.maxMana);
                        this.setChanged();
                        this.markPoolDirty();
                    }
                }
            }

            this.redstoneIn = 0;
            for (Direction dir : Direction.values()) {
                int redstonePower = this.level.getSignal(this.getBlockPos().relative(dir), dir);
                this.redstoneIn = Math.max(redstonePower, this.redstoneIn);
            }
        }

        double particleChance = 1.0D - this.mana / (double) this.maxMana / 3.5;

        this.didWork = false;
        this.tickFlower();

        if (this.level.isClientSide) {
            if (this.didWork)
                particleChance = 3 * particleChance;
            float red = (float) (this.color >> 16 & 0xFF) / 255.0f;
            float green = (float) (this.color >> 8 & 0xFF) / 255.0f;
            float blue = (float) (this.color & 255) / 255.0f;
            if (Math.random() > particleChance) {
                BotaniaAPI.instance().sparkleFX(this.getLevel(), (double) this.getBlockPos().getX() + 0.3D + Math.random() * 0.5D, (double) this.getBlockPos().getY() + 0.5D + Math.random() * 0.5D, (double) this.getBlockPos().getZ() + 0.3D + Math.random() * 0.5D, red, green, blue, (float) Math.random(), 5);
            }
        }
    }

    protected abstract void tickFlower();

    @Override
    public boolean canSelect(Player player, ItemStack stack, BlockPos pos, Direction direction) {
        return true;
    }

    @Override
    public boolean bindTo(Player player, ItemStack stack, BlockPos pos, Direction direction) {
        int range = 10;
        range = range * range;
        double dist = pos.distSqr(this.getBlockPos());
        if ((double) range >= dist) {
            BlockEntity tile = player.level.getBlockEntity(pos);
            if (this.isGenerating && tile instanceof IManaCollector) {
                this.pool = tile.getBlockPos();
                this.spreaderTile = (IManaCollector) tile;
                this.poolTile = null;
                this.setChanged();
                return true;
            } else if (!this.isGenerating && tile instanceof IManaPool) {
                this.pool = tile.getBlockPos();
                this.poolTile = (IManaPool) tile;
                this.spreaderTile = null;
                this.setChanged();
                return true;
            }
        }

        return false;
    }

    @Nullable
    @Override
    public BlockPos getBinding() {
        return this.pool;
    }

    public void linkPool() {
        Object theTileObj = this.isGenerating ? this.spreaderTile : this.poolTile;
        BlockEntity theTile;
        if (!(theTileObj instanceof BlockEntity)) {
            theTile = null;
        } else {
            theTile = (BlockEntity) theTileObj;
        }
        //noinspection ConstantConditions
        if ((this.pool != null && theTile == null) || this.pool != null && !this.pool.equals(theTile.getBlockPos()) || (this.pool != null && this.level.getBlockEntity(this.pool) != theTile)) {
            // tile is outdated. Update it
            //noinspection ConstantConditions
            BlockEntity te = this.level.getBlockEntity(this.pool);
            if (this.isGenerating) {
                this.poolTile = null;
                if (!(te instanceof IManaCollector)) {
                    this.spreaderTile = null;
                } else {
                    this.spreaderTile = (IManaCollector) te;
                }
            } else {
                if (!(te instanceof IManaPool)) {
                    this.poolTile = null;
                } else {
                    this.poolTile = (IManaPool) te;
                }
                this.spreaderTile = null;
            }
        }

        if (this.pool == null) {
            if (this.isGenerating) {
                IManaNetwork network = BotaniaAPI.instance().getManaNetworkInstance();
                int size = network.getAllCollectorsInWorld(this.getLevel()).size();
                if (size != this.sizeLastCheck) {
                    BlockEntity te = network.getClosestCollector(this.getBlockPos(), this.getLevel(), 10);
                    if (te instanceof IManaCollector) {
                        this.pool = te.getBlockPos();
                        this.poolTile = null;
                        this.spreaderTile = (IManaCollector) te;
                        this.setChanged();
                    }
                    this.sizeLastCheck = size;
                }
            } else {
                IManaNetwork network = BotaniaAPI.instance().getManaNetworkInstance();
                int size = network.getAllPoolsInWorld(this.getLevel()).size();
                if (size != this.sizeLastCheck) {
                    BlockEntity te = network.getClosestPool(this.getBlockPos(), this.getLevel(), 10);
                    if (te instanceof IManaPool) {
                        this.pool = te.getBlockPos();
                        this.poolTile = (IManaPool) te;
                        this.spreaderTile = null;
                        this.setChanged();
                    }
                    this.sizeLastCheck = size;
                }
            }
        }

        this.setChanged();
    }

    @Override
    public void load(@Nonnull CompoundTag nbt) {
        super.load(nbt);
        if (nbt.contains("mana", Tag.TAG_INT)) {
            this.mana = Mth.clamp(nbt.getInt("mana"), 0, this.maxMana);
        } else {
            this.mana = 0;
        }
        if (nbt.contains("pool")) {
            CompoundTag poolTag = nbt.getCompound("pool");
            this.pool = new BlockPos(poolTag.getInt("x"), poolTag.getInt("y"), poolTag.getInt("z"));
        } else {
            this.pool = null;
        }
        this.floating = nbt.getBoolean("floating");
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("mana", Mth.clamp(this.mana, 0, this.maxMana));
        if (this.pool != null) {
            CompoundTag poolTag = new CompoundTag();
            poolTag.putInt("x", this.pool.getX());
            poolTag.putInt("y", this.pool.getY());
            poolTag.putInt("z", this.pool.getZ());
            compound.put("pool", poolTag);
        }
        compound.putBoolean("floating", this.floating);
    }

    @Nonnull
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        //noinspection ConstantConditions
        if (!this.level.isClientSide) {
            tag.putInt("mana", Mth.clamp(this.mana, 0, this.maxMana));
            if (this.pool != null) {
                CompoundTag poolTag = new CompoundTag();
                poolTag.putInt("x", this.pool.getX());
                poolTag.putInt("y", this.pool.getY());
                poolTag.putInt("z", this.pool.getZ());
                tag.put("pool", poolTag);
            }
            tag.putBoolean("floating", this.floating);
        }
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag nbt) {
        //noinspection ConstantConditions
        if (this.level.isClientSide) {
            this.mana = Mth.clamp(nbt.getInt("mana"), 0, this.maxMana);
            if (nbt.contains("pool")) {
                CompoundTag poolTag = nbt.getCompound("pool");
                this.pool = new BlockPos(poolTag.getInt("x"), poolTag.getInt("y"), poolTag.getInt("z"));
            } else {
                this.pool = null;
            }
            this.floating = nbt.getBoolean("floating");
        }
    }

    public boolean isValidBinding() {
        Object theTileObj = this.isGenerating ? this.spreaderTile : this.poolTile;
        if (!(theTileObj instanceof BlockEntity theTile))
            return false;
        // noinspection ConstantConditions,deprecation,deprecation
        return this.pool != null && theTile != null && theTile.hasLevel() && !theTile.isRemoved() && this.level.hasChunkAt(theTile.getBlockPos()) && this.getLevel().getBlockEntity(this.pool) == theTile;
    }

    public int getCurrentMana() {
        return this.mana;
    }

    public boolean isFloating() {
        return this.floating;
    }

    public void setFloating(boolean floating) {
        this.floating = floating;
        this.setChanged();
    }

    @OnlyIn(Dist.CLIENT)
    public RadiusDescriptor getRadius() {
        return null;
    }

    @Override
    public AABB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    @Override
    public boolean onUsedByWand(@Nullable Player player, ItemStack stack, Direction side) {
        if (this.level != null && this.level.isClientSide) {
            LibX.getNetwork().requestBE(this.level, this.worldPosition);
        }
        return true;
    }
    
    @Override
    public void renderHUD(PoseStack poseStack, Minecraft minecraft) {
        if (this.level == null) return;
        String name = I18n.get(this.blockState.getBlock().getDescriptionId());
        BotaniaAPIClient.instance().drawComplexManaHUD(poseStack, this.color, this.getCurrentMana(), this.maxMana, name, new ItemStack(ForgeRegistries.ITEMS.getValue(this.isGenerating ? SPREADER_ID : POOL_ID)), this.isValidBinding());
    }
    
    private void markPoolDirty() {
        if (this.poolTile instanceof BlockEntity) {
            ((BlockEntity) this.poolTile).setChanged();
        }
        if (this.spreaderTile instanceof BlockEntity) {
            ((BlockEntity) this.spreaderTile).setChanged();
        }
    }
}
