package mythicbotany.data.recipes.builder;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mythicbotany.ModRecipes;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.common.core.helper.ItemNBTHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class InfuserRecipeBuilder {
    
    private final ItemStack result;
    private final List<Ingredient> ingredients = new ArrayList<>();
    private String group;
    private int manaCost = -1;
    private int fromColor = 0xFFFFFF;
    private int toColor = 0xFFFFFF;

    private InfuserRecipeBuilder(ItemStack result) {
        this.result = result;
    }

    public static InfuserRecipeBuilder infuserRecipe(IItemProvider result) {
        return new InfuserRecipeBuilder(new ItemStack(result)).setGroup("infuser");
    }

    public static InfuserRecipeBuilder infuserRecipe(ItemStack result) {
        return new InfuserRecipeBuilder(result).setGroup("infuser");
    }

    public InfuserRecipeBuilder addIngredient(ITag<Item> tag) {
        return this.addIngredient(Ingredient.fromTag(tag));
    }

    public InfuserRecipeBuilder addIngredient(IItemProvider item) {
        return this.addIngredient(Ingredient.fromItems(item), 1);
    }

    public InfuserRecipeBuilder addIngredient(IItemProvider item, int quantity) {
        for (int i = 0; i < quantity; i++) {
            this.addIngredient(Ingredient.fromItems(item));
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

    public void build(Consumer<IFinishedRecipe> consumer) {
        this.build(consumer, this.result.getItem().getRegistryName());
    }

    public void build(Consumer<IFinishedRecipe> consumerIn, ResourceLocation id) {
        this.validate(id);
        consumerIn.accept(new InfuserRecipeBuilder.FinishedRecipe(new ResourceLocation(id.getNamespace(), "mythicbotany_infusion/" + id.getPath()), this.result, this.manaCost, this.group == null ? "" : this.group, this.fromColor, this.toColor, this.ingredients));
    }

    private void validate(ResourceLocation id) {
        if (this.manaCost < 0) {
            throw new IllegalStateException("No mana cost set for " + id);
        }
    }

    private static class FinishedRecipe implements IFinishedRecipe {

        private final ResourceLocation id;
        private final ItemStack output;
        private final int mana;
        private final String group;
        private final int fromColor;
        private final int toColor;
        private final List<Ingredient> inputs;

        private FinishedRecipe(ResourceLocation id, ItemStack output, int mana, String group, int fromColor, int toColor, List<Ingredient> inputs) {
            this.id = id;
            this.output = output;
            this.mana = mana;
            this.group = group;
            this.fromColor = fromColor;
            this.toColor = toColor;
            this.inputs = inputs;
        }

        @Override
        public void serialize(@Nonnull JsonObject json) {
            if (!this.group.isEmpty()) {
                json.addProperty("group", this.group);
            }
            json.add("output", ItemNBTHelper.serializeStack(this.output));
            JsonArray ingredients = new JsonArray();
            for (Ingredient ingredient : inputs) {
                ingredients.add(ingredient.serialize());
            }
            json.addProperty("mana", mana);
            json.add("ingredients", ingredients);
            json.addProperty("fromColor", fromColor);
            json.addProperty("toColor", toColor);
        }

        @Nonnull
        @Override
        public ResourceLocation getID() {
            return this.id;
        }

        @Nonnull
        @Override
        public IRecipeSerializer<?> getSerializer() {
            return ModRecipes.INFUSER_SERIALIZER;
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
