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
        boolean prevFloating = floating;
        floating = this.getBlockState().getBlock() instanceof BlockFloatingFunctionalFlower<?>;
        if (prevFloating != floating)
            setChanged();

        this.linkPool();

        //noinspection ConstantConditions
        if (!level.isClientSide) {
            if (isGenerating) {
                if (spreaderTile != null) {
                    if (mana > 0) {
                        int manaTransfer = Math.min(maxTransfer, Math.min(mana, spreaderTile.getMaxMana() - spreaderTile.getCurrentMana()));
                        spreaderTile.receiveMana(manaTransfer);
                        mana = Mth.clamp(mana - manaTransfer, 0, maxMana);
                        setChanged();
                        markPoolDirty();
                    }
                }
            } else {
                if (poolTile != null) {
                    if (mana < maxMana) {
                        int manaTransfer = Math.min(maxTransfer, Math.min(maxMana - mana, poolTile.getCurrentMana()));
                        poolTile.receiveMana(-manaTransfer);
                        mana = Mth.clamp(mana + manaTransfer, 0, maxMana);
                        setChanged();
                        markPoolDirty();
                    }
                }
            }

            redstoneIn = 0;
            for (Direction dir : Direction.values()) {
                int redstonePower = level.getSignal(this.getBlockPos().relative(dir), dir);
                redstoneIn = Math.max(redstonePower, redstoneIn);
            }
        }

        double particleChance = 1.0D - mana / (double) maxMana / 3.5;

        didWork = false;
        tickFlower();

        if (level.isClientSide) {
            if (didWork)
                particleChance = 3 * particleChance;
            float red = (float) (color >> 16 & 0xFF) / 255.0f;
            float green = (float) (color >> 8 & 0xFF) / 255.0f;
            float blue = (float) (color & 255) / 255.0f;
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
            if (isGenerating && tile instanceof IManaCollector) {
                this.pool = tile.getBlockPos();
                this.spreaderTile = (IManaCollector) tile;
                this.poolTile = null;
                setChanged();
                return true;
            } else if (!isGenerating && tile instanceof IManaPool) {
                this.pool = tile.getBlockPos();
                this.poolTile = (IManaPool) tile;
                this.spreaderTile = null;
                setChanged();
                return true;
            }
        }

        return false;
    }

    @Nullable
    @Override
    public BlockPos getBinding() {
        return pool;
    }

    public void linkPool() {
        Object theTileObj = isGenerating ? spreaderTile : poolTile;
        BlockEntity theTile;
        if (!(theTileObj instanceof BlockEntity)) {
            theTile = null;
        } else {
            theTile = (BlockEntity) theTileObj;
        }
        //noinspection ConstantConditions
        if ((pool != null && theTile == null) || pool != null && !pool.equals(theTile.getBlockPos()) || (pool != null && level.getBlockEntity(pool) != theTile)) {
            // tile is outdated. Update it
            //noinspection ConstantConditions
            BlockEntity te = level.getBlockEntity(pool);
            if (isGenerating) {
                poolTile = null;
                if (!(te instanceof IManaCollector)) {
                    spreaderTile = null;
                } else {
                    spreaderTile = (IManaCollector) te;
                }
            } else {
                if (!(te instanceof IManaPool)) {
                    poolTile = null;
                } else {
                    poolTile = (IManaPool) te;
                }
                spreaderTile = null;
            }
        }

        if (pool == null) {
            if (isGenerating) {
                IManaNetwork network = BotaniaAPI.instance().getManaNetworkInstance();
                int size = network.getAllCollectorsInWorld(this.getLevel()).size();
                if (size != this.sizeLastCheck) {
                    BlockEntity te = network.getClosestCollector(this.getBlockPos(), this.getLevel(), 10);
                    if (te instanceof IManaCollector) {
                        pool = te.getBlockPos();
                        poolTile = null;
                        spreaderTile = (IManaCollector) te;
                        setChanged();
                    }
                    this.sizeLastCheck = size;
                }
            } else {
                IManaNetwork network = BotaniaAPI.instance().getManaNetworkInstance();
                int size = network.getAllPoolsInWorld(this.getLevel()).size();
                if (size != this.sizeLastCheck) {
                    BlockEntity te = network.getClosestPool(this.getBlockPos(), this.getLevel(), 10);
                    if (te instanceof IManaPool) {
                        pool = te.getBlockPos();
                        poolTile = (IManaPool) te;
                        spreaderTile = null;
                        setChanged();
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
            this.mana = Mth.clamp(nbt.getInt("mana"), 0, maxMana);
        } else {
            this.mana = 0;
        }
        if (nbt.contains("pool")) {
            CompoundTag poolTag = nbt.getCompound("pool");
            pool = new BlockPos(poolTag.getInt("x"), poolTag.getInt("y"), poolTag.getInt("z"));
        } else {
            pool = null;
        }
        floating = nbt.getBoolean("floating");
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("mana", Mth.clamp(mana, 0, maxMana));
        if (pool != null) {
            CompoundTag poolTag = new CompoundTag();
            poolTag.putInt("x", pool.getX());
            poolTag.putInt("y", pool.getY());
            poolTag.putInt("z", pool.getZ());
            compound.put("pool", poolTag);
        }
        compound.putBoolean("floating", floating);
    }

    @Nonnull
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        //noinspection ConstantConditions
        if (!level.isClientSide) {
            tag.putInt("mana", Mth.clamp(mana, 0, maxMana));
            if (pool != null) {
                CompoundTag poolTag = new CompoundTag();
                poolTag.putInt("x", pool.getX());
                poolTag.putInt("y", pool.getY());
                poolTag.putInt("z", pool.getZ());
                tag.put("pool", poolTag);
            }
            tag.putBoolean("floating", floating);
        }
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag nbt) {
        //noinspection ConstantConditions
        if (level.isClientSide) {
            mana = Mth.clamp(nbt.getInt("mana"), 0, maxMana);
            if (nbt.contains("pool")) {
                CompoundTag poolTag = nbt.getCompound("pool");
                pool = new BlockPos(poolTag.getInt("x"), poolTag.getInt("y"), poolTag.getInt("z"));
            } else {
                pool = null;
            }
            floating = nbt.getBoolean("floating");
        }
    }

    public boolean isValidBinding() {
        Object theTileObj = isGenerating ? spreaderTile : poolTile;
        if (!(theTileObj instanceof BlockEntity theTile))
            return false;
        // noinspection ConstantConditions,deprecation,deprecation
        return pool != null && theTile != null && theTile.hasLevel() && !theTile.isRemoved() && level.hasChunkAt(theTile.getBlockPos()) && getLevel().getBlockEntity(pool) == theTile;
    }

    public int getCurrentMana() {
        return mana;
    }

    public boolean isFloating() {
        return floating;
    }

    public void setFloating(boolean floating) {
        this.floating = floating;
        setChanged();
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
            LibX.getNetwork().requestBE(level, this.worldPosition);
        }
        return true;
    }
    
    @Override
    public void renderHUD(PoseStack poseStack, Minecraft minecraft) {
        if (this.level == null) return;
        String name = I18n.get(this.blockState.getBlock().getDescriptionId());
        BotaniaAPIClient.instance().drawComplexManaHUD(poseStack, this.color, this.getCurrentMana(), this.maxMana, name, new ItemStack(ForgeRegistries.ITEMS.getValue(isGenerating ? SPREADER_ID : POOL_ID)), this.isValidBinding());
    }
    
    private void markPoolDirty() {
        if (poolTile instanceof BlockEntity) {
            ((BlockEntity) poolTile).setChanged();
        }
        if (spreaderTile instanceof BlockEntity) {
            ((BlockEntity) spreaderTile).setChanged();
        }
    }
}
