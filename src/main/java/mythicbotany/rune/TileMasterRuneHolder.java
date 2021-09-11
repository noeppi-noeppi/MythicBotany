package mythicbotany.rune;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Either;
import io.github.noeppi_noeppi.libx.util.BoundingBoxUtils;
import io.github.noeppi_noeppi.libx.util.NBTX;
import mythicbotany.ModBlocks;
import mythicbotany.ModItems;
import mythicbotany.ModRecipes;
import mythicbotany.misc.SolidifiedMana;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.*;
import net.minecraftforge.common.util.Constants;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TileMasterRuneHolder extends TileRuneHolder implements ITickableTileEntity {

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
    private CompoundNBT specialNbt = new CompoundNBT();

    public TileMasterRuneHolder(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public void tick() {
        if (world == null) {
            return;
        }
        if (recipeId != null) {
            if (recipe == null || !recipeId.equals(recipe.getId())) {
                IRecipe<?> foundRecipe = world.getRecipeManager().getRecipe(recipeId).orElse(null);
                if (foundRecipe instanceof RuneRitualRecipe) {
                    recipe = (RuneRitualRecipe) foundRecipe;
                    recipeId = recipe.getId();
                    markDirty();
                    markDispatchable();
                } else {
                    recipeId = null;
                }
            }
        }
        if (!world.isRemote) {
            if (recipe != null) {
                if (!recipeValid(recipe, transformId)) {
                    cancelRecipe();
                } else {
                    if (progress == 0) {
                        markDispatchable();
                    }
                    progress += 1;
                    if (progress >= recipe.getTicks()) {
                        getInventory().setStackInSlot(0, ItemStack.EMPTY);
                        for (ItemStack result : recipe.getOutputs()) {
                            ItemEntity ie = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, result);
                            ie.setPickupDelay(40);
                            ie.setGlowing(true);
                            world.addEntity(ie);
                        }

                        for (RuneRitualRecipe.RunePosition rune : recipe.getRunes()) {
                            BlockPos runePos = pos.add(rune.getX(transformId), 0, rune.getZ(transformId));
                            BlockState state = Objects.requireNonNull(world).getBlockState(runePos);
                            if (state.getBlock() == ModBlocks.runeHolder) {
                                TileRuneHolder tile = ModBlocks.runeHolder.getTile(world, runePos);
                                tile.setTarget(null, 0, true);
                                ItemStack runeStack = tile.getInventory().getStackInSlot(0);
                                tile.getInventory().setStackInSlot(0, ItemStack.EMPTY);
                                if (!rune.isConsumed() && !runeStack.isEmpty()) {
                                    ItemEntity ie = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, runeStack);
                                    world.addEntity(ie);
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
                        specialNbt = new CompoundNBT();

                        if (recipeCopy.getSpecialOutput() != null) {
                            recipeCopy.getSpecialOutput().apply(world, pos, consumedStacksCops);
                        }
                        markDispatchable();
                    } else {
                        updatePatterns(recipe, transformId, progress / (double) recipe.getTicks(), false);
                    }
                }
                markDirty();
            } else {
                recipeId = null;
                progress = 0;
                transformId = 0;
                if (!consumedStacks.isEmpty()) {
                    consumedStacks = new ArrayList<>();
                }
                specialNbt = new CompoundNBT();
                markDirty();
                markDispatchable();
            }
        } else {
            if (recipe != null) {
                if (progress < recipe.getTicks() && progress > 0) {
                    progress += 1;
                    double progressScaled = progress / (double) recipe.getTicks();
                    updatePatterns(recipe, transformId, progressScaled, false);
                    if (progress == recipe.getTicks() - 1) {
                        world.addParticle(ParticleTypes.FLASH, pos.getX() + 0.5, pos.getY() + 0.45, pos.getZ() + 0.5, 0, 0, 0);
                    } else if (progress < recipe.getTicks() - 2) {
                        progressScaled = Math.max(0, (progress - 2) / (double) recipe.getTicks());
                        for (RuneRitualRecipe.RunePosition rune : recipe.getRunes()) {
                            TileEntity runeHolderTE = world.getTileEntity(pos.add(rune.getX(transformId), 0, rune.getZ(transformId)));
                            if (runeHolderTE instanceof TileRuneHolder) {
                                ItemStack stack = ((TileRuneHolder) runeHolderTE).getInventory().getStackInSlot(0);
                                if (!stack.isEmpty()) {
                                    double x = rune.getX(transformId) * (1 - progressScaled);
                                    double y = Math.sin(progressScaled * Math.PI);
                                    double z = rune.getZ(transformId) * (1 - progressScaled);
                                    double xr = (world.rand.nextDouble() * 0.6) - 0.3;
                                    double yr = (world.rand.nextDouble() * 0.6) - 0.3;
                                    double zr = (world.rand.nextDouble() * 0.6) - 0.3;
                                    world.addParticle(getParticle(stack.getItem()), pos.getX() + 0.5 + x + xr, pos.getY() + 0.25 + y + yr, pos.getZ() + 0.5 + z + zr, 0, 0, 0);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void tryStartRitual(PlayerEntity player) {
        tryStartRitual(
                msg -> player.sendMessage(msg, player.getUniqueID()),
                mana -> ManaItemHandler.instance().requestManaExact(new ItemStack(Items.COBBLESTONE), player, mana, false),
                mana -> ManaItemHandler.instance().requestManaExact(new ItemStack(Items.COBBLESTONE), player, mana, true)
        );
    }
    
    public void tryStartRitual(Consumer<ITextComponent> messages, Function<Integer, Boolean> manaTest, Consumer<Integer> manaRequest) {
        if (recipe != null) {
            messages.accept(new TranslationTextComponent("message.mythicbotany.ritual_running").mergeStyle(TextFormatting.GRAY));
        } else {
            Pair<RuneRitualRecipe, Integer> recipe = findRecipe();
            if (recipe == null) {
                messages.accept(new TranslationTextComponent("message.mythicbotany.ritual_wrong_shape").mergeStyle(TextFormatting.GRAY));
            } else {
                tryStart(recipe.getLeft(), recipe.getRight(), messages, manaTest, manaRequest);
            }
        }
    }

    @Nullable
    private Pair<RuneRitualRecipe, Integer> findRecipe() {
        if (world == null || pos == null) {
            return null;
        }
        return world.getRecipeManager().getRecipesForType(ModRecipes.RUNE_RITUAL).stream()
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
    
    private void tryStart(RuneRitualRecipe recipe, int transform, Consumer<ITextComponent> messages, Function<Integer, Boolean> manaTest, Consumer<Integer> manaRequest) {
        if (recipe.getMana() > 0) {
            // We need to give a stack here or the request will always fail. The stack may not be empty.
            // So we just pass a piece of cobblestone.
            if (!manaTest.apply(recipe.getMana())) {
                messages.accept(new TranslationTextComponent("message.mythicbotany.ritual_less_mana").mergeStyle(TextFormatting.GRAY));
                return;
            }
        }
        List<ItemEntity> inputs = Objects.requireNonNull(world).getEntitiesWithinAABB(EntityType.ITEM, BoundingBoxUtils.expand(new Vector3d(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5), 2), e -> true);
        List<MutableTriple<ItemEntity, ItemStack, Integer>> stacks = inputs.stream()
                .map(e -> MutableTriple.of(e, e.getItem(), e.getItem().getCount()))
                .filter(t -> !t.getMiddle().isEmpty())
                .collect(Collectors.toList());
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
            messages.accept(new TranslationTextComponent("message.mythicbotany.ritual_wrong_items").mergeStyle(TextFormatting.GRAY));
            return;
        }

        // Special input must be the last as check and apply is in one method here. After this we apply everything
        if (recipe.getSpecialInput() != null) {
            Either<IFormattableTextComponent, CompoundNBT> result = recipe.getSpecialInput().apply(world, pos, recipe);
            Optional<IFormattableTextComponent> tc = result.left();
            if (tc.isPresent()) {
                messages.accept(tc.get().mergeStyle(TextFormatting.GRAY));
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
        
        markDirty();
        markDispatchable();
    }

    private boolean runePatternMatches(RuneRitualRecipe recipe, int idx) {
        for (RuneRitualRecipe.RunePosition rune : recipe.getRunes()) {
            BlockPos runePos = pos.add(rune.getX(idx), 0, rune.getZ(idx));
            BlockState state = Objects.requireNonNull(world).getBlockState(runePos);
            if (state.getBlock() != ModBlocks.runeHolder) {
                return false;
            }
            TileRuneHolder tile = ModBlocks.runeHolder.getTile(world, runePos);
            if (!rune.getRune().test(tile.getInventory().getStackInSlot(0))) {
                return false;
            }
        }
        return true;
    }

    private void updatePatterns(RuneRitualRecipe recipe, int transformId, double progress, boolean sync) {
        if (transformId < 0 || transformId >= 8) transformId = 0;
        setTarget(pos, 0, sync);
        for (RuneRitualRecipe.RunePosition rune : recipe.getRunes()) {
            BlockPos runePos = pos.add(rune.getX(transformId), 0, rune.getZ(transformId));
            BlockState state = Objects.requireNonNull(world).getBlockState(runePos);
            if (state.getBlock() == ModBlocks.runeHolder) {
                TileRuneHolder tile = ModBlocks.runeHolder.getTile(world, runePos);
                tile.setTarget(progress == 0 ? null : pos, progress, sync);
            }
        }
    }

    public void cancelRecipe() {
        if (world != null && recipe != null) {
            updatePatterns(recipe, transformId, 0, true);
            for (ItemStack stack : consumedStacks) {
                ItemEntity ie = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
                world.addEntity(ie);
            }
            if (recipe.getSpecialInput() != null) {
                recipe.getSpecialInput().cancel(world, pos, recipe, specialNbt);
            }
            SolidifiedMana.dropMana(world, pos, recipe.getMana());
            recipe = null;
            recipeId = null;
            progress = 0;
            transformId = 0;
            consumedStacks = new ArrayList<>();
            specialNbt = new CompoundNBT();
            markDirty();
            markDispatchable();
        }
    }

    private boolean recipeValid(RuneRitualRecipe recipe, int transformId) {
        if (!recipe.getCenterRune().test(getInventory().getStackInSlot(0))) {
            return false;
        }
        if (transformId < 0 || transformId >= 8) transformId = 0;
        for (RuneRitualRecipe.RunePosition rune : recipe.getRunes()) {
            BlockPos runePos = pos.add(rune.getX(transformId), 0, rune.getZ(transformId));
            BlockState state = Objects.requireNonNull(world).getBlockState(runePos);
            if (state.getBlock() != ModBlocks.runeHolder) {
                return false;
            }
            TileRuneHolder tile = ModBlocks.runeHolder.getTile(world, runePos);
            if (!rune.getRune().test(tile.getInventory().getStackInSlot(0))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void read(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
        super.read(state, nbt);
        ResourceLocation id = NBTX.getRL(nbt, "recipe", MISSIGNO);
        recipeId = id == MISSIGNO ? null : id;
        progress = nbt.getInt("progress");
        transformId = nbt.getInt("transform");
        if (nbt.contains("Consumed", Constants.NBT.TAG_LIST)) {
            ListNBT consumed = nbt.getList("Consumed", Constants.NBT.TAG_COMPOUND);
            consumedStacks = new ArrayList<>();
            for (int i = 0; i < consumed.size(); i++) {
                ItemStack stack = ItemStack.read(consumed.getCompound(i));
                if (!stack.isEmpty()) {
                    consumedStacks.add(stack);
                }
            }
        } else {
            consumedStacks = new ArrayList<>();
        }
        specialNbt = nbt.getCompound("SpecialInputData").copy();
    }

    @Nonnull
    @Override
    public CompoundNBT write(@Nonnull CompoundNBT nbt) {
        NBTX.putRL(nbt, "recipe", recipe == null ? MISSIGNO : recipe.getId());
        nbt.putInt("progress", progress);
        nbt.putInt("transform", transformId);
        ListNBT consumed = new ListNBT();
        for (ItemStack stack : consumedStacks) {
            consumed.add(stack.serializeNBT());
        }
        nbt.put("Consumed", consumed);
        nbt.put("SpecialInputData", specialNbt.copy());
        return super.write(nbt);
    }

    @Nonnull
    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt = super.getUpdateTag();
        //noinspection ConstantConditions
        if (!world.isRemote) {
            NBTX.putRL(nbt, "recipe", recipe == null ? MISSIGNO : recipe.getId());
            nbt.putInt("progress", progress);
            nbt.putInt("transform", transformId);
        }
        return nbt;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT nbt) {
        super.handleUpdateTag(state, nbt);
        //noinspection ConstantConditions
        if (world.isRemote) {
            ResourceLocation id = NBTX.getRL(nbt, "recipe", MISSIGNO);
            recipeId = id == MISSIGNO ? null : id;
            progress = nbt.getInt("progress");
            transformId = nbt.getInt("transform");
        }
    }
    
    private IParticleData getParticle(Item rune) {
        if (rune == ModItems.fimbultyrTablet) {
            return new ItemParticleData(ParticleTypes.ITEM, new ItemStack(Items.GOLD_BLOCK));
        } else if (rune == vazkii.botania.common.item.ModItems.runeMana) {
            return WispParticleData.wisp(0.2f, 0, 0, 1, 0.3f);
        } else if (rune == vazkii.botania.common.item.ModItems.runeFire) {
            return ParticleTypes.FLAME;
        } else if (rune == vazkii.botania.common.item.ModItems.runeAir) {
            return ParticleTypes.CLOUD;
        } else if (rune == vazkii.botania.common.item.ModItems.runeEarth) {
            return new ItemParticleData(ParticleTypes.ITEM, new ItemStack(Items.DIRT));
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
