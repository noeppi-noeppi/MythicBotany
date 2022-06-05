package mythicbotany.wand;

import com.google.gson.JsonObject;
import mythicbotany.ModItems;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import vazkii.botania.common.block.decor.BlockModMushroom;
import vazkii.botania.common.crafting.recipe.TwigWandRecipe;
import vazkii.botania.common.item.ItemTwigWand;
import vazkii.botania.common.item.material.ItemPetal;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RecipeDreamwoodWand extends TwigWandRecipe {
    public static final RecipeSerializer<RecipeDreamwoodWand> SERIALIZER = new Serializer();

    public RecipeDreamwoodWand(ShapedRecipe compose) {
        super(compose);
    }

    @Nonnull
    public ItemStack assemble(CraftingContainer inv) {
        int first = -1;

        for(int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack stack = inv.getItem(i);
            Item item = stack.getItem();
            int colorId;
            if (item instanceof ItemPetal) {
                colorId = ((ItemPetal)item).color.getId();
            } else {
                if (!(item instanceof BlockItem) || !(((BlockItem)item).getBlock() instanceof BlockModMushroom)) {
                    continue;
                }
                colorId = ((BlockModMushroom)((BlockItem)item).getBlock()).color.getId();
            }
            if (first != -1) {
                return ItemDreamwoodWand.setColors(getResultItem().copy(), first, colorId);
            }
            first = colorId;
        }

        return ItemTwigWand.setColors(getResultItem().copy(), first != -1 ? first : 0, 0);
    }

    @Nonnull
    public ItemStack getResultItem() {
        return new ItemStack(ModItems.dreamwoodTwigWand);
    }

    @Nonnull
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    private static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<RecipeDreamwoodWand> {

        private Serializer() {

        }

        @Nonnull
        public RecipeDreamwoodWand fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
            return new RecipeDreamwoodWand(SHAPED_RECIPE.fromJson(recipeId, json));
        }

        @Nullable
        public RecipeDreamwoodWand fromNetwork(@Nonnull ResourceLocation recipeId, @Nonnull FriendlyByteBuf buffer) {
            return new RecipeDreamwoodWand(SHAPED_RECIPE.fromNetwork(recipeId, buffer));
        }

        public void toNetwork(@Nonnull FriendlyByteBuf buffer, @Nonnull RecipeDreamwoodWand recipe) {
            TwigWandRecipe.SERIALIZER.toNetwork(buffer, recipe);
        }
    }
}
