package mythicbotany;

import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

// Backport of LibX EmptyRecipe for 1.17
public class EmptyRecipe implements Recipe<Container> {
    // TODO remove
    
    public static final ResourceLocation ID = new ResourceLocation(MythicBotany.getInstance().modid, "empty");
    public static final RecipeType<EmptyRecipe> TYPE = RecipeType.register(ID.toString());
    
    private final ResourceLocation id;

    public EmptyRecipe(ResourceLocation id) {
        this.id = id;
    }

    @Override
    public boolean matches(@Nonnull Container inv, @Nonnull Level level) {
        return false;
    }

    @Nonnull
    @Override
    public ItemStack assemble(@Nonnull Container inv) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Nonnull
    @Override
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Nonnull
    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Nonnull
    @Override
    public RecipeType<?> getType() {
        return TYPE;
    }

    @Nonnull
    @Override
    public NonNullList<ItemStack> getRemainingItems(@Nonnull Container inv) {
        return NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);
    }

    @Nonnull
    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.create();
    }
    
    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<EmptyRecipe> {

        public static final Serializer INSTANCE = new Serializer();
        
        private Serializer() {
            
        }
        
        @Nonnull
        @Override
        public EmptyRecipe fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
            return new EmptyRecipe(recipeId);
        }

        @Nullable
        @Override
        public EmptyRecipe fromNetwork(@Nonnull ResourceLocation recipeId, @Nonnull FriendlyByteBuf buffer) {
            return new EmptyRecipe(recipeId);
        }

        @Override
        public void toNetwork(@Nonnull FriendlyByteBuf buffer, @Nonnull EmptyRecipe recipe) {
            //
        }
    }
    
    public static FinishedRecipe empty(ResourceLocation id) {
        
        return new FinishedRecipe() {

            @Override
            public void serializeRecipeData(@Nonnull JsonObject json) {
                //
            }

            @Nonnull
            @Override
            public ResourceLocation getId() {
                return id;
            }

            @Nonnull
            @Override
            public RecipeSerializer<?> getType() {
                return Serializer.INSTANCE;
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
        };
    }
}
