package mythicbotany.data.recipes.builder;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.noeppi_noeppi.libx.data.CraftingHelper2;
import mythicbotany.ModRecipes;
import mythicbotany.rune.RuneRitualRecipe;
import mythicbotany.rune.SpecialRuneInput;
import mythicbotany.rune.SpecialRuneOutput;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class RuneRitualRecipeBuilder {

    private final Ingredient centerRune;
    private final List<RuneRitualRecipe.RunePosition> runes = new ArrayList<>();
    private int manaCost = 0;
    private int tickTime = RuneRitualRecipe.DEFAULT_TICKS;
    private final List<Ingredient> inputs = new ArrayList<>();
    private final List<ItemStack> outputs = new ArrayList<>();
    @Nullable
    private SpecialRuneInput specialInput;
    @Nullable
    private SpecialRuneOutput specialOutput;

    public static RuneRitualRecipeBuilder runeRitual(IItemProvider centerRune) {
        return new RuneRitualRecipeBuilder(Ingredient.fromItems(centerRune));
    }

    public static RuneRitualRecipeBuilder runeRitual(ITag<Item> centerRune) {
        return new RuneRitualRecipeBuilder(Ingredient.fromTag(centerRune));
    }
    
    public static RuneRitualRecipeBuilder runeRitual(Ingredient centerRune) {
        return new RuneRitualRecipeBuilder(centerRune);
    }

    private RuneRitualRecipeBuilder(Ingredient centerRune) {
        this.centerRune = centerRune;
    }

    public RuneRitualRecipeBuilder rune(IItemProvider rune, int x, int z, boolean consume) {
        return rune(Ingredient.fromItems(rune), x, z, consume);
    }

    public RuneRitualRecipeBuilder rune(ITag<Item> rune, int x, int z, boolean consume) {
        return rune(Ingredient.fromTag(rune), x, z, consume);
    }

    public RuneRitualRecipeBuilder rune(Ingredient rune, int x, int z, boolean consume) {
        if (x < -5 || x > 5 || z < -5 || z > 5) {
            throw new IllegalStateException("Rune positions should not be more than 5 blocks away frm the master rune holder: (" + x + "," + z + ")");
        }
        this.runes.add(new RuneRitualRecipe.RunePosition(rune, x, z, consume));
        return this;
    }

    public RuneRitualRecipeBuilder rune4(IItemProvider rune, int x, int z, boolean consume) {
        if (x == 0) {
            rune(rune, 0, -z, consume);
            rune(rune, 0, z, consume);
            rune(rune, -z, 0, consume);
            rune(rune, z, 0, consume);
        } else if (z == 0) {
            rune(rune, -x, 0, consume);
            rune(rune, x, 0, consume);
            rune(rune, 0, -x, consume);
            rune(rune, 0, x, consume);
        } else {
            rune(rune, -x, -z, consume);
            rune(rune, -x, z, consume);
            rune(rune, x, -z, consume);
            rune(rune, x, z, consume);
        }
        return this;
    }

    public RuneRitualRecipeBuilder rune4(ITag<Item> rune, int x, int z, boolean consume) {
        if (x == 0) {
            rune(rune, 0, -z, consume);
            rune(rune, 0, z, consume);
            rune(rune, -z, 0, consume);
            rune(rune, z, 0, consume);
        } else if (z == 0) {
            rune(rune, -x, 0, consume);
            rune(rune, x, 0, consume);
            rune(rune, 0, -x, consume);
            rune(rune, 0, x, consume);
        } else {
            rune(rune, -x, -z, consume);
            rune(rune, -x, z, consume);
            rune(rune, x, -z, consume);
            rune(rune, x, z, consume);
        }
        return this;
    }

    public RuneRitualRecipeBuilder rune4(Ingredient rune, int x, int z, boolean consume) {
        if (x == 0) {
            rune(rune, 0, -z, consume);
            rune(rune, 0, z, consume);
            rune(rune, -z, 0, consume);
            rune(rune, z, 0, consume);
        } else if (z == 0) {
            rune(rune, -x, 0, consume);
            rune(rune, x, 0, consume);
            rune(rune, 0, -x, consume);
            rune(rune, 0, x, consume);
        } else {
            rune(rune, -x, -z, consume);
            rune(rune, -x, z, consume);
            rune(rune, x, -z, consume);
            rune(rune, x, z, consume);
        }
        return this;
    }
    
    public RuneRitualRecipeBuilder rune2(IItemProvider rune, int x, int z, boolean consume) {
        rune(rune, -x, -z, consume);
        rune(rune, x, z, consume);
        return this;
    }

    public RuneRitualRecipeBuilder rune2(ITag<Item> rune, int x, int z, boolean consume) {
        rune(rune, -x, -z, consume);
        rune(rune, x, z, consume);
        return this;
    }

    public RuneRitualRecipeBuilder rune2(Ingredient rune, int x, int z, boolean consume) {
        rune(rune, -x, -z, consume);
        rune(rune, x, z, consume);
        return this;
    }

    public RuneRitualRecipeBuilder rune(IItemProvider rune, int x, int z) {
        return rune(rune, x, z, false);
    }

    public RuneRitualRecipeBuilder rune(ITag<Item> rune, int x, int z) {
        return rune(rune, x, z, false);
    }

    public RuneRitualRecipeBuilder rune(Ingredient rune, int x, int z) {
        return rune(rune, x, z, false);
    }

    public RuneRitualRecipeBuilder rune4(IItemProvider rune, int x, int z) {
        return rune4(rune, x, z, false);
    }

    public RuneRitualRecipeBuilder rune4(ITag<Item> rune, int x, int z) {
        return rune4(rune, x, z, false);
    }

    public RuneRitualRecipeBuilder rune4(Ingredient rune, int x, int z) {
        return rune4(rune, x, z, false);
    }

    public RuneRitualRecipeBuilder rune2(IItemProvider rune, int x, int z) {
        return rune2(rune, x, z, false);
    }

    public RuneRitualRecipeBuilder rune2(ITag<Item> rune, int x, int z) {
        return rune2(rune, x, z, false);
    }

    public RuneRitualRecipeBuilder rune2(Ingredient rune, int x, int z) {
        return rune2(rune, x, z, false);
    }
    
    public RuneRitualRecipeBuilder mana(int manaCost) {
        this.manaCost = manaCost;
        return this;
    }

    public RuneRitualRecipeBuilder time(int tickTime) {
        this.tickTime = tickTime;
        return this;
    }

    public RuneRitualRecipeBuilder input(IItemProvider input) {
        return input(Ingredient.fromItems(input));
    }

    public RuneRitualRecipeBuilder input(ITag<Item> input) {
        return input(Ingredient.fromTag(input));
    }

    public RuneRitualRecipeBuilder input(Ingredient input) {
        this.inputs.add(input);
        return this;
    }

    public RuneRitualRecipeBuilder output(IItemProvider output) {
        return output(new ItemStack(output));
    }

    public RuneRitualRecipeBuilder output(ItemStack output) {
        this.outputs.add(output);
        return this;
    }

    public RuneRitualRecipeBuilder special(@Nullable SpecialRuneInput special) {
        this.specialInput = special;
        return this;
    }
    
    public RuneRitualRecipeBuilder special(@Nullable SpecialRuneOutput special) {
        this.specialOutput = special;
        return this;
    }
    
    public void build(Consumer<IFinishedRecipe> consumer) {
        ResourceLocation rl = null;
        if (this.outputs.size() == 1) {
            rl = this.outputs.get(0).getItem().getRegistryName();
        } else if (this.specialOutput != null) {
            rl = this.specialOutput.id;
        }
        if (rl == null) {
            throw new IllegalStateException("Failed to infer recipe id for rune ritual recipe.w");
        }
        this.build(consumer, rl);
    }

    public void build(Consumer<IFinishedRecipe> consumerIn, ResourceLocation id) {
        consumerIn.accept(new FinishedRecipe(new ResourceLocation(id.getNamespace(), "mythicbotany_rune_rituals/" + id.getPath()), centerRune, runes, manaCost, tickTime, inputs, outputs, specialInput, specialOutput));
    }

    private static class FinishedRecipe implements IFinishedRecipe {

        private final ResourceLocation id;
        private final Ingredient centerRune;
        private final List<RuneRitualRecipe.RunePosition> runes;
        private final int manaCost;
        private final int tickTime;
        private final List<Ingredient> inputs;
        private final List<ItemStack> outputs;
        @Nullable
        private final SpecialRuneInput specialInput;
        @Nullable
        private final SpecialRuneOutput specialOutput;

        private FinishedRecipe(ResourceLocation id, Ingredient centerRune, List<RuneRitualRecipe.RunePosition> runes, int manaCost, int tickTime, List<Ingredient> inputs, List<ItemStack> outputs, @Nullable SpecialRuneInput specialInput, @Nullable SpecialRuneOutput specialOutput) {
            this.id = id;
            this.centerRune = centerRune;
            this.runes = ImmutableList.copyOf(runes);
            this.manaCost = manaCost;
            this.tickTime = tickTime;
            this.inputs = ImmutableList.copyOf(inputs);
            this.outputs = ImmutableList.copyOf(outputs);
            this.specialInput = specialInput;
            this.specialOutput = specialOutput;
        }

        @Nonnull
        @Override
        public ResourceLocation getID() {
            return id;
        }

        @Override
        public void serialize(@Nonnull JsonObject json) {
            json.addProperty("group", "rune_rituals");
            json.add("center", centerRune.serialize());
            JsonArray runesJson = new JsonArray();
            runes.stream().map(rune -> {
                JsonObject obj = new JsonObject();
                obj.add("rune", rune.getRune().serialize());
                obj.addProperty("x", rune.getX());
                obj.addProperty("z", rune.getZ());
                obj.addProperty("consume", rune.isConsumed());
                return obj;
            }).forEach(runesJson::add);
            json.add("runes", runesJson);
            
            json.addProperty("mana", manaCost);
            json.addProperty("ticks", tickTime);
            
            JsonArray inputsJson = new JsonArray();
            inputs.stream().map(Ingredient::serialize).forEach(inputsJson::add);
            json.add("inputs", inputsJson);
            
            JsonArray outputsJson = new JsonArray();
            outputs.stream().map(stack -> CraftingHelper2.serializeItemStack(stack, true)).forEach(outputsJson::add);
            json.add("outputs", outputsJson);
            
            if (specialInput != null) {
                json.addProperty("special_input", specialInput.id.toString());
            }
            
            if (specialOutput != null) {
                json.addProperty("special_output", specialOutput.id.toString());
            }
        }

        @Nonnull
        @Override
        public IRecipeSerializer<?> getSerializer() {
            return ModRecipes.RUNE_RITUAL_SERIALIZER;
        }

        @Nullable
        @Override
        public JsonObject getAdvancementJson() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementID() {
            return null;
        }
    }
}
