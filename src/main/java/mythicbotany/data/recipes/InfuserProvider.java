package mythicbotany.data.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mythicbotany.MythicBotany;
import mythicbotany.recipes.RecipeTypes;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.ModTags;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

public class InfuserProvider extends RecipeProvider {
    public InfuserProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Nonnull
    @Override
    public String getName() {
        return "MythicBotany infuser recipes";
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        consumer.accept(new FinishedRecipe(new ItemStack(ModItems.terrasteel), 500000, 0x0000FF, 0x00FF00,
                Ingredient.fromTag(ModTags.Items.INGOTS_MANASTEEL),
                Ingredient.fromItems(ModItems.manaPearl),
                Ingredient.fromTag(ModTags.Items.GEMS_MANA_DIAMOND)));

        consumer.accept(new FinishedRecipe(new ItemStack(mythicbotany.ModItems.alfsteelIngot), 2_000_000, 0xFF008D, 0xFF9600,
                Ingredient.fromTag(ModTags.Items.INGOTS_ELEMENTIUM),
                Ingredient.fromTag(ModTags.Items.GEMS_DRAGONSTONE),
                Ingredient.fromItems(ModItems.pixieDust)));
    }

    private static ResourceLocation idFor(String s) {
        return new ResourceLocation(MythicBotany.MODID, "infuser/" + s);
    }

    private static class FinishedRecipe implements IFinishedRecipe {
        private final ResourceLocation id;
        private final ItemStack output;
        private final int mana;
        private final int fromColor;
        private final int toColor;
        private final Ingredient[] inputs;

        private FinishedRecipe(ItemStack output, int mana, int fromColor, int toColor, Ingredient... inputs) {
            //noinspection ConstantConditions
            this.id = idFor(output.getItem().getRegistryName().getPath());
            this.output = output;
            this.mana = mana;
            this.fromColor = fromColor;
            this.toColor = toColor;
            this.inputs = inputs;
        }

        private FinishedRecipe(ItemStack output, int mana, Ingredient... inputs) {
            this(output, mana, -1, -1, inputs);
        }

        @Override
        public void serialize(JsonObject json) {
            json.add("output", ItemNBTHelper.serializeStack(output));
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
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementID() {
            return null;
        }
    }
}
