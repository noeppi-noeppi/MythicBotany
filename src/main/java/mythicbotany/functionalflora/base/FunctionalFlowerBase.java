package mythicbotany.functionalflora.base;

import mythicbotany.base.TileEntityBase;
import mythicbotany.network.MythicNetwork;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.internal.IManaNetwork;
import vazkii.botania.api.mana.IManaCollector;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.wand.IWandBindable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class FunctionalFlowerBase extends TileEntityBase implements ITickableTileEntity, IWandBindable {

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

    public FunctionalFlowerBase(TileEntityType<?> tileEntityTypeIn, int color, boolean isGenerating) {
        super(tileEntityTypeIn);
        this.color = color;
        this.maxMana = DEFAULT_MAX_MANA;
        this.maxTransfer = isGenerating ? DEFAULT_MAX_TRANSFER : Integer.MAX_VALUE;
        this.isGenerating = isGenerating;
    }

    public FunctionalFlowerBase(TileEntityType<?> tileEntityTypeIn, int color, int maxMana, int maxTransfer, boolean isGenerating) {
        super(tileEntityTypeIn);
        this.color = color;
        this.maxMana = maxMana;
        this.maxTransfer = maxTransfer;
        this.isGenerating = isGenerating;
    }

    @Override
    public final void tick() {
        boolean prevFloating = floating;
        floating = this.getBlockState().getBlock() instanceof BlockFloatingFunctionalFlower<?>;
        if (prevFloating != floating)
            markDirty();

        this.linkPool();

        //noinspection ConstantConditions
        if (!world.isRemote) {
            if (isGenerating) {
                if (spreaderTile != null) {
                    if (mana > 0) {
                        int manaTransfer = Math.min(maxTransfer, Math.min(mana, spreaderTile.getMaxMana() - spreaderTile.getCurrentMana()));
                        spreaderTile.receiveMana(manaTransfer);
                        mana = MathHelper.clamp(mana - manaTransfer, 0, maxMana);
                        markDirty();
                        markPoolDirty();
                    }
                }
            } else {
                if (poolTile != null) {
                    if (mana < maxMana) {
                        int manaTransfer = Math.min(maxTransfer, Math.min(maxMana - mana, poolTile.getCurrentMana()));
                        poolTile.receiveMana(-manaTransfer);
                        mana = MathHelper.clamp(mana + manaTransfer, 0, maxMana);
                        markDirty();
                        markPoolDirty();
                    }
                }
            }

            redstoneIn = 0;
            for (Direction dir : Direction.values()) {
                int redstonePower = world.getRedstonePower(this.getPos().offset(dir), dir);
                redstoneIn = Math.max(redstonePower, redstoneIn);
            }
        }

        double particleChance = 1.0D - mana / (double) maxMana / 3.5;

        didWork = false;
        tickFlower();

        if (world.isRemote) {
            if (didWork)
                particleChance = 3 * particleChance;
            float red = (float) (color >> 16 & 0xFF) / 255.0f;
            float green = (float) (color >> 8 & 0xFF) / 255.0f;
            float blue = (float) (color & 255) / 255.0f;
            if (Math.random() > particleChance) {
                BotaniaAPI.instance().sparkleFX(this.getWorld(), (double) this.getPos().getX() + 0.3D + Math.random() * 0.5D, (double) this.getPos().getY() + 0.5D + Math.random() * 0.5D, (double) this.getPos().getZ() + 0.3D + Math.random() * 0.5D, red, green, blue, (float) Math.random(), 5);
            }
        }
    }

    protected abstract void tickFlower();

    @Override
    public boolean canSelect(PlayerEntity player, ItemStack stack, BlockPos pos, Direction direction) {
        return true;
    }

    @Override
    public boolean bindTo(PlayerEntity player, ItemStack stack, BlockPos pos, Direction direction) {
        int range = 10;
        range = range * range;
        double dist = pos.distanceSq(this.getPos());
        if ((double) range >= dist) {
            TileEntity tile = player.world.getTileEntity(pos);
            if (isGenerating && tile instanceof IManaCollector) {
                this.pool = tile.getPos();
                this.spreaderTile = (IManaCollector) tile;
                this.poolTile = null;
                markDirty();
                return true;
            } else if (!isGenerating && tile instanceof IManaPool) {
                this.pool = tile.getPos();
                this.poolTile = (IManaPool) tile;
                this.spreaderTile = null;
                markDirty();
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
        TileEntity theTile;
        if (!(theTileObj instanceof TileEntity)) {
            theTile = null;
        } else {
            theTile = (TileEntity) theTileObj;
        }
        //noinspection ConstantConditions
        if ((pool != null && theTile == null) || pool != null && !pool.equals(theTile.getPos()) || (pool != null && world.getTileEntity(pool) != theTile)) {
            // tile is outdated. Update it
            //noinspection ConstantConditions
            TileEntity te = world.getTileEntity(pool);
            if (isGenerating) {
                if (!(te instanceof IManaCollector)) {
                    poolTile = null;
                    spreaderTile = null;
                } else {
                    poolTile = null;
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
                int size = network.getAllCollectorsInWorld(this.getWorld()).size();
                if (BotaniaAPI.instance().shouldForceCheck() || size != this.sizeLastCheck) {
                    TileEntity te = network.getClosestCollector(this.getPos(), this.getWorld(), 10);
                    if (te instanceof IManaCollector) {
                        pool = te.getPos();
                        poolTile = null;
                        spreaderTile = (IManaCollector) te;
                        markDirty();
                    }
                    this.sizeLastCheck = size;
                }
            } else {
                IManaNetwork network = BotaniaAPI.instance().getManaNetworkInstance();
                int size = network.getAllPoolsInWorld(this.getWorld()).size();
                if (BotaniaAPI.instance().shouldForceCheck() || size != this.sizeLastCheck) {
                    TileEntity te = network.getClosestPool(this.getPos(), this.getWorld(), 10);
                    if (te instanceof IManaPool) {
                        pool = te.getPos();
                        poolTile = (IManaPool) te;
                        spreaderTile = null;
                        markDirty();
                    }
                    this.sizeLastCheck = size;
                }
            }
        }

        this.markDirty();
    }

    @Override
    public void read(@Nonnull BlockState stateIn, @Nonnull CompoundNBT nbtIn) {
        super.read(stateIn, nbtIn);
        if (nbtIn.contains("mana", Constants.NBT.TAG_INT)) {
            this.mana = MathHelper.clamp(nbtIn.getInt("mana"), 0, maxMana);
        } else {
            this.mana = 0;
        }
        if (nbtIn.contains("pool")) {
            CompoundNBT poolTag = nbtIn.getCompound("pool");
            pool = new BlockPos(poolTag.getInt("x"), poolTag.getInt("y"), poolTag.getInt("z"));
        } else {
            pool = null;
        }
        floating = nbtIn.getBoolean("floating");
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        compound.putInt("mana", MathHelper.clamp(mana, 0, maxMana));
        if (pool != null) {
            CompoundNBT poolTag = new CompoundNBT();
            poolTag.putInt("x", pool.getX());
            poolTag.putInt("y", pool.getY());
            poolTag.putInt("z", pool.getZ());
            compound.put("pool", poolTag);
        }
        compound.putBoolean("floating", floating);
        return super.write(compound);
    }

    @Nonnull
    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = new CompoundNBT();
        //noinspection ConstantConditions
        if (!world.isRemote) {
            tag.putInt("mana", MathHelper.clamp(mana, 0, maxMana));
            if (pool != null) {
                CompoundNBT poolTag = new CompoundNBT();
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
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        //noinspection ConstantConditions
        if (world.isRemote) {
            mana = MathHelper.clamp(tag.getInt("mana"), 0, maxMana);
            if (tag.contains("pool")) {
                CompoundNBT poolTag = tag.getCompound("pool");
                pool = new BlockPos(poolTag.getInt("x"), poolTag.getInt("y"), poolTag.getInt("z"));
            } else {
                pool = null;
            }
            floating = tag.getBoolean("floating");
        }
    }

    public boolean isValidBinding() {
        Object theTileObj = isGenerating ? spreaderTile : poolTile;
        if (!(theTileObj instanceof TileEntity))
            return false;
        TileEntity theTile = (TileEntity) theTileObj;
        // noinspection ConstantConditions,deprecation,deprecation
        return pool != null && theTile != null && theTile.hasWorld() && !theTile.isRemoved() && world.isBlockLoaded(theTile.getPos()) && getWorld().getTileEntity(pool) == theTile;
    }

    public int getCurrentMana() {
        return mana;
    }

    public boolean isFloating() {
        return floating;
    }

    public void setFloating(boolean floating) {
        this.floating = floating;
        markDirty();
    }

    @OnlyIn(Dist.CLIENT)
    public RadiusDescriptor getRadius() {
        return null;
    }

    @Override
    public void onLoad() {
        //noinspection ConstantConditions
        if (world.isRemote) {
            MythicNetwork.requestTE(world, pos);
        }
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    private void markPoolDirty() {
        if (poolTile instanceof TileEntity) {
            ((TileEntity) poolTile).markDirty();
        }
        if (spreaderTile instanceof TileEntity) {
            ((TileEntity) spreaderTile).markDirty();
        }
    }
}
