package mythicbotany.data.recipes.extension;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mythicbotany.rune.RuneRitualRecipe;
import mythicbotany.rune.SpecialRuneInput;
import mythicbotany.rune.SpecialRuneOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import org.moddingx.libx.crafting.RecipeHelper;
import org.moddingx.libx.datagen.provider.recipe.RecipeExtension;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public interface RuneRitualExtension extends RecipeExtension {

    default RuneRitualRecipeBuilder runeRitual(ItemLike centerRune) {
        return new RuneRitualRecipeBuilder(this, Ingredient.of(centerRune));
    }

    default RuneRitualRecipeBuilder runeRitual(TagKey<Item> centerRune) {
        return new RuneRitualRecipeBuilder(this, Ingredient.of(centerRune));
    }

    default RuneRitualRecipeBuilder runeRitual(Ingredient centerRune) {
        return new RuneRitualRecipeBuilder(this, centerRune);
    }
    
    class RuneRitualRecipeBuilder {

        private final RecipeExtension ext;
        private final Ingredient centerRune;
        private final List<RuneRitualRecipe.RunePosition> runes = new ArrayList<>();
        private int manaCost = 0;
        private int tickTime = 200;
        private final List<Ingredient> inputs = new ArrayList<>();
        private final List<ItemStack> outputs = new ArrayList<>();
        @Nullable
        private SpecialRuneInput specialInput;
        @Nullable
        private SpecialRuneOutput specialOutput;

        private RuneRitualRecipeBuilder(RecipeExtension ext, Ingredient centerRune) {
            this.ext = ext;
            this.centerRune = centerRune;
        }

        public RuneRitualRecipeBuilder rune(ItemLike rune, int x, int z, boolean consume) {
            return this.rune(Ingredient.of(rune), x, z, consume);
        }

        public RuneRitualRecipeBuilder rune(TagKey<Item> rune, int x, int z, boolean consume) {
            return this.rune(Ingredient.of(rune), x, z, consume);
        }

        public RuneRitualRecipeBuilder rune(Ingredient rune, int x, int z, boolean consume) {
            if (x < -5 || x > 5 || z < -5 || z > 5) {
                throw new IllegalStateException("Rune positions should not be more than 5 blocks away frm the central rune holder: (" + x + "," + z + ")");
            }
            this.runes.add(new RuneRitualRecipe.RunePosition(rune, x, z, consume));
            return this;
        }

        public RuneRitualRecipeBuilder rune4(ItemLike rune, int x, int z, boolean consume) {
            if (x == 0) {
                this.rune(rune, 0, -z, consume);
                this.rune(rune, 0, z, consume);
                this.rune(rune, -z, 0, consume);
                this.rune(rune, z, 0, consume);
            } else if (z == 0) {
                this.rune(rune, -x, 0, consume);
                this.rune(rune, x, 0, consume);
                this.rune(rune, 0, -x, consume);
                this.rune(rune, 0, x, consume);
            } else {
                this.rune(rune, -x, -z, consume);
                this.rune(rune, -x, z, consume);
                this.rune(rune, x, -z, consume);
                this.rune(rune, x, z, consume);
            }
            return this;
        }

        public RuneRitualRecipeBuilder rune4(TagKey<Item> rune, int x, int z, boolean consume) {
            if (x == 0) {
                this.rune(rune, 0, -z, consume);
                this.rune(rune, 0, z, consume);
                this.rune(rune, -z, 0, consume);
                this.rune(rune, z, 0, consume);
            } else if (z == 0) {
                this.rune(rune, -x, 0, consume);
                this.rune(rune, x, 0, consume);
                this.rune(rune, 0, -x, consume);
                this.rune(rune, 0, x, consume);
            } else {
                this.rune(rune, -x, -z, consume);
                this.rune(rune, -x, z, consume);
                this.rune(rune, x, -z, consume);
                this.rune(rune, x, z, consume);
            }
            return this;
        }

        public RuneRitualRecipeBuilder rune4(Ingredient rune, int x, int z, boolean consume) {
            if (x == 0) {
                this.rune(rune, 0, -z, consume);
                this.rune(rune, 0, z, consume);
                this.rune(rune, -z, 0, consume);
                this.rune(rune, z, 0, consume);
            } else if (z == 0) {
                this.rune(rune, -x, 0, consume);
                this.rune(rune, x, 0, consume);
                this.rune(rune, 0, -x, consume);
                this.rune(rune, 0, x, consume);
            } else {
                this.rune(rune, -x, -z, consume);
                this.rune(rune, -x, z, consume);
                this.rune(rune, x, -z, consume);
                this.rune(rune, x, z, consume);
            }
            return this;
        }

        public RuneRitualRecipeBuilder rune2(ItemLike rune, int x, int z, boolean consume) {
            this.rune(rune, -x, -z, consume);
            this.rune(rune, x, z, consume);
            return this;
        }

        public RuneRitualRecipeBuilder rune2(TagKey<Item> rune, int x, int z, boolean consume) {
            this.rune(rune, -x, -z, consume);
            this.rune(rune, x, z, consume);
            return this;
        }

        public RuneRitualRecipeBuilder rune2(Ingredient rune, int x, int z, boolean consume) {
            this.rune(rune, -x, -z, consume);
            this.rune(rune, x, z, consume);
            return this;
        }

        public RuneRitualRecipeBuilder rune(ItemLike rune, int x, int z) {
            return this.rune(rune, x, z, false);
        }

        public RuneRitualRecipeBuilder rune(TagKey<Item> rune, int x, int z) {
            return this.rune(rune, x, z, false);
        }

        public RuneRitualRecipeBuilder rune(Ingredient rune, int x, int z) {
            return this.rune(rune, x, z, false);
        }

        public RuneRitualRecipeBuilder rune4(ItemLike rune, int x, int z) {
            return this.rune4(rune, x, z, false);
        }

        public RuneRitualRecipeBuilder rune4(TagKey<Item> rune, int x, int z) {
            return this.rune4(rune, x, z, false);
        }

        public RuneRitualRecipeBuilder rune4(Ingredient rune, int x, int z) {
            return this.rune4(rune, x, z, false);
        }

        public RuneRitualRecipeBuilder rune2(ItemLike rune, int x, int z) {
            return this.rune2(rune, x, z, false);
        }

        public RuneRitualRecipeBuilder rune2(TagKey<Item> rune, int x, int z) {
            return this.rune2(rune, x, z, false);
        }

        public RuneRitualRecipeBuilder rune2(Ingredient rune, int x, int z) {
            return this.rune2(rune, x, z, false);
        }

        public RuneRitualRecipeBuilder mana(int manaCost) {
            this.manaCost = manaCost;
            return this;
        }

        public RuneRitualRecipeBuilder time(int tickTime) {
            this.tickTime = tickTime;
            return this;
        }

        public RuneRitualRecipeBuilder input(ItemLike input) {
            return this.input(Ingredient.of(input));
        }

        public RuneRitualRecipeBuilder input(TagKey<Item> input) {
            return this.input(Ingredient.of(input));
        }

        public RuneRitualRecipeBuilder input(Ingredient input) {
            this.inputs.add(input);
            return this;
        }

        public RuneRitualRecipeBuilder output(ItemLike output) {
            return this.output(new ItemStack(output));
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

        public void build() {
            ResourceLocation rl = null;
            if (this.outputs.size() == 1) {
                rl = this.ext.provider().loc(this.outputs.get(0).getItem());
            } else if (this.specialOutput != null) {
                rl = this.specialOutput.id;
            }
            if (rl == null) {
                throw new IllegalStateException("Failed to infer recipe id for rune ritual recipe.");
            }
            this.build(rl);
        }

        public void build(ResourceLocation id) {
            this.ext.consumer().accept(new TheRecipe(new ResourceLocation(id.getNamespace(), "mythicbotany_rune_rituals/" + id.getPath()), this.centerRune, this.runes, this.manaCost, this.tickTime, this.inputs, this.outputs, this.specialInput, this.specialOutput));
        }

        @SuppressWarnings("ClassCanBeRecord")
        private static class TheRecipe implements FinishedRecipe {

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

            private TheRecipe(ResourceLocation id, Ingredient centerRune, List<RuneRitualRecipe.RunePosition> runes, int manaCost, int tickTime, List<Ingredient> inputs, List<ItemStack> outputs, @Nullable SpecialRuneInput specialInput, @Nullable SpecialRuneOutput specialOutput) {
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
            public ResourceLocation getId() {
                return this.id;
            }

            @Override
            public void serializeRecipeData(@Nonnull JsonObject json) {
                json.addProperty("group", "rune_rituals");
                json.add("center", this.centerRune.toJson());
                JsonArray runesJson = new JsonArray();
                this.runes.stream().map(rune -> {
                    JsonObject obj = new JsonObject();
                    obj.add("rune", rune.getRune().toJson());
                    obj.addProperty("x", rune.getX());
                    obj.addProperty("z", rune.getZ());
                    obj.addProperty("consume", rune.isConsumed());
                    return obj;
                }).forEach(runesJson::add);
                json.add("runes", runesJson);

                json.addProperty("mana", this.manaCost);
                json.addProperty("ticks", this.tickTime);

                JsonArray inputsJson = new JsonArray();
                this.inputs.stream().map(Ingredient::toJson).forEach(inputsJson::add);
                json.add("inputs", inputsJson);

                JsonArray outputsJson = new JsonArray();
                this.outputs.stream().map(stack -> RecipeHelper.serializeItemStack(stack, true)).forEach(outputsJson::add);
                json.add("outputs", outputsJson);

                if (this.specialInput != null) {
                    json.addProperty("special_input", this.specialInput.id.toString());
                }

                if (this.specialOutput != null) {
                    json.addProperty("special_output", this.specialOutput.id.toString());
                }
            }

            @Nonnull
            @Override
            public RecipeSerializer<?> getType() {
                return RuneRitualRecipe.Serializer.INSTANCE;
            }

            @Nullable
            @Override
            public JsonObject serializeAdvancement() {
                return null;
            }

            @Nullable
            @Override
            public ResourceLocation getAdvancementId() {
                return null;
            }
        }
    }
}
