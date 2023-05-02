package mythicbotany.infuser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import mythicbotany.register.ModRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class InfuserRecipe implements Recipe<Container> {
    
    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> inputs;
    private final int mana;
    private final int fromColor;
    private final int toColor;

    public InfuserRecipe(ResourceLocation id, ItemStack output, int mana, int fromColor, int toColor, Ingredient... inputs) {
        this.id = id;
        this.output = output;
        this.mana = mana;
        this.fromColor = fromColor;
        this.toColor = toColor;
        this.inputs = NonNullList.of(Ingredient.EMPTY, inputs);
    }

    @Nonnull
    @Override
    public RecipeType<?> getType() {
        return ModRecipes.infuser;
    }

    @Nonnull
    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Nonnull
    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    public int getManaUsage() {
        return this.mana;
    }

    public int fromColor() {
        return this.fromColor;
    }

    public int toColor() {
        return this.toColor;
    }

    @Override
    public boolean matches(@Nonnull Container inv, @Nonnull Level level) {
        List<Ingredient> ingredientsMissing = new ArrayList<>(this.inputs);
        IntStream.range(0, inv.getContainerSize()).boxed().map(inv::getItem).filter(stack -> !stack.isEmpty()).forEach(stack ->
                ingredientsMissing.stream().filter(ingredient -> ingredient.test(stack)).findFirst().ifPresent(ingredientsMissing::remove)
        );
        return ingredientsMissing.isEmpty();
    }

    @Nonnull
    @Override
    public ItemStack getResultItem() {
        return this.output;
    }

    @Nonnull
    @Override
    public ItemStack assemble(@Nonnull Container container) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    public ItemStack result(List<ItemStack> inputs) {
        if (inputs.size() != this.inputs.size())
            return ItemStack.EMPTY;
        outer: for (Ingredient item : this.inputs) {
            for (ItemStack stack : inputs) {
                if (item.test(stack))
                    continue outer;
            }
            return ItemStack.EMPTY;
        }
        return this.output.copy();
    }

    @Nonnull
    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.inputs;
    }

    @Nullable
    public static Pair<InfuserRecipe, ItemStack> getOutput(Level level, List<ItemStack> inputs) {
        if (!inputs.isEmpty()) {
            if (inputs.stream().anyMatch(stack -> stack.getCount() != 1)) {
                return null;
            }
            for (Recipe<?> recipe : level.getRecipeManager().byType(ModRecipes.infuser).values()) {
                if (recipe instanceof InfuserRecipe infuserRecipe) {
                    ItemStack stack = infuserRecipe.result(inputs);
                    if (!stack.isEmpty()) return Pair.of(infuserRecipe, stack.copy());
                }
            }
        }
        return null;
    }

    public static class Serializer implements RecipeSerializer<InfuserRecipe> {

        public static Serializer INSTANCE = new Serializer();

        private Serializer() {

        }

        @Nonnull
        @Override
        public InfuserRecipe fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
            ItemStack output = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "output"), true);
            int mana = GsonHelper.getAsInt(json, "mana");
            int fromColor = GsonHelper.getAsInt(json, "fromColor");
            int toColor = GsonHelper.getAsInt(json, "toColor");
            JsonArray ingrs = GsonHelper.getAsJsonArray(json, "ingredients");
            List<Ingredient> inputs = new ArrayList<>();
            for (JsonElement e : ingrs) {
                inputs.add(Ingredient.fromJson(e));
            }
            return new InfuserRecipe(recipeId, output, mana, fromColor, toColor, inputs.toArray(new Ingredient[0]));
        }

        @Nullable
        @Override
        public InfuserRecipe fromNetwork(@Nonnull ResourceLocation recipeId, @Nonnull FriendlyByteBuf buffer) {
            Ingredient[] inputs = new Ingredient[buffer.readInt()];
            for (int i = 0; i < inputs.length; i++) {
                inputs[i] = Ingredient.fromNetwork(buffer);
            }
            ItemStack output = buffer.readItem();
            int mana = buffer.readInt();
            int fromColor = buffer.readInt();
            int toColor = buffer.readInt();
            return new InfuserRecipe(recipeId, output, mana, fromColor, toColor, inputs);
        }

        @Override
        public void toNetwork(@Nonnull FriendlyByteBuf buffer, @Nonnull InfuserRecipe recipe) {
            buffer.writeInt(recipe.getIngredients().size());
            for (Ingredient input : recipe.getIngredients()) {
                input.toNetwork(buffer);
            }
            buffer.writeItemStack(recipe.getResultItem(), false);
            buffer.writeInt(recipe.getManaUsage());
            buffer.writeInt(recipe.fromColor);
            buffer.writeInt(recipe.toColor);
        }
    }
}
