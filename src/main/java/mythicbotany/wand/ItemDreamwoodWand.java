package mythicbotany.wand;

import com.google.common.collect.ImmutableSet;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.noeppi_noeppi.libx.mod.registration.Registerable;
import io.github.noeppi_noeppi.libx.render.RenderHelper;
import mythicbotany.ModItems;
import mythicbotany.MythicBotany;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.BotaniaForgeClientCapabilities;
import vazkii.botania.api.block.IWandHUD;
import vazkii.botania.api.corporea.ICorporeaSpark;
import vazkii.botania.api.mana.spark.SparkHelper;
import vazkii.botania.api.mana.spark.SparkUpgradeType;
import vazkii.botania.common.entity.EntityCorporeaSpark;
import vazkii.botania.common.entity.EntityManaSpark;
import vazkii.botania.common.helper.ItemNBTHelper;
import vazkii.botania.common.helper.PlayerHelper;
import vazkii.botania.common.item.ItemSparkUpgrade;
import vazkii.botania.common.item.ItemTwigWand;
import vazkii.botania.forge.CapabilityUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class ItemDreamwoodWand extends ItemTwigWand implements Registerable {

    public ItemDreamwoodWand(Properties builder) {
        super(builder);
        MinecraftForge.EVENT_BUS.addListener(this::onEntityInteract);
    }

    @Override
    public Set<Object> getAdditionalRegisters(ResourceLocation id) {
        return ImmutableSet.of(RecipeDreamwoodWand.SERIALIZER);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void registerClient(ResourceLocation id, Consumer<Runnable> defer) {
        defer.accept(() -> {
            ItemProperties.register(ModItems.dreamwoodTwigWand, new ResourceLocation(MythicBotany.getInstance().modid, "bindmode"), (stack, world, entity, seed) -> ItemTwigWand.getBindMode(stack) ? 1 : 0);
            Minecraft.getInstance().getItemColors().register((stack, colorId) -> colorId == 1 ? DyeColor.byId(getColor1(stack)).getTextColor() : (colorId == 2 ? DyeColor.byId(getColor2(stack)).getTextColor() : -1), ModItems.dreamwoodTwigWand);
        });
        MinecraftForge.EVENT_BUS.addListener(this::onRenderGameOverlay);
    }

    @OnlyIn(Dist.CLIENT)
    private void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null || mc.options.hideGui) {
            return;
        }
        if (mc.hitResult instanceof BlockHitResult result) {
            PoseStack poseStack = event.getMatrixStack();
            BlockPos pos = result.getBlockPos();
            BlockState state = mc.level.getBlockState(pos);
            BlockEntity blockEntity = mc.level.getBlockEntity(pos);
            if (PlayerHelper.hasAnyHeldItem(mc.player)) {
                if (PlayerHelper.hasHeldItem(mc.player, ModItems.dreamwoodTwigWand)) {
                    IWandHUD hud = CapabilityUtil.findCapability(BotaniaForgeClientCapabilities.WAND_HUD, mc.level, pos, state, blockEntity);
                    if (hud != null) {
                        RenderHelper.resetColor();
                        hud.renderHUD(poseStack, mc);
                        RenderHelper.resetColor();
                    }
                }
            }
        }
    }

    private void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        Level level = event.getWorld();
        Player player = event.getPlayer();
        ItemStack held = player.getItemInHand(event.getHand());
        if (!level.isClientSide && !held.isEmpty() && held.getItem() == ModItems.dreamwoodTwigWand) {
            if (event.getTarget() instanceof EntityManaSpark spark) {
                if (player.isShiftKeyDown()) {
                    if (spark.getUpgrade() != SparkUpgradeType.NONE) {
                        spark.spawnAtLocation(ItemSparkUpgrade.getByType(spark.getUpgrade()), 0.0F);
                        spark.setUpgrade(SparkUpgradeType.NONE);
                        spark.getTransfers().clear();
                    } else {
                        SparkUpgradeType upgrade = spark.getUpgrade();
                        spark.spawnAtLocation(new ItemStack(vazkii.botania.common.item.ModItems.spark), 0.0F);
                        if (upgrade != SparkUpgradeType.NONE) {
                            spark.spawnAtLocation(ItemSparkUpgrade.getByType(upgrade), 0.0F);
                        }
                        spark.remove(Entity.RemovalReason.DISCARDED);
                    }
                } else {
                    SparkHelper.getSparksAround(spark.level, spark.getX(), spark.getY() + spark.getBbHeight() / 2d, spark.getZ(), spark.getNetwork()).forEach((s) -> EntityManaSpark.particleBeam(player, spark, s.entity()));
                }
            } else if (event.getTarget() instanceof EntityCorporeaSpark spark) {
                if (player.isShiftKeyDown()) {
                    boolean master = spark.isMaster();
                    boolean creative = spark.isCreative();
                    if (creative) {
                        spark.spawnAtLocation(new ItemStack(vazkii.botania.common.item.ModItems.corporeaSparkCreative));
                    } else if (master) {
                        spark.spawnAtLocation(new ItemStack(vazkii.botania.common.item.ModItems.corporeaSparkMaster));
                    } else {
                        spark.spawnAtLocation(new ItemStack(vazkii.botania.common.item.ModItems.corporeaSpark));
                    }
                    spark.remove(Entity.RemovalReason.DISCARDED);
                    if (master) {
                        spark.getConnections().clear();
                        spark.getRelatives().clear();
                        if (spark.getMaster() != null) {
                            ICorporeaSpark oldMaster = spark.getMaster();
                            ObfuscationReflectionHelper.setPrivateValue(EntityCorporeaSpark.class, spark, null, "master");
                            oldMaster.registerConnections(oldMaster, spark, new ArrayList<>());
                        }
                    }
                    event.setCanceled(true);
                    event.setCancellationResult(InteractionResult.sidedSuccess(false));
                } else {
                    displayRelatives(player, new ArrayList<>(), spark.getMaster());
                }
            }
        }
    }

    private static void displayRelatives(Player player, List<ICorporeaSpark> checked, ICorporeaSpark spark) {
        if (spark != null) {
            List<ICorporeaSpark> sparks = spark.getRelatives();
            if (sparks.isEmpty()) {
                EntityManaSpark.particleBeam(player, spark.entity(), spark.getMaster().entity());
            } else {
                for (ICorporeaSpark other : sparks) {
                    if (!checked.contains(other)) {
                        EntityManaSpark.particleBeam(player, spark.entity(), other.entity());
                        checked.add(other);
                        displayRelatives(player, checked, other);
                    }
                }
            }
        }
    }

    @Nonnull
    @Override
    protected String getOrCreateDescriptionId() {
        return vazkii.botania.common.item.ModItems.twigWand.getDescriptionId();
    }

    @Override
    public void fillItemCategory(@Nonnull CreativeModeTab category, @Nonnull NonNullList<ItemStack> stacks) {
        if (this.allowdedIn(category)) {
            for (int i = 0; i < 16; ++i) {
                stacks.add(forColors(i, i));
            }
        }
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return CapabilityUtil.makeProvider(BotaniaForgeCapabilities.COORD_BOUND_ITEM, new ItemTwigWand.CoordBoundItem(stack));
    }

    public static ItemStack forColors(int color1, int color2) {
        ItemStack stack = new ItemStack(ModItems.dreamwoodTwigWand);
        ItemNBTHelper.setInt(stack, "color1", color1);
        ItemNBTHelper.setInt(stack, "color2", color2);
        return stack;
    }
}
