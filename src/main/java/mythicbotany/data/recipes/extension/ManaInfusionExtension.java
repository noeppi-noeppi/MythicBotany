package mythicbotany.data.recipes.extension;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.moddingx.libx.datagen.provider.recipe.RecipeExtension;
import vazkii.botania.data.recipes.ManaInfusionProvider;

public interface ManaInfusionExtension extends RecipeExtension {

    default void manaInfusion(ItemLike input, ItemLike result, int mana) {
        this.manaInfusion(input, new ItemStack(result), mana);
    }

    default void manaInfusion(TagKey<Item> input, ItemLike result, int mana) {
        this.manaInfusion(input, new ItemStack(result), mana);
    }

    default void manaInfusion(Ingredient input, ItemLike result, int mana) {
        this.manaInfusion(input, new ItemStack(result), mana);
    }
    
    default void manaInfusion(ItemLike input, ItemStack result, int mana) {
        this.manaInfusion(Ingredient.of(input), result, mana);
    }
    
    default void manaInfusion(TagKey<Item> input, ItemStack result, int mana) {
        this.manaInfusion(Ingredient.of(input), result, mana);
    }
    
    default void manaInfusion(Ingredient input, ItemStack result, int mana) {
        this.consumer().accept(Wrapper.create(this.provider().loc(result.getItem(), "mana_infusion"), result, input, mana));
    }

    default void manaAlchemy(ItemLike input, ItemLike result, int mana) {
        this.manaAlchemy(input, new ItemStack(result), mana);
    }

    default void manaAlchemy(TagKey<Item> input, ItemLike result, int mana) {
        this.manaAlchemy(input, new ItemStack(result), mana);
    }

    default void manaAlchemy(Ingredient input, ItemLike result, int mana) {
        this.manaAlchemy(input, new ItemStack(result), mana);
    }
    
    default void manaAlchemy(ItemLike input, ItemStack result, int mana) {
        this.manaAlchemy(Ingredient.of(input), result, mana);
    }

    default void manaAlchemy(TagKey<Item> input, ItemStack result, int mana) {
        this.manaAlchemy(Ingredient.of(input), result, mana);
    }

    default void manaAlchemy(Ingredient input, ItemStack result, int mana) {
        this.consumer().accept(Wrapper.alchemy(this.provider().loc(result.getItem(), "mana_alchemy"), result, input, mana));
    }

    default void manaConjuration(ItemLike input, ItemLike result, int mana) {
        this.manaConjuration(input, new ItemStack(result), mana);
    }

    default void manaConjuration(TagKey<Item> input, ItemLike result, int mana) {
        this.manaConjuration(input, new ItemStack(result), mana);
    }

    default void manaConjuration(Ingredient input, ItemLike result, int mana) {
        this.manaConjuration(input, new ItemStack(result), mana);
    }
    
    default void manaConjuration(ItemLike input, ItemStack result, int mana) {
        this.manaConjuration(Ingredient.of(input), result, mana);
    }

    default void manaConjuration(TagKey<Item> input, ItemStack result, int mana) {
        this.manaConjuration(Ingredient.of(input), result, mana);
    }

    default void manaConjuration(Ingredient input, ItemStack result, int mana) {
        this.consumer().accept(Wrapper.conjuration(this.provider().loc(result.getItem(), "mana_conjuration"), result, input, mana));
    }
    
    class Wrapper extends ManaInfusionProvider {
        
        public Wrapper(PackOutput packOutput) {
            super(packOutput);
        }

        private static FinishedRecipe create(ResourceLocation id, ItemStack output, Ingredient input, int mana) {
            return new FinishedRecipe(id, output, input, mana);
        }

        private static FinishedRecipe alchemy(ResourceLocation id, ItemStack output, Ingredient input, int mana) {
            return FinishedRecipe.alchemy(id, output, input, mana);
        }

        private static FinishedRecipe conjuration(ResourceLocation id, ItemStack output, Ingredient input, int mana) {
            return FinishedRecipe.conjuration(id, output, input, mana);
        }
    }
}
