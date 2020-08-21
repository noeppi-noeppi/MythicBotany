package mythicbotany.wand;

import com.google.gson.JsonObject;
import mythicbotany.ModItems;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import vazkii.botania.common.block.decor.BlockModMushroom;
import vazkii.botania.common.crafting.recipe.TwigWandRecipe;
import vazkii.botania.common.item.ItemTwigWand;
import vazkii.botania.common.item.material.ItemPetal;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RecipeDreamwoodWand extends TwigWandRecipe {
    public static final IRecipeSerializer<RecipeDreamwoodWand> SERIALIZER = new Serializer();

    public RecipeDreamwoodWand(ShapedRecipe compose) {
        super(compose);
    }

    @Nonnull
    public ItemStack getCraftingResult(CraftingInventory inv) {
        int first = -1;

        for(int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);
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
                return ItemDreamwoodWand.forColors(first, colorId);
            }
            first = colorId;
        }

        return ItemTwigWand.forColors(first != -1 ? first : 0, 0);
    }

    @Nonnull
    public ItemStack getRecipeOutput() {
        return new ItemStack(ModItems.dreamwoodWand);
    }

    @Nonnull
    public IRecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    private static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<RecipeDreamwoodWand> {

        private Serializer() {

        }

        @Nonnull
        public RecipeDreamwoodWand read(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
            return new RecipeDreamwoodWand(CRAFTING_SHAPED.read(recipeId, json));
        }

        @Nullable
        public RecipeDreamwoodWand read(@Nonnull ResourceLocation recipeId, @Nonnull PacketBuffer buffer) {
            return new RecipeDreamwoodWand(CRAFTING_SHAPED.read(recipeId, buffer));
        }

        public void write(@Nonnull PacketBuffer buffer, @Nonnull RecipeDreamwoodWand recipe) {
            TwigWandRecipe.SERIALIZER.write(buffer, recipe);
        }
    }
}
