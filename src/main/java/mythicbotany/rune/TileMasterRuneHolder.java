package mythicbotany.rune;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Either;
import io.github.noeppi_noeppi.libx.base.tile.TickableBlock;
import io.github.noeppi_noeppi.libx.util.NBTX;
import mythicbotany.ModBlocks;
import mythicbotany.ModItems;
import mythicbotany.ModRecipes;
import mythicbotany.misc.SolidifiedMana;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.MutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.client.fx.WispParticleData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class TileMasterRuneHolder extends TileRuneHolder implements TickableBlock {

    private static final ResourceLocation MISSIGNO = new ResourceLocation("minecraft", "missingno");
    private static final Map<Item, Integer> RUNE_COLORS = ImmutableMap.<Item, Integer>builder()
            .put(vazkii.botania.common.item.ModItems.runeAir, 0x68B0EA)
            .put(vazkii.botania.common.item.ModItems.runeSpring, 0xFF919F)
            .put(vazkii.botania.common.item.ModItems.runeSummer, 0x00DDED)
            .put(vazkii.botania.common.item.ModItems.runeAutumn, 0xE5C200)
            .put(vazkii.botania.common.item.ModItems.runeWinter, 0xE0DBD5)
            .put(vazkii.botania.common.item.ModItems.runeLust, 0xF346D1)
            .put(vazkii.botania.common.item.ModItems.runeGluttony, 0x6E6E6E)
            .put(vazkii.botania.common.item.ModItems.runeGreed, 0x009431)
            .put(vazkii.botania.common.item.ModItems.runeSloth, 0xBB9661)
            .put(vazkii.botania.common.item.ModItems.runeWrath, 0xFF2424)
            .put(vazkii.botania.common.item.ModItems.runeEnvy, 0xC858E6)
            .put(vazkii.botania.common.item.ModItems.runePride, 0x2C3237)
            .put(ModItems.asgardRune, 0xE1C500)
            .put(ModItems.vanaheimRune, 0x5FC748)
            .put(ModItems.alfheimRune, 0xFF76F7)
            .put(ModItems.midgardRune, 0x19B13B)
            .put(ModItems.joetunheimRune, 0xB16719)
            .put(ModItems.muspelheimRune, 0xBF0000)
            .put(ModItems.niflheimRune, 0x00B1BF)
            .put(ModItems.nidavellirRune, 0x6F6F6F)
            .put(ModItems.helheimRune, 0x780000)
            .build();
    
    @Nullable
    private RuneRitualRecipe recipe;
    @Nullable
    private ResourceLocation recipeId;
    private int progress;
    private int transformId;
    private List<ItemStack> consumedStacks = new ArrayList<>();
    private CompoundTag specialNbt = new CompoundTag();

    public TileMasterRuneHolder(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void tick() {
        if (level == null) {
            return;
        }
        if (recipeId != null) {
            if (recipe == null || !recipeId.equals(recipe.getId())) {
                Recipe<?> foundRecipe = level.getRecipeManager().byKey(recipeId).orElse(null);
                if (foundRecipe instanceof RuneRitualRecipe) {
                    recipe = (RuneRitualRecipe) foundRecipe;
                    recipeId = recipe.getId();
                    setChanged();
                    setDispatchable();
                } else {
                    recipeId = null;
                }
            }
        }
        if (!level.isClientSide) {
            if (recipe != null) {
                if (!recipeValid(recipe, transformId)) {
                    cancelRecipe();
                } else {
                    if (progress == 0) {
                        setDispatchable();
                    }
                    progress += 1;
                    if (progress >= recipe.getTicks()) {
                        getInventory().setStackInSlot(0, ItemStack.EMPTY);
                        for (ItemStack result : recipe.getOutputs()) {
                            ItemEntity ie = new ItemEntity(level, worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, result.copy());
                            ie.setPickUpDelay(40);
                            ie.setGlowingTag(true);
                            level.addFreshEntity(ie);
                        }

                        for (RuneRitualRecipe.RunePosition rune : recipe.getRunes()) {
                            BlockPos runePos = worldPosition.offset(rune.getX(transformId), 0, rune.getZ(transformId));
                            BlockState state = Objects.requireNonNull(level).getBlockState(runePos);
                            if (state.getBlock() == ModBlocks.runeHolder) {
                                TileRuneHolder tile = ModBlocks.runeHolder.getBlockEntity(level, runePos);
                                tile.setTarget(null, 0, true);
                                ItemStack runeStack = tile.getInventory().getStackInSlot(0);
                                tile.getInventory().setStackInSlot(0, ItemStack.EMPTY);
                                if (!rune.isConsumed() && !runeStack.isEmpty()) {
                                    ItemEntity ie = new ItemEntity(level, worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, runeStack);
                                    level.addFreshEntity(ie);
                                }
                            }
                        }
                        RuneRitualRecipe recipeCopy = recipe;
                        recipe = null;
                        recipeId = null;
                        progress = 0;
                        transformId = 0;
                        List<ItemStack> consumedStacksCops = consumedStacks;
                        consumedStacks = new ArrayList<>();
                        specialNbt = new CompoundTag();

                        if (recipeCopy.getSpecialOutput() != null) {
                            recipeCopy.getSpecialOutput().apply(level, worldPosition, consumedStacksCops);
                        }
                        setDispatchable();
                    } else {
                        updabePatterns(recipe, transformId, progress / (double) recipe.getTicks(), false);
                    }
                }
                setChanged();
            } else {
                recipeId = null;
                progress = 0;
                transformId = 0;
                if (!consumedStacks.isEmpty()) {
                    consumedStacks = new ArrayList<>();
                }
                specialNbt = new CompoundTag();
                setChanged();
                setDispatchable();
            }
        } else {
            if (recipe != null) {
                if (progress < recipe.getTicks() && progress > 0) {
                    progress += 1;
                    double progressScaled = progress / (double) recipe.getTicks();
                    updabePatterns(recipe, transformId, progressScaled, false);
                    if (progress == recipe.getTicks() - 1) {
                        level.addParticle(ParticleTypes.FLASH, worldPosition.getX() + 0.5, worldPosition.getY() + 0.45, worldPosition.getZ() + 0.5, 0, 0, 0);
                    } else if (progress < recipe.getTicks() - 2) {
                        progressScaled = Math.max(0, (progress - 2) / (double) recipe.getTicks());
                        for (RuneRitualRecipe.RunePosition rune : recipe.getRunes()) {
                            BlockEntity runeHolderBE = level.getBlockEntity(worldPosition.offset(rune.getX(transformId), 0, rune.getZ(transformId)));
                            if (runeHolderBE instanceof TileRuneHolder) {
                                ItemStack stack = ((TileRuneHolder) runeHolderBE).getInventory().getStackInSlot(0);
                                if (!stack.isEmpty()) {
                                    double x = rune.getX(transformId) * (1 - progressScaled);
                                    double y = Math.sin(progressScaled * Math.PI);
                                    double z = rune.getZ(transformId) * (1 - progressScaled);
                                    double xr = (level.random.nextDouble() * 0.6) - 0.3;
                                    double yr = (level.random.nextDouble() * 0.6) - 0.3;
                                    double zr = (level.random.nextDouble() * 0.6) - 0.3;
                                    level.addParticle(getParticle(stack.getItem()), worldPosition.getX() + 0.5 + x + xr, worldPosition.getY() + 0.25 + y + yr, worldPosition.getZ() + 0.5 + z + zr, 0, 0, 0);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void tryStartRitual(Player player) {
        tryStartRitual(
                msg -> player.sendMessage(msg, player.getUUID()),
                mana -> ManaItemHandler.instance().requestManaExact(new ItemStack(Items.COBBLESTONE), player, mana, false),
                mana -> ManaItemHandler.instance().requestManaExact(new ItemStack(Items.COBBLESTONE), player, mana, true)
        );
    }
    
    public void tryStartRitual(Consumer<Component> messages, Function<Integer, Boolean> manaBest, Consumer<Integer> manaRequest) {
        if (recipe != null) {
            messages.accept(new TranslatableComponent("message.mythicbotany.ritual_running").withStyle(ChatFormatting.GRAY));
        } else {
            Pair<RuneRitualRecipe, Integer> recipe = findRecipe();
            if (recipe == null) {
                messages.accept(new TranslatableComponent("message.mythicbotany.ritual_wrong_shape").withStyle(ChatFormatting.GRAY));
            } else {
                tryStart(recipe.getLeft(), recipe.getRight(), messages, manaBest, manaRequest);
            }
        }
    }

    @Nullable
    private Pair<RuneRitualRecipe, Integer> findRecipe() {
        if (level == null) {
            return null;
        }
        return level.getRecipeManager().getAllRecipesFor(ModRecipes.RUNE_RITUAL).stream()
                .flatMap(this::recipeMatches)
                .findFirst().orElse(null);
    }

    private Stream<Pair<RuneRitualRecipe, Integer>> recipeMatches(RuneRitualRecipe recipe) {
        if (!recipe.getCenterRune().test(getInventory().getStackInSlot(0))) {
            return Stream.empty();
        }
        int transformId = -1;
        for (int i = 0; i < 8; i++) {
            if (runePatternMatches(recipe, i)) {
                transformId = i;
                break;
            }
        }
        if (transformId < 0) {
            return Stream.empty();
        }

        return Stream.of(Pair.of(recipe, transformId));
    }
    
    private void tryStart(RuneRitualRecipe recipe, int transform, Consumer<Component> messages, Function<Integer, Boolean> manaBest, Consumer<Integer> manaRequest) {
        if (recipe.getMana() > 0) {
            // We need to give a stack here or the request will always fail. The stack may not be empty.
            // So we just pass a piece of cobblestone.
            if (!manaBest.apply(recipe.getMana())) {
                messages.accept(new TranslatableComponent("message.mythicbotany.ritual_less_mana").withStyle(ChatFormatting.GRAY));
                return;
            }
        }
        Vec3 center = new Vec3(worldPosition.getX() + 0.5, worldPosition.getY(), worldPosition.getZ() + 0.5);
        AABB aabb = new AABB(center, center).inflate(2);
        List<ItemEntity> inputs = Objects.requireNonNull(level).getEntities(EntityType.ITEM, aabb, e -> true);
        List<MutableTriple<ItemEntity, ItemStack, Integer>> stacks = inputs.stream()
                .map(e -> MutableTriple.of(e, e.getItem(), e.getItem().getCount()))
                .filter(t -> !t.getMiddle().isEmpty()).toList();
        List<ItemStack> consumedStacks = new ArrayList<>();
        ingredientLoop:
        for (Ingredient ingr : recipe.getInputs()) {
            for (MutableTriple<ItemEntity, ItemStack, Integer> triple : stacks) {
                if (triple.getRight() > 0 && ingr.test(triple.getMiddle())) {
                    ItemStack copy = triple.getMiddle().copy();
                    copy.setCount(1);
                    consumedStacks.add(copy);
                    triple.setRight(triple.getRight() - 1);
                    continue ingredientLoop;
                }
            }
            messages.accept(new TranslatableComponent("message.mythicbotany.ritual_wrong_items").withStyle(ChatFormatting.GRAY));
            return;
        }

        // Special input must be the last as check and apply is in one method here. After this we apply everything
        if (recipe.getSpecialInput() != null) {
            Either<MutableComponent, CompoundTag> result = recipe.getSpecialInput().apply(level, worldPosition, recipe);
            Optional<MutableComponent> tc = result.left();
            if (tc.isPresent()) {
                messages.accept(tc.get().withStyle(ChatFormatting.GRAY));
                return;
            }
            result.ifRight(nbt -> specialNbt = nbt);
        }
        
        manaRequest.accept(recipe.getMana());
        for (MutableTriple<ItemEntity, ItemStack, Integer> triple : stacks) {
            ItemStack stack = triple.getMiddle();
            stack.setCount(triple.getRight());
            triple.getLeft().setItem(stack);
        }

        this.recipe = recipe;
        this.progress = 0;
        this.transformId = transform;
        this.consumedStacks = consumedStacks;
        
        setChanged();
        setDispatchable();
    }

    private boolean runePatternMatches(RuneRitualRecipe recipe, int idx) {
        for (RuneRitualRecipe.RunePosition rune : recipe.getRunes()) {
            BlockPos runePos = worldPosition.offset(rune.getX(idx), 0, rune.getZ(idx));
            BlockState state = Objects.requireNonNull(level).getBlockState(runePos);
            if (state.getBlock() != ModBlocks.runeHolder) {
                return false;
            }
            TileRuneHolder tile = ModBlocks.runeHolder.getBlockEntity(level, runePos);
            if (!rune.getRune().test(tile.getInventory().getStackInSlot(0))) {
                return false;
            }
        }
        return true;
    }

    private void updabePatterns(RuneRitualRecipe recipe, int transformId, double progress, boolean sync) {
        if (transformId < 0 || transformId >= 8) transformId = 0;
        setTarget(worldPosition, 0, sync);
        for (RuneRitualRecipe.RunePosition rune : recipe.getRunes()) {
            BlockPos runePos = worldPosition.offset(rune.getX(transformId), 0, rune.getZ(transformId));
            BlockState state = Objects.requireNonNull(level).getBlockState(runePos);
            if (state.getBlock() == ModBlocks.runeHolder) {
                TileRuneHolder tile = ModBlocks.runeHolder.getBlockEntity(level, runePos);
                tile.setTarget(progress == 0 ? null : worldPosition, progress, sync);
            }
        }
    }

    public void cancelRecipe() {
        if (level != null && recipe != null) {
            updabePatterns(recipe, transformId, 0, true);
            for (ItemStack stack : consumedStacks) {
                ItemEntity ie = new ItemEntity(level, worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, stack);
                level.addFreshEntity(ie);
            }
            if (recipe.getSpecialInput() != null) {
                recipe.getSpecialInput().cancel(level, worldPosition, recipe, specialNbt);
            }
            SolidifiedMana.dropMana(level, worldPosition, recipe.getMana());
            recipe = null;
            recipeId = null;
            progress = 0;
            transformId = 0;
            consumedStacks = new ArrayList<>();
            specialNbt = new CompoundTag();
            setChanged();
            setDispatchable();
        }
    }

    private boolean recipeValid(RuneRitualRecipe recipe, int transformId) {
        if (!recipe.getCenterRune().test(getInventory().getStackInSlot(0))) {
            return false;
        }
        if (transformId < 0 || transformId >= 8) transformId = 0;
        for (RuneRitualRecipe.RunePosition rune : recipe.getRunes()) {
            BlockPos runePos = worldPosition.offset(rune.getX(transformId), 0, rune.getZ(transformId));
            BlockState state = Objects.requireNonNull(level).getBlockState(runePos);
            if (state.getBlock() != ModBlocks.runeHolder) {
                return false;
            }
            TileRuneHolder tile = ModBlocks.runeHolder.getBlockEntity(level, runePos);
            if (!rune.getRune().test(tile.getInventory().getStackInSlot(0))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void load(@Nonnull CompoundTag nbt) {
        super.load(nbt);
        ResourceLocation id = NBTX.getRL(nbt, "recipe", MISSIGNO);
        recipeId = id == MISSIGNO ? null : id;
        progress = nbt.getInt("progress");
        transformId = nbt.getInt("transform");
        if (nbt.contains("Consumed", Tag.TAG_LIST)) {
            ListTag consumed = nbt.getList("Consumed", Tag.TAG_COMPOUND);
            consumedStacks = new ArrayList<>();
            for (int i = 0; i < consumed.size(); i++) {
                ItemStack stack = ItemStack.of(consumed.getCompound(i));
                if (!stack.isEmpty()) {
                    consumedStacks.add(stack);
                }
            }
        } else {
            consumedStacks = new ArrayList<>();
        }
        specialNbt = nbt.getCompound("SpecialInputData").copy();
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag nbt) {
        super.saveAdditional(nbt);
        NBTX.putRL(nbt, "recipe", recipe == null ? MISSIGNO : recipe.getId());
        nbt.putInt("progress", progress);
        nbt.putInt("transform", transformId);
        ListTag consumed = new ListTag();
        for (ItemStack stack : consumedStacks) {
            consumed.add(stack.serializeNBT());
        }
        nbt.put("Consumed", consumed);
        nbt.put("SpecialInputData", specialNbt.copy());
    }

    @Nonnull
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        //noinspection ConstantConditions
        if (!level.isClientSide) {
            NBTX.putRL(nbt, "recipe", recipe == null ? MISSIGNO : recipe.getId());
            nbt.putInt("progress", progress);
            nbt.putInt("transform", transformId);
        }
        return nbt;
    }

    @Override
    public void handleUpdateTag(CompoundTag nbt) {
        super.handleUpdateTag(nbt);
        //noinspection ConstantConditions
        if (level.isClientSide) {
            ResourceLocation id = NBTX.getRL(nbt, "recipe", MISSIGNO);
            recipeId = id == MISSIGNO ? null : id;
            progress = nbt.getInt("progress");
            transformId = nbt.getInt("transform");
        }
    }
    
    private ParticleOptions getParticle(Item rune) {
        if (rune == ModItems.fimbultyrTablet) {
            return new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.GOLD_BLOCK));
        } else if (rune == vazkii.botania.common.item.ModItems.runeMana) {
            return WispParticleData.wisp(0.2f, 0, 0, 1, 0.3f);
        } else if (rune == vazkii.botania.common.item.ModItems.runeFire) {
            return ParticleTypes.FLAME;
        } else if (rune == vazkii.botania.common.item.ModItems.runeAir) {
            return ParticleTypes.CLOUD;
        } else if (rune == vazkii.botania.common.item.ModItems.runeEarth) {
            return new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.DIRT));
        } else if (rune == vazkii.botania.common.item.ModItems.runeWater) {
            return ParticleTypes.DOLPHIN;
        } else {
            int color = RUNE_COLORS.getOrDefault(rune, 0xFFFFFF);
            float r = ((color >> 16) & 0xFF) / 255f;
            float g = ((color >> 8) & 0xFF) / 255f;
            float b = (color & 0xFF) / 255f;
            return SparkleParticleData.sparkle(2, r, g, b, 6);
        }
    }
}
