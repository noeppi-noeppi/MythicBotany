package mythicbotany.infuser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import mythicbotany.ModRecipes;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class InfuserRecipe implements IInfuserRecipe {

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
        this.inputs = NonNullList.from(Ingredient.EMPTY, inputs);
    }

    @Override
    public int getManaUsage() {
        return this.mana;
    }

    @Override
    public int fromColor() {
        return this.fromColor;
    }

    @Override
    public int toColor() {
        return this.toColor;
    }

    @Override
    public boolean matches(@Nonnull IInventory inv, @Nonnull World worldIn) {
        List<Ingredient> ingredientsMissing = new ArrayList<>(inputs);
        IntStream.range(0, inv.getSizeInventory()).boxed().map(inv::getStackInSlot).filter(stack -> !stack.isEmpty()).forEach(stack ->
                ingredientsMissing.stream().filter(ingredient -> ingredient.test(stack)).findFirst().ifPresent(ingredientsMissing::remove)
        );
        return ingredientsMissing.isEmpty();
    }

    @Nonnull
    @Override
    public ItemStack getRecipeOutput() {
        return this.output;
    }

    @Nonnull
    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Nonnull
    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipes.INFUSER_SERIALIZER;
    }

    @Override
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
        return output.copy();
    }

    @Nonnull
    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.inputs;
    }

    @Nullable
    public static Pair<IInfuserRecipe, ItemStack> getOutput(World world, List<ItemStack> inputs) {
        for (IRecipe<?> recipe : world.getRecipeManager().getRecipes()) {
            if (recipe instanceof IInfuserRecipe) {
                ItemStack stack = ((IInfuserRecipe) recipe).result(inputs);
                if (!stack.isEmpty())
                    return Pair.of((IInfuserRecipe) recipe, stack);
            }
        }
        return null;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<InfuserRecipe> {
        @Nonnull
        @Override
        public InfuserRecipe read(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
            ItemStack output = CraftingHelper.getItemStack(JSONUtils.getJsonObject(json, "output"), true);
            int mana = JSONUtils.getInt(json, "mana");
            int fromColor = JSONUtils.getInt(json, "fromColor");
            int toColor = JSONUtils.getInt(json, "toColor");
            JsonArray ingrs = JSONUtils.getJsonArray(json, "ingredients");
            List<Ingredient> inputs = new ArrayList<>();
            for (JsonElement e : ingrs) {
                inputs.add(Ingredient.deserialize(e));
            }
            return new InfuserRecipe(recipeId, output, mana, fromColor, toColor, inputs.toArray(new Ingredient[0]));
        }

        @Nullable
        @Override
        public InfuserRecipe read(@Nonnull ResourceLocation recipeId, @Nonnull PacketBuffer buffer) {
            Ingredient[] inputs = new Ingredient[buffer.readInt()];
            for (int i = 0; i < inputs.length; i++) {
                inputs[i] = Ingredient.read(buffer);
            }
            ItemStack output = buffer.readItemStack();
            int mana = buffer.readInt();
            int fromColor = buffer.readInt();
            int toColor = buffer.readInt();
            return new InfuserRecipe(recipeId, output, mana, fromColor, toColor, inputs);
        }

        @Override
        public void write(@Nonnull PacketBuffer buffer, @Nonnull InfuserRecipe recipe) {
            buffer.writeInt(recipe.getIngredients().size());
            for (Ingredient input : recipe.getIngredients()) {
                input.write(buffer);
            }
            buffer.writeItemStack(recipe.getRecipeOutput(), false);
            buffer.writeInt(recipe.getManaUsage());
            buffer.writeInt(recipe.fromColor);
            buffer.writeInt(recipe.toColor);
        }
    }
}
