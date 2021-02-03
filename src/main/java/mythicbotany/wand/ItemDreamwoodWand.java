package mythicbotany.wand;

import com.google.common.collect.ImmutableSet;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.noeppi_noeppi.libx.mod.registration.Registerable;
import mythicbotany.ModItems;
import mythicbotany.MythicBotany;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import vazkii.botania.api.corporea.ICorporeaSpark;
import vazkii.botania.api.mana.spark.SparkHelper;
import vazkii.botania.api.mana.spark.SparkUpgradeType;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.entity.EntityCorporeaSpark;
import vazkii.botania.common.entity.EntitySpark;
import vazkii.botania.common.item.ItemSparkUpgrade;
import vazkii.botania.common.item.ItemTwigWand;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ItemDreamwoodWand extends ItemTwigWand implements Registerable {

    public ItemDreamwoodWand(Properties builder) {
        super(builder);
        MinecraftForge.EVENT_BUS.addListener(this::onEntityInteract);
    }

    @Override
    public Set<Object> getAdditionalRegisters() {
        return ImmutableSet.of(RecipeDreamwoodWand.SERIALIZER);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void registerClient(ResourceLocation id) {
        ItemModelsProperties.registerProperty(ModItems.dreamwoodWand, new ResourceLocation(MythicBotany.getInstance().modid, "bindmode"), (stack, world, entity) -> ItemTwigWand.getBindMode(stack) ? 1 : 0);
        Minecraft.getInstance().getItemColors().register((stack, colorId) -> colorId == 1 ? DyeColor.byId(getColor1(stack)).getColorValue() : (colorId == 2 ? DyeColor.byId(getColor2(stack)).getColorValue() : -1), ModItems.dreamwoodWand);
        MinecraftForge.EVENT_BUS.addListener(this::onRenderGameOverlay);
    }

    @OnlyIn(Dist.CLIENT)
    private void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.world == null || minecraft.player == null)
            return;
        RayTraceResult pos = minecraft.objectMouseOver;
        if (pos != null) {
            BlockPos bpos = pos.getType() == RayTraceResult.Type.BLOCK ? ((BlockRayTraceResult) pos).getPos() : null;
            BlockState state = bpos != null ? minecraft.world.getBlockState(bpos) : null;
            Block block = state == null ? null : state.getBlock();
            if (PlayerHelper.hasAnyHeldItem(minecraft.player)) {
                if (PlayerHelper.hasHeldItem(minecraft.player, ModItems.dreamwoodWand) && block instanceof IWandHUD) {
                    //noinspection deprecation
                    RenderSystem.pushTextureAttributes();
                    event.getMatrixStack().push();
                    minecraft.getProfiler().startSection("wandItemDreamwood");
                    ((IWandHUD) block).renderHUD(event.getMatrixStack(), minecraft, minecraft.world, bpos);
                    minecraft.getProfiler().endSection();
                    event.getMatrixStack().pop();
                    //noinspection deprecation
                    RenderSystem.popAttributes();
                    //noinspection deprecation
                    RenderSystem.color4f(1, 1, 1, 1);
                }
            }
        }
    }

    private void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        World world = event.getWorld();
        PlayerEntity player = event.getPlayer();
        ItemStack held = player.getHeldItem(event.getHand());
        if (!world.isRemote && !held.isEmpty() && held.getItem() == ModItems.dreamwoodWand) {
            if (event.getTarget() instanceof EntitySpark) {
                EntitySpark spark = (EntitySpark) event.getTarget();
                if (player.isSneaking()) {
                    if (spark.getUpgrade() != SparkUpgradeType.NONE) {
                        spark.entityDropItem(ItemSparkUpgrade.getByType(spark.getUpgrade()), 0.0F);
                        spark.setUpgrade(SparkUpgradeType.NONE);
                        spark.getTransfers().clear();
                    } else {
                        spark.entityDropItem(new ItemStack(vazkii.botania.common.item.ModItems.spark), 0.0F);
                        spark.remove();
                    }
                    event.setCanceled(true);
                    event.setCancellationResult(ActionResultType.func_233537_a_(false));
                } else {
                    SparkHelper.getSparksAround(world, spark.getPosX(), spark.getPosY() + (double) (spark.getHeight() / 2.0F), spark.getPosZ(), spark.getNetwork()).forEach((otherSpark) -> EntitySpark.particleBeam(player, spark, (Entity) otherSpark));
                }
            } else if (event.getTarget() instanceof EntityCorporeaSpark) {
                EntityCorporeaSpark spark = (EntityCorporeaSpark) event.getTarget();
                if (player.isSneaking()) {
                    boolean master = spark.isMaster();
                    spark.entityDropItem(new ItemStack(master ? vazkii.botania.common.item.ModItems.corporeaSparkMaster : vazkii.botania.common.item.ModItems.corporeaSpark), 0.0F);
                    spark.remove();
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
                    event.setCancellationResult(ActionResultType.func_233537_a_(false));
                } else {
                    displayRelatives(player, new ArrayList<>(), spark.getMaster());
                }
            }
        }
    }

    private static void displayRelatives(PlayerEntity player, List<ICorporeaSpark> checked, ICorporeaSpark spark) {
        if (spark != null) {
            List<ICorporeaSpark> sparks = spark.getRelatives();
            if (sparks.isEmpty()) {
                EntitySpark.particleBeam(player, (Entity) spark, (Entity) spark.getMaster());
            } else {
                for (ICorporeaSpark endSpark : sparks) {
                    if (!checked.contains(endSpark)) {
                        EntitySpark.particleBeam(player, (Entity) spark, (Entity) endSpark);
                        checked.add(endSpark);
                        displayRelatives(player, checked, endSpark);
                    }
                }
            }
        }

    }

    @Nonnull
    @Override
    protected String getDefaultTranslationKey() {
        return vazkii.botania.common.item.ModItems.twigWand.getTranslationKey();
    }

    @Override
    public void fillItemGroup(@Nonnull ItemGroup group, @Nonnull NonNullList<ItemStack> stacks) {
        if (this.isInGroup(group)) {
            for (int i = 0; i < 16; ++i) {
                stacks.add(forColors(i, i));
            }
        }
    }

    public static ItemStack forColors(int color1, int color2) {
        ItemStack stack = new ItemStack(ModItems.dreamwoodWand);
        ItemNBTHelper.setInt(stack, "color1", color1);
        ItemNBTHelper.setInt(stack, "color2", color2);
        return stack;
    }
}
