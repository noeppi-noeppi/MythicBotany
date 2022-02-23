package mythicbotany.data.recipes.extension;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.noeppi_noeppi.libx.crafting.CraftingHelper2;
import io.github.noeppi_noeppi.libx.data.provider.recipe.RecipeExtension;
import mythicbotany.ModRecipes;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.ItemLike;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public interface InfuserExtension extends RecipeExtension {

    default InfuserRecipeBuilder infuser(ItemLike result) {
        return new InfuserRecipeBuilder(this, new ItemStack(result)).setGroup("infuser");
    }

    default InfuserRecipeBuilder infuser(ItemStack result) {
        return new InfuserRecipeBuilder(this, result).setGroup("infuser");
    }
    
    class InfuserRecipeBuilder {

        private final RecipeExtension ext;
        private final ItemStack result;
        private final List<Ingredient> ingredients = new ArrayList<>();
        private String group;
        private int manaCost = -1;
        private int fromColor = 0xFFFFFF;
        private int toColor = 0xFFFFFF;

        private InfuserRecipeBuilder(RecipeExtension ext, ItemStack result) {
            this.ext = ext;
            this.result = result;
        }

        public InfuserRecipeBuilder addIngredient(Tag<Item> tag) {
            return this.addIngredient(Ingredient.of(tag));
        }

        public InfuserRecipeBuilder addIngredient(ItemLike item) {
            return this.addIngredient(Ingredient.of(item), 1);
        }

        public InfuserRecipeBuilder addIngredient(ItemLike item, int quantity) {
            for (int i = 0; i < quantity; i++) {
                this.addIngredient(Ingredient.of(item));
            }

            return this;
        }

        public InfuserRecipeBuilder addIngredient(Ingredient ingredient) {
            return this.addIngredient(ingredient, 1);
        }

        public InfuserRecipeBuilder addIngredient(Ingredient ingredient, int quantity) {
            for (int i = 0; i < quantity; i++) {
                this.ingredients.add(ingredient);
            }

            return this;
        }

        public InfuserRecipeBuilder setManaCost(int mana) {
            this.manaCost = mana;
            return this;
        }

        public InfuserRecipeBuilder setColors(int fromColor, int toColor) {
            this.fromColor = fromColor;
            this.toColor = toColor;
            return this;
        }

        public InfuserRecipeBuilder setGroup(String group) {
            this.group = group;
            return this;
        }

        public void build() {
            this.build(this.result.getItem().getRegistryName());
        }

        public void build(ResourceLocation id) {
            this.validate(id);
            this.ext.consumer().accept(new TheRecipe(new ResourceLocation(id.getNamespace(), "mythicbotany_infusion/" + id.getPath()), this.result, this.manaCost, this.group == null ? "" : this.group, this.fromColor, this.toColor, this.ingredients));
        }

        private void validate(ResourceLocation id) {
            if (this.manaCost < 0) {
                throw new IllegalStateException("No mana cost set for " + id);
            }
        }

        private static class TheRecipe implements FinishedRecipe {

            private final ResourceLocation id;
            private final ItemStack output;
            private final int mana;
            private final String group;
            private final int fromColor;
            private final int toColor;
            private final List<Ingredient> inputs;

            private TheRecipe(ResourceLocation id, ItemStack output, int mana, String group, int fromColor, int toColor, List<Ingredient> inputs) {
                this.id = id;
                this.output = output;
                this.mana = mana;
                this.group = group;
                this.fromColor = fromColor;
                this.toColor = toColor;
                this.inputs = inputs;
            }

            @Override
            public void serializeRecipeData(@Nonnull JsonObject json) {
                if (!this.group.isEmpty()) {
                    json.addProperty("group", this.group);
                }
                json.add("output", CraftingHelper2.serializeItemStack(this.output, true));
                JsonArray ingredients = new JsonArray();
                for (Ingredient ingredient : this.inputs) {
                    ingredients.add(ingredient.toJson());
                }
                json.addProperty("mana", this.mana);
                json.add("ingredients", ingredients);
                json.addProperty("fromColor", this.fromColor);
                json.addProperty("toColor", this.toColor);
            }

            @Nonnull
            @Override
            public ResourceLocation getId() {
                return this.id;
            }

            @Nonnull
            @Override
            public RecipeSerializer<?> getType() {
                return ModRecipes.INFUSER_SERIALIZER;
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
