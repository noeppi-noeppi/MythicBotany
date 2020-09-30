package mythicbotany.recipes;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import vazkii.botania.common.core.helper.ItemNBTHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class InfuserRecipeBuilder {
    private final ItemStack result;
    private final List<Ingredient> ingredients = Lists.newArrayList();
    private final Advancement.Builder advancementBuilder = Advancement.Builder.builder();
    private String group;
    private int manaCost = -1;
    private int fromColor = -1;
    private int toColor = -1;

    public InfuserRecipeBuilder(ItemStack result) {
        this.result = result;
    }

    public static InfuserRecipeBuilder infuserRecipe(IItemProvider result) {
        return new InfuserRecipeBuilder(new ItemStack(result));
    }

    public static InfuserRecipeBuilder infuserRecipe(ItemStack result) {
        return new InfuserRecipeBuilder(result);
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

    public InfuserRecipeBuilder addManaCost(int mana) {
        this.manaCost = mana;
        return this;
    }

    public InfuserRecipeBuilder addColors(int fromColor, int toColor) {
        this.fromColor = fromColor;
        this.toColor = toColor;
        return this;
    }

    public InfuserRecipeBuilder addCriterion(String name, ICriterionInstance criterion) {
        this.advancementBuilder.withCriterion(name, criterion);
        return this;
    }

    public InfuserRecipeBuilder setGroup(String group) {
        this.group = group;
        return this;
    }

    public void build(Consumer<IFinishedRecipe> consumer) {
        this.build(consumer, Registry.ITEM.getKey(this.result.getItem()));
    }

    public void build(Consumer<IFinishedRecipe> consumerIn, String save) {
        ResourceLocation resourcelocation = Registry.ITEM.getKey(this.result.getItem());
        if ((new ResourceLocation(save)).equals(resourcelocation)) {
            throw new IllegalStateException("Infuser Recipe " + save + " should remove its 'save' argument");
        } else {
            this.build(consumerIn, new ResourceLocation(save));
        }
    }

    public void build(Consumer<IFinishedRecipe> consumerIn, ResourceLocation id) {
        this.validate(id);
        this.advancementBuilder.withParentId(new ResourceLocation("recipes/root")).withCriterion("has_the_recipe", RecipeUnlockedTrigger.create(id)).withRewards(AdvancementRewards.Builder.recipe(id)).withRequirementsStrategy(IRequirementsStrategy.OR);
        consumerIn.accept(new InfuserRecipeBuilder.FinishedRecipe(id, this.result, this.manaCost, this.group == null ? "" : this.group, this.fromColor, this.toColor, this.ingredients, this.advancementBuilder, new ResourceLocation(id.getNamespace(), "recipes/" + this.result.getItem().getGroup().getPath() + "/" + id.getPath())));
    }

    private void validate(ResourceLocation id) {
        if (this.advancementBuilder.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + id);
        }
        if (this.manaCost < 0) {
            throw new IllegalStateException("No mana requirement set for " + id);
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
        private final Advancement.Builder advancementBuilder;
        private final ResourceLocation advancementId;

        private FinishedRecipe(ResourceLocation id, ItemStack output, int mana, String group, int fromColor, int toColor, List<Ingredient> inputs, Advancement.Builder advancementBuilder, ResourceLocation advancementLocation) {
            this.id = id;
            this.output = output;
            this.mana = mana;
            this.group = group;
            this.fromColor = fromColor;
            this.toColor = toColor;
            this.inputs = inputs;
            this.advancementBuilder = advancementBuilder;
            this.advancementId = advancementLocation;
        }

        @Override
        public void serialize(JsonObject json) {
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
            return RecipeTypes.INFUSER_SERIALIZER;
        }

        @Nullable
        @Override
        public JsonObject getAdvancementJson() {
            return this.advancementBuilder.serialize();
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementID() {
            return this.advancementId;
        }
    }
}
